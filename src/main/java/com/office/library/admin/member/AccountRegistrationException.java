package com.office.library.admin.member;

/**
 * 회원가입 과정 중 데이터 유효성이나 구조적 문제가 발생했을 때 사용하는 커스텀 예외입니다.
 * 이 예외는 DAO에서 DB 관련 예외가 포착되었을 때, 비즈니스 레벨에서 처리할 수 있도록 던져집니다.
 */
public class AccountRegistrationException extends RuntimeException {
    public AccountRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}