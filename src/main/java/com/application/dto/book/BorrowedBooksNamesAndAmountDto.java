package com.application.dto.book;

public record BorrowedBooksNamesAndAmountDto(
        String title,
        Integer borrowedCopiesAmount
) {
}
