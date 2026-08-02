package com.diet.skill;

import com.diet.enums.Intent;
import com.diet.exception.DietException;
import com.diet.skill.model.SkillMetadata;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SkillRegistry {

    private final SkillRouter router = new SkillRouter();
    private final Map<String, SkillMetadata> metadataByName;

    public SkillRegistry(SkillDocumentParser parser) {
        this.metadataByName = Collections.unmodifiableMap(loadIndex(parser));
    }

    public Optional<SkillMetadata> findByIntent(Intent intent) {
        return router.route(intent).map(metadataByName::get);
    }

    public Optional<SkillMetadata> findByName(String skillName) {
        return Optional.ofNullable(metadataByName.get(skillName));
    }

    private Map<String, SkillMetadata> loadIndex(SkillDocumentParser parser) {
        Map<String, SkillMetadata> result = new HashMap<>();
        try {
            String index = new String(new ClassPathResource("diet/skills/index.txt").getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);
            for (String line : index.replace("\r\n", "\n").split("\n")) {
                String resourcePath = line.trim();
                if (resourcePath.isEmpty() || resourcePath.startsWith("#")) {
                    continue;
                }
                try {
                    SkillMetadata metadata = parser.parse(resourcePath,
                            new String(new ClassPathResource(resourcePath).getInputStream().readAllBytes(), StandardCharsets.UTF_8))
                            .metadata();
                    if (result.putIfAbsent(metadata.name(), metadata) != null) {
                        throw new DietException("Skill 名称重复: " + metadata.name());
                    }
                } catch (DietException | IOException invalidSkill) {
                    // 单个 Skill 无效时隔离该条目，保证其他合法 Skill 仍可用。
                }
            }
            return result;
        } catch (IOException | RuntimeException error) {
            if (error instanceof DietException dietException) {
                throw dietException;
            }
            throw new DietException("Skill 目录加载失败", error);
        }
    }
}
