package dto.actionsDTO;

public class ConditionDTO extends ActionDTO{
    private String singularity;
    private int numOfThen;
    private int numOfElse;
    private String property;
    private String value;
    private String operator;
    private String logic;
    private int numOfCondInLogic;

    public ConditionDTO(String actionName, String primaryEntity, String secondaryEntity, String singularity,
                        int numOfThen, int numOfElse, String property, String value, String operator, String logic, int numOfCondInLogic) {
        super(actionName, primaryEntity, secondaryEntity);
        this.singularity = singularity;
        this.numOfThen = numOfThen;
        this.numOfElse = numOfElse;
        this.property = property;
        this.value = value;
        this.operator = operator;
        this.logic = logic;
        this.numOfCondInLogic = numOfCondInLogic;
    }

    public String getSingularity(){ return singularity;}
    public int getNumOfThen(){ return numOfThen;}
    public int getNumOfElse(){ return numOfElse;}
    public String getProperty(){ return property;}
    public String getValue(){ return value;}
    public String getOperator(){ return operator;}
    public String getLogic(){ return logic;}
    public int getNumOfCondInLogic(){ return numOfCondInLogic;}
}
