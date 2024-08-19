package exceptions;

public class ActionOnNonExistingEntityInstanceException extends Exception{
    private String exceptionMessage;
    public ActionOnNonExistingEntityInstanceException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}

