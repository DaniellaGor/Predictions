package exceptions;

public class ValueDoesNotMatchFunctionException extends Exception{
    private String exceptionMessage;
    public ValueDoesNotMatchFunctionException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
