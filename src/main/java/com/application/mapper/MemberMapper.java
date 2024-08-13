package com.application.mapper;

import com.application.config.MapperConfig;
import com.application.dto.member.MemberResponseDto;
import com.application.entity.Member;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface MemberMapper {
    MemberResponseDto toDto(Member member);
}
