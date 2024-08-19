package dto.actionsDTO;

public class ReplaceDTO extends ActionDTO{
    private String kill;
    private String create;
    private String mode;

    public ReplaceDTO(String actionName, String primaryEntity, String secondaryEntity, String kill, String create, String mode) {
        super(actionName, primaryEntity, secondaryEntity);
        this.kill = kill;
        this.create = create;
        this.mode = mode;
    }

    public String getKill(){ return kill;}
    public String getCreate(){ return create;}

    public String getMode(){ return mode;}
}
