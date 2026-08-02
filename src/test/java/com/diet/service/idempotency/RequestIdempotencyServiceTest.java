package com.diet.service.idempotency;

import com.diet.model.ChatResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestIdempotencyServiceTest {
    @Test
    void returnsStoredResponseOnlyForSameOwnerAndRequest() {
        RequestIdempotencyService service = new RequestIdempotencyService(600);
        ChatResponse response = ChatResponse.answer("s1", "ok", null, "DONE");
        service.store(7L, "s1", "req-1", response);

        assertEquals(response, service.find(7L, "s1", "req-1").orElseThrow());
        assertTrue(service.find(8L, "s1", "req-1").isEmpty());
        assertTrue(service.find(7L, "s2", "req-1").isEmpty());
    }
}
