package com.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.application.dto.borrowing.BorrowingRequestDto;
import com.application.dto.borrowing.BorrowingResponseDto;
import com.application.entity.Book;
import com.application.entity.Borrowing;
import com.application.entity.Member;
import com.application.mapper.BorrowingMapper;
import com.application.repo.BookRepository;
import com.application.repo.BorrowingRepository;
import com.application.repo.MemberRepository;
import com.application.service.impl.BorrowingServiceImpl;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {
    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingMapper borrowingMapper;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    @Test
    @DisplayName("Verify borrow() correctly creates and saves object")
    void borrow_ValidRequest_ShouldReturnBorrowingResponseDto() {
        //Given
        Long memberId = 1L;
        Long bookId = 1L;

        Member member = new Member("John Doe");
        member.setId(memberId);

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Title");
        book.setAuthor("Name Surname");
        book.setAmount(5);

        BorrowingRequestDto requestDto = new BorrowingRequestDto(memberId, bookId);

        Borrowing borrowing = new Borrowing();
        borrowing.setMember(member);
        borrowing.setBook(book);

        Borrowing savedBorrowing = new Borrowing();
        savedBorrowing.setId(1L);
        savedBorrowing.setMember(member);
        savedBorrowing.setBook(book);

        BorrowingResponseDto responseDto = new BorrowingResponseDto(
                1L,
                memberId,
                member.getName(),
                bookId,
                book.getTitle(),
                LocalDateTime.now());

        when(borrowingRepository.countByMemberId(memberId)).thenReturn(-1);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(borrowingRepository.save(any(Borrowing.class))).thenReturn(savedBorrowing);
        when(borrowingMapper.toDto(savedBorrowing)).thenReturn(responseDto);

        //When
        BorrowingResponseDto actual = borrowingService.borrow(requestDto);

        //Then
        assertEquals(responseDto, actual);
        verify(bookRepository).findById(bookId);
        verify(memberRepository).findById(memberId);
        verify(borrowingRepository).save(any(Borrowing.class));
        verify(borrowingMapper).toDto(savedBorrowing);
    }

    @Test
    @DisplayName("Verify borrow() throws correct exception when exceeding limit")
    void borrow_ExceedsLimit_ShouldThrowIllegalStateException() {
        //Given
        Long memberId = 1L;
        Long bookId = 2L;
        BorrowingRequestDto requestDto = new BorrowingRequestDto(memberId, bookId);

        when(borrowingRepository.countByMemberId(memberId)).thenReturn(11);

        // When & Then
        assertThrows(IllegalStateException.class, () -> borrowingService.borrow(requestDto));
    }

    @Test
    @DisplayName("Verify borrow() throws correct exception when zero amount of books")
    public void borrow_ZeroBookAmount_ShouldThrowIllegalStateException() {
        //Given
        Long memberId = 1L;
        Long bookId = 2L;
        BorrowingRequestDto requestDto = new BorrowingRequestDto(memberId, bookId);

        Member member = new Member();
        member.setId(memberId);
        member.setName("John Doe");

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Sample Book");
        book.setAmount(0);

        when(borrowingRepository.countByMemberId(memberId)).thenReturn(0);

        //When & Then
        assertThrows(IllegalStateException.class, () -> borrowingService.borrow(requestDto));
    }

    @Test
    @DisplayName("Verify returnBook() returns true when borrowing exists")
    void returnBook_BookExists_ShouldReturnTrue() {
        //Given
        Long memberId = 1L;
        Long bookId = 2L;
        int amountToReturn = 1;

        Book book = new Book();
        book.setId(bookId);
        book.setAmount(5);

        BorrowingRequestDto requestDto = new BorrowingRequestDto(memberId, bookId);

        when(borrowingRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(true);
        when(borrowingRepository.countByMemberIdAndBookId(memberId, bookId)).thenReturn(amountToReturn);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        //When
        boolean result = borrowingService.returnBook(requestDto);

        //Then
        assertTrue(result);
        assertEquals(6, book.getAmount());
        verify(borrowingRepository).deleteByMemberIdAndBookId(memberId, bookId);
    }

    @Test
    @DisplayName("Verify returnBook() returns false when borrowing doesn't exist")
    void returnBook_BookDoesNotExist_ShouldReturnFalse() {
        //Given
        Long memberId = 1L;
        Long bookId = 2L;

        BorrowingRequestDto requestDto = new BorrowingRequestDto(memberId, bookId);

        when(borrowingRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(false);

        //When
        boolean result = borrowingService.returnBook(requestDto);

        //Then
        assertFalse(result);
        verify(borrowingRepository, never()).deleteByMemberIdAndBookId(memberId, bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }
}
