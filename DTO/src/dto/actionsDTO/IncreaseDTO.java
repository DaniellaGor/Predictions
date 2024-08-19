package dto.actionsDTO;

public class IncreaseDTO extends ActionDTO{
    private String property;
    private String byExpression;

    public IncreaseDTO(String actionName, String primaryEntity, String secondaryEntity, String property, String byExpression) {
        super(actionName, primaryEntity, secondaryEntity);
        this.property = property;
        this.byExpression = byExpression;
    }

    public String getProperty(){ return property;}
    public String getByExpression(){ return byExpression;}
}
