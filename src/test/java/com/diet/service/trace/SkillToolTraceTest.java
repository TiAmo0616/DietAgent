package com.diet.service.trace;

import com.diet.enums.Intent;
import com.diet.mapper.AgentTraceMapper;
import com.diet.model.RequestTraceRow;
import com.diet.model.ToolTracePayload;
import com.diet.skill.model.SkillExecutionContext;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SkillToolTraceTest {

    @Test
    void recordsSkillAndToolEventsWithoutSensitivePayload() {
        AgentTraceMapper mapper = mock(AgentTraceMapper.class);
        AgentTraceService service = new AgentTraceService(mapper, new com.fasterxml.jackson.databind.ObjectMapper());
        SkillExecutionContext skill = new SkillExecutionContext("meal-recommendation", "v1", "do not expose password", Set.of("search_meals"));

        try (AgentTraceService.TraceScope ignored = service.openTrace("trace-test", "session-test", 7L)) {
            service.recordSkillEvent("SKILL_LOADED", skill, Intent.MEAL_RECOMMENDATION);
            service.recordToolEvent("TOOL_CALL_SUCCEEDED", new ToolTracePayload(
                    "meal-recommendation", "v1", "search_meals", "INTERNAL", "SUCCEEDED",
                    List.of("slots"), 3, 12L, null));
        }

        ArgumentCaptor<RequestTraceRow> captor = ArgumentCaptor.forClass(RequestTraceRow.class);
        verify(mapper).insert(captor.capture());
        String traceJson = captor.getValue().getTraceJson();
        assertTrue(traceJson.contains("SKILL_LOADED"));
        assertTrue(traceJson.contains("TOOL_CALL_SUCCEEDED"));
        assertFalse(traceJson.contains("password"));
        assertFalse(traceJson.contains("Bearer "));
    }
}
