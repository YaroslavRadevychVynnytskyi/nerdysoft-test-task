package com.application.service;

import com.application.dto.borrowing.BorrowingRequestDto;
import com.application.dto.borrowing.BorrowingResponseDto;

public interface BorrowingService {
    BorrowingResponseDto borrow(BorrowingRequestDto requestDto);

    boolean returnBook(BorrowingRequestDto requestDto);
}
