package dto.actionsDTO;

public abstract class ActionDTO {
    protected String actionName;
    protected String primaryEntity;
    protected String secondaryEntity;

    public ActionDTO(String actionName, String primaryEntity, String secondaryEntity){
        this.actionName = actionName;
        this.primaryEntity = primaryEntity;
        this.secondaryEntity = secondaryEntity;
    }

    public String getName(){ return actionName;}
    public String getPrimaryEntity(){ return primaryEntity;}
    public String getSecondaryEntity(){ return secondaryEntity;}

}
