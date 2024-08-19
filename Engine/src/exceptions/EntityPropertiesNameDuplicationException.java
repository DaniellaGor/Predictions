package exceptions;

public class EntityPropertiesNameDuplicationException extends Exception {
    private String exceptionMessage;
    public EntityPropertiesNameDuplicationException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
