package exceptions;

public class PropertyDoesNotExistException extends Exception{
    private String exceptionMessage;
    public PropertyDoesNotExistException(String message){
        exceptionMessage = message;
    }
    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
