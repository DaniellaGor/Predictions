package exceptions;

public class MathActionVariablesAreNotNumericException extends Exception{
    private String exceptionMessage;
    public MathActionVariablesAreNotNumericException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
