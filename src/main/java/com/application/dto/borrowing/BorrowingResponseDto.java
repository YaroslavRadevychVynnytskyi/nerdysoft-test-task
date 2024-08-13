package com.application.dto.borrowing;

import java.time.LocalDateTime;

public record BorrowingResponseDto(
        Long id,

        Long memberId,
        String memberName,

        Long bookId,
        String bookTitle,

        LocalDateTime borrowedAt
) {
}
