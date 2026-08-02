package com.diet.skill;

import com.diet.enums.Intent;
import com.diet.skill.model.LoadedSkill;
import com.diet.skill.model.SkillExecutionContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillOrchestrationService {

    private final SkillRegistry registry;
    private final SkillLoader loader;
    private final SkillRouter router = new SkillRouter();

    public SkillOrchestrationService(SkillRegistry registry, SkillLoader loader) {
        this.registry = registry;
        this.loader = loader;
    }

    public Optional<SkillExecutionContext> resolve(Intent intent) {
        Optional<String> route = router.route(intent);
        if (route.isEmpty() || registry.findByName(route.get()).isEmpty()) {
            return Optional.empty();
        }
        Optional<LoadedSkill> loaded = loader.load(route.get());
        return loaded.map(skill -> new SkillExecutionContext(
                skill.metadata().name(),
                skill.metadata().version(),
                skill.instructions(),
                skill.metadata().allowedTools()));
    }
}
