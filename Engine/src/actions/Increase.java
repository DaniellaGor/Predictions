package actions;

import actions.context.ContextImpl;
import actions.selection.Selection;
import definition.properties.PropertyType;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.property.PropertyInstanceImpl;

public class Increase extends Actions {
    private String expression;
    private String propertyName;

    public Increase(String givenEntityName, String secondaryEntity, Selection selection, String givenExpression, String propertyGivenName) {
        super(givenEntityName, secondaryEntity, "Increase", selection);
        expression = givenExpression;
        propertyName = propertyGivenName;
    }

    @Override
    public void applyAction(ContextImpl context) throws VariableDoesNotExistsException, ValueDoesNotMatchFunctionException, ValueDoesNotMatchPropertyTypeException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        EntityInstanceImpl currentEntity = null;
        if (context.getPrimaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
            currentEntity = context.getPrimaryEntity();
        else if (context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
                currentEntity = context.getSecondaryEntity();
        }

        if (currentEntity != null) {
            PropertyInstanceImpl currProperty = currentEntity.getPropertiesMap().get(propertyName);
            Object expressionValue = checkExpression(expression, currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getType()
                    , currentEntity, context);
            //check that property is numeric
            if (currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getType().equals(PropertyType.DECIMAL)) {
                applyActionOnInteger(context, currentEntity, expressionValue);
            } else if (currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getType().equals(PropertyType.FLOAT)) {
                applyActionOnFloat(context, currentEntity, expressionValue);
            }
        }
    }


    @Override
    public String getActionName() {
        return actionName;
    }

    public String getExpression(){ return expression;}

    public String getPropertyName(){ return propertyName;}
    void applyActionOnInteger(ContextImpl context, EntityInstanceImpl currentEntity, Object expressionCheckedValue){
        int propertyValue = (int)currentEntity.getPropertiesMap().get(propertyName).getValue();
        int res = propertyValue;
        int topRange = (int)currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeTop();
        int bottomRange = (int)currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeBottom();

        if(expressionCheckedValue instanceof String){
            if(!((Integer.parseInt(expression) + propertyValue) > topRange &&
                    (Integer.parseInt(expression) + propertyValue) < bottomRange))
                res = Integer.parseInt(expression) + propertyValue;
        }
        else{
            if(!(((int)expressionCheckedValue + propertyValue) > topRange
                    || ((int)expressionCheckedValue + propertyValue) < bottomRange))
                res = (int)expressionCheckedValue + propertyValue;
        }
        currentEntity.getPropertiesMap().get(propertyName).setValue(res, context.getCurrentTick());

    }

    void applyActionOnFloat(ContextImpl context, EntityInstanceImpl currentEntity, Object expressionCheckedValue){
        float propertyValue = (float)currentEntity.getPropertiesMap().get(propertyName).getValue();
        float res = propertyValue;
        float topRange = currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeTop();
        float bottomRange = currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeBottom();

        if(expressionCheckedValue instanceof String){
            if(!((Float.parseFloat(expression) + propertyValue) > topRange ||
                    (Float.parseFloat(expression) + propertyValue) < bottomRange))
                res = Float.parseFloat(expression) + propertyValue;
        }
        else{
            if(!(((float)expressionCheckedValue + propertyValue) > topRange ||
                    ((float)expressionCheckedValue + propertyValue) < bottomRange))
                res = (float)expressionCheckedValue + propertyValue;
        }
        currentEntity.getPropertiesMap().get(propertyName).setValue(res, context.getCurrentTick());
    }
}
