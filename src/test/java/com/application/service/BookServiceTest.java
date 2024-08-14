package com.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.application.dto.book.BookResponseDto;
import com.application.dto.book.BorrowedBooksNamesAndAmountDto;
import com.application.dto.book.CreateBookRequestDto;
import com.application.entity.Book;
import com.application.entity.Borrowing;
import com.application.mapper.BookMapper;
import com.application.repo.BookRepository;
import com.application.repo.BorrowingRepository;
import com.application.service.impl.BookServiceImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private Pageable pageable;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify the correct BookResponseDto was returned by create() when book doesn't exist in the db")
    void create_WithFreshCreateBookRequestDto_ShouldReturnValidBookResponseDto() {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Kobzar",
                "Taras Shevchenko",
                15);

        Book book = new Book();
        book.setTitle("Kobzar");
        book.setAuthor("Taras Shevchenko");
        book.setAmount(15);

        BookResponseDto expected = new BookResponseDto(
                1L,
                "Kobzar",
                "Taras Shevchenko",
                15
        );


        when(bookRepository.findByTitleAndAuthor(requestDto.title(), requestDto.author()))
                .thenReturn(Optional.empty());
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        book.setId(1L);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        //When
        BookResponseDto actual = bookService.create(requestDto);

        //Then
        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findByTitleAndAuthor(requestDto.title(), requestDto.author());
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Verify the correct BookResponseDto was returned by create() when book exists in the db")
    void create_WithExistingCreateBookRequestDto_ShouldIncrementBookAmount() {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Brave New World",
                "Aldous Huxley",
                4
        );

        Book book = new Book();
        book.setTitle("Brave New World");
        book.setAuthor("Aldous Huxley");
        book.setAmount(14);

        BookResponseDto expected = new BookResponseDto(
                1L,
                "Brave New World",
                "Aldous Huxley",
                15
        );

        when(bookRepository.findByTitleAndAuthor(requestDto.title(), requestDto.author())).thenReturn(Optional.of(book));
        book.setId(1L);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        //When
        BookResponseDto actual = bookService.create(requestDto);

        //Then
        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findByTitleAndAuthor(requestDto.title(), requestDto.author());
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Verify the correct list of BookResponseDto was returned by getAll()")
    void getAll_AllOk_ShouldReturnCorrectListOfBookResponseDto() {
        //Given
        Book book1 = new Book();
        book1.setTitle("To Kill a Mockingbird");
        book1.setAuthor("Harper Lee");
        book1.setAmount(10);

        BookResponseDto dto1 = new BookResponseDto(
                1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                10);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book1), pageable, 1);


        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book1)).thenReturn(dto1);

        //When
        List<BookResponseDto> actual = bookService.getAll(pageable);

        //Then
        assertEquals(1, actual.size());
        assertEquals(dto1, actual.get(0));

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book1);
    }

    @Test
    @DisplayName("Verify the BookResponseDto returned by getById() is correct")
    void getById_ShouldReturnCorrectBookResponseDto() {
        // Given
        Long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("To Kill a Mockingbird");
        book.setAuthor("Harper Lee");
        book.setAmount(10);

        BookResponseDto dto = new BookResponseDto(
                bookId,
                "To Kill a Mockingbird",
                "Harper Lee",
                10
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(dto);

        // When
        BookResponseDto actual = bookService.getById(bookId);

        // Then
        assertEquals(dto, actual);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Verify the BookResponseDto returned by update() is correct")
    void update_ShouldReturnUpdatedBookResponseDto() {
        // Given
        Long bookId = 1L;

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setAmount(5);

        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "New Title",
                "New Author",
                10
        );

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("New Title");
        updatedBook.setAuthor("New Author");
        updatedBook.setAmount(10);

        BookResponseDto responseDto = new BookResponseDto(
                bookId,
                "New Title",
                "New Author",
                10
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookMapper).updateFromDto(existingBook, requestDto);
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(responseDto);

        // When
        BookResponseDto actual = bookService.update(bookId, requestDto);

        // Then
        assertEquals(responseDto, actual);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateFromDto(existingBook, requestDto);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(updatedBook);
    }

    @Test
    @DisplayName("Verify deleteById() deletes the book when it is not borrowed")
    void deleteById_BookNotBorrowed_ShouldDeleteAndReturnTrue() {
        // Given
        Long bookId = 1L;

        when(borrowingRepository.findByBookId(bookId)).thenReturn(Collections.emptyList());

        // When
        boolean actual = bookService.deleteById(bookId);

        // Then
        assertTrue(actual);
        verify(borrowingRepository).findByBookId(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Verify deleteById() does not delete the book when it is borrowed")
    void deleteById_BookBorrowed_ShouldNotDeleteAndReturnFalse() {
        // Given
        Long bookId = 1L;

        when(borrowingRepository.findByBookId(bookId)).thenReturn(List.of(new Borrowing()));

        // When
        boolean result = bookService.deleteById(bookId);

        // Then
        assertFalse(result);
        verify(borrowingRepository).findByBookId(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }

    @Test
    @DisplayName("Verify getBooksBorrowedBy() returns the correct list of BookResponseDto")
    void getBooksBorrowedBy_ShouldReturnCorrectListOfBookResponseDto() {
        // Given
        String memberName = "John Doe";

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("The Great Gatsby");
        book1.setAuthor("F. Scott Fitzgerald");
        book1.setAmount(3);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("1984");
        book2.setAuthor("George Orwell");
        book2.setAmount(5);

        BookResponseDto dto1 = new BookResponseDto(
                1L,
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                3
        );

        BookResponseDto dto2 = new BookResponseDto(
                2L,
                "1984",
                "George Orwell",
                5
        );

        List<Book> books = Arrays.asList(book1, book2);
        List<BookResponseDto> dtos = Arrays.asList(dto1, dto2);

        when(borrowingRepository.findAllBooksBorrowedByMemberName(memberName)).thenReturn(books);
        when(bookMapper.toDto(book1)).thenReturn(dto1);
        when(bookMapper.toDto(book2)).thenReturn(dto2);

        // When
        List<BookResponseDto> actual = bookService.getBooksBorrowedBy(memberName);

        // Then
        assertEquals(dtos, actual);

        verify(borrowingRepository).findAllBooksBorrowedByMemberName(memberName);
        verify(bookMapper).toDto(book1);
        verify(bookMapper).toDto(book2);
    }

    @Test
    @DisplayName("Verify getAllBorrowedBooksDistinctNames() returns the distinct list of borrowed book names correctly")
    void getAllBorrowedBooksDistinctNames_ShouldReturnCorrectListOfDistinctTitles() {
        // Given
        Book book1 = new Book();
        book1.setTitle("To Kill a Mockingbird");

        Book book2 = new Book();
        book2.setTitle("1984");

        Book book3 = new Book();
        book3.setTitle("To Kill a Mockingbird");

        List<Book> books = Arrays.asList(book1, book2, book3);
        List<String> distinctTitles = books.stream()
                .distinct()
                .map(Book::getTitle)
                .collect(Collectors.toList());

        when(borrowingRepository.findAllBooks()).thenReturn(books);

        // When
        List<String> actual = bookService.getAllBorrowedBooksDistinctNames();

        // Then
        assertEquals(distinctTitles, actual);

        verify(borrowingRepository).findAllBooks();
    }

    @Test
    @DisplayName("Verify the correct list of BorrowedBooksNamesAndAmountDto is returned by getAllBorrowedBooksNamesAndAmount()")
    void getAllBorrowedBooksNamesAndAmount_ShouldReturnCorrectListOfDto() {
        // Given
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("To Kill a Mockingbird");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("1984");

        when(borrowingRepository.findAllBooks()).thenReturn(Arrays.asList(book1, book2));
        when(borrowingRepository.countByBookId(1L)).thenReturn(5);
        when(borrowingRepository.countByBookId(2L)).thenReturn(3);

        BorrowedBooksNamesAndAmountDto dto1 = new BorrowedBooksNamesAndAmountDto(
                "To Kill a Mockingbird",
                5
        );
        BorrowedBooksNamesAndAmountDto dto2 = new BorrowedBooksNamesAndAmountDto("1984", 3);

        List<BorrowedBooksNamesAndAmountDto> expected = Arrays.asList(dto1, dto2);

        // When
        List<BorrowedBooksNamesAndAmountDto> actual = bookService.getAllBorrowedBooksNamesAndAmount();

        // Then
        assertEquals(expected, actual);

        verify(borrowingRepository).findAllBooks();
        verify(borrowingRepository).countByBookId(1L);
        verify(borrowingRepository).countByBookId(2L);
    }

}

