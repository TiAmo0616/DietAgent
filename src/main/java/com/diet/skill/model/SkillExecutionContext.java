package com.diet.skill.model;

import java.util.Set;

public record SkillExecutionContext(
        String skillName,
        String skillVersion,
        String instructions,
        Set<String> allowedTools
) {
    public SkillExecutionContext {
        allowedTools = Set.copyOf(allowedTools);
    }
}
