package com.application.service.impl;

import com.application.dto.BookResponseDto;
import com.application.dto.CreateBookRequestDto;
import com.application.entity.Book;
import com.application.mapper.BookMapper;
import com.application.repo.BookRepository;
import com.application.repo.BorrowingRepository;
import com.application.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDto create(CreateBookRequestDto requestDto) {
        Optional<Book> optionalBook = bookRepository
                .findByTitleAndAuthor(requestDto.title(), requestDto.author());
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setAmount(book.getAmount() + 1);

            Book updatedBook = bookRepository.save(book);
            return bookMapper.toDto(updatedBook);
        }
        Book newBook = bookMapper.toModel(requestDto);
        Book savedBook = bookRepository.save(newBook);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookResponseDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponseDto getById(Long bookId) {
        Book book = getBookById(bookId);
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto update(Long bookId, CreateBookRequestDto requestDto) {
        Book book = getBookById(bookId);
        bookMapper.updateFromDto(book, requestDto);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public boolean deleteById(Long bookId) {
        if (borrowingRepository.findByBookId(bookId).isEmpty()) {
            bookRepository.deleteById(bookId);
            return true;
        }
        return false;
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Can't find book with ID: " + bookId));
    }
}
