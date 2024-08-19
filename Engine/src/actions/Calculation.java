package actions;

import actions.context.ContextImpl;
import actions.selection.Selection;
import definition.properties.PropertyType;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;

public class Calculation extends Actions{

    private String propertyName;
    private String calculationType;

    private String arg1;

    private String arg2;

    public Calculation(String givenEntityName, String secondaryEntityName, Selection selection, String propertyGivenName, String givenType, String givenArg1, String givenArg2){
        super(givenEntityName, secondaryEntityName, "Calculation", selection);
        propertyName = propertyGivenName;
        calculationType = givenType;
        arg1 = givenArg1;
        arg2 = givenArg2;
    }
    @Override
    public void applyAction(ContextImpl context) throws ArithmeticException, VariableDoesNotExistsException, ValueDoesNotMatchFunctionException, ValueDoesNotMatchPropertyTypeException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException { //Map<String, PropertyInstance> propertiesMap) {
        EntityInstanceImpl currentEntity = null;
        if(context.getPrimaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
            currentEntity = context.getPrimaryEntity();
        else if(context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
                currentEntity = context.getSecondaryEntity();
        }

        if(currentEntity!=null) {
            Object checkedArg1 = checkExpression(arg1, currentEntity.getPropertiesMap().get(propertyName).
                    getPropertyDefinition().getType(), currentEntity, context);
            Object checkedArg2 = checkExpression(arg2, currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getType(), currentEntity, context);
            PropertyType propertyType = currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getType();
            if (propertyType.equals(PropertyType.DECIMAL))
                applyActionOnInteger(context, currentEntity, checkedArg1, checkedArg2);
            else
                applyActionOnDouble(context, currentEntity, checkedArg1, checkedArg2);
        }
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    public String getPropertyName(){ return propertyName;}
    public String getCalculationType(){ return calculationType;}
    public String getArg1(){ return arg1;}
    public String getArg2(){ return arg2;}

    void applyActionOnInteger(ContextImpl context, EntityInstanceImpl currentEntity, Object checkedArg1, Object checkedArg2) throws ArithmeticException{
        int numeralArg1, numeralArg2, res;
        int topRange = (int)currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeTop();
        int bottomRange = (int)currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeBottom();

        if(checkedArg1.getClass() == String.class)
            numeralArg1 = Integer.parseInt(arg1);
        else //expression value is not a string, do i need to check instance of?
            numeralArg1 = (int)checkedArg1;

        if(checkedArg2.getClass() == String.class)
            numeralArg2 =  Integer.parseInt(arg2);
        else //expression value is not a string, do i need to check instance of?
            numeralArg2 = (int)checkedArg2;

        if(calculationType.equals("multiply"))
            res = numeralArg1*numeralArg2;
        else {
            if (numeralArg2 == 0)
                throw new ArithmeticException("Trying to divide by zero!");
            else
                res = numeralArg1 / numeralArg2;
        }
        if(!(res>topRange || res<bottomRange))
            currentEntity.getPropertiesMap().get(propertyName).setValue(res, context.getCurrentTick());
    }

    void applyActionOnDouble(ContextImpl context, EntityInstanceImpl currentEntity, Object checkedArg1, Object checkedArg2) throws ArithmeticException{
        float numeralArg1, numeralArg2, res;
        float topRange = currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeTop();
        float bottomRange = currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getRangeBottom();

        if(checkedArg1.getClass() == String.class)
            numeralArg1 = Float.parseFloat((String) checkedArg1);
        else //expression value is not a string, do i need to check instance of?
            numeralArg1 = (float)checkedArg1;

        if(checkedArg2.getClass() == String.class)
            numeralArg2 =  Float.parseFloat((String) checkedArg2);
        else //expression value is not a string, do i need to check instance of?
            numeralArg2 = (float)checkedArg2;

        if(calculationType.equals("multiply"))
            res = numeralArg1*numeralArg2;
        else {
            if (numeralArg2 == 0.0)
                throw new ArithmeticException("Trying to divide by zero!");
            else
                res = numeralArg1 / numeralArg2;
        }
        if(!(res > topRange || res < bottomRange))
            currentEntity.getPropertiesMap().get(propertyName).setValue(res, context.getCurrentTick());
    }
}
