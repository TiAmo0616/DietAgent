package com.diet.tool.mcp;

import com.diet.exception.DietException;
import com.diet.tool.DietToolCall;
import com.diet.tool.DietToolName;
import com.diet.tool.DietToolRegistry;
import com.diet.tool.DietToolResult;
import com.diet.tool.ToolCallContext;
import com.diet.tool.ToolCallSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * MCP boundary for read-only diet tools. It deliberately does not implement
 * a network transport; an MCP HTTP/stdio adapter can depend on this gateway.
 */
@Service
public class McpReadOnlyToolGateway {
    private final DietToolRegistry registry;

    public McpReadOnlyToolGateway(DietToolRegistry registry) {
        this.registry = registry;
    }

    public List<McpToolDescriptor> listTools() {
        return List.of(
                descriptor(DietToolName.SEARCH_MEALS, "按槽位检索可访问餐食", List.of("slots", "excludeMealIds")),
                descriptor(DietToolName.RANK_MEALS, "按槽位对候选餐食排序", List.of("candidates", "slots")),
                descriptor(DietToolName.GET_MEAL_DETAIL, "查询单道可访问餐食详情", List.of("mealId")),
                descriptor(DietToolName.GET_SLOT_OPTIONS, "查询合法槽位候选值", List.of()),
                descriptor(DietToolName.CHECK_HEALTH_RISK, "检查饮食建议中的健康风险", List.of("intent"))
        );
    }

    public DietToolResult call(DietToolCall call, ToolCallContext context) {
        if (call == null || context == null || context.source() != ToolCallSource.MCP) {
            throw new DietException("MCP 工具调用必须使用 MCP 来源上下文");
        }
        if (listTools().stream().noneMatch(tool -> tool.name().equals(call.toolName().wireName()))) {
            throw new DietException("工具未通过 MCP 只读白名单");
        }
        return registry.call(call, context);
    }

    private McpToolDescriptor descriptor(DietToolName name, String description, List<String> required) {
        return new McpToolDescriptor(name.wireName(), description,
                Map.of("type", "object", "required", required), true);
    }
}
