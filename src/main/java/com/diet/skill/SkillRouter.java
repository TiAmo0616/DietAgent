package com.diet.skill;

import com.diet.enums.Intent;

import java.util.Map;
import java.util.Optional;

public class SkillRouter {

    private static final Map<Intent, String> ROUTES = Map.of(
            Intent.MEAL_RECOMMENDATION, "meal-recommendation",
            Intent.CLARIFY_NEEDED, "meal-recommendation",
            Intent.MEAL_ADJUST, "meal-adjustment",
            Intent.MEAL_PLAN, "meal-planning",
            Intent.HEALTH_RISK, "health-risk-response");

    public Optional<String> route(Intent intent) {
        return Optional.ofNullable(intent).map(ROUTES::get);
    }
}
