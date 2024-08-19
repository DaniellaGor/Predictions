package actions;

import actions.conditions.Conditions;
import actions.conditions.MultipleConditions;
import actions.conditions.SingleCondition;
import actions.context.ContextImpl;
import actions.selection.Selection;
import definition.properties.PropertyType;
import dto.actionsDTO.*;
import exceptions.*;
import execution.functions.Functions;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.property.PropertyInstanceImpl;

public abstract class Actions {
    protected String primaryEntityName;
    protected String secondaryEntityName;
    protected String actionName;
    protected Selection selection;


    public Actions(String primaryEntityName, String secondaryEntityName, String actionName, Selection selection){
        this.primaryEntityName = primaryEntityName;
        this.secondaryEntityName = secondaryEntityName;
        this.actionName = actionName;
        this.selection = selection;
    }
    public abstract void applyAction(ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException; //Map<String, PropertyInstance> propertiesMap);

    public abstract String getActionName();
    public Selection getSelection(){
        return selection;
    }

    public String getPrimaryEntity(){
        return primaryEntityName;
    }

    public String getSecondaryEntityName(){
        return secondaryEntityName;
    }

    //check expression: 1. function, 2. property, 3. string
    public Object checkExpression(String expression, PropertyType propertyType, EntityInstanceImpl entityInstance,
                                  ContextImpl context) throws VariableDoesNotExistsException, ValueDoesNotMatchFunctionException, ValueDoesNotMatchPropertyTypeException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
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
        if (endInd > 0) {
            envVar = expression.substring(startInd, endInd);
            if (expression.startsWith("environment")) {
                if (context.getActiveEnvironment().getProperty(envVar) == null)
                    throw new VariableDoesNotExistsException("The environment variable: " + envVar + ", does not exist.");
                PropertyInstanceImpl envProp = context.getActiveEnvironment().getProperty(envVar);
                if (envProp.getPropertyDefinition().getType().equals(PropertyType.FLOAT) && propertyType.equals(PropertyType.DECIMAL)) {
                    if(!actionName.equalsIgnoreCase("proximity"))
                        //if its proximity, he check is on depth - can be float
                        throw new ValueDoesNotMatchPropertyTypeException("In Environment function - can not cast float to integer.");
                } else if (!envProp.getPropertyDefinition().getType().equals(propertyType)) {
                    throw new ValueDoesNotMatchPropertyTypeException("Environment variable value type does not match" +
                            " argument's type.");
                }

                return function.environmentFunction(envProp);

            } else if (expression.startsWith("random")) {
                try {
                    int num = Integer.parseInt(envVar);
                    if (propertyType == PropertyType.STRING
                            || propertyType == PropertyType.BOOLEAN)
                        throw new ValueDoesNotMatchPropertyTypeException("Random argument is not decimal.");
                    return function.randomFunction(num);
                } catch (Exception e) {
                    System.out.println("in check expression");
                    throw new ValueDoesNotMatchFunctionException("Random argument is not decimal.");
                   /* throw new ValueDoesNotMatchFunctionException("Random function can get only" +
                            " decimal values, but was given otherwise.");*/
                }

            } else if (expression.startsWith("evaluate")) {
                return function.evaluateFunction(entityInstance, envVar, context);

            } else if (expression.startsWith("percent")) {
                /*int firstNum, tryToFindInt, separatorInd = envVar.indexOf(",");
                float secondNumF = 0;
                int secondNumInt = 0;
                String firstExpression = envVar.substring(0, separatorInd);
                String secondExpression = envVar.substring(separatorInd + 1);
                Object firstVar = firstExpression, secondVar = secondExpression;
                for (tryToFindInt = 0; tryToFindInt < firstExpression.length(); tryToFindInt++)
                    if (firstExpression.charAt(tryToFindInt) == '(')
                        break;
                if (tryToFindInt == 0)
                    firstVar = checkExpression(firstExpression, propertyType, entityInstance, context);

                for (tryToFindInt = 0; tryToFindInt < secondExpression.length(); tryToFindInt++)
                    if (secondExpression.charAt(tryToFindInt) == '(')
                        break;

                if (tryToFindInt == 0)
                    secondVar = checkExpression(secondExpression, propertyType, entityInstance, context);*/

                int numOfSeparators = 0, firstInd =0, secondInd=0, thirdInd=0;
                String subExpr = expression.substring(startInd, endInd);
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
                checkedFirst = checkExpression(firstExp, propertyType, entityInstance, context);
                checkedSecond = checkExpression(secondExp, propertyType, entityInstance, context);
                    if(checkedFirst instanceof Integer || checkedFirst instanceof String || checkedFirst instanceof Float){
                        //check second exp - should be int or float
                        float first = 0; //(int)checkedFirst;
                        int secondI = 0;
                        float secondF = 0;
                        if(checkedFirst instanceof Integer)
                            first = ((Integer) checkedFirst).floatValue();
                        else if(checkedFirst instanceof Float)
                            first = (float) checkedFirst;
                        else{
                            try{
                                first = Float.parseFloat((String) checkedFirst);
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
                    else
                        throw new ValueDoesNotMatchFunctionException("First argument in percent is not decimal.");
                }

               /* int firstNum, tryToFindInt, separatorInd = envVar.indexOf(",");
                float secondNumF = 0;
                int secondNumInt = 0;
                String firstExpression = envVar.substring(0, separatorInd);
                String secondExpression = envVar.substring(separatorInd + 1);
                Object firstVar = firstExpression, secondVar = secondExpression;
                for (tryToFindInt = 0; tryToFindInt < firstExpression.length(); tryToFindInt++)
                    if (firstExpression.charAt(tryToFindInt) == '(')
                        break;
                if (tryToFindInt == 0)
                    firstVar = checkExpression(firstExpression, propertyType, entityInstance, context);

                for (tryToFindInt = 0; tryToFindInt < secondExpression.length(); tryToFindInt++)
                    if (secondExpression.charAt(tryToFindInt) == '(')
                        break;

                if (tryToFindInt == 0)
                    secondVar = checkExpression(secondExpression, propertyType, entityInstance, context);


                try {
                    firstNum = Integer.parseInt(firstExpression);
                }catch (Exception e1) {
                    try {
                        firstNum = (int) firstVar;
                    } catch (Exception e) {
                        System.out.println("in check expression");
                        throw new ValueDoesNotMatchFunctionException("Invalid arguments in percent function.");
                    }
                }
                try{
                    secondNumF = Float.parseFloat(secondExpression);
                } catch (Exception e) {
                    try{
                        secondNumInt = Integer.parseInt(secondExpression);
                    }catch (Exception e3) {
                        try {
                            secondNumInt = (int) secondVar;
                        } catch (Exception e4) {
                            try {
                                secondNumF = (float) secondVar;
                            } catch (Exception e2) {
                                System.out.println("in check expression");
                                throw new ValueDoesNotMatchFunctionException("Invalid arguments in percent function.");
                            }
                        }
                    }*/
                    /*System.out.println("in check expression");
                    try {
                        firstNum = (int) firstVar;
                        if(secondVar instanceof Integer)
                            secondNumInt = (int)secondVar;
                        else if(secondVar instanceof Float)
                            secondNumF = (float) secondVar;
                    } catch (Exception e1) {
                        System.out.println("in check expression");
                        throw new ValueDoesNotMatchFunctionException("Invalid arguments in percent function.");
                        *//*throw new ValueDoesNotMatchFunctionException("Expecting first value to be" +
                                " decimal and second value to be decimal/float but gotten otherwise.");*//*
                    }*/
                    /*if(secondNumInt == 0 && secondNumF !=0)
                        return function.percentFunction(firstNum, secondNumF);
                    else if(secondNumInt != 0 && secondNumF == 0)
                        return function.percentFunction(firstNum, secondNumInt);*/

            else { //(expression.startsWith("ticks"))
                return function.ticksFunction(entityInstance, envVar, context);
            }
        }
        else if (entityInstance.getPropertiesMap().get(expression) != null) {
            PropertyInstanceImpl expressionIsProperty = entityInstance.getPropertiesMap().get(expression);
            if(expressionIsProperty.getPropertyDefinition().getType().equals(propertyType))
                return entityInstance.getPropertiesMap().get(expression).getValue();
            else throw new ValueDoesNotMatchPropertyTypeException("Argument is of type " + propertyType.name() +
                    ", but expression property is of type " + expressionIsProperty.getPropertyDefinition().getType().name() + ".");
        }

        //else if(expression instanceof TextField)

        else // expression is a string
            {
            if(propertyType.equals(PropertyType.STRING)){
                try{
                    int num = Integer.parseInt(expression);
                    throw new ValueDoesNotMatchPropertyTypeException("Argument is of type String, but value is decimal.");
                }catch (Exception e){
                    System.out.println("in check expression");
                    try{
                        float num = Float.parseFloat(expression);
                        throw new ValueDoesNotMatchPropertyTypeException("Argument is of type String, but value is float.");
                    }catch (Exception e1){
                        System.out.println("in check expression");
                        if(expression.equalsIgnoreCase("true") || expression.equalsIgnoreCase("false"))
                            throw new ValueDoesNotMatchPropertyTypeException("Argument is of type String, but value is boolean.");
                    }
                }
            }
        }
        return expression;
    }

    /*public int convertToInt(Object value){
        return (int)(value);
    }*/

    /*public float convertToFloat(Object value){
        return (float)(value);
    }
*/
    public ActionDTO createActionDto(){
        ActionDTO actionDTO = null;
        switch(actionName){
            case("Increase"):{
                Increase increase = (Increase) this;
                actionDTO = new IncreaseDTO(actionName, primaryEntityName, secondaryEntityName, increase.getPropertyName(), increase.getExpression());
            }break;
            case("Decrease"):{
                Decrease decrease =(Decrease) this;
                actionDTO = new DecreaseDTO(actionName, primaryEntityName, secondaryEntityName, decrease.getPropertyName(), decrease.getExpression());

            }break;
            case("Calculation"):{
                Calculation calculation = (Calculation) this;
                actionDTO = new CalculationDTO(actionName, primaryEntityName, secondaryEntityName,
                        calculation.getPropertyName(), calculation.getCalculationType(), calculation.getArg1(), calculation.getArg2());

            }break;
            case("Condition"):{
                int numOfThenActions=0, numOfElseActions=0;
                Conditions condition = (Conditions) this;
                if(condition.getSingularity().equalsIgnoreCase("single")){
                    SingleCondition singleCondition = (SingleCondition) this;
                    if(singleCondition.getThenOp()!=null)
                        numOfThenActions = singleCondition.getThenOp().getActionsList().size();
                    if(singleCondition.getElseOp()!=null)
                        numOfElseActions = singleCondition.getElseOp().getActionsList().size();
                    actionDTO = new ConditionDTO(actionName, primaryEntityName, secondaryEntityName, singleCondition.getSingularity(),
                            numOfThenActions, numOfElseActions, singleCondition.getExpression(), singleCondition.getValue(),
                            singleCondition.getOperator(), "", 0);
                }
                else{
                    MultipleConditions multipleCondition = (MultipleConditions) this;
                    if(multipleCondition.getThenOp()!=null)
                        numOfThenActions = multipleCondition.getThenOp().getActionsList().size();
                    if(multipleCondition.getElseOp()!=null)
                        numOfElseActions = multipleCondition.getElseOp().getActionsList().size();
                    actionDTO = new ConditionDTO(actionName, primaryEntityName, secondaryEntityName, multipleCondition.getSingularity(),
                            numOfThenActions, numOfElseActions, "", "", "",
                            multipleCondition.getLogical(), multipleCondition.getConditionList().size());
                }
            }break;
            case("Set"):{
                Set set = (Set)this;
                actionDTO = new SetDTO(actionName, primaryEntityName, secondaryEntityName, set.getPropertyName(), set.getExpression());
                //createSetController(actionDTO);
            }break;
            case("Replace"):{
                Replace replace = (Replace) this;
                actionDTO = new ReplaceDTO(actionName, primaryEntityName, secondaryEntityName, replace.getKillEntity(),
                        replace.getCreateEntity(), replace.getMode());
            }break;
            case("Proximity"):{
                Proximity proximity = (Proximity) this;
                actionDTO = new ProximityDTO(actionName, primaryEntityName, secondaryEntityName, proximity.getSourceEntity(),
                        proximity.getTargetEntity(), proximity.getDepth(), proximity.getActionsList().size());
            }break;
            case("Kill"):{
                Kill kill = (Kill) this;
                actionDTO = new KillDTO(actionName, primaryEntityName, secondaryEntityName);
            }break;
        }

        return actionDTO;
    }

}

