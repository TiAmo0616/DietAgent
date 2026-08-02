package com.diet.tool;

public record DietToolResult(DietToolName toolName, Object data, int resultCount, long latencyMs) {
}
