package io.github.sanghyeon.boardservice.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailOrNicknameException extends RuntimeException {
    public DuplicateEmailOrNicknameException() {
        super("사용중인 이메일 혹은 닉네임 입니다.");
    }

    public DuplicateEmailOrNicknameException(String message) {
        super(message);
    }

}
