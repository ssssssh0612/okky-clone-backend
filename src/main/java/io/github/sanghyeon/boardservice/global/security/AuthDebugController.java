package io.github.sanghyeon.boardservice.global.security;

import io.github.sanghyeon.boardservice.global.security.principal.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.server.ResponseStatusException;

@Profile({"dev", "test"})
@RestController
@RequestMapping("/api/auth")
public class AuthDebugController {

    /**
     * 세션 기반 인증 디버깅용 엔드포인트
     *
     * 기대 동작:
     * - 미인증 요청: 일반적으로 SecurityFilterChain에서 401로 차단됨
     * - 인증 + 세션 존재: 200 + 사용자/세션 정보 반환
     *
     * 예외 처리 정책(디버그 목적):
     * - principal == null: 인증 주체 누락 (보안 설정 이상 가능) -> 401
     * - session == null: 세션 기반 인증 전제 위반 (설정/인증방식 변경 가능) -> 500
     *
     * 즉, session == null은 클라이언트 오류가 아니라 서버 구성 이상으로 간주한다.
     */

    @GetMapping("/test")
    public Map<String, Object> test(
            @AuthenticationPrincipal MemberPrincipal principal,
            HttpServletRequest request
    ) {
        if (principal == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "인증 주체(principal)가 없습니다. Security 설정을 확인하세요."
            );
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "인증된 요청인데 HttpSession이 없습니다. 세션 기반 인증 설정을 확인하세요."
            );
        }

        List<String> authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Map.of(
                "memberId", principal.getMemberId(),
                "email", principal.getUsername(),
                "nickname", principal.getNickname(),
                "authorities", authorities,
                "sessionId", session.getId()
        );
    }
}
