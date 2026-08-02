package com.diet.model;

import java.util.List;

public record ToolTracePayload(
        String skillName,
        String skillVersion,
        String toolName,
        String source,
        String status,
        List<String> argumentKeys,
        Integer resultCount,
        Long latencyMs,
        String errorType
) {
    public ToolTracePayload {
        argumentKeys = argumentKeys == null ? List.of() : List.copyOf(argumentKeys);
    }
}
