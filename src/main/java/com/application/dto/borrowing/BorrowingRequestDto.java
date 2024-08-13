package com.application.dto.borrowing;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BorrowingRequestDto(
        @NotNull
        @Positive
        Long memberId,

        @NotNull
        @Positive
        Long bookId
) {
}
