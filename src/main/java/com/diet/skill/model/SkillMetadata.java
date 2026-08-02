package com.diet.skill.model;

import java.util.Set;

public record SkillMetadata(
        String name,
        String description,
        String version,
        Set<String> allowedTools,
        String resourcePath
) {
    public SkillMetadata {
        allowedTools = Set.copyOf(allowedTools);
    }
}
