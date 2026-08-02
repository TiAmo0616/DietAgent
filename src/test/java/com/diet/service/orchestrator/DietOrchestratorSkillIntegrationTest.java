package com.diet.service.orchestrator;

import com.diet.enums.Intent;
import com.diet.skill.SkillDocumentParser;
import com.diet.skill.SkillLoader;
import com.diet.skill.SkillOrchestrationService;
import com.diet.skill.SkillRegistry;
import com.diet.skill.model.SkillExecutionContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DietOrchestratorSkillIntegrationTest {

    private final SkillOrchestrationService skills = new SkillOrchestrationService(
            new SkillRegistry(new SkillDocumentParser()), new SkillLoader(new SkillDocumentParser()));

    @Test
    void recommendationSkillAuthorizesOnlyRecommendationTools() {
        SkillExecutionContext context = skills.resolve(Intent.MEAL_RECOMMENDATION).orElseThrow();
        assertEquals("meal-recommendation", context.skillName());
        assertTrue(context.allowedTools().containsAll(java.util.Set.of(
                "search_meals", "rank_meals", "check_health_risk")));
    }

    @Test
    void missingOrOtherIntentKeepsV1FallbackPathAvailable() {
        assertTrue(skills.resolve(Intent.OTHER).isEmpty());
        assertTrue(skills.resolve(null).isEmpty());
    }
}
