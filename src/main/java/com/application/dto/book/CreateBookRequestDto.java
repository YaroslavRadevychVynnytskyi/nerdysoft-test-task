package com.application.dto.book;

import com.application.validation.annotation.ValidAuthor;
import com.application.validation.annotation.ValidBook;
import jakarta.validation.constraints.Min;

public record CreateBookRequestDto(
        @ValidBook
        String title,

        @ValidAuthor
        String author,

        @Min(1)
        int amount
) {
}
