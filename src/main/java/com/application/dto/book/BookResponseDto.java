package com.application.dto.book;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        int amount
) {
}
