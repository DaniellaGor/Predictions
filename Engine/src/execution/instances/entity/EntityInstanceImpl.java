package execution.instances.entity;

import actions.Actions;
import actions.conditions.Conditions;
import actions.context.ContextImpl;
import actions.selection.Selection;
import definition.entities.EntityDefinitionImpl;
import dto.EntityDefinitionDTO;
import dto.EntityInstanceDTO;
import dto.PropertyDTO;
import exceptions.*;
import execution.instances.property.PropertyInstanceImpl;
import simulation.point.PointImpl;

import java.util.*;

public class EntityInstanceImpl implements EntityInstance {
    private EntityDefinitionImpl entityDefinition;
    private Map<String, PropertyInstanceImpl> propertiesMap;
    private int id;
    private boolean doNotKill;
    private PointImpl position;

    public EntityInstanceImpl(EntityDefinitionImpl entityDefinition, int id){
        // createPropertiesMap(properties);
        this.entityDefinition = entityDefinition;
        this.id = id;
        doNotKill = true;
        propertiesMap = new HashMap<>();
    }

    public void setPosition(PointImpl position){
        this.position = position;
    }

    public PointImpl getPosition(){
        return position;
    }

    public void addProperty(PropertyInstanceImpl property){
        //PropertyInstance newInstance = createPropertyInstance(property);
        propertiesMap.put(property.getName(), property);
    }

    public EntityDefinitionImpl getEntityDefinition() {
        return entityDefinition;
    }

    public EntityInstanceDTO createEntityInstanceDTO(int num, boolean isFinished){
        EntityDefinitionDTO entityDefinitionDTO = entityDefinition.createEntityDefinitionDTO();
        Map<String, PropertyDTO> propertyDTOMap = new HashMap<>();
        for(Map.Entry<String, PropertyInstanceImpl> entry: propertiesMap.entrySet()){
            propertyDTOMap.put(entry.getKey(), entry.getValue().createPropertyDTOFromPropertyInstance(isFinished));
        }
        return new EntityInstanceDTO(entityDefinitionDTO, propertyDTOMap, num);
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean doNotKill() {
        return doNotKill;
    }

    @Override
    public void setDoNotKill(boolean kill) {
        doNotKill = kill;
    }

    public void invokeActions(List<Actions> actionsList, ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        boolean kill = false;
        for(Actions action: actionsList){
            //replace and proximity do not have primary entity
            if(!action.getActionName().equalsIgnoreCase("Replace") && !action.getActionName().equalsIgnoreCase("Proximity")){
                    if(action.getPrimaryEntity().equals(entityDefinition.getName())) {
                        //check if there's a secondary entity to work with
                        if (!action.getSecondaryEntityName().equals("")) {
                            //list of all secondaries
                            List<EntityInstanceImpl> secondaryEntities = getSecondaryEntitiesList(context, action);

                            for (EntityInstanceImpl currSecondaryEntity : secondaryEntities) {
                                ContextImpl currContext = new ContextImpl(this, currSecondaryEntity,
                                        context.getEntityInstancesManager(), context.getActiveEnvironment(),
                                        context.getGrid(), context.getEntityDefinitionMap(), context.getCurrentTick(), context.getReplaceEntities());
                                action.applyAction(currContext);
                                if (action.getActionName().equalsIgnoreCase("kill") || action.getActionName().equalsIgnoreCase("replace"))
                                    kill = true;
                            }
                            // }
                        } else { //no secondary, just primary, replace or proximity
                            action.applyAction(context);
                            /*if (action.getActionName().equalsIgnoreCase("kill"))
                                kill = true;*/
                        }
                    }
            } if(action.getActionName().equalsIgnoreCase("proximity") || action.getActionName().equalsIgnoreCase("replace")) {
                action.applyAction(context);
                /*if (action.getActionName().equalsIgnoreCase("replace"))
                    kill = true;*/
                //else primary entity of this action is not this entity, do nothing
            }
        }
        //return kill;
    }

    public List<EntityInstanceImpl> getSecondaryEntitiesList(ContextImpl context, Actions action) throws ValueDoesNotMatchFunctionException, VariableDoesNotExistsException, ValueDoesNotMatchPropertyTypeException, BooleanCanNotBeHigherOrLowerException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        List<EntityInstanceImpl> tempSecondaryEntities = new ArrayList<>();
        List<EntityInstanceImpl> finalList = new ArrayList<>();
        Random random = new Random();
        Selection selection = action.getSelection();
        String secondaryEntity = action.getSecondaryEntityName();
        Conditions condition = action.getSelection().getCondition();
        int count = selection.getCount();
        if(selection.getCondition()!=null) {
                for (EntityInstanceImpl currEntity : context.getEntityInstancesManager().getEntitiesMap().get(secondaryEntity)) {
                    ContextImpl currContext = new ContextImpl(currEntity, null, context.getEntityInstancesManager(),
                            context.getActiveEnvironment(), context.getGrid(), context.getEntityDefinitionMap(), context.getCurrentTick(), context.getReplaceEntities());
                    condition.applyAction(currContext);
                    if (condition.getRes())
                        tempSecondaryEntities.add(currEntity);

            }
        }
        else tempSecondaryEntities = context.getEntityInstancesManager().getEntitiesMap().get(secondaryEntity);

        for(int i=0; i<count && i<tempSecondaryEntities.size(); i++){
            int randomInd = random.nextInt(count);
            finalList.add(tempSecondaryEntities.get(randomInd));
        }
        return finalList;
    }

    public Map<String, PropertyInstanceImpl> getPropertiesMap(){
        return propertiesMap;
    }

}
