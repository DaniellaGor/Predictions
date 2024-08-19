package actions;

import actions.context.ContextImpl;
import actions.context.ReplaceNode;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;

public class Replace extends Actions{
    private String killEntity;
    private String createEntity;
    private String mode;

    public Replace(String killEntity, String createEntity, String mode){
        super("","", "Replace", null);
        this.killEntity = killEntity;
        this.createEntity = createEntity;
        this.mode = mode;
    }

    public String getMode(){ return mode;}

    public String getKillEntity(){ return killEntity;}

    public String getCreateEntity(){ return createEntity;}

    @Override
    public void applyAction(ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException {
        EntityInstanceImpl currentEntity = null;
        if(context.getPrimaryEntity().getEntityDefinition().getName().equals(killEntity))
            currentEntity = context.getPrimaryEntity();
        else if(context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(killEntity))
                currentEntity = context.getSecondaryEntity();
        }

        if(currentEntity!=null) {

            context.getReplaceEntities().add(new ReplaceNode(currentEntity, createEntity, mode));

            //if (currentEntity.getEntityDefinition().getName().equals(killEntity)) {
               /* EntityInstanceImpl newEntity;
                if (mode.equals("scratch")) {
                    newEntity = context.getEntityInstancesManager().
                            createSingleEntityInstance(context.getEntityDefinitionMap().
                                    get(createEntity), context.getGrid(), currentEntity.getId(), context.getCurrentTick());
                } else {
                    newEntity = createNewEntityDerived(currentEntity, context);
                }

                currentEntity.setDoNotKill(false);
                *//*if(context.getEntityInstancesManager().getEntitiesMap().get(newEntity.getEntityDefinition().getName()).isEmpty()){
                    context.getEntityInstancesManager().getEntitiesMap().get(newEntity.getEntityDefinition().getName()) = new ArrayList<>();
                }*//*
                context.getEntityInstancesManager().getEntitiesMap().get(newEntity.getEntityDefinition().getName()).add(newEntity);
                //context.getGrid().setPosition(newEntity.getPosition(), newEntity);
            //}*/
        }
    }

    /*public EntityInstanceImpl createNewEntityDerived(EntityInstanceImpl oldEntity, ContextImpl context){
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
    }*/

    @Override
    public String getActionName() {
        return actionName;
    }
}
