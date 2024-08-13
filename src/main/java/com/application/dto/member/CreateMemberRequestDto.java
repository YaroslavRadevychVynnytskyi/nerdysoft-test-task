package com.application.dto.member;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequestDto(
        @NotBlank(message = "Name can't be null or empty")
        String name
) {
}
