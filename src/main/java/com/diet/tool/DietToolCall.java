package com.diet.tool;

import com.diet.enums.Intent;
import com.diet.model.MealRankRequest;
import com.diet.model.MealSearchRequest;
import com.diet.model.RecommendResult;
import com.diet.model.ResponseResult;

public sealed interface DietToolCall permits DietToolCall.SearchMeals, DietToolCall.RankMeals,
        DietToolCall.GetMealDetail, DietToolCall.GetSlotOptions, DietToolCall.CheckHealthRisk {

    DietToolName toolName();

    record SearchMeals(MealSearchRequest request) implements DietToolCall {
        @Override
        public DietToolName toolName() { return DietToolName.SEARCH_MEALS; }
    }

    record RankMeals(MealRankRequest request) implements DietToolCall {
        @Override
        public DietToolName toolName() { return DietToolName.RANK_MEALS; }
    }

    record GetMealDetail(Long mealId) implements DietToolCall {
        @Override
        public DietToolName toolName() { return DietToolName.GET_MEAL_DETAIL; }
    }

    record GetSlotOptions() implements DietToolCall {
        @Override
        public DietToolName toolName() { return DietToolName.GET_SLOT_OPTIONS; }
    }

    record CheckHealthRisk(String userInput, Intent intent, RecommendResult recommendResult,
                           ResponseResult responseResult) implements DietToolCall {
        @Override
        public DietToolName toolName() { return DietToolName.CHECK_HEALTH_RISK; }
    }
}
