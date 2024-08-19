package exceptions;

public class FileDoesNotExistsException extends Exception{
    private String exceptionMessage;
    public FileDoesNotExistsException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
