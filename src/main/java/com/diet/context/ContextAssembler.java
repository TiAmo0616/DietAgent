package com.diet.context;

import com.diet.model.ConversationTurn;
import com.diet.model.SessionState;
import com.diet.model.SlotBundle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Deterministically assembles prompt context in priority order:
 * current request, structured session state, then recent conversation.
 * Long-term/vector memory is intentionally not part of this stage.
 */
@Service
public class ContextAssembler {
    private static final int CHARS_PER_TOKEN = 4;
    private static final int MAX_FIELD_CHARS = 800;
    private static final List<String> SENSITIVE_KEYS = List.of(
            "password", "token", "authorization", "secret", "api_key", "apikey", "cookie"
    );

    private final ObjectMapper objectMapper;
    private final int tokenBudget;

    public ContextAssembler(ObjectMapper objectMapper,
                            @Value("${diet.context.intent-token-budget:1800}") int tokenBudget) {
        this.objectMapper = objectMapper;
        this.tokenBudget = Math.max(256, tokenBudget);
    }

    public ContextSnapshot assemble(String message, SlotBundle knownSlots,
                                    SessionState state, List<ConversationTurn> history,
                                    Map<String, Object> requestContext) {
        String current = compact(message, MAX_FIELD_CHARS);
        String stateText = stateText(knownSlots, state);
        String requestText = jsonWithoutSensitive(requestContext);

        List<String> selectedHistory = new ArrayList<>();
        int used = estimate(current) + estimate(stateText) + estimate(requestText);
        if (history != null) {
            for (int i = history.size() - 1; i >= 0; i--) {
                ConversationTurn turn = history.get(i);
                String line = compact(String.valueOf(turn), 360);
                int next = used + estimate(line);
                if (next > tokenBudget) {
                    break;
                }
                selectedHistory.add(0, line);
                used = next;
            }
        }
        return new ContextSnapshot(current, stateText, selectedHistory, requestText,
                used, tokenBudget);
    }

    private String stateText(SlotBundle knownSlots, SessionState state) {
        Map<String, Object> values = new LinkedHashMap<>();
        if (state != null) {
            values.put("phase", state.phase());
            values.put("sourceMode", state.sourceMode());
            values.put("currentIntent", state.currentIntent());
            values.put("lastRecommendations", state.lastRecommendations());
        }
        values.put("knownSlots", knownSlots == null ? SlotBundle.empty() : knownSlots);
        return jsonWithoutSensitive(values);
    }

    private String jsonWithoutSensitive(Map<String, ?> values) {
        if (values == null || values.isEmpty()) {
            return "{}";
        }
        Map<String, Object> safe = new LinkedHashMap<>();
        values.forEach((key, value) -> {
            String normalized = key == null ? "" : key.toLowerCase(Locale.ROOT);
            if (SENSITIVE_KEYS.stream().noneMatch(normalized::contains)) {
                safe.put(key, value);
            }
        });
        try {
            return objectMapper.writeValueAsString(safe);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private String compact(String value, int maxChars) {
        if (value == null) {
            return "";
        }
        String normalized = value.replace("\r", " ").replace("\n", " ").trim();
        return normalized.length() <= maxChars ? normalized : normalized.substring(0, maxChars);
    }

    private int estimate(String value) {
        return Math.max(1, (value == null ? 0 : value.length() + CHARS_PER_TOKEN - 1) / CHARS_PER_TOKEN);
    }
}
