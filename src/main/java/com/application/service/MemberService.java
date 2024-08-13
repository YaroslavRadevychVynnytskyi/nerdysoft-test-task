package com.application.service;

import com.application.dto.member.CreateMemberRequestDto;
import com.application.dto.member.MemberResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    MemberResponseDto create(CreateMemberRequestDto requestDto);

    List<MemberResponseDto> getAll(Pageable pageable);

    MemberResponseDto getById(Long memberId);

    MemberResponseDto update(Long memberId, CreateMemberRequestDto requestDto);

    boolean deleteById(Long memberId);
}
