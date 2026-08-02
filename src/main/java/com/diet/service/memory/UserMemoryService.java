package com.diet.service.memory;

import com.diet.mapper.UserMemoryMapper;
import com.diet.model.SlotBundle;
import com.diet.model.UserMemoryRow;
import com.diet.service.slot.SlotOptionService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Structured preference memory. Only allow-listed slot values can be stored;
 * free-form model text never becomes long-term memory.
 */
@Service
public class UserMemoryService {
    private static final Set<String> MEMORY_KEYS = Set.of("cuisine", "taste", "healthGoal", "convenience");

    private final UserMemoryMapper mapper;
    private final SlotOptionService slotOptionService;

    public UserMemoryService(UserMemoryMapper mapper, SlotOptionService slotOptionService) {
        this.mapper = mapper;
        this.slotOptionService = slotOptionService;
    }

    public Map<String, List<String>> load(Long userId) {
        if (userId == null) {
            return Map.of();
        }
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (UserMemoryRow row : mapper.listByUser(userId)) {
            if (row == null || !MEMORY_KEYS.contains(row.getMemoryKey())
                    || row.getMemoryValue() == null || row.getMemoryValue().isBlank()) {
                continue;
            }
            result.computeIfAbsent(row.getMemoryKey(), ignored -> new java.util.ArrayList<>())
                    .add(row.getMemoryValue());
        }
        return result;
    }

    public void learnFromSlots(Long userId, SlotBundle slots, String source) {
        if (userId == null || slots == null) {
            return;
        }
        Map<String, List<String>> options = slotOptionService.findAllOptions();
        save(userId, "cuisine", slots.cuisine(), options, source);
        save(userId, "taste", slots.taste(), options, source);
        save(userId, "healthGoal", slots.healthGoal(), options, source);
        save(userId, "convenience", slots.convenience(), options, source);
    }

    private void save(Long userId, String key, List<String> values,
                      Map<String, List<String>> options, String source) {
        if (!MEMORY_KEYS.contains(key) || values == null) {
            return;
        }
        List<String> allowed = options.getOrDefault(key, List.of());
        for (String value : values) {
            if (value == null || !allowed.contains(value)) {
                continue;
            }
            UserMemoryRow row = new UserMemoryRow();
            row.setUserId(userId);
            row.setMemoryKey(key);
            row.setMemoryValue(value);
            row.setSource(source == null || source.isBlank() ? "RULE_VALIDATED_SLOT" : source);
            mapper.upsert(row);
        }
    }
}
