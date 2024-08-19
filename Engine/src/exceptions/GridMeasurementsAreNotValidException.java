package exceptions;

public class GridMeasurementsAreNotValidException extends Exception{
    private String exceptionMessage;
    public GridMeasurementsAreNotValidException(String message){
        exceptionMessage = message;
    }

    @Override
    public String getMessage(){
        return exceptionMessage;
    }
}
