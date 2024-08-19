package world.fileValidation;

import exceptions.*;
import generated.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileValidationImpl implements FileValidation {
    @Override
    public void checkIfFileExistsAndOfTypeXML(File xmlFile) throws FileNotFoundException, FileIsNotXMLFileException, FileDoesNotExistsException {
        if (!xmlFile.exists())
            throw new FileDoesNotExistsException("File '" + xmlFile.getName() + "' was not found.");
        else if (!xmlFile.getName().endsWith(".xml"))
            throw new FileIsNotXMLFileException("The file " + xmlFile.getName() + " is not of type XML.");
    }

    @Override
    public void checkSpaces(PRDWorld prdWorld) throws SpacesInNameException {
        //Entities
        String currEntityName, currPropertyName, currEnvPropertyName, currRuleName;
        for (PRDEntity currEntity : prdWorld.getPRDEntities().getPRDEntity()) {
            currEntityName = currEntity.getName();
            if (currEntityName.trim().contains(" "))
                throw new SpacesInNameException("Entity name is not allowed to have spaces in it." +
                        "The following entity name:" + currEntityName + " has spaces in it.");
            for (PRDProperty currProperty : currEntity.getPRDProperties().getPRDProperty()) {
                currPropertyName = currProperty.getPRDName();
                if (currPropertyName.trim().contains(" "))
                    throw new SpacesInNameException("Property name is not allowed to have spaces in it." +
                            "The following property name:" + currPropertyName + " has spaces in it.");
            }
           /* //Rules
            for (PRDRule currRule : prdWorld.getPRDRules().getPRDRule()) {
                currRuleName = currRule.getName();
                if (currRuleName.trim().contains(" "))
                    throw new SpacesInNameException("Rule name is not allowed to have spaces in it." +
                            "The following rule name: " + currRuleName + " has spaces in it.");
            }*/
            //EnvProperties
            for (PRDEnvProperty currEnvProperty : prdWorld.getPRDEnvironment().getPRDEnvProperty()) {
                currEnvPropertyName = currEnvProperty.getPRDName();
                if (currEnvPropertyName.trim().contains(" "))
                    throw new SpacesInNameException("Environment property name is not allowed to have spaces in it." +
                            "The following environment property name:" + currEnvPropertyName + " has spaces in it.");
            }
        }
    }


    @Override
    public void checkIfEnvironmentVariableNamesAreUnique(PRDWorld prdWorld) throws EnvironmentVariablesNameDuplicationException {

        Map<String, List<PRDEnvProperty>> groupedByName = prdWorld.getPRDEnvironment()
                .getPRDEnvProperty().stream().collect(Collectors.groupingBy(PRDEnvProperty::getPRDName));

        List<String> duplicateNames = groupedByName.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!duplicateNames.isEmpty()) {
            String duplicatesMessage = "The following environment property names repeat themselves more than once: " +
                    duplicateNames.stream().collect(Collectors.joining(", "));
            throw new EnvironmentVariablesNameDuplicationException(duplicatesMessage);

        }
    }

    @Override
    public void checkIfEntitiesPropertiesNamesAreUnique(PRDEntities prdEntities) throws EntityPropertiesNameDuplicationException {
        for (PRDEntity prdEntity : prdEntities.getPRDEntity()) {
            Map<String, List<PRDProperty>> groupedByName = prdEntity.getPRDProperties().getPRDProperty()
                    .stream().collect(Collectors.groupingBy(PRDProperty::getPRDName));

            List<String> duplicateNames = groupedByName.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (!duplicateNames.isEmpty()) {
                String duplicatesMessage = "The following property names repeat themselves more than once: " +
                        duplicateNames.stream().collect(Collectors.joining(", "));
                throw new EntityPropertiesNameDuplicationException(duplicatesMessage);
            }
        }
    }

    @Override
    public void checkIfEntityInvokingActionExist(PRDEntities prdEntities, PRDRules prdRules) throws ActionOnNonExistingEntityInstanceException {
        Map<String, PRDEntity> mapOfEntitiesByNames = prdEntities.getPRDEntity().stream()
                .collect(Collectors.toMap(PRDEntity::getName, prd -> prd));

        for (PRDRule currPrdRule : prdRules.getPRDRule())
            for (PRDAction currAction : currPrdRule.getPRDActions().getPRDAction()) {
                String actionEntityName = currAction.getEntity();
                if (actionEntityName!=null) {
                    if (mapOfEntitiesByNames.get(actionEntityName)==null)
                        throw new ActionOnNonExistingEntityInstanceException("In action: " + currAction.getType()
                                + ", using a non-existing entity.");
                }
            }
    }

    @Override
    public void checkIfValueMatchesPropertyType(PRDWorld prdWorld) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException {
        for(PRDEntity prdEntity: prdWorld.getPRDEntities().getPRDEntity())
            for(PRDProperty prdProperty: prdEntity.getPRDProperties().getPRDProperty()){
                String initValue = prdProperty.getPRDValue().getInit();
                if(initValue!=null){
                    String exceptionMessage = "For property of type " + prdProperty.getType();
                    checkIfValueMatches(initValue, prdProperty,exceptionMessage,prdWorld);
                }
            }
    }

    @Override
    public void checkIfPropertyExistsWhenInvokingActions(PRDEntities prdEntities, PRDRules prdRules) throws PropertyDoesNotExistException, ValueDoesNotMatchActionException, EntityDoesNotExistException {
        String currEntityName;
        Map<String, PRDEntity> mapOfEntitiesByNames = prdEntities.getPRDEntity().stream()
                .collect(Collectors.toMap(PRDEntity::getName, prd -> prd));

        for (PRDRule currPrdRule : prdRules.getPRDRule())
            for (PRDAction currAction : currPrdRule.getPRDActions().getPRDAction()) {
                if (!(currAction.getType().equals("replace") || currAction.getType().equals("proximity"))) {
                    currEntityName = currAction.getEntity();
                    Map<String, PRDProperty> mapOfPropertiesByNames = mapOfEntitiesByNames.get(currEntityName).getPRDProperties()
                            .getPRDProperty().stream().collect(Collectors.toMap(PRDProperty::getPRDName, prd -> prd));

                    checkSingleAction(prdEntities, currAction, mapOfPropertiesByNames);
                }
            }
    }

    public void checkSingleAction(PRDEntities prdEntities, PRDAction prdAction, Map<String, PRDProperty> mapOfPropertiesByNames) throws PropertyDoesNotExistException, ValueDoesNotMatchActionException, EntityDoesNotExistException {

        if (prdAction.getType().equals("condition")) {
            //checkIfPropertyExistsInConditionAction(prdAction.getPRDCondition(), mapOfPropertiesByNames);
            if (prdAction.getPRDThen()!=null) {
                for (PRDAction currAction : prdAction.getPRDThen().getPRDAction())
                    checkSingleAction(prdEntities, currAction, mapOfPropertiesByNames);
            }
            if (prdAction.getPRDElse()!=null) {
                for (PRDAction currAction : prdAction.getPRDElse().getPRDAction()) {
                    checkSingleAction(prdEntities, currAction, mapOfPropertiesByNames);

                }
            }
        } else if(prdAction.getType().equals("calculation")){
            String currPropertyName = prdAction.getResultProp();
            if (mapOfPropertiesByNames.get(currPropertyName)==null)
                throw new PropertyDoesNotExistException("In action: " + prdAction.getType() +
                        ", property is required but does not exist.");
        } else if(prdAction.getType().equals("proximity")){
            int depth;
            try {
                depth = Integer.parseInt(prdAction.getPRDEnvDepth().getOf());
            }catch (Exception e){
                System.out.println("in check single action file validation");
                try{
                    depth = (int) Float.parseFloat(prdAction.getPRDEnvDepth().getOf());
                }catch(Exception e2){
                    System.out.println("in check single action file validation");
                    throw new ValueDoesNotMatchActionException("Proximity depth value should be decimal or float, but it is not.");
                }
            }
            boolean foundSource = false, foundTarget = false;
            for(PRDEntity prdEntity: prdEntities.getPRDEntity()){
                if(prdEntity.getName().equals(prdAction.getPRDBetween().getSourceEntity()))
                    foundSource = true;
                if(prdEntity.getName().equals(prdAction.getPRDBetween().getTargetEntity()))
                    foundTarget = true;
            }
            if(!foundSource)
                throw new EntityDoesNotExistException("Entity " + prdAction.getPRDBetween().getSourceEntity() + " does not exist");
            if(!foundTarget)
                throw new EntityDoesNotExistException("Entity " + prdAction.getPRDBetween().getTargetEntity() + " does not exist");
        }

        else if(prdAction.getType().equals("replace")){
            boolean foundKillEntity = false, foundCreateEntity = false;
            int i=0;
            List<PRDEntity> prdEntityList = prdEntities.getPRDEntity();
            while(i<prdEntityList.size() && !(foundCreateEntity && foundKillEntity)){
                PRDEntity currPrdEntity = prdEntityList.get(i);
                if(currPrdEntity.getName().equals(prdAction.getKill()))
                    foundKillEntity = true;
                if(currPrdEntity.getName().equals(prdAction.getCreate()))
                    foundCreateEntity = true;
                i++;
            }
            if(!foundCreateEntity)
                throw new EntityDoesNotExistException("Entity '" + prdAction.getCreate() + "' in action 'Replace' does not exist.");
            if(!foundKillEntity)
                throw new EntityDoesNotExistException("Entity '" + prdAction.getKill() + "' in action 'Replace' does not exist.");
        }

        else if (!prdAction.getType().equals("kill")) {
            String currPropertyName = prdAction.getProperty();
            if (mapOfPropertiesByNames.get(currPropertyName)==null)
                throw new PropertyDoesNotExistException("In action: " + prdAction.getType() +
                        ", property is required but does not exist.");
        }
    }

   /* public void checkIfPropertyExistsInConditionAction(PRDCondition prdCondition, Map<String, PRDProperty> mapOfPropertiesByNames) throws PropertyDoesNotExistException {

        if (prdCondition.getSingularity().equals("single")) {
            String currPropertyName = prdCondition.getProperty();
            if (mapOfPropertiesByNames.get(currPropertyName)==null)
                throw new PropertyDoesNotExistException("In action: condition, property is required" +
                        " but does not exists.");
            //else return true;
        } else {
            List<PRDCondition> conditionList = prdCondition.getPRDCondition();
            for (PRDCondition currConditionNode : conditionList)
                checkIfPropertyExistsInConditionAction(currConditionNode, mapOfPropertiesByNames);
        }
    }*/

    @Override
    public void checkIfMathActionVariablesAreNumericHelper(PRDWorld prdWorld) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, PropertyDoesNotExistException, EntityDoesNotExistException {
        for (PRDRule prdRule : prdWorld.getPRDRules().getPRDRule()) {
            for (PRDAction currAction : prdRule.getPRDActions().getPRDAction()) {
                checkIfMathActionVariablesAreNumeric(prdWorld, currAction);
            }
        }
    }


    public void checkIfMathActionVariablesAreNumeric(PRDWorld prdWorld,PRDAction currAction) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, PropertyDoesNotExistException, EntityDoesNotExistException {
        if(!(currAction.getType().equals("replace") || currAction.getType().equals("proximity"))) {
            String currEntity = currAction.getEntity();
            Map<String, PRDEntity> mapOfEntitiesByName = prdWorld.getPRDEntities().getPRDEntity()
                    .stream().collect(Collectors.toMap(PRDEntity::getName, prd -> prd));
            Map<String, PRDProperty> mapOfEntityPropertiesByName = mapOfEntitiesByName.get(currEntity).getPRDProperties()
                    .getPRDProperty().stream().collect(Collectors.toMap(PRDProperty::getPRDName, prd -> prd));
            String currProperty;
            //check
            if (currAction.getType().equals("increase") || currAction.getType().equals("decrease")) {
                currProperty = currAction.getProperty();
                checkIfIncreaseDecreaseVariablesAreNumeric(prdWorld, currAction, mapOfEntityPropertiesByName, currProperty);
            } else if (currAction.getType().equals("calculation")) {
                currProperty = currAction.getResultProp();
                checkIfCalculationVariablesAreNumeric(prdWorld, currAction, mapOfEntityPropertiesByName, currProperty);
            } else if (currAction.getType().equals("set")) {
                currProperty = currAction.getProperty();
                String value = currAction.getValue();
                String exceptionMessage = "For action " + currAction.getType() + ", with property "
                        + mapOfEntityPropertiesByName.get(currProperty).getType();
                if (!checkFunctions(currAction, prdWorld, mapOfEntityPropertiesByName.get(currProperty), value))
                    checkIfValueMatches(value, mapOfEntityPropertiesByName.get(currProperty), exceptionMessage, prdWorld);

            } else if (currAction.getType().equals("condition")) {
                if (currAction.getPRDCondition().getSingularity().equals("multiple")) {
                    if (currAction.getPRDThen() != null)
                        for (PRDAction currActionNode : currAction.getPRDThen().getPRDAction())
                            checkIfMathActionVariablesAreNumeric(prdWorld, currActionNode);
                    if (currAction.getPRDElse() != null)
                        for (PRDAction currActionNode : currAction.getPRDElse().getPRDAction())
                            checkIfMathActionVariablesAreNumeric(prdWorld, currActionNode);
                }
            }
        }
    }

    public void checkIfValueMatches(String value, PRDProperty prdProperty,String message, PRDWorld prdWorld) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException {

        switch (prdProperty.getType()) {
            case ("decimal"): {
                    try {
                        int num = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        System.out.println("in check if value matches file validation");
                        throw new ValueDoesNotMatchPropertyTypeException(message + " the value should be decimal but it is not.");
                    }
            }
            break;
            case ("float"): {
                    try {
                        float num = Float.parseFloat(value);
                    } catch (NumberFormatException e) {
                        System.out.println("in check if value matches file validation");
                        throw new ValueDoesNotMatchPropertyTypeException(message + " the value should be float but it is not.");
                    }
                }
            break;
            case ("boolean"): {
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
                        throw new ValueDoesNotMatchPropertyTypeException(message + " the value should be boolean but it is not.");
                }
            break;
            case("string"):{
                boolean parseSucceeded = false;
                try{
                    int num = Integer.parseInt(value);
                    parseSucceeded = true;
                }catch (Exception e){
                    System.out.println("in check if value matches file validation");
                    System.out.println(e.getMessage());
                    /*if(parseSucceeded) {
                        throw e;
                    }*/
                }
                if(parseSucceeded)
                    throw new ValueDoesNotMatchPropertyTypeException("Property '" + prdProperty.getPRDName() + "' is of type String, but value is decimal.");
                else {
                    try {
                        float num = Float.parseFloat(value);
                        parseSucceeded = true;
                        //throw new ValueDoesNotMatchPropertyTypeException("Property '" + prdProperty.getPRDName() + "' is of type String, but value is float.");
                    } catch (Exception e) {
                        System.out.println("in check if value matches file validation");
                        System.out.println(e.getMessage());
                    }
                        /*if(parseSucceeded)
                            throw e;

                        if (!parseSucceeded && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")))
                            throw new ValueDoesNotMatchPropertyTypeException("Property '" + prdProperty.getPRDName() + "' is of type String, but value is boolean.");
                    }*/
                    if(parseSucceeded)
                        throw new ValueDoesNotMatchPropertyTypeException("Property '" + prdProperty.getPRDName() + "' is of type String, but value is float.");

                    else{
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
                            throw new ValueDoesNotMatchPropertyTypeException("Property '" + prdProperty.getPRDName() + "' is of type String, but value is boolean.");
                    }
                }
            }

        }

    }
    public void checkIfCalculationVariablesAreNumeric(PRDWorld prdWorld,PRDAction currAction, Map<String, PRDProperty> propertyMap, String currProperty) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, PropertyDoesNotExistException, EntityDoesNotExistException {
        String arg1, arg2;
        boolean checkArg1, checkArg2;
        if (currAction.getPRDMultiply()!=null) {
            arg1 = currAction.getPRDMultiply().getArg1();
            arg2 = currAction.getPRDMultiply().getArg2();
        } else {
            arg1 = currAction.getPRDDivide().getArg1();
            arg2 = currAction.getPRDDivide().getArg2();
        }
        checkArg1 = checkFunctions(currAction, prdWorld,  propertyMap.get(currProperty),arg1);
        if(!checkArg1){
            if(propertyMap.get(arg1)!=null){
                if(!propertyMap.get(arg1).getType().equalsIgnoreCase(propertyMap.get(currProperty).getType()))
                    throw new ValueDoesNotMatchPropertyTypeException("Value of property '" + arg1 +"' does not match the current property.");
                checkArg1 = true;
            }
        }

        checkArg2 = checkFunctions(currAction, prdWorld, propertyMap.get(currProperty),arg2);

        if(!checkArg2){
            if(propertyMap.get(arg2)!=null){
                if(!propertyMap.get(arg2).getType().equalsIgnoreCase(propertyMap.get(currProperty).getType()))
                    throw new ValueDoesNotMatchPropertyTypeException("Value of property '" + arg2 +"' does not match the current property.");
                checkArg2 = true;
            }
        }

        if (!checkArg1 || !checkArg2) {
            if (propertyMap.get(currProperty).getType().equals("decimal")) {
                try {
                    int num1, num2;
                    if (checkArg2)
                        num1 = Integer.parseInt(arg1);
                    else
                        num2 = Integer.parseInt(arg2);
                } catch (NumberFormatException e) {
                    System.out.println("in check calculation");
                    throw new MathActionVariablesAreNotNumericException("For action 'calculation', with" +
                            " property '" + currProperty + "' the values should be decimal, but they are not.");
                }
            } else if (propertyMap.get(currProperty).getType().equals("float")) {
                try {
                    float num1, num2;
                    if (checkArg2)
                        num1 = Float.parseFloat(arg1);
                    else
                        num2 = Float.parseFloat(arg2);
                } catch (NumberFormatException e) {
                    System.out.println("in check calculation");
                    throw new MathActionVariablesAreNotNumericException("For action 'calculation', with" +
                            " property '" + currProperty + "' the values should be float, but they are not.");
                }
            }
        }
    }

    public void checkIfIncreaseDecreaseVariablesAreNumeric(PRDWorld prdWorld,PRDAction currAction, Map<String, PRDProperty> propertyMap, String currProperty) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, PropertyDoesNotExistException, EntityDoesNotExistException {
        String expression = currAction.getBy();
        if (!checkFunctions(currAction, prdWorld, propertyMap.get(currProperty),expression)) {
            if (propertyMap.get(currProperty).getType().equals("decimal"))
                try {
                    int num = Integer.parseInt(expression);
                } catch (NumberFormatException e) {
                    System.out.println("in check increase decrease");
                    throw new MathActionVariablesAreNotNumericException("For action 'increase', with" +
                            " property '" + currProperty + "' the value should be decimal, but it is not.");
                }
            else //must be float ,if (propertyMap.get(currProperty).getType().equals("float"))
                try {
                    float num = Float.parseFloat(expression);
                } catch (NumberFormatException e) {
                    System.out.println("in check increase decrease");
                    throw new MathActionVariablesAreNotNumericException("For action 'increase', with" +
                            " property '" + currProperty + "' the value should be float, but it is not.");
                }
        }
    }

    public boolean checkFunctions(PRDAction currAction, PRDWorld prdWorld, PRDProperty currPrdProperty, String expression) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, EntityDoesNotExistException, PropertyDoesNotExistException {
        String envArgument;
        int startInd;
        int endInd;
        for(startInd=0; startInd< expression.length(); startInd++)
            if(expression.charAt(startInd)=='(')
                break;
        startInd++;

        for(endInd= expression.length() -1; endInd >0; endInd--)
            if(expression.charAt(endInd)==')')
                break;


        Map<String, PRDEnvProperty> mapOfEnvPropertiesByNames = prdWorld.getPRDEnvironment().getPRDEnvProperty().stream()
                .collect(Collectors.toMap(PRDEnvProperty::getPRDName, prd -> prd));

        if (expression.startsWith("environment")) {
            envArgument = expression.substring(startInd, endInd);
            String envVarType;
            envVarType = mapOfEnvPropertiesByNames.get(envArgument).getType();
            if(!(currAction.getType().equalsIgnoreCase("kill"))){
                if(currAction.getType().equalsIgnoreCase("set")){
                    if(!(currPrdProperty.getType().equalsIgnoreCase("float") && envVarType.equalsIgnoreCase("decimal")) &&
                    !currPrdProperty.getType().equalsIgnoreCase(envVarType))
                        throw new ValueDoesNotMatchPropertyTypeException("Environment variable type does not match the current property type.");
                }
                if(envVarType.equals("boolean") || envVarType.equals("string")){
                    throw new MathActionVariablesAreNotNumericException("Action '" + currAction.getType() +"' requires arithmetic value" +
                            " but was given differently.");
                }
            }
            return true;
        } else if (expression.startsWith("random")) {
            envArgument = expression.substring(startInd, endInd);
            try {
                int num = Integer.parseInt(envArgument);
                if((currPrdProperty.getType().equalsIgnoreCase("string")) ||  (currPrdProperty.getType().equalsIgnoreCase("boolean"))){
                    throw new ValueDoesNotMatchPropertyTypeException("Random value is arithmetic, but property type is not.");
                }
            } catch (NumberFormatException e) {
                System.out.println("in check functions file valid");
                throw new MathActionVariablesAreNotNumericException("Random function requires decimal type" +
                        " but was given differently.");}
            return true;
        } else if (expression.startsWith("evaluate") || expression.startsWith("ticks")){
            int i, dotIndex = expression.indexOf(".");
            boolean valid = false;
            PRDEntity currEntity=null;
            PRDProperty currPropertyInFunction = null;
            String entityName = expression.substring(0, dotIndex);
            String propertyName = expression.substring(dotIndex+1);
            for(i=0; i< prdWorld.getPRDEntities().getPRDEntity().size() ; i++){
                currEntity = prdWorld.getPRDEntities().getPRDEntity().get(i);
                if(currEntity.getName().equals(entityName)) {
                    //valid = true;
                    for(i=0; i< currEntity.getPRDProperties().getPRDProperty().size() ; i++) {
                        currPropertyInFunction = currEntity.getPRDProperties().getPRDProperty().get(i);
                        if (currPropertyInFunction.getPRDName().equals(propertyName)) {
                            valid = true;
                            if(currPropertyInFunction.getType().equalsIgnoreCase("boolean") || currPropertyInFunction.getType().equalsIgnoreCase("string"))
                                throw new MathActionVariablesAreNotNumericException("Property in function should be of arithmetic type");
                            else break;
                        }
                    }
                    if(!valid)
                        throw new PropertyDoesNotExistException("Property '" + propertyName + "' does not exist in" +
                                " entity '" + currEntity.getName() + "'.");
                }
            }

            if(!valid)
                throw new EntityDoesNotExistException("Function receives entity '"
                        + entityName +"' but it does not exist.");
            /*if(expression.startsWith("evaluate") &&
                    currPropertyInFunction.getType().equalsIgnoreCase("boolean") || currPropertyInFunction.getType()
                    .equalsIgnoreCase("string"))
                throw new MathActionVariablesAreNotNumericException("Action '" + currAction.getType() +"' requires arithmetic value" +
                        " but was given differently.");*/

        } else if(expression.startsWith("percent")){
            /*String allExp = expression.substring(startInd, endInd); //get the expression, might be a function in it, check it
            String firstExp, secondExp;
            int psikAmount = 0, psikInd, fisrtPsik = 0, secondPsik = 0, lastPsik = 0;
            for(int i=0; i<allExp.length(); i++)
                if(allExp.charAt(i) == ',') {
                    psikAmount++;
                    if(fisrtPsik == 0)
                        fisrtPsik = i;
                    else if(secondPsik == 0)
                        secondPsik = 1;
                    else lastPsik = i;
                }

            if(psikAmount == 3){
                firstExp = allExp.substring(0, secondPsik);
                secondExp = allExp.substring(secondPsik+1);

            }
            else if(psikAmount == 2){

            }
            else{
                psikInd = allExp.indexOf(",");
                firstExp = allExp.substring(0, psikInd);
                secondExp = allExp.substring(psikInd+1);
            }*/
            return true;
        }
        return false;
    }

    public void checkGrid(PRDWorld prdWorld) throws GridMeasurementsAreNotValidException {
        if(prdWorld.getPRDGrid().getRows()<10 || prdWorld.getPRDGrid().getRows() > 100 ||
        prdWorld.getPRDGrid().getColumns() < 10 || prdWorld.getPRDGrid().getColumns() > 100)
            throw new GridMeasurementsAreNotValidException("Number of rows and columns should be between 10 and 100.");

    }
}