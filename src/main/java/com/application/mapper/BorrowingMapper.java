package com.application.mapper;

import com.application.config.MapperConfig;
import com.application.dto.borrowing.BorrowingResponseDto;
import com.application.entity.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BorrowingMapper {
    @Mapping(target = "memberId", source = "borrowing.member.id")
    @Mapping(target = "memberName", source = "borrowing.member.name")
    @Mapping(target = "bookId", source = "borrowing.book.id")
    @Mapping(target = "bookTitle", source = "borrowing.book.title")
    BorrowingResponseDto toDto(Borrowing borrowing);
}
