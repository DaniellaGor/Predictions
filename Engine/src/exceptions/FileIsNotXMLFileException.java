package exceptions;

public class FileIsNotXMLFileException extends Exception{
    private String exceptionMessage;
    public FileIsNotXMLFileException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
