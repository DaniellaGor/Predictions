package execution.functions;

import actions.context.ContextImpl;
import exceptions.EntityDoesNotMatchFunctionException;
import exceptions.PropertyDoesNotExistException;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.property.PropertyInstanceImpl;

import java.util.Random;

public class Functions {
   public Object environmentFunction(PropertyInstanceImpl propertyInstance){
        return propertyInstance.getValue();
    }

    public int randomFunction(int someArgument){
        Random random = new Random();
        //if(!someArgument.getClass().isInstance(int.class))
            //manage exception
        return random.nextInt(someArgument+1);
    }

    public Object evaluateFunction(EntityInstanceImpl entityInstance, String entityPropertyName, ContextImpl context) throws EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        int dotIndex = entityPropertyName.indexOf(".");
        String entityName = entityPropertyName.substring(0, dotIndex);
        String propertyName = entityPropertyName.substring(dotIndex+1);

        //check if argument in function is one of the contexts entities
        //first check if not same as primary
        if(!(entityName.equals(context.getPrimaryEntity().getEntityDefinition().getName()))){
            /*if(context.getSecondaryEntity()!=null){
                //check if its the secondary
                if(!entityName.equals(context.getSecondaryEntity().getEntityDefinition().getName()))
                    throw new EntityDoesNotMatchFunctionException("Entity '" + entityName + "' is not the same as current entity or " +
                            "as secondary entity.");
                //else it's the secondary entity
            }*/

            if(context.getSecondaryEntity()==null || !entityName.equals(context.getSecondaryEntity().getEntityDefinition().getName())) {
                throw new EntityDoesNotMatchFunctionException("Entity '" + entityName + "' is not the same as current entity or " +
                        "as secondary entity.");
                //else it's the secondary entity
            }
            else{
                if(context.getSecondaryEntity().getPropertiesMap().get(propertyName) == null)
                    throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in entity "
                    + entityName);
                else return context.getSecondaryEntity().getPropertiesMap().get(propertyName).getValue();

            }
        }
        else{ //the argument is the primary
            if(context.getPrimaryEntity().getPropertiesMap().get(propertyName) == null){
                throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in entity "
                        + entityName);
            }
            else return context.getPrimaryEntity().getPropertiesMap().get(propertyName).getValue();
        }

        /*if(!entityInstance.getEntityDefinition().getName().equals(entityName))
            throw new EntityDoesNotMatchFunctionException("Entity '" + entityName + "' is not the same as current entity - '" +
                    entityInstance.getEntityDefinition().getName() + "'.");*/
        /*if(entityInstance.getPropertiesMap().get(propertyName) == null)
            throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in this entity.");*/

       //return entityInstance.getPropertiesMap().get(propertyName).getValue();
    }

    public float percentFunction(float first, float second){
        return ((first*second)/(float)100);
    }

    public int ticksFunction(EntityInstanceImpl entityInstance, String entityPropertyName, ContextImpl context) throws EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        int dotIndex = entityPropertyName.indexOf(".");
        String entityName = entityPropertyName.substring(0, dotIndex);
        String propertyName = entityPropertyName.substring(dotIndex+1);

        //check if argument in function is one of the contexts entities
        //first check if not same as primary
        if(!(entityName.equals(context.getPrimaryEntity().getEntityDefinition().getName()))){
            /*if(context.getSecondaryEntity()!=null){
                //check if its the secondary
                if(!entityName.equals(context.getSecondaryEntity().getEntityDefinition().getName()))
                    throw new EntityDoesNotMatchFunctionException("Entity '" + entityName + "' is not the same as current entity or " +
                            "as secondary entity.");
                //else it's the secondary entity
            }*/

            if(context.getSecondaryEntity()==null || !entityName.equals(context.getSecondaryEntity().getEntityDefinition().getName())) {
                throw new EntityDoesNotMatchFunctionException("Entity '" + entityName + "' is not the same as current entity or " +
                        "as secondary entity.");
                //else it's the secondary entity
            }
            else{
                if(context.getSecondaryEntity().getPropertiesMap().get(propertyName) == null)
                    throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in entity "
                            + entityName);
                else{
                    /*ArrayList<ChangesInValueNodeImpl> tempValueChangesArray = context.getSecondaryEntity().getPropertiesMap().
                            get(propertyName).getValueChangesArray();*/
                    //return context.getCurrentTick() - tempValueChangesArray.get(tempValueChangesArray.size()-1).getStart();
                    return context.getCurrentTick() - context.getSecondaryEntity().getPropertiesMap().get(propertyName).getLastChangeInValue();
                }

            }
        }
        else{ //the argument is the primary
            if(context.getPrimaryEntity().getPropertiesMap().get(propertyName) == null){
                throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in entity "
                        + entityName);
            }
            else {
                /*ArrayList<ChangesInValueNodeImpl> tempValueChangesArray = context.getPrimaryEntity().getPropertiesMap().
                        get(propertyName).getValueChangesArray();*/
                //return context.getCurrentTick() - tempValueChangesArray.get(tempValueChangesArray.size()-1).getStart();
                return context.getCurrentTick() - context.getPrimaryEntity().getPropertiesMap().get(propertyName).getLastChangeInValue();
            }
        }

/*
        if(!entityInstance.getEntityDefinition().getName().equals(entityName))
            throw new EntityDoesNotMatchFunctionException("Entity '" + entityName + "' is not the same as current entity - '" +
                    entityInstance.getEntityDefinition().getName() + "'.");
        if(entityInstance.getPropertiesMap().get(propertyName) == null)
            throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in this entity.");

        ArrayList<ChangesInValueNodeImpl> tempValueChangesArray = entityInstance.getPropertiesMap().
                get(propertyName).getValueChangesArray();
        //now tick - start tick of this property (which was inserted to list)
        return context.getCurrentTick() - tempValueChangesArray.get(tempValueChangesArray.size()-1).getStart();*/

    }
}
