package com.diet.service.idempotency;

import com.diet.model.ChatResponse;
import com.diet.exception.DietException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Single-instance request deduplication with ownership-scoped keys and TTL. */
@Service
public class RequestIdempotencyService {
    private record Entry(ChatResponse response, String fingerprint, Instant expiresAt) {}
    private final Map<String, Entry> entries = new ConcurrentHashMap<>();
    private final Duration ttl;
    private final int maxEntries;

    public RequestIdempotencyService(
            @Value("${diet.idempotency.ttl-seconds:600}") long ttlSeconds,
            @Value("${diet.idempotency.max-entries:10000}") int maxEntries) {
        this.ttl = Duration.ofSeconds(Math.max(30, ttlSeconds));
        this.maxEntries = Math.max(100, maxEntries);
    }

    public RequestIdempotencyService(long ttlSeconds) {
        this(ttlSeconds, 10000);
    }

    public Optional<ChatResponse> find(Long userId, String sessionId, String requestId, String fingerprint) {
        String key = key(userId, sessionId, requestId);
        Entry entry = key == null ? null : entries.get(key);
        if (entry == null) return Optional.empty();
        if (!entry.fingerprint().equals(fingerprint)) {
            throw new DietException("requestId 已被其他请求占用");
        }
        if (entry.expiresAt().isBefore(Instant.now())) {
            entries.remove(key, entry);
            return Optional.empty();
        }
        return Optional.of(entry.response());
    }

    public void store(Long userId, String sessionId, String requestId, String fingerprint, ChatResponse response) {
        String key = key(userId, sessionId, requestId);
        if (key != null && response != null) {
            cleanupExpired();
            if (entries.size() >= maxEntries) {
                entries.keySet().stream().findFirst().ifPresent(entries::remove);
            }
            entries.put(key, new Entry(response, fingerprint == null ? "" : fingerprint, Instant.now().plus(ttl)));
        }
    }

    private void cleanupExpired() {
        Instant now = Instant.now();
        entries.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
    }

    private String key(Long userId, String sessionId, String requestId) {
        if (userId == null || sessionId == null || sessionId.isBlank()
                || requestId == null || requestId.isBlank() || requestId.length() > 128) return null;
        return userId + "|" + sessionId + "|" + requestId;
    }
}
