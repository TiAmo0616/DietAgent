package com.diet.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMemoryRow {
    private Long id;
    private Long userId;
    private String memoryKey;
    private String memoryValue;
    private String source;
    private LocalDateTime updatedAt;
}
