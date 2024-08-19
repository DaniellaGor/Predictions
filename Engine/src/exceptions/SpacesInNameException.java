package exceptions;

public class SpacesInNameException extends Exception{
    private String exceptionMessage;
    public SpacesInNameException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
