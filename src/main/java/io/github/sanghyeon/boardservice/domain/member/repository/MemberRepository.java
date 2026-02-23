package io.github.sanghyeon.boardservice.domain.member.repository;

import io.github.sanghyeon.boardservice.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
