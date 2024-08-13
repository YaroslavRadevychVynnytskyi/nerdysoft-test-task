package com.application.service.impl;

import com.application.dto.member.CreateMemberRequestDto;
import com.application.dto.member.MemberResponseDto;
import com.application.entity.Member;
import com.application.mapper.MemberMapper;
import com.application.repo.BorrowingRepository;
import com.application.repo.MemberRepository;
import com.application.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public MemberResponseDto create(CreateMemberRequestDto requestDto) {
        Member member = new Member(requestDto.name());
        Member savedMember = memberRepository.save(member);
        return memberMapper.toDto(savedMember);
    }

    @Override
    public List<MemberResponseDto> getAll(Pageable pageable) {
        return memberRepository.findAll(pageable).stream()
                .map(memberMapper::toDto)
                .toList();
    }

    @Override
    public MemberResponseDto getById(Long memberId) {
        Member member = getMemberById(memberId);
        return memberMapper.toDto(member);
    }

    @Override
    public MemberResponseDto update(Long memberId, CreateMemberRequestDto requestDto) {
        Member member = getMemberById(memberId);
        member.setName(requestDto.name());
        Member updatedMember = memberRepository.save(member);

        return memberMapper.toDto(updatedMember);
    }

    @Override
    public boolean deleteById(Long memberId) {
        if (borrowingRepository.findByMemberId(memberId).isEmpty()) {
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new EntityNotFoundException("Can't find member with ID: " + memberId));
    }
}
