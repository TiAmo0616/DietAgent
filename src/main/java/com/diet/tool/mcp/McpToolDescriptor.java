package com.diet.tool.mcp;

import java.util.Map;

/** Transport-neutral descriptor matching the MCP tools/list shape. */
public record McpToolDescriptor(String name, String description, Map<String, Object> inputSchema,
                                boolean readOnly) {
    public McpToolDescriptor {
        inputSchema = inputSchema == null ? Map.of() : Map.copyOf(inputSchema);
    }
}
