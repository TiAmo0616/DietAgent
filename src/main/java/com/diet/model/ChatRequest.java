package com.diet.model;

import java.util.Map;

import com.diet.enums.SourceMode;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    /** 客户端幂等键，同一用户/会话内用于去重重复提交。 */
    private String requestId;
    private String sessionId;
    private String message;
    private SourceMode sourceMode;
    private Map<String, Object> context;
}
