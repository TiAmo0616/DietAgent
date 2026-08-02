package com.diet.context;

import java.util.List;

/**
 * A bounded, already-sanitized context passed to an LLM node.
 * The snapshot deliberately contains presentation strings instead of mutable
 * domain objects so a prompt cannot accidentally change session state.
 */
public record ContextSnapshot(
        String currentRequest,
        String sessionState,
        List<String> recentTurns,
        String requestContext,
        int estimatedTokens,
        int tokenBudget
) {
    public ContextSnapshot {
        currentRequest = currentRequest == null ? "" : currentRequest;
        sessionState = sessionState == null ? "{}" : sessionState;
        recentTurns = recentTurns == null ? List.of() : List.copyOf(recentTurns);
        requestContext = requestContext == null ? "{}" : requestContext;
    }

    public String toPromptBlock() {
        return """
                <context>
                currentRequest: %s
                sessionState: %s
                recentTurns: %s
                requestContext: %s
                estimatedTokens: %d/%d
                </context>
                """.formatted(currentRequest, sessionState, recentTurns, requestContext,
                estimatedTokens, tokenBudget);
    }
}
