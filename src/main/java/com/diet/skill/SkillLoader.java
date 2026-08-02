package com.diet.skill;

import com.diet.exception.DietException;
import com.diet.skill.model.LoadedSkill;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class SkillLoader {

    private final SkillDocumentParser parser;

    public SkillLoader(SkillDocumentParser parser) {
        this.parser = parser;
    }

    public Optional<LoadedSkill> load(String skillName) {
        if (skillName == null || !skillName.matches("[a-z0-9]+(?:-[a-z0-9]+)*")) {
            return Optional.empty();
        }
        String resourcePath = "diet/skills/" + skillName + "/SKILL.md";
        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            if (!resource.exists()) {
                return Optional.empty();
            }
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return Optional.of(parser.parse(resourcePath, content));
        } catch (IOException error) {
            throw new DietException("Skill 正文读取失败: " + skillName, error);
        } catch (DietException error) {
            return Optional.empty();
        }
    }
}
