package io.github.sanghyeon.boardservice.domain.member.dto;

public record MemberProfileResponse(Long id,
                                    String email,
                                    String nickname,
                                    String role) {
}
