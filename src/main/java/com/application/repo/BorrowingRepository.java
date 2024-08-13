package com.application.repo;

import com.application.entity.Book;
import com.application.entity.Borrowing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByBookId(Long bookId);

    int countByMemberId(Long memberId);

    List<Borrowing> findByMemberId(Long memberId);

    int countByMemberIdAndBookId(Long memberId, Long bookId);

    void deleteByMemberIdAndBookId(Long memberId, Long bookId);

    @Query("SELECT b.book FROM Borrowing b WHERE b.member.name = :memberName")
    List<Book> findAllBooksBorrowedByMemberName(String memberName);

    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

    @Query("SELECT b.book FROM Borrowing b")
    List<Book> findAllBooks();

    int countByBookId(Long bookId);
}
