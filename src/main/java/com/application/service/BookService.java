package com.application.service;

import com.application.dto.book.BookResponseDto;
import com.application.dto.book.BorrowedBooksNamesAndAmountDto;
import com.application.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto create(CreateBookRequestDto requestDto);

    List<BookResponseDto> getAll(Pageable pageable);

    BookResponseDto getById(Long bookId);

    BookResponseDto update(Long bookId, CreateBookRequestDto requestDto);

    boolean deleteById(Long bookId);

    List<BookResponseDto> getBooksBorrowedBy(String memberName);

    List<String> getAllBorrowedBooksDistinctNames();

    List<BorrowedBooksNamesAndAmountDto> getAllBorrowedBooksNamesAndAmount();
}
