package com.application.controller;

import com.application.dto.borrowing.BorrowingRequestDto;
import com.application.dto.borrowing.BorrowingResponseDto;
import com.application.service.BorrowingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Borrowing management", description = "Endpoints for managing library book borrowings")
@RestController
@RequestMapping("/borrowings")
@RequiredArgsConstructor
public class BorrowingController {
    private final BorrowingService borrowingService;

    @Operation(summary = "Borrow a book", description = "Creates a borrowing in the db")
    @PostMapping
    public ResponseEntity<BorrowingResponseDto> borrow(@RequestBody
                                                       @Valid BorrowingRequestDto requestDto) {
        return ResponseEntity.ok(borrowingService.borrow(requestDto));
    }

    @Operation(summary = "Return a book", description = "Allows a member to return book")
    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody @Valid BorrowingRequestDto requestDto) {
        if (borrowingService.returnBook(requestDto)) {
            return new ResponseEntity<>("Successfully returned a book", HttpStatus.OK);
        }
        return new ResponseEntity<>("Such borrowing does not exist", HttpStatus.BAD_REQUEST);
    }
}
