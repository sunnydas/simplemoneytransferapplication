package com.transfer.money.exception;

public class MoneyTransferException extends Exception {

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    ExceptionCode exceptionCode;

    public MoneyTransferException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public MoneyTransferException(String message, Throwable cause, ExceptionCode exceptionCode) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }
}
