package com.diet.skill;

import com.diet.exception.DietException;
import com.diet.skill.model.SkillExecutionContext;
import com.diet.tool.DietToolName;
import org.springframework.stereotype.Service;

@Service
public class SkillPolicyService {

    public void requireAllowed(SkillExecutionContext skill, DietToolName toolName) {
        if (skill == null || toolName == null || !skill.allowedTools().contains(toolName.wireName())) {
            throw new DietException("工具未被当前 Skill 授权: " + (toolName == null ? "unknown" : toolName.wireName()));
        }
    }
}
