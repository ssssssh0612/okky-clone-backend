package io.github.sanghyeon.boardservice.domain.member.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 60)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String role;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false,
            insertable = false,
            columnDefinition = "timestamp default current_timestamp"
    )
    private LocalDateTime createdAt;

    protected Member() {}

    public Member(String email, String passwordHash, String nickname) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.role = "ROLE_USER";
    }

}
