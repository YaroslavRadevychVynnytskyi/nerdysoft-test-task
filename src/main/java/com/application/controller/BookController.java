package com.application.controller;

import com.application.dto.book.BookResponseDto;
import com.application.dto.book.BorrowedBooksNamesAndAmountDto;
import com.application.dto.book.CreateBookRequestDto;
import com.application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for library book management")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create book", description = "Saves new book to db")
    public ResponseEntity<BookResponseDto> create(@RequestBody
                                                  @Valid CreateBookRequestDto requestDto) {
        return ResponseEntity.ok(bookService.create(requestDto));
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves all books available in library")
    public ResponseEntity<List<BookResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Retrieves certain book based on id")
    public ResponseEntity<BookResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book by id", description = "Updates book based on id")
    public ResponseEntity<BookResponseDto> update(@PathVariable Long id,
                                                  @RequestBody
                                                  @Valid CreateBookRequestDto requestDto) {
        return ResponseEntity.ok(bookService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id", description = "Deletes book if it is not borrowed")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (bookService.deleteById(id)) {
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Can't delete. Book is borrowed", HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "Get books borrowed by member",
            description = "Retrieves all books borrowed by a specific member by name")
    @GetMapping("/borrowed-by")
    public ResponseEntity<List<BookResponseDto>> getBooksBorrowedBy(@RequestParam String name) {
        return ResponseEntity.ok(bookService.getBooksBorrowedBy(name));
    }

    @Operation(summary = "Get borrowed distinct books names",
            description = "Retrieves all borrowed distinct book names")
    @GetMapping("/borrowed-distinct")
    public ResponseEntity<List<String>> getAllBorrowedBooksDistinctNames() {
        return ResponseEntity.ok(bookService.getAllBorrowedBooksDistinctNames());
    }

    @Operation(summary = "Get borrowed distinct books names and amount",
            description = "Retrieves all borrowed distinct books names and amount of copies borrowed")
    @GetMapping("/borrowed-distinct/amount")
    public ResponseEntity<List<BorrowedBooksNamesAndAmountDto>> getAllBorrowedBooksNamesAndAmount() {
        return ResponseEntity.ok(bookService.getAllBorrowedBooksNamesAndAmount());
    }

}
