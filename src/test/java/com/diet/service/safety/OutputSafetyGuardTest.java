package com.diet.service.safety;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputSafetyGuardTest {
    @Test
    void rewritesMedicalPromises() {
        String safe = OutputSafetyGuard.sanitizeSpeech("这道菜可以治愈胃病，保证降糖，不能替代药物。");
        assertFalse(safe.contains("治愈"));
        assertFalse(safe.contains("保证降糖"));
        assertTrue(safe.contains("辅助改善"));
    }
}
