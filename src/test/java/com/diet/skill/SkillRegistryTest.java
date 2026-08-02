package com.diet.skill;

import com.diet.enums.Intent;
import com.diet.skill.model.SkillExecutionContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillRegistryTest {

    @Test
    void discoversBundledSkillsAndLoadsOnDemand() {
        SkillRegistry registry = new SkillRegistry(new SkillDocumentParser());
        SkillLoader loader = new SkillLoader(new SkillDocumentParser());
        SkillOrchestrationService service = new SkillOrchestrationService(registry, loader);

        assertEquals("meal-recommendation", registry.findByIntent(Intent.MEAL_RECOMMENDATION).orElseThrow().name());
        SkillExecutionContext context = service.resolve(Intent.MEAL_RECOMMENDATION).orElseThrow();
        assertEquals("meal-recommendation", context.skillName());
        assertTrue(context.instructions().contains("真实候选"));
    }

    @Test
    void otherIntentDoesNotLoadAnySkill() {
        SkillOrchestrationService service = new SkillOrchestrationService(
                new SkillRegistry(new SkillDocumentParser()), new SkillLoader(new SkillDocumentParser()));
        assertTrue(service.resolve(Intent.OTHER).isEmpty());
    }
}
