package execution.instances.entity.manager;

import definition.entities.EntityDefinitionImpl;
import simulation.grid.GridImpl;

public interface EntityInstanceManager {
    //List<EntityInstanceImpl> getEntitiesList();
    void createEntityInstances(EntityDefinitionImpl entityDefinition, GridImpl positionGrid, int currTick);
    void killEntities(String entityName, GridImpl grid);
}
