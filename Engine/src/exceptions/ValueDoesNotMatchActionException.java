package exceptions;

public class ValueDoesNotMatchActionException extends Exception{
    private String exceptionMessage;
    public ValueDoesNotMatchActionException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
