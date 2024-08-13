package com.application.repo;

import com.application.entity.Borrowing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByBookId(Long bookId);
}
