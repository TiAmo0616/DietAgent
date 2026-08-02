package com.diet.tool;

import com.diet.enums.SourceMode;
import com.diet.exception.DietException;
import com.diet.model.MealItem;
import com.diet.model.SlotBundle;
import com.diet.service.meal.MealSearchService;
import com.diet.service.meal.MealService;
import com.diet.service.meal.MealRankService;
import com.diet.service.risk.RiskGuardService;
import com.diet.service.slot.SlotOptionService;
import com.diet.skill.model.SkillExecutionContext;
import com.diet.skill.SkillPolicyService;
import com.diet.service.trace.AgentTraceService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class DietToolRegistryTest {

    @Test
    void retriesTransientToolFailureButNotPolicyErrors() throws Exception {
        MealSearchService searchService = mock(MealSearchService.class);
        org.mockito.Mockito.when(searchService.search(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new IllegalStateException("temporary"))
                .thenReturn(List.of());
        DietToolRegistry registry = new DietToolRegistry(searchService, mock(MealRankService.class),
                mock(MealService.class), mock(SlotOptionService.class), mock(RiskGuardService.class),
                new SkillPolicyService(), mock(AgentTraceService.class));
        var field = DietToolRegistry.class.getDeclaredField("maxRetries");
        field.setAccessible(true);
        field.setInt(registry, 1);
        ToolCallContext context = new ToolCallContext(7L, "trace-retry", SourceMode.PUBLIC, ToolCallSource.INTERNAL,
                new SkillExecutionContext("meal-recommendation", "v1", "", Set.of(DietToolName.SEARCH_MEALS.wireName())));

        assertDoesNotThrow(() -> registry.call(new DietToolCall.SearchMeals(
                new com.diet.model.MealSearchRequest(SourceMode.PUBLIC, null, SlotBundle.empty(), List.of())), context));
        org.mockito.Mockito.verify(searchService, org.mockito.Mockito.times(2)).search(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deniedToolDoesNotCallBusinessService() {
        MealSearchService searchService = mock(MealSearchService.class);
        DietToolRegistry registry = new DietToolRegistry(searchService, mock(MealRankService.class),
                mock(MealService.class), mock(SlotOptionService.class), mock(RiskGuardService.class),
                new SkillPolicyService(), mock(AgentTraceService.class));
        ToolCallContext context = new ToolCallContext(7L, "trace-test", SourceMode.PUBLIC, ToolCallSource.INTERNAL,
                new SkillExecutionContext("health-risk-response", "v1", "", Set.of(DietToolName.CHECK_HEALTH_RISK.wireName())));

        assertThrows(DietException.class, () -> registry.call(
                new DietToolCall.SearchMeals(new com.diet.model.MealSearchRequest(
                        SourceMode.PUBLIC, null, SlotBundle.empty(), List.of())), context));
        verifyNoInteractions(searchService);
    }

    @Test
    void authorizedToolCanDispatch() {
        MealSearchService searchService = mock(MealSearchService.class);
        org.mockito.Mockito.when(searchService.search(org.mockito.ArgumentMatchers.any()))
                .thenReturn(List.of(new MealItem(1L, SourceMode.PUBLIC, null, "meal", SlotBundle.empty(), 0)));
        DietToolRegistry registry = new DietToolRegistry(searchService, mock(MealRankService.class),
                mock(MealService.class), mock(SlotOptionService.class), mock(RiskGuardService.class),
                new SkillPolicyService(), mock(AgentTraceService.class));
        ToolCallContext context = new ToolCallContext(7L, "trace-test", SourceMode.PUBLIC, ToolCallSource.INTERNAL,
                new SkillExecutionContext("meal-recommendation", "v1", "", Set.of(DietToolName.SEARCH_MEALS.wireName())));

        registry.call(new DietToolCall.SearchMeals(new com.diet.model.MealSearchRequest(
                SourceMode.PUBLIC, null, SlotBundle.empty(), List.of())), context);
    }
}
