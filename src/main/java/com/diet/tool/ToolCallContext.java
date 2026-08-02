package com.diet.tool;

import com.diet.enums.SourceMode;
import com.diet.skill.model.SkillExecutionContext;

public record ToolCallContext(
        Long userId,
        String traceId,
        SourceMode sourceMode,
        ToolCallSource source,
        SkillExecutionContext skill
) {
}
