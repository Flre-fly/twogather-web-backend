package com.twogather.twogatherwebbackend.exception;

public class BusinessHourException extends RuntimeException{
    public enum BusinessHourErrorCode {
        NO_SUCH_BUSINESS_HOUR_BY_STORE_ID("해당 가게의 영업시간 정보가 없습니다"),
        NO_SUCH_BUSINESS_HOUR_BY_BUSINESS_HOUR_ID("해당 영업시간 정보는 존재하지 않습니다"),
        DUPLICATE_DAY_OF_WEEK("이미 해당 요일에 대한 영업 시간이 등록되어 있습니다"),
        INVALID_TIME("시간정보가 유효하지 않습니다.");
        private final String message;

        BusinessHourErrorCode(String message) {
            this.message = message;
        }
        public String getMessage(){ return message; }
    }
    private final BusinessHourErrorCode errorCode;
    public BusinessHourException(BusinessHourErrorCode errorCode){
        this.errorCode = errorCode;
    }
    public BusinessHourErrorCode getErrorCode(){
        return errorCode;
    }
}
