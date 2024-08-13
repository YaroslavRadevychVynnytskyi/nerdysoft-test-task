package com.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.application.dto.member.CreateMemberRequestDto;
import com.application.dto.member.MemberResponseDto;
import com.application.entity.Borrowing;
import com.application.entity.Member;
import com.application.mapper.MemberMapper;
import com.application.repo.BorrowingRepository;
import com.application.repo.MemberRepository;
import com.application.service.impl.MemberServiceImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
public class MemberServiceTest {
    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("Verify create() creates and returns member correctly")
    void create_AllOk_ShouldReturnCreatedMember() {
        //Given
        String memberName = "John Doe";
        CreateMemberRequestDto requestDto = new CreateMemberRequestDto(memberName);

        Member member = new Member(memberName);
        member.setId(1L);
        member.setStartedAt(LocalDateTime.now());

        MemberResponseDto responseDto = new MemberResponseDto(1L, memberName, member.getStartedAt());

        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberMapper.toDto(member)).thenReturn(responseDto);

        //When
        MemberResponseDto result = memberService.create(requestDto);

        //Then
        assertEquals(responseDto, result);
    }

    @Test
    @DisplayName("Verify getAll() returns correct list of MemberResponseDto")
    void getAll_ShouldReturnCorrectListOfMemberResponseDto() {
        //Given
        Member member1 = new Member("Alice");
        member1.setId(1L);
        member1.setStartedAt(LocalDateTime.now());

        Member member2 = new Member("Bob");
        member2.setId(2L);
        member2.setStartedAt(LocalDateTime.now());

        MemberResponseDto dto1 = new MemberResponseDto(1L, "Alice", member1.getStartedAt());
        MemberResponseDto dto2 = new MemberResponseDto(2L, "Bob", member2.getStartedAt());

        Pageable pageable = PageRequest.of(0, 2);
        Page<Member> memberPage = new PageImpl<>(List.of(member1, member2), pageable, 2);

        when(memberRepository.findAll(pageable)).thenReturn(memberPage);
        when(memberMapper.toDto(member1)).thenReturn(dto1);
        when(memberMapper.toDto(member2)).thenReturn(dto2);

        //When
        List<MemberResponseDto> actual = memberService.getAll(pageable);

        //Then
        assertEquals(2, actual.size());
        assertEquals(dto1, actual.get(0));
        assertEquals(dto2, actual.get(1));

        verify(memberRepository).findAll(pageable);
        verify(memberMapper).toDto(member1);
        verify(memberMapper).toDto(member2);
    }

    @Test
    @DisplayName("Verify getById() returns correct MemberResponseDto by ID")
    void getById_ShouldReturnCorrectMemberResponseDto() {
        //Given
        Long memberId = 1L;

        Member member = new Member("Alice");
        member.setId(memberId);
        member.setStartedAt(LocalDateTime.now());

        MemberResponseDto responseDto = new MemberResponseDto(memberId, "Alice", member.getStartedAt());

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberMapper.toDto(member)).thenReturn(responseDto);

        //When
        MemberResponseDto actual = memberService.getById(memberId);

        //Then
        assertEquals(responseDto, actual);

        verify(memberRepository).findById(memberId);
        verify(memberMapper).toDto(member);
    }

    @Test
    @DisplayName("Verify update() returns correct MemberResponseDto after updating member")
    void update_ShouldReturnUpdatedMemberResponseDto() {
        //Given
        Long memberId = 1L;
        String newName = "Updated Name";

        Member existingMember = new Member("Original Name");
        existingMember.setId(memberId);
        existingMember.setStartedAt(LocalDateTime.now());

        CreateMemberRequestDto requestDto = new CreateMemberRequestDto(newName);

        Member updatedMember = new Member(newName);
        updatedMember.setId(memberId);
        updatedMember.setStartedAt(existingMember.getStartedAt());

        MemberResponseDto responseDto = new MemberResponseDto(memberId, newName, updatedMember.getStartedAt());

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(existingMember)).thenReturn(updatedMember);
        when(memberMapper.toDto(updatedMember)).thenReturn(responseDto);

        //When
        MemberResponseDto actual = memberService.update(memberId, requestDto);

        //Then
        assertEquals(responseDto, actual);

        verify(memberRepository).findById(memberId);
        verify(memberRepository).save(existingMember);
        verify(memberMapper).toDto(updatedMember);
    }

    @Test
    @DisplayName("Verify deleteById() deletes member when no borrowings exist")
    void deleteById_NoBorrowings_ShouldReturnTrue() {
        //Given
        Long memberId = 1L;

        when(borrowingRepository.findByMemberId(memberId)).thenReturn(Collections.emptyList());

        //When
        boolean result = memberService.deleteById(memberId);

        //Then
        assertTrue(result);
        verify(borrowingRepository).findByMemberId(memberId);
        verify(memberRepository).deleteById(memberId);
    }

    @Test
    @DisplayName("Verify deleteById() doesn't delete member if he has borrowings")
    void deleteById_WithBorrowings_ShouldReturnFalse() {
        //Given
        Long memberId = 1L;

        when(borrowingRepository.findByMemberId(memberId)).thenReturn(List.of(new Borrowing()));

        //When
        boolean result = memberService.deleteById(memberId);

        //Then
        assertFalse(result);
        verify(borrowingRepository).findByMemberId(memberId);
        verify(memberRepository, never()).deleteById(memberId);
    }
}
