package com.diet.tool.mcp;

import com.diet.enums.SourceMode;
import com.diet.exception.DietException;
import com.diet.skill.model.SkillExecutionContext;
import com.diet.tool.DietToolCall;
import com.diet.tool.DietToolRegistry;
import com.diet.tool.DietToolResult;
import com.diet.tool.ToolCallContext;
import com.diet.tool.ToolCallSource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class McpReadOnlyToolGatewayTest {
    @Test
    void exposesOnlyDeclaredReadOnlyToolsAndForwardsMcpContext() {
        DietToolRegistry registry = mock(DietToolRegistry.class);
        McpReadOnlyToolGateway gateway = new McpReadOnlyToolGateway(registry);
        SkillExecutionContext skill = new SkillExecutionContext("meal-recommendation", "v1", "", Set.of("get_slot_options"));
        ToolCallContext context = new ToolCallContext(7L, "trace-mcp", SourceMode.PUBLIC, ToolCallSource.MCP, skill);
        DietToolCall call = new DietToolCall.GetSlotOptions();
        DietToolResult result = new DietToolResult(call.toolName(), List.of(), 0, 1L);
        when(registry.call(call, context)).thenReturn(result);

        assertEquals(5, gateway.listTools().size());
        assertEquals(result, gateway.call(call, context));
    }

    @Test
    void rejectsInternalCallsAtMcpBoundary() {
        McpReadOnlyToolGateway gateway = new McpReadOnlyToolGateway(mock(DietToolRegistry.class));
        SkillExecutionContext skill = new SkillExecutionContext("meal-recommendation", "v1", "", Set.of("get_slot_options"));
        ToolCallContext internal = new ToolCallContext(7L, "trace", SourceMode.PUBLIC, ToolCallSource.INTERNAL, skill);

        assertThrows(DietException.class, () -> gateway.call(new DietToolCall.GetSlotOptions(), internal));
    }
}
