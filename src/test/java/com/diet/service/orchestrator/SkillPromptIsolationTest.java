package com.diet.service.orchestrator;

import com.diet.agent.factory.AgentFactory;
import com.diet.service.recommend.RecommendResponseAgentService;
import com.diet.service.trace.AgentTraceService;
import com.diet.skill.model.SkillExecutionContext;
import com.diet.util.LlmJsonService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SkillPromptIsolationTest {

    @Test
    void emptySkillInstructionsPreservePromptAndNonEmptyInstructionsAppearOnce() throws Exception {
        RecommendResponseAgentService service = new RecommendResponseAgentService(
                mock(AgentFactory.class), mock(LlmJsonService.class), mock(AgentTraceService.class), "test-model");
        Method method = RecommendResponseAgentService.class.getDeclaredMethod(
                "appendSkillInstructions", String.class, SkillExecutionContext.class);
        method.setAccessible(true);

        assertEquals("base", method.invoke(service, "base", null));
        SkillExecutionContext context = new SkillExecutionContext("demo", "v1", "use only real candidates", Set.of("search_meals"));
        String result = (String) method.invoke(service, "base", context);
        assertTrue(result.contains("<skill_constraints>"));
        assertEquals(1, result.split("<skill_constraints>", -1).length - 1);
    }
}
