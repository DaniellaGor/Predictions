package exceptions;

public class EntityDoesNotMatchFunctionException extends Exception{
    private String exceptionMessage;
    public EntityDoesNotMatchFunctionException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
