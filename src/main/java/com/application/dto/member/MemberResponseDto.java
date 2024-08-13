package com.application.dto.member;

import java.time.LocalDateTime;

public record MemberResponseDto(
        Long id,
        String name,
        LocalDateTime startedAt
) {
}
