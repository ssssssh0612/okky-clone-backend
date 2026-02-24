package io.github.sanghyeon.boardservice.global.security.userdetails;

import io.github.sanghyeon.boardservice.domain.member.entity.Member;
import io.github.sanghyeon.boardservice.domain.member.repository.MemberRepository;
import io.github.sanghyeon.boardservice.global.security.principal.MemberPrincipal;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(normalize(email))
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
        return MemberPrincipal.from(member);
    }

    // 유니크 컬럼 정책과 맞추기
    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
