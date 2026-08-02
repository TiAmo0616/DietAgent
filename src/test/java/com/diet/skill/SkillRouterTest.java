package com.diet.skill;

import com.diet.enums.Intent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillRouterTest {

    private final SkillRouter router = new SkillRouter();

    @Test
    void mapsRecommendationAndClarificationToSameSkill() {
        assertEquals("meal-recommendation", router.route(Intent.MEAL_RECOMMENDATION).orElseThrow());
        assertEquals("meal-recommendation", router.route(Intent.CLARIFY_NEEDED).orElseThrow());
    }

    @Test
    void mapsAllSupportedDomainIntents() {
        assertEquals("meal-adjustment", router.route(Intent.MEAL_ADJUST).orElseThrow());
        assertEquals("meal-planning", router.route(Intent.MEAL_PLAN).orElseThrow());
        assertEquals("health-risk-response", router.route(Intent.HEALTH_RISK).orElseThrow());
        assertTrue(router.route(Intent.OTHER).isEmpty());
    }
}
