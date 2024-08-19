package exceptions;

public class EvaluateFunctionRequiresAnEntityAndAPropertyException extends Exception{
    private String exceptionMessage;
    public EvaluateFunctionRequiresAnEntityAndAPropertyException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
