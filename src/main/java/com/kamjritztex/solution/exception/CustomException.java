package com.kamjritztex.solution.exception;

public class CustomException extends RuntimeException{

    private String errorCode;
    private String errorMessage;
    public CustomException(String errorMessage,String errorCode ){
        super(errorMessage);
        this.errorMessage=errorMessage;
        this.errorCode=errorCode;
    }
    
    public CustomException(Throwable th){
        super(th);
        this.errorMessage=th.getMessage();
        this.errorCode="001";
    }
    public String getErrorCode(){
        return this.errorCode;
    }
    public String getMessage(){
        return this.errorMessage;
    }
    
}
