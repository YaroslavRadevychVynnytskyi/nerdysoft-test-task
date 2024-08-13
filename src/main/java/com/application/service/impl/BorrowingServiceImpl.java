package com.application.service.impl;

import com.application.dto.borrowing.BorrowingRequestDto;
import com.application.dto.borrowing.BorrowingResponseDto;
import com.application.entity.Book;
import com.application.entity.Borrowing;
import com.application.entity.Member;
import com.application.mapper.BorrowingMapper;
import com.application.repo.BookRepository;
import com.application.repo.BorrowingRepository;
import com.application.repo.MemberRepository;
import com.application.service.BorrowingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {
    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingMapper borrowingMapper;

    @Value("${borrow.limit}")
    private int borrowLimit;

    @Override
    public BorrowingResponseDto borrow(BorrowingRequestDto requestDto) {
        checkBorrowLimit(requestDto.memberId());

        Book book = getBookById(requestDto.bookId());
        checkBookAmount(book);

        Member member = getMemberById(requestDto.memberId());

        Borrowing borrowing = new Borrowing();
        borrowing.setMember(member);
        borrowing.setBook(book);
        book.setAmount(book.getAmount() - 1);

        Borrowing savedBorrowing = borrowingRepository.save(borrowing);
        return borrowingMapper.toDto(savedBorrowing);
    }

    @Override
    @Transactional
    public boolean returnBook(BorrowingRequestDto requestDto) {
        if (borrowingRepository.existsByMemberIdAndBookId(requestDto.memberId(), requestDto.bookId())) {
            Book book = getBookById(requestDto.bookId());
            int amountToReturn = borrowingRepository
                    .countByMemberIdAndBookId(requestDto.memberId(), requestDto.bookId());

            borrowingRepository.deleteByMemberIdAndBookId(requestDto.memberId(), requestDto.bookId());
            book.setAmount(book.getAmount() + amountToReturn);
            return true;
        }
        return false;
    }

    private void checkBorrowLimit(Long memberId) {
        if (borrowingRepository.countByMemberId(memberId) >= borrowLimit) {
            throw new IllegalStateException("Member with ID: " + memberId + " exceeded borrow limit");
        }
    }

    private void checkBookAmount(Book book) {
        if (book.getAmount() == 0) {
            throw new IllegalStateException("Can't borrow book with ID: " + book.getId()
                    + " due to zero amount");
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new EntityNotFoundException("Can't find member with ID: " + memberId));
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("Can't find book with ID: " + bookId));
    }
}
