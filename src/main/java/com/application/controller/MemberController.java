package com.application.controller;

import com.application.dto.member.CreateMemberRequestDto;
import com.application.dto.member.MemberResponseDto;
import com.application.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member management", description = "Endpoints for managing members of library")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "Create member", description = "Saves new member to db")
    @PostMapping
    public ResponseEntity<MemberResponseDto> create(@RequestBody
                                                    @Valid CreateMemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.create(requestDto));
    }

    @Operation(summary = "Get all members", description = "Retrieves a list of library members")
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(memberService.getAll(pageable));
    }

    @Operation(summary = "Get member by id", description = "Retrieves a member based on id")
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    @Operation(summary = "Update member by id", description = "Updates member based on id")
    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponseDto> update(@PathVariable Long id,
                                                    @RequestBody
                                                    @Valid CreateMemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.update(id, requestDto));
    }

    @Operation(summary = "Delete member by id",
            description = "Deletes member unless he doesn't have borrowed books")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (memberService.deleteById(id)) {
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Can't delete a member with borrowed books", HttpStatus.FORBIDDEN);
    }
}
