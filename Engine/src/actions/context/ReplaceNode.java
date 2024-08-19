package actions.context;

import execution.instances.entity.EntityInstanceImpl;

public class ReplaceNode {
    private EntityInstanceImpl killEntity;
    private String createEntity;
    private String mode;

    public ReplaceNode(EntityInstanceImpl killEntity, String createEntity, String mode){
        this.killEntity = killEntity;
        this.createEntity = createEntity;
        this.mode = mode;
    }

    public EntityInstanceImpl getKillEntity(){ return killEntity;}
    public String getCreateEntity(){ return createEntity;}
    public String getMode(){ return mode;}
}
