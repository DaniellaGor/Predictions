package dto.actionsDTO;

public class CalculationDTO extends ActionDTO{
    private String resultProp;
    private String calculationType;
    private String arg1;
    private String arg2;
    public CalculationDTO(String actionName, String primaryEntity, String secondaryEntity, String resultProp, String calculationType,
                          String arg1, String arg2) {
        super(actionName, primaryEntity, secondaryEntity);
        this.resultProp = resultProp;
        this.calculationType = calculationType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getResultProp(){ return resultProp;}
    public String getCalculationType(){ return calculationType;}
    public String getArg1(){ return arg1;}
    public String getArg2(){ return arg2;}
}
