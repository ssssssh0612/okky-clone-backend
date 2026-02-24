package io.github.sanghyeon.boardservice.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("이미 사용 중인 이메일입니다.");
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}