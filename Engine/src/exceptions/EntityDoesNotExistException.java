package exceptions;

public class EntityDoesNotExistException extends Exception{
    private String exceptionMessage;
    public EntityDoesNotExistException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
