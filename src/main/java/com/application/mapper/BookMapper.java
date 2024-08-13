package com.application.mapper;

import com.application.config.MapperConfig;
import com.application.dto.BookResponseDto;
import com.application.dto.CreateBookRequestDto;
import com.application.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto requestDto);

    BookResponseDto toDto(Book book);

    void updateFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
