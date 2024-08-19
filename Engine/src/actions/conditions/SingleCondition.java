package actions.conditions;

import actions.Actions;
import actions.conditions.thenAndElse.ThenElseImpl;
import actions.context.ContextImpl;
import actions.selection.Selection;
import definition.properties.PropertyType;
import exceptions.*;
import execution.functions.Functions;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.property.PropertyInstanceImpl;

public class SingleCondition extends Conditions{
    //List<Condition> conditions;
    private String expression;
    private String operator;
    private String value;
    private boolean res;

    public SingleCondition(String entityName, String secondaryEntity, Selection selection, ThenElseImpl thenOp, ThenElseImpl elseOp, String expression, String operator, String value) {
        super(entityName, secondaryEntity, selection,"single", thenOp, elseOp);
        this.expression = expression;
        this.operator = operator;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public String getOperator(){
        return operator;
    }

    public String getExpression(){
        return expression;
    }



    @Override
    public void applyAction(ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        EntityInstanceImpl currentEntity = null;
        if(context.getPrimaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
            currentEntity = context.getPrimaryEntity();
        else if(context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
                currentEntity = context.getSecondaryEntity();
        }

        if(currentEntity!=null) {
            Object checkedConditionValue;
            PropertyType expressionType;
            Object checkedPropertyVal;
            //check property expression of condition
            Object checkedPropertyExpressionVal = checkSingleConditionPropertyExpression(expression, currentEntity
                    , context); //= currentEntity.getPropertiesMap().get(propertyName).getValue();
            if (!(checkedPropertyExpressionVal instanceof PropertyInstanceImpl)) { //expression is not a property
                expressionType = checkExpressionPropertyType(checkedPropertyExpressionVal);
                checkedConditionValue = checkExpression(value, expressionType, currentEntity
                        , context);
                checkedPropertyVal = checkedPropertyExpressionVal;
            } else { //expression is property
                String propertyName = ((PropertyInstanceImpl) checkedPropertyExpressionVal).getName();
                checkedConditionValue = checkExpression(value,
                        currentEntity.getPropertiesMap().get(propertyName).getPropertyDefinition().getType()
                        , currentEntity, context);
                checkedPropertyVal = ((PropertyInstanceImpl) checkedPropertyExpressionVal).getValue();

                if (checkedPropertyVal instanceof String)
                    checkedPropertyVal = checkExpression((String) checkedPropertyVal, ((PropertyInstanceImpl) checkedPropertyExpressionVal).getPropertyDefinition().getType()
                            , currentEntity, context);
                expressionType = ((PropertyInstanceImpl) checkedPropertyExpressionVal).getPropertyDefinition().getType();
            }

            res = operatorResult(checkedConditionValue, checkedPropertyVal, expressionType);

            if (res)
                for (Actions action : getThenOp().getActionsList()) {
                    action.applyAction(context);
                }
            else if (getElseOp() != null)
                for (Actions action : getElseOp().getActionsList())
                    action.applyAction(context);
            //else do nothing
        }

    }

    public PropertyType checkExpressionPropertyType(Object expressionProperty){
        if(expressionProperty instanceof Integer)
            return PropertyType.DECIMAL;
        else if(expressionProperty instanceof Float)
            return PropertyType.FLOAT;
        else if(expressionProperty instanceof Boolean ||
                (expressionProperty instanceof String && checkIfStringMeansBooleanVal((String)expressionProperty)))
            return PropertyType.BOOLEAN;
        else return PropertyType.STRING;
    }

    public Object checkSingleConditionPropertyExpression(String expression, EntityInstanceImpl entityInstance, ContextImpl context) throws VariableDoesNotExistsException, ValueDoesNotMatchFunctionException, ValueDoesNotMatchPropertyTypeException, EvaluateFunctionRequiresAnEntityAndAPropertyException, PropertyDoesNotExistException, EntityDoesNotMatchFunctionException {
        Functions function = new Functions();
        int startInd=0;
        int endInd=0;
        for(int i=0; i< expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                startInd = i + 1;
                break;
            }
        }

        for(int i = expression.length() -1; i >0; i--) {
            if (expression.charAt(i) == ')') {
                endInd = i;
                break;
            }
        }

        String envVar;
        if (endInd>0) {
            envVar = expression.substring(startInd, endInd);
            if (expression.startsWith("environment")) {
                PropertyInstanceImpl envProp = context.getActiveEnvironment().getProperty(envVar);
                return function.environmentFunction(envProp);

            } else if (expression.startsWith("random")) {
                try {
                    int num = Integer.parseInt(envVar);
                    return function.randomFunction(num);
                } catch (Exception e) {
                    System.out.println("in check expression of single condition");
                    throw new ValueDoesNotMatchFunctionException("Random function can get only decimal values, but was given otherwise.");
                }

            } else if(expression.startsWith("evaluate")){
                return function.evaluateFunction(entityInstance, envVar, context);
            } else if(expression.startsWith("percent")) {
                int numOfSeparators = 0, firstInd =0, secondInd=0, thirdInd=0;
                String subExpr = expression.substring(startInd);
                //check separators
                for(int i=0; i<subExpr.length(); i++){
                    if(subExpr.charAt(i) == ','){
                        if(firstInd == 0) {
                            firstInd = i;
                            numOfSeparators++;
                        }
                        else if(secondInd==0) {
                            secondInd = i;
                            numOfSeparators++;
                        }
                        else {
                            thirdInd = i;
                            numOfSeparators++;
                        }
                    }
                }
                String firstExp;
                String secondExp;
                Object checkedFirst;
                Object checkedSecond;
                if(numOfSeparators == 1 || numOfSeparators == 3) {
                    int separatorInd;
                    if (numOfSeparators == 1)
                        separatorInd = subExpr.indexOf(",");
                    else separatorInd = secondInd;

                    firstExp = subExpr.substring(0, separatorInd);
                    secondExp = subExpr.substring(separatorInd + 1);
                    //checkedFirst = checkExpression(firstExp, propertyType, entityInstance, context);
                    //checkedSecond = checkExpression(secondExp, propertyType, entityInstance, context);
                }
                else {
                    if (subExpr.startsWith("percent")) {
                        firstExp = subExpr.substring(0, secondInd);
                        secondExp = subExpr.substring(secondInd + 1);
                    } else {
                        //percent is second var
                        firstExp = subExpr.substring(0, firstInd);
                        secondExp = subExpr.substring(firstInd + 1);
                    }
                }
                checkedFirst = checkSingleConditionPropertyExpression(firstExp, entityInstance, context);
                checkedSecond = checkSingleConditionPropertyExpression(secondExp, entityInstance, context);
                if(checkedFirst instanceof Integer || checkedFirst instanceof String){
                    //check second exp - should be int or float
                    int first = 0; //(int)checkedFirst;
                    int secondI = 0;
                    float secondF = 0;
                    if(checkedFirst instanceof Integer)
                        first = (int)checkedFirst;
                    else{
                        try{
                            first = Integer.parseInt((String) checkedFirst);
                        }catch (Exception e){
                            throw new ValueDoesNotMatchFunctionException("Invalid arguments in percent function.");
                        }

                    }
                    if(checkedSecond instanceof Integer){
                        secondI = (int) checkedSecond;
                        return function.percentFunction(first, secondI);
                    }
                    else if(checkedSecond instanceof  Float){
                        secondF = (float) checkedSecond;
                        return function.percentFunction(first, secondF);
                    }
                    else if(checkedSecond instanceof String){
                        try{
                            secondF = Float.parseFloat((String)checkedSecond);
                            return function.percentFunction(first, secondF);
                        } catch (Exception e){
                            try{
                                secondI = Integer.parseInt((String) checkedSecond);
                                return function.percentFunction(first, secondI);
                            }catch (Exception e1){
                                throw new ValueDoesNotMatchFunctionException("Invalid arguments in percent function.");
                            }
                        }
                    }
                }

                } else{//(expression.startsWith("ticks")){
                    return function.ticksFunction(entityInstance, envVar, context);
                }
        }
        else if(entityInstance.getPropertiesMap().get(expression) != null)
            return entityInstance.getPropertiesMap().get(expression);

        return expression; //expression is simply a string
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    public void setRes(boolean res){
        this.res = res;
    }

    @Override
    public boolean getRes() {
        return res;
    }

    public boolean operatorResult(Object conditionCheckedVal, Object checkedPropertyVal, PropertyType propertyType) throws BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException {
        boolean res = false;
        Object propertyValue = checkedPropertyVal, conditionValue = conditionCheckedVal;
        if(checkedPropertyVal instanceof String)
            propertyValue = getValueFromString((String)checkedPropertyVal, propertyType);

        //do i need to check the property value at this point?
        if(conditionCheckedVal instanceof String)
            conditionValue = getValueFromString((String)conditionCheckedVal, propertyType);

        switch (operator){
            case("="):{
                if(conditionValue instanceof String && propertyType.equals(PropertyType.BOOLEAN))
                    if(!checkIfStringMeansBooleanVal((String)conditionValue))
                        throw new ValueDoesNotMatchPropertyTypeException("Property type is boolean, but value in condition" +
                                "does not match 'true' or 'false'");
                res = propertyValue.equals(conditionValue);
            }break;
            case("!="):{
                if(conditionValue instanceof String && propertyType.equals(PropertyType.BOOLEAN))
                    if(!checkIfStringMeansBooleanVal((String)conditionValue))
                        throw new ValueDoesNotMatchPropertyTypeException("Property type is boolean, but value in condition" +
                                "does not match 'true' or 'false'");
                res = !(propertyValue.equals(conditionValue));
            }break;
            case("bt"):{
                if(propertyValue instanceof Integer)
                    res = (int) propertyValue > (int) conditionValue;
                else if(propertyValue instanceof Float || propertyValue instanceof Double)
                    res = (float)propertyValue > (float) conditionValue;
                else if(propertyValue instanceof String) {
                    int compareString = ((String) propertyValue).compareTo((String) conditionValue);
                    res = compareString > 0;
                }
                else //boolean
                    throw new BooleanCanNotBeHigherOrLowerException("Can not check if one boolean value" +
                            " is bigger than the other.");
                //do i need to throw exception if boolean?
            }break;
            case("lt"):{
                if(propertyValue instanceof Integer)
                    res = (int) propertyValue < (int) conditionValue;
                else if(propertyValue instanceof Float || propertyValue instanceof Double)
                    res = (float)propertyValue < (float) conditionValue;
                else if(propertyValue instanceof String) {
                    int compareString = ((String) propertyValue).compareTo((String) conditionValue);
                    res = compareString < 0;
                }
                else //boolean
                    throw new BooleanCanNotBeHigherOrLowerException("Can not check if one boolean value" +
                            " is smaller than the other.");

            }break;
        }
        return res;
    }

    public boolean checkIfStringMeansBooleanVal(String expression)
    {
        return(expression.toLowerCase().equals("true") || expression.toLowerCase().equals("false"));
    }

    public Object getValueFromString(String val, PropertyType type) throws ValueDoesNotMatchPropertyTypeException {
        int num = type.ordinal();
        Object res;
        switch (num){
            case (0):{
                res = Integer.parseInt(val);
            }break;
            case(1):{
                if(checkIfStringMeansBooleanVal(val))
                    res = Boolean.parseBoolean(val);
                else throw new ValueDoesNotMatchPropertyTypeException("Property type is boolean, but value in condition" +
                        "does not match 'true' or 'false'");
            }break;
            case(2):{
                res = Float.parseFloat(val);
            }break;
            default:
                res = val;
        }
        return res;
    }

}

