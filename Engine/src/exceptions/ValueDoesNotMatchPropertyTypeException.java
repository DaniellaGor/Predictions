package exceptions;

public class ValueDoesNotMatchPropertyTypeException extends Exception{
    private String exceptionMessage;
    public ValueDoesNotMatchPropertyTypeException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
