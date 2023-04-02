package com.twogather.twogatherwebbackend.exception;

public class AuthException extends RuntimeException{
    public enum AuthExceptionErrorCode {
        AUTHORIZATION_HEADER_UNINVOLVED("인증 헤더가 빠져있거나 요청이 유효하지 않습니다"),
        TOKEN_EXPIRED("토큰이 만료되었습니다"),
        INVALID_TOKEN("토큰이 유효하지 않습니다"),
        ACCESS_DENIED("권한이 없습니다"),
        UNAUTHORIZED("인증된 사용자가 아닙니다");
        private final String message;

        AuthExceptionErrorCode(String message) {
            this.message = message;
        }
        public String getMessage(){ return message; }
    }
    private final AuthExceptionErrorCode errorCode;

    public AuthException(AuthExceptionErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public AuthExceptionErrorCode getErrorCode(){
        return errorCode;
    }
}