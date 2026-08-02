package com.diet.context;

import com.diet.enums.SourceMode;
import com.diet.model.ConversationTurn;
import com.diet.model.SessionState;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextAssemblerTest {
    private final ContextAssembler assembler = new ContextAssembler(
            new com.fasterxml.jackson.databind.ObjectMapper(), 80);

    @Test
    void keepsCurrentAndStateBeforeOldHistoryWhenBudgetIsTight() {
        List<ConversationTurn> history = List.of(
                new ConversationTurn("user", null, "oldest ".repeat(300), 1L),
                new ConversationTurn("assistant", null, "newest", 2L));

        ContextSnapshot snapshot = assembler.assemble("我要低脂晚餐".repeat(100), null,
                SessionState.fresh("s1", 7L, SourceMode.PUBLIC), history, Map.of());

        assertTrue(snapshot.currentRequest().startsWith("我要低脂晚餐"));
        assertTrue(snapshot.sessionState().contains("PUBLIC"));
        assertFalse(snapshot.recentTurns().stream().anyMatch(turn -> turn.contains("oldest")));
        assertTrue(snapshot.estimatedTokens() <= snapshot.tokenBudget());
    }

    @Test
    void removesSensitiveRequestContextKeys() {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("traceId", "trace-1");
        context.put("password", "should-not-enter-prompt");
        context.put("Authorization", "Bearer secret");

        ContextSnapshot snapshot = assembler.assemble("hello", null, null, List.of(), context);

        assertTrue(snapshot.requestContext().contains("traceId"));
        assertFalse(snapshot.requestContext().contains("should-not-enter-prompt"));
        assertFalse(snapshot.requestContext().contains("Bearer secret"));
    }
}
