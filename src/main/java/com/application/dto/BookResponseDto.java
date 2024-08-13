package com.application.dto;

public record BookResponseDto(
        Long id,
        String title,
        String author,
        int amount
) {
}
