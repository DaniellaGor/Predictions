package dto.actionsDTO;

public class ProximityDTO extends ActionDTO{
    private String source;
    private String target;
    private int numOfActions;
    private String depth;
    public ProximityDTO(String actionName, String primaryEntity, String secondaryEntity, String source, String target,String depth,  int numOfActions) {
        super(actionName, primaryEntity, secondaryEntity);
        this.source = source;
        this.target = target;
        this.numOfActions = numOfActions;
        this.depth = depth;
    }

    public String getSource(){ return source;}
    public String getTarget(){ return target;}
    public int getNumOfActions(){ return numOfActions;}
    public String getDepth(){ return depth;}
}
