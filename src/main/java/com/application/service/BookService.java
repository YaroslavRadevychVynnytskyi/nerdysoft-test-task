package com.application.service;

import com.application.dto.BookResponseDto;
import com.application.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto create(CreateBookRequestDto requestDto);

    List<BookResponseDto> getAll(Pageable pageable);

    BookResponseDto getById(Long bookId);

    BookResponseDto update(Long bookId, CreateBookRequestDto requestDto);

    boolean deleteById(Long bookId);
}
