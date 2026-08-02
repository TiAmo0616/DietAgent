package com.diet.tool;

public enum DietToolName {
    SEARCH_MEALS("search_meals"),
    RANK_MEALS("rank_meals"),
    GET_MEAL_DETAIL("get_meal_detail"),
    GET_SLOT_OPTIONS("get_slot_options"),
    CHECK_HEALTH_RISK("check_health_risk");

    private final String wireName;

    DietToolName(String wireName) {
        this.wireName = wireName;
    }

    public String wireName() {
        return wireName;
    }
}
