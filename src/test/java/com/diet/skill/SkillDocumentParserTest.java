package com.diet.skill;

import com.diet.exception.DietException;
import com.diet.skill.model.LoadedSkill;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkillDocumentParserTest {

    private final SkillDocumentParser parser = new SkillDocumentParser();

    @Test
    void parsesMetadataAndMarkdownBody() {
        LoadedSkill skill = parser.parse("diet/skills/demo/SKILL.md", "---\n"
                + "name: demo\n"
                + "description: test\n"
                + "version: v1\n"
                + "allowed-tools:\n"
                + "  - search_meals\n"
                + "---\n"
                + "Step one.\n");

        assertEquals("demo", skill.metadata().name());
        assertEquals("test", skill.metadata().description());
        assertEquals("v1", skill.metadata().version());
        assertEquals(Set.of("search_meals"), skill.metadata().allowedTools());
        assertEquals("Step one.", skill.instructions().trim());
    }

    @Test
    void rejectsMissingRequiredMetadata() {
        assertThrows(DietException.class, () -> parser.parse("diet/skills/demo/SKILL.md",
                "---\nname: demo\nversion: v1\nallowed-tools:\n  - search_meals\n---\nbody"));
    }

    @Test
    void rejectsUnknownToolsAndUnsafePaths() {
        assertThrows(DietException.class, () -> parser.parse("diet/skills/demo/SKILL.md",
                "---\nname: demo\ndescription: test\nversion: v1\nallowed-tools:\n  - delete_database\n---\nbody"));
        assertThrows(DietException.class, () -> parser.parse("../secret/SKILL.md",
                "---\nname: demo\ndescription: test\nversion: v1\nallowed-tools:\n  - search_meals\n---\nbody"));
    }

    @Test
    void rejectsDuplicateFrontmatterKeys() {
        assertThrows(DietException.class, () -> parser.parse("diet/skills/demo/SKILL.md",
                "---\nname: demo\nname: duplicate\ndescription: test\nversion: v1\n"
                        + "allowed-tools:\n  - search_meals\n---\nbody"));
    }
}
