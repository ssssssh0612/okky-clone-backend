package io.github.sanghyeon.boardservice.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
        super("이미 사용 중인 닉네임입니다.");
    }

    public DuplicateNicknameException(String message) {
        super(message);
    }
}