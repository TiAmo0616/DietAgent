package com.diet.skill;

import com.diet.exception.DietException;
import com.diet.skill.model.LoadedSkill;
import com.diet.skill.model.SkillMetadata;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SkillDocumentParser {

    private static final Set<String> ALLOWED_KEYS = Set.of("name", "description", "version", "allowed-tools");
    private static final Set<String> ALLOWED_TOOLS = Set.of(
            "search_meals", "rank_meals", "get_meal_detail", "get_slot_options", "check_health_risk");

    public LoadedSkill parse(String resourcePath, String content) {
        validatePath(resourcePath);
        if (content == null || content.isBlank()) {
            throw new DietException("Skill 文档不能为空");
        }

        String normalized = content.replace("\r\n", "\n");
        String[] lines = normalized.split("\n", -1);
        if (lines.length < 3 || !"---".equals(lines[0].trim())) {
            throw new DietException("Skill 文档缺少 frontmatter");
        }

        int closingIndex = -1;
        for (int i = 1; i < lines.length; i++) {
            if ("---".equals(lines[i].trim())) {
                closingIndex = i;
                break;
            }
        }
        if (closingIndex < 0) {
            throw new DietException("Skill frontmatter 未闭合");
        }

        Map<String, String> scalars = new java.util.LinkedHashMap<>();
        Set<String> seenKeys = new HashSet<>();
        List<String> tools = new ArrayList<>();
        String activeKey = null;
        for (int i = 1; i < closingIndex; i++) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("-")) {
                if (!"allowed-tools".equals(activeKey)) {
                    throw new DietException("Skill 列表字段非法");
                }
                String tool = unquote(line.substring(1).trim());
                if (tool.isBlank()) {
                    throw new DietException("Skill 工具名不能为空");
                }
                tools.add(tool);
                continue;
            }
            int colon = line.indexOf(':');
            if (colon <= 0) {
                throw new DietException("Skill frontmatter 字段格式非法");
            }
            String key = line.substring(0, colon).trim();
            if (!ALLOWED_KEYS.contains(key) || !seenKeys.add(key)) {
                throw new DietException("Skill frontmatter 字段非法或重复: " + key);
            }
            activeKey = key;
            String value = unquote(line.substring(colon + 1).trim());
            if (!"allowed-tools".equals(key)) {
                if (value.isBlank()) {
                    throw new DietException("Skill 字段不能为空: " + key);
                }
                scalars.put(key, value);
            } else if (!value.isBlank()) {
                for (String item : value.split(",")) {
                    tools.add(unquote(item.trim()));
                }
            }
        }

        require(scalars, "name");
        require(scalars, "description");
        require(scalars, "version");
        if (tools.isEmpty()) {
            throw new DietException("Skill allowed-tools 不能为空");
        }
        if (!ALLOWED_TOOLS.containsAll(tools) || new HashSet<>(tools).size() != tools.size()) {
            throw new DietException("Skill 包含未知或重复工具");
        }

        StringBuilder body = new StringBuilder();
        for (int i = closingIndex + 1; i < lines.length; i++) {
            body.append(lines[i]);
            if (i < lines.length - 1) {
                body.append('\n');
            }
        }
        SkillMetadata metadata = new SkillMetadata(
                scalars.get("name"), scalars.get("description"), scalars.get("version"), Set.copyOf(tools), resourcePath);
        return new LoadedSkill(metadata, body.toString().strip());
    }

    private void validatePath(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank() || resourcePath.startsWith("/")
                || resourcePath.startsWith("\\") || resourcePath.contains(":")) {
            throw new DietException("Skill 资源路径非法");
        }
        for (String segment : resourcePath.replace('\\', '/').split("/")) {
            if ("..".equals(segment) || segment.isBlank()) {
                throw new DietException("Skill 资源路径越界");
            }
        }
    }

    private void require(Map<String, String> values, String key) {
        if (!values.containsKey(key)) {
            throw new DietException("Skill 缺少必填字段: " + key);
        }
    }

    private String unquote(String value) {
        if (value.length() >= 2 && ((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'")))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
