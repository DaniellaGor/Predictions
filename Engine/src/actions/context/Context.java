package actions.context;

import definition.entities.EntityDefinitionImpl;
import execution.environment.active.ActiveEnvironmentImpl;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.entity.manager.EntityInstancesManagerImpl;
import simulation.grid.GridImpl;

import java.util.Map;

public interface Context {
    EntityInstanceImpl getPrimaryEntity();
    EntityInstanceImpl getSecondaryEntity();
    void setSecondaryEntity(EntityInstanceImpl secondaryEntity);
    ActiveEnvironmentImpl getActiveEnvironment();

    EntityInstancesManagerImpl getEntityInstancesManager();

    GridImpl getGrid();

    Map<String,EntityDefinitionImpl> getEntityDefinitionMap();

    int getCurrentTick();

}