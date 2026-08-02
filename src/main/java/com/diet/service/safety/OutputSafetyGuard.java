package com.diet.service.safety;

import java.util.Map;

/** Deterministic post-processing for high-risk medical claims in LLM text. */
public final class OutputSafetyGuard {
    private static final Map<String, String> REPLACEMENTS = Map.of(
            "治愈", "辅助改善",
            "根治", "辅助管理",
            "替代药物", "不能替代药物",
            "保证降糖", "可能有助于控制血糖",
            "保证减肥", "可作为体重管理中的饮食参考"
    );

    private OutputSafetyGuard() {}

    public static String sanitizeSpeech(String text) {
        if (text == null || text.isBlank()) return "";
        String safe = text;
        for (var replacement : REPLACEMENTS.entrySet()) {
            safe = safe.replace(replacement.getKey(), replacement.getValue());
        }
        return safe;
    }
}
