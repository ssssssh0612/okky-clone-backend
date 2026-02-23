package io.github.sanghyeon.boardservice.domain.member.service;

import io.github.sanghyeon.boardservice.domain.member.dto.SignupRequest;
import io.github.sanghyeon.boardservice.domain.member.entity.Member;
import io.github.sanghyeon.boardservice.domain.member.exception.DuplicateEmailException;
import io.github.sanghyeon.boardservice.domain.member.exception.DuplicateEmailOrNicknameException;
import io.github.sanghyeon.boardservice.domain.member.exception.DuplicateNicknameException;
import io.github.sanghyeon.boardservice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signup(SignupRequest req) {
        String email = normalizeEmail(req.email());
        String password = req.password();
        String nickname = req.nickname();

        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }

        String hash = passwordEncoder.encode(password);
        Member member = new Member(email, hash, nickname);

        try {
            return memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailOrNicknameException();
        }

    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
