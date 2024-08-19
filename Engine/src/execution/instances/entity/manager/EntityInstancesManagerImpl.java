package execution.instances.entity.manager;

import actions.context.ContextImpl;
import actions.context.ReplaceNode;
import definition.entities.EntityDefinitionImpl;
import definition.properties.PropertyDefinitionImpl;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.property.PropertyInstanceImpl;
import simulation.grid.GridImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstancesManagerImpl implements EntityInstanceManager{
    //private List<EntityInstanceImpl> entityInstancesList;
    private Map<String, List<EntityInstanceImpl>> entitiesMap;
    private int count;
    public EntityInstancesManagerImpl(){
        count = 0;
        //entityInstancesList = new ArrayList<>();
        entitiesMap = new HashMap<>();
    }
    //@Override
   /* public List<EntityInstanceImpl> getEntitiesList() {
        return entityInstancesList;
    }*/

    public Map<String, List<EntityInstanceImpl>> getEntitiesMap(){
        return entitiesMap;
    }
    @Override
    public void createEntityInstances(EntityDefinitionImpl entityDefinition, GridImpl positionGrid, int currTick) {
        count++;
        int num = 1;
        while(num <= entityDefinition.getPopulation()){
          /*  EntityInstanceImpl newEntityInstance = new EntityInstanceImpl(entityDefinition, num);
            //create properties defs
            for(PropertyDefinitionImpl currPropertyDef: entityDefinition.getProperties()){
                PropertyInstanceImpl newPropInstance = new PropertyInstanceImpl(currPropertyDef);
                newEntityInstance.addProperty(newPropInstance);
                positionGrid.setStartingPosition(newEntityInstance);
            }*/
            //int id = entitiesMap.get(entityDefinition.getName()).size();
            entitiesMap.get(entityDefinition.getName()).add(createSingleEntityInstance(entityDefinition,positionGrid,num, currTick));
            num++;
        }

        //return newEntityInstance;
    }

    public EntityInstanceImpl createSingleEntityInstance(EntityDefinitionImpl entityDefinition, GridImpl positionGrid, int id, int currTick){
        EntityInstanceImpl newEntityInstance = new EntityInstanceImpl(entityDefinition, id);
        //create properties defs
        for(PropertyDefinitionImpl currPropertyDef: entityDefinition.getProperties()){
            PropertyInstanceImpl newPropInstance = new PropertyInstanceImpl(currPropertyDef, currTick);
            newEntityInstance.addProperty(newPropInstance);
            //positionGrid.setStartingPosition(newEntityInstance);
        }
        positionGrid.setStartingPosition(newEntityInstance);
        return newEntityInstance;
    }

    @Override
    public void killEntities(String entityName, GridImpl grid) {
        List<EntityInstanceImpl> newList = new ArrayList<>();
        //newList = entityInstancesList.stream().filter(EntityInstanceImpl::doNotKill).collect(Collectors.toList());
        for(EntityInstanceImpl entityInstance: entitiesMap.get(entityName)){
            if(entityInstance.doNotKill())
                newList.add(entityInstance);
            else{
                //if was just killed and not replaced
                if(grid.getEntityInPoint(entityInstance.getPosition().getX(), entityInstance.getPosition().getY()) == entityInstance)
                    grid.setPosition(entityInstance.getPosition(), null);
                //else, was replaced - no need to set null in grid - there's a different entity there!
            }
        }

        //newList = entitiesMap.get(entityName).stream().filter(EntityInstanceImpl::doNotKill).collect(Collectors.toList());
        //entityInstancesList.clear();
        //entityInstancesList = newList;
        entitiesMap.put(entityName, newList);
    }

    public void replaceEntities(ContextImpl context){
        EntityInstanceImpl newEntity;
        String mode;
        for(ReplaceNode curr: context.getReplaceEntities()) {
            mode = curr.getMode();
            if (mode.equals("scratch")) {
                newEntity = context.getEntityInstancesManager().
                        createSingleEntityInstance(context.getEntityDefinitionMap().
                                get(curr.getCreateEntity()), context.getGrid(), curr.getKillEntity().getId(), context.getCurrentTick());
            } else {
                newEntity = createNewEntityDerived(curr.getKillEntity(), curr.getCreateEntity(), context);
            }

            curr.getKillEntity().setDoNotKill(false);
                /*if(context.getEntityInstancesManager().getEntitiesMap().get(newEntity.getEntityDefinition().getName()).isEmpty()){
                    context.getEntityInstancesManager().getEntitiesMap().get(newEntity.getEntityDefinition().getName()) = new ArrayList<>();
                }*/
            context.getEntityInstancesManager().getEntitiesMap().get(newEntity.getEntityDefinition().getName()).add(newEntity);
            //context.getGrid().setPosition(newEntity.getPosition(), newEntity);
            //}
        }
        context.getReplaceEntities().clear();
    }

    public EntityInstanceImpl createNewEntityDerived(EntityInstanceImpl oldEntity, String createEntity, ContextImpl context){
        EntityInstanceImpl newEntity = context.getEntityInstancesManager().
                createSingleEntityInstance(context.getEntityDefinitionMap().get(createEntity), context.getGrid(),
                        oldEntity.getId(), context.getCurrentTick());
        for(Map.Entry<String, PropertyInstanceImpl> entry: oldEntity.getPropertiesMap().entrySet()){
            PropertyInstanceImpl currOldProperty = entry.getValue();
            if(newEntity.getPropertiesMap().get(currOldProperty.getName()) != null){ //if the new entity has this property
                PropertyInstanceImpl currNewProperty = newEntity.getPropertiesMap().get(currOldProperty.getName());
                if(currOldProperty.getPropertyDefinition().getType().equals(currNewProperty.getPropertyDefinition().getType())){
                    //if they are of the same type
                    currNewProperty.setValue(currOldProperty.getValue(), context.getCurrentTick());
                }
            }
        }
        //newEntity.setPosition(oldEntity.getPosition());
        context.getGrid().setPosition(oldEntity.getPosition(), newEntity);
        return newEntity;
    }
}
