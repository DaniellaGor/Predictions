package actions;

import actions.context.ContextImpl;
import actions.selection.Selection;
import definition.properties.PropertyType;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.property.PropertyInstanceImpl;

public class Set extends Actions {

    private String expression;
    private String propertyName;

    public Set(String givenEntityName, String secondaryEntity, Selection selection, String givenExpression, String givenProperty){
        super(givenEntityName, secondaryEntity, "Set", selection);
        expression = givenExpression;
        propertyName = givenProperty;
    }

    public String getExpression(){ return expression;}

    public String getPropertyName(){ return propertyName;}
    @Override
    public void applyAction(ContextImpl context) throws VariableDoesNotExistsException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        EntityInstanceImpl currentEntity = null;
        if(context.getPrimaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
            currentEntity = context.getPrimaryEntity();
        else if(context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
                currentEntity = context.getSecondaryEntity();
        }

        if(currentEntity!=null) {
            Object expressionValue = checkExpression(expression, currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition()
                    .getType(), currentEntity, context);
            PropertyInstanceImpl currProperty = currentEntity.getPropertiesMap().get(propertyName);
            PropertyType currPropertyType = currProperty.getPropertyDefinition().getType();
            if (currPropertyType.equals(PropertyType.DECIMAL)) {
                if (expressionValue instanceof String) {
                    try {
                        int res = Integer.parseInt((String) expressionValue);
                        currentEntity.getPropertiesMap().get(currProperty.getName()).setValue(res, context.getCurrentTick());
                    } catch (Exception e) {
                        System.out.println("in apply action of set");
                        throw new ValueDoesNotMatchPropertyTypeException("Property type is decimal but value is not.");
                    }
                } else if (expressionValue instanceof Integer)
                    currentEntity.getPropertiesMap().get(currProperty.getName()).setValue(expressionValue, context.getCurrentTick());
                else throw new ValueDoesNotMatchPropertyTypeException("Property type is decimal but value is not.");
            } else if (currPropertyType.equals(PropertyType.FLOAT)) {
                if (expressionValue instanceof String) {
                    try {
                        float res = Float.parseFloat((String) expressionValue);
                        currentEntity.getPropertiesMap().get(currProperty.getName()).setValue(res, context.getCurrentTick());
                    } catch (Exception e) {
                        System.out.println("in apply action of set");
                        throw new ValueDoesNotMatchPropertyTypeException("Property type is float but value is not.");
                    }
                } else if (expressionValue instanceof Float)
                    currentEntity.getPropertiesMap().get(currProperty.getName()).setValue(expressionValue, context.getCurrentTick());
                else throw new ValueDoesNotMatchPropertyTypeException("Property type is float but value is not.");
            } else if (currPropertyType.equals(PropertyType.BOOLEAN)) {
                if (expressionValue instanceof String) {
                    if (((String) expressionValue).equalsIgnoreCase("true") ||
                            ((String) expressionValue).equalsIgnoreCase("false"))
                        currentEntity.getPropertiesMap().get(currProperty.getName())
                                .setValue(Boolean.parseBoolean((String) expressionValue), context.getCurrentTick());
                    else
                        throw new ValueDoesNotMatchPropertyTypeException("Property type is boolean but value is not 'true' or 'false'.");
                }
            } else // type is string
            {
                if (expressionValue instanceof String) {
                    currentEntity.getPropertiesMap().get(currProperty.getName())
                            .setValue(Boolean.parseBoolean((String) expressionValue), context.getCurrentTick());
                } else throw new ValueDoesNotMatchPropertyTypeException("Property type is string but value is not.");
            }
        }
    }

    @Override
    public String getActionName() {
        return actionName;
    }
}
