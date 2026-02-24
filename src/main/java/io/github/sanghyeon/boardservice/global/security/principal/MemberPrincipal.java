package io.github.sanghyeon.boardservice.global.security.principal;

import io.github.sanghyeon.boardservice.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class MemberPrincipal implements UserDetails {


    private final Long memberId;
    private final String email;
    private final String passwordHash;
    private final String nickname;
    private final boolean active;
    private final List<GrantedAuthority> authorities;

    private MemberPrincipal(Long memberId, String email, String passwordHash, String nickname,
                            boolean active, List<GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.active = active;
        this.authorities = authorities;
    }

    public static MemberPrincipal from(Member m) {
        return new MemberPrincipal(
                m.getId(),
                m.getEmail(),
                m.getPasswordHash(),
                m.getNickname(),
                m.isActive(),
                List.of(new SimpleGrantedAuthority(m.getRole()))
        );
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return active; }
    @Override public boolean isEnabled() { return active; }
}
