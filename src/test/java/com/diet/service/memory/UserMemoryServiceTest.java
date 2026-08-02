package com.diet.service.memory;

import com.diet.mapper.UserMemoryMapper;
import com.diet.model.SlotBundle;
import com.diet.service.slot.SlotOptionService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

class UserMemoryServiceTest {
    @Test
    void writesOnlyAllowListedSlotValues() {
        UserMemoryMapper mapper = mock(UserMemoryMapper.class);
        SlotOptionService options = mock(SlotOptionService.class);
        org.mockito.Mockito.when(options.findAllOptions()).thenReturn(Map.of(
                "taste", List.of("清淡"), "cuisine", List.of("川菜"),
                "healthGoal", List.of(), "convenience", List.of()));
        UserMemoryService service = new UserMemoryService(mapper, options);

        service.learnFromSlots(7L, new SlotBundle(List.of(), List.of(), List.of(), List.of(),
                List.of("川菜", "未在字典中的菜系"), List.of("清淡"), List.of()), "test");

        verify(mapper).upsert(argThat(row -> "cuisine".equals(row.getMemoryKey())
                && "川菜".equals(row.getMemoryValue())));
        verify(mapper).upsert(argThat(row -> "taste".equals(row.getMemoryKey())
                && "清淡".equals(row.getMemoryValue())));
        verify(mapper, never()).upsert(argThat(row -> "未在字典中的菜系".equals(row.getMemoryValue())));
    }
}
