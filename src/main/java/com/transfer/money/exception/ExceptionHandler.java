package com.transfer.money.exception;

import com.transfer.money.service.ServiceResponse;

public class ExceptionHandler implements ServiceResponse {

    private Exception e;

    public String getMessage() {
        return message;
    }

    private String message;

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setExceptionCode(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public ExceptionHandler() {
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    private ExceptionCode exceptionCode;

    public ExceptionHandler(Exception e) {
        this.message = e.getMessage();
        if(e instanceof MoneyTransferException){
            this.exceptionCode = ((MoneyTransferException) e).getExceptionCode();
        }
        else{
            this.exceptionCode = ExceptionCode.GENERAL_ERROR;
        }
    }

}
