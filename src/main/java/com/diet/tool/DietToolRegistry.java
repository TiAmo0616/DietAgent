package com.diet.tool;

import com.diet.enums.SourceMode;
import com.diet.exception.DietException;
import com.diet.model.MealRankRequest;
import com.diet.model.MealSearchRequest;
import com.diet.service.meal.MealRankService;
import com.diet.service.meal.MealSearchService;
import com.diet.service.meal.MealService;
import com.diet.service.risk.RiskGuardService;
import com.diet.service.slot.SlotOptionService;
import com.diet.service.trace.AgentTraceService;
import com.diet.skill.SkillPolicyService;
import com.diet.skill.model.SkillExecutionContext;
import com.diet.model.ToolTracePayload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DietToolRegistry {

    private final MealSearchService mealSearchService;
    private final MealRankService mealRankService;
    private final MealService mealService;
    private final SlotOptionService slotOptionService;
    private final RiskGuardService riskGuardService;
    private final SkillPolicyService policyService;
    private final AgentTraceService traceService;

    public DietToolRegistry(MealSearchService mealSearchService, MealRankService mealRankService,
                            MealService mealService, SlotOptionService slotOptionService,
                            RiskGuardService riskGuardService, SkillPolicyService policyService,
                            AgentTraceService traceService) {
        this.mealSearchService = mealSearchService;
        this.mealRankService = mealRankService;
        this.mealService = mealService;
        this.slotOptionService = slotOptionService;
        this.riskGuardService = riskGuardService;
        this.policyService = policyService;
        this.traceService = traceService;
    }

    public DietToolResult call(DietToolCall call, ToolCallContext context) {
        validateContext(call, context);
        traceService.recordToolEvent("TOOL_CALL_REQUESTED", tracePayload(call, context, "REQUESTED", null, null, null));
        try {
            policyService.requireAllowed(context.skill(), call.toolName());
        } catch (RuntimeException error) {
            traceService.recordToolEvent("TOOL_CALL_DENIED", tracePayload(call, context, "DENIED", null, null, error));
            throw error;
        }
        traceService.recordToolEvent("TOOL_CALL_AUTHORIZED", tracePayload(call, context, "AUTHORIZED", null, null, null));
        long startedAt = System.nanoTime();
        try {
            Object data = switch (call.toolName()) {
                case SEARCH_MEALS -> search(((DietToolCall.SearchMeals) call).request(), context);
                case RANK_MEALS -> rank(((DietToolCall.RankMeals) call).request());
                case GET_MEAL_DETAIL -> detail(((DietToolCall.GetMealDetail) call).mealId(), context);
                case GET_SLOT_OPTIONS -> slotOptions();
                case CHECK_HEALTH_RISK -> {
                    DietToolCall.CheckHealthRisk risk = (DietToolCall.CheckHealthRisk) call;
                    yield riskGuardService.check(risk.userInput(), risk.intent(), risk.recommendResult(), risk.responseResult());
                }
            };
            long latencyMs = elapsedMs(startedAt);
            int resultCount = resultCount(data);
            traceService.recordToolEvent("TOOL_CALL_SUCCEEDED",
                    tracePayload(call, context, "SUCCEEDED", resultCount, latencyMs, null));
            return new DietToolResult(call.toolName(), data, resultCount, latencyMs);
        } catch (RuntimeException error) {
            traceService.recordToolEvent("TOOL_CALL_FAILED",
                    tracePayload(call, context, "FAILED", null, elapsedMs(startedAt), error));
            throw error;
        }
    }

    private Object search(MealSearchRequest request, ToolCallContext context) {
        if (request == null || request.slots() == null || context.sourceMode() == null) {
            throw new DietException("search_meals 参数非法");
        }
        if (context.sourceMode() == SourceMode.PERSONAL && context.userId() == null) {
            throw new DietException("PERSONAL 工具调用缺少可信用户");
        }
        return mealSearchService.search(new MealSearchRequest(
                context.sourceMode(), context.sourceMode() == SourceMode.PERSONAL ? context.userId() : null,
                request.slots(), request.excludeMealIds()));
    }

    private Object rank(MealRankRequest request) {
        if (request == null || request.candidates() == null || request.slots() == null) {
            throw new DietException("rank_meals 参数非法");
        }
        return mealRankService.rank(request);
    }

    private Object detail(Long mealId, ToolCallContext context) {
        if (mealId == null || context.sourceMode() == null) {
            throw new DietException("get_meal_detail 参数非法");
        }
        var item = mealService.findAccessibleMeal(context.userId(), context.sourceMode(), mealId);
        if (item == null) {
            throw new DietException("餐食不存在或无权访问");
        }
        return item;
    }

    private Map<String, List<String>> slotOptions() {
        return slotOptionService.findAllOptions();
    }

    private void validateContext(DietToolCall call, ToolCallContext context) {
        if (call == null || context == null || context.source() == null || context.traceId() == null
                || context.traceId().isBlank() || context.skill() == null) {
            throw new DietException("工具调用上下文非法");
        }
    }

    private int resultCount(Object data) {
        if (data instanceof List<?> list) return list.size();
        if (data instanceof Map<?, ?> map) return map.size();
        return data == null ? 0 : 1;
    }

    private long elapsedMs(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000;
    }

    private ToolTracePayload tracePayload(DietToolCall call, ToolCallContext context, String status,
                                          Integer resultCount, Long latencyMs, Exception error) {
        return new ToolTracePayload(
                context.skill().skillName(), context.skill().skillVersion(), call.toolName().wireName(),
                context.source().name(), status, argumentKeys(call), resultCount, latencyMs,
                error == null ? null : error.getClass().getSimpleName());
    }

    private List<String> argumentKeys(DietToolCall call) {
        return switch (call.toolName()) {
            case SEARCH_MEALS -> List.of("sourceMode", "slots", "excludeMealIds");
            case RANK_MEALS -> List.of("candidates", "slots", "excludeMealIds");
            case GET_MEAL_DETAIL -> List.of("mealId");
            case GET_SLOT_OPTIONS -> List.of();
            case CHECK_HEALTH_RISK -> List.of("intent", "recommendResult", "responseResult");
        };
    }
}
