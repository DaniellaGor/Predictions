package actions.context;

import definition.entities.EntityDefinitionImpl;
import execution.environment.active.ActiveEnvironmentImpl;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.entity.manager.EntityInstancesManagerImpl;
import simulation.grid.GridImpl;

import java.util.List;
import java.util.Map;

public class ContextImpl implements Context{

    private EntityInstanceImpl primaryEntity;
    private EntityInstanceImpl secondaryEntity;
    //private boolean primaryIsCurrent;
    private EntityInstancesManagerImpl entityInstancesManager;
    private ActiveEnvironmentImpl activeEnvironment;
    private GridImpl grid;
    private Map<String,EntityDefinitionImpl> entityDefinitionMap;
    private int currentTick;
    private boolean secondaryListIsEmpty;
    private List<ReplaceNode> replaceEntities;

    public ContextImpl(EntityInstanceImpl primaryEntity, EntityInstancesManagerImpl entityInstancesManager,
                       ActiveEnvironmentImpl activeEnvironment, GridImpl grid,
                       Map<String,EntityDefinitionImpl> entityDefinitionMap, int currentTick, List<ReplaceNode> replaceEntities){
        this.primaryEntity = primaryEntity;
        this.entityInstancesManager = entityInstancesManager;
        this.activeEnvironment = activeEnvironment;
        this.secondaryEntity = null;
        this.grid = grid;
        this.entityDefinitionMap = entityDefinitionMap;
        this.currentTick = currentTick;
        secondaryListIsEmpty = false;
        this.replaceEntities = replaceEntities;
       // this.primaryIsCurrent = true;
    }

   /* public boolean getCurrentEntityInstance(){
        return primaryIsCurrent;
    }

    public void setCurrentEntityInstance(boolean whatInstance){
        primaryIsCurrent = whatInstance;
    }*/

    public ContextImpl(EntityInstanceImpl primaryEntity, EntityInstanceImpl secondaryEntity,
                       EntityInstancesManagerImpl entityInstancesManager, ActiveEnvironmentImpl activeEnvironment,
                       GridImpl grid, Map<String,EntityDefinitionImpl> entityDefinitionMap, int currentTick, List<ReplaceNode> replaceEntities){
        this.primaryEntity = primaryEntity;
        this.entityInstancesManager = entityInstancesManager;
        this.activeEnvironment = activeEnvironment;
        this.secondaryEntity = secondaryEntity;
        this.entityDefinitionMap = entityDefinitionMap;
        this.currentTick = currentTick;
        this.grid = grid;
        this.replaceEntities = replaceEntities;
        secondaryListIsEmpty = false;
    }

    //for empty secondary lists
    public ContextImpl(EntityInstanceImpl primaryEntity, EntityInstanceImpl secondaryEntity,
                       EntityInstancesManagerImpl entityInstancesManager, ActiveEnvironmentImpl activeEnvironment,
                       GridImpl grid, Map<String,EntityDefinitionImpl> entityDefinitionMap, int currentTick, boolean secondaryListIsEmpty, List<ReplaceNode> replaceEntities){
        this.primaryEntity = primaryEntity;
        this.entityInstancesManager = entityInstancesManager;
        this.activeEnvironment = activeEnvironment;
        this.secondaryEntity = secondaryEntity;
        this.entityDefinitionMap = entityDefinitionMap;
        this.currentTick = currentTick;
        this.secondaryListIsEmpty = secondaryListIsEmpty;
        this.replaceEntities = replaceEntities;
        this.grid = grid;
    }

    public List<ReplaceNode> getReplaceEntities(){ return replaceEntities;}



    @Override
    public EntityInstanceImpl getPrimaryEntity() {
        return primaryEntity;
    }

    @Override
    public EntityInstanceImpl getSecondaryEntity() {
        return secondaryEntity;
    }

    @Override
    public void setSecondaryEntity(EntityInstanceImpl secondaryEntity) {
        this.secondaryEntity = secondaryEntity;
    }

    @Override
    public ActiveEnvironmentImpl getActiveEnvironment() {
        return activeEnvironment;
    }

    @Override
    public EntityInstancesManagerImpl getEntityInstancesManager() {
        return entityInstancesManager;
    }

    public GridImpl getGrid(){
        return grid;
    }

    @Override
    public Map<String,EntityDefinitionImpl> getEntityDefinitionMap() {
        return entityDefinitionMap;
    }

    public int getCurrentTick(){
        return currentTick;
    }
}
