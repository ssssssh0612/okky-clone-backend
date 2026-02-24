package io.github.sanghyeon.boardservice.domain.member.controller;

import io.github.sanghyeon.boardservice.domain.member.dto.*;
import io.github.sanghyeon.boardservice.domain.member.entity.Member;
import io.github.sanghyeon.boardservice.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberProfileResponse> signup(@Valid @RequestBody SignupRequest req) {
        Member member = memberService.signup(req);
        return ResponseEntity.ok(
                new MemberProfileResponse(member.getId(), member.getEmail(),
                        member.getNickname(), member.getRole()));
    }

}