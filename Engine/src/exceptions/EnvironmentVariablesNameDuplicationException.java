package exceptions;

public class EnvironmentVariablesNameDuplicationException extends Exception {
    private String exceptionMessage;
    public EnvironmentVariablesNameDuplicationException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
