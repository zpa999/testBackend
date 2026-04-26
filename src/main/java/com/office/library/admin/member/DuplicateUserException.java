package com.office.library.admin.member;

/**
 * 회원가입 시 중복된 사용자가 발생했을 때 사용하는 커스텀 예외입니다.
 * 이 예외는 일반적으로 Primary Key 또는 Unique Constraint 위반 시 발생합니다.
 */
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}