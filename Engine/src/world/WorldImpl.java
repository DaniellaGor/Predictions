package world;

import actions.*;
import actions.conditions.Conditions;
import actions.conditions.MultipleConditions;
import actions.conditions.SingleCondition;
import actions.conditions.thenAndElse.ThenElseImpl;
import actions.selection.Selection;
import definition.entities.EntityDefinitionImpl;
import definition.properties.PropertyDefinitionImpl;
import definition.properties.PropertyType;
import dto.*;
import dto.actionsDTO.ActionDTO;
import exceptions.*;
import execution.environment.manager.EnvironmentManagerImpl;
import generated.*;
import rules.Rules;
import rules.activation.ActivationImpl;
import world.fileValidation.FileValidationImpl;
import terminate.TerminateImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldImpl implements World {
    private final static String JAXB_XML_PACKAGE_NAME = "generated";
    //private final List<EntityDefinitionImpl> entityDefinitionList;
    private final Map<String, EntityDefinitionImpl> entityDefinitionMap;
    //private EntityInstancesManagerImpl entityInstancesManager;
    private EnvironmentManagerImpl environmentManager;
    private final List<Rules> rulesList;
    private TerminateImpl terminate;
    private int numOfThreads;

    private int rows;
    private int cols;

    public WorldImpl(String filePath) throws JAXBException, FileNotFoundException, PropertyDoesNotExistException, FileIsNotXMLFileException, ActionOnNonExistingEntityInstanceException, EnvironmentVariablesNameDuplicationException, EntityPropertiesNameDuplicationException, MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, SpacesInNameException, FileDoesNotExistsException, GridMeasurementsAreNotValidException, ValueDoesNotMatchActionException, EntityDoesNotExistException {
        entityDefinitionMap = new HashMap<>();
        rulesList = new ArrayList<>();
        InputStream inputStream;
        File xmlFile = new File(filePath);
        try{
           inputStream = new FileInputStream(xmlFile);
        }catch (Exception e){
            System.out.println("in worldImpl ctor");
            throw new FileDoesNotExistsException("File '" + xmlFile.getName() + "' was not found.");
        }
        PRDWorld prdWorld = getDataFromXML(inputStream);
        //check file existence + data
        validateXMLData(xmlFile, prdWorld);
        copyDataFromValidXML(prdWorld);
    }

    public GridDTO getGridDTODimensions(){
        return new GridDTO(rows, cols);
    }
    @Override
    public Map<String, EntityDefinitionImpl> getEntitiesDefinitionMap() {
        return entityDefinitionMap;
    }

    public Map<String, EntityDefinitionDTO> getEntitiesDefinitionDTOMap(){
        Map<String, EntityDefinitionDTO> dtoMap = new HashMap<>();
        for(Map.Entry<String, EntityDefinitionImpl> entry: entityDefinitionMap.entrySet()){
            dtoMap.put(entry.getKey(), entry.getValue().createEntityDefinitionDTO());
        }
        return dtoMap;
    }
    @Override
    public List<Rules> getRulesList() {
        return rulesList;
    }

    @Override
    public EnvironmentManagerImpl getEnvironmentManager() {
        return environmentManager;
    }

    @Override
    public TerminateImpl getTerminateConditions() {
        return terminate;
    }

    private static PRDWorld getDataFromXML(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (PRDWorld) u.unmarshal(in);
    }

    public void validateXMLData(File xmlFile, PRDWorld prdWorld) throws FileIsNotXMLFileException, FileNotFoundException, EnvironmentVariablesNameDuplicationException, SpacesInNameException, EntityPropertiesNameDuplicationException, ActionOnNonExistingEntityInstanceException, PropertyDoesNotExistException, MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, FileDoesNotExistsException, GridMeasurementsAreNotValidException, ValueDoesNotMatchActionException, EntityDoesNotExistException {
        FileValidationImpl checkFileData = new FileValidationImpl();
        checkFileData.checkIfFileExistsAndOfTypeXML(xmlFile);
        checkFileData.checkSpaces(prdWorld);
        checkFileData.checkIfEnvironmentVariableNamesAreUnique(prdWorld);
        checkFileData.checkIfEntitiesPropertiesNamesAreUnique(prdWorld.getPRDEntities());
        checkFileData.checkIfEntityInvokingActionExist(prdWorld.getPRDEntities(),prdWorld.getPRDRules());
        checkFileData.checkIfValueMatchesPropertyType(prdWorld);
        checkFileData.checkIfPropertyExistsWhenInvokingActions(prdWorld.getPRDEntities(),prdWorld.getPRDRules());
        checkFileData.checkIfMathActionVariablesAreNumericHelper(prdWorld);
        checkFileData.checkGrid(prdWorld);
    }

    public WorldDTO createWorldDTO(){

        Map<String, EntityDefinitionDTO> entitiesDTOMap = new HashMap<>();
        for(Map.Entry<String, EntityDefinitionImpl> entry: entityDefinitionMap.entrySet()){
            entitiesDTOMap.put(entry.getKey(),entry.getValue().createEntityDefinitionDTO());
        }

        List<RulesDTO> rulesDTOList = new ArrayList<>();
        for(Rules rule: rulesList){
            List<ActionDTO> actionsDTO = new ArrayList<>();
            for(Actions action: rule.getActionsList()){
                //actionsDTONames.add(action.getActionName());
                actionsDTO.add(action.createActionDto());
            }
            RuleActivationDTO activationDTO = new RuleActivationDTO(rule.getActivation().getTicks(),
                    rule.getActivation().getProbability());
            rulesDTOList.add(new RulesDTO(rule.getName(), activationDTO, actionsDTO.size(), actionsDTO));
        }

        EnvironmentManagerDTO environmentManagerDTO = environmentManager.createEnvironmentManagerDTO();

        TerminationDTO terminationDTO = new TerminationDTO(terminate.getTicksAmount(),
                terminate.getSecondsAmount(), false, terminate.getTerminateByUser(), terminate.getIfUserIsAllowedToTerminate());

        return new WorldDTO(entitiesDTOMap, environmentManagerDTO, rulesDTOList, terminationDTO, numOfThreads);
    }

    public List<EntityDefinitionDTO> createEntityDefDTOList(){
        List<EntityDefinitionDTO> DTOList = new ArrayList<>();
        for(Map.Entry<String, EntityDefinitionImpl> entry: entityDefinitionMap.entrySet())
        //for(EntityDefinitionImpl entityDefinition: entityDefinitionList)
            DTOList.add(entry.getValue().createEntityDefinitionDTO());
        return DTOList;
    }
    @Override
    public void copyDataFromValidXML(PRDWorld prdWorld) {
        numOfThreads = prdWorld.getPRDThreadCount();
        environmentManager = new EnvironmentManagerImpl();
        //create environment manager
        List<PropertyDefinitionImpl> propertyDefinitionList = createEnvProertyList(prdWorld.getPRDEnvironment());
        for(PropertyDefinitionImpl currProperty: propertyDefinitionList)
            environmentManager.addEnvironmentToManager(currProperty);

        terminate = copyTerminate(prdWorld);
        copyEntityDefinition(prdWorld);
        rows = prdWorld.getPRDGrid().getRows();
        cols = prdWorld.getPRDGrid().getColumns();
        copyRules(prdWorld);
    }

    public int getRows(){ return rows;}
    public int getCols(){ return cols;}
    public TerminateImpl copyTerminate(PRDWorld prdWorld){
        int seconds = 0, ticks=0;
        boolean secondsBool = false, ticksBool = false, allowUserToTerminate = false;
        if(!prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks().isEmpty()){
            for(Object terminateCondition: prdWorld.getPRDTermination().getPRDBySecondOrPRDByTicks()){
                if(terminateCondition instanceof PRDBySecond) {
                    PRDBySecond tempSeconds = (PRDBySecond) terminateCondition;
                    seconds = tempSeconds.getCount();
                    if(seconds != 0)
                        secondsBool = true;
                }
                else if(terminateCondition instanceof PRDByTicks){
                    PRDByTicks tempTick = (PRDByTicks) terminateCondition;
                    ticks = tempTick.getCount();
                    if(ticks != 0)
                        ticksBool = true;
                }
                else{
                    if(terminateCondition != null)
                        allowUserToTerminate = true;
                }
            }
        }
        else allowUserToTerminate = true;
        return new TerminateImpl(ticks, seconds, secondsBool, ticksBool, allowUserToTerminate);
    }
    public void copyRules(PRDWorld prdWorld){
        for(PRDRule prdRule: prdWorld.getPRDRules().getPRDRule()){
            int ticks = 1;
            double probability = 1;
            if(prdRule.getPRDActivation()!=null) {
                if (prdRule.getPRDActivation().getTicks() != null)
                    ticks = prdRule.getPRDActivation().getTicks();
                if (prdRule.getPRDActivation().getProbability() != null)
                    probability = prdRule.getPRDActivation().getProbability();
            }
            ActivationImpl activation = new ActivationImpl(ticks, probability);
            Rules newRule = new Rules(prdRule.getName(), prdRule.getPRDActions().getPRDAction().size(), activation);

            for(PRDAction prdAction: prdRule.getPRDActions().getPRDAction()){
                newRule.addAction(copyPRDAction(prdAction));
            }
            rulesList.add(newRule);
        }
    }

    public Actions copyPRDAction(PRDAction prdAction) {
        Actions newAction = null;
        Selection selection = null;
        String secondaryEntity = "";
        if(prdAction.getPRDSecondaryEntity()!=null){
            secondaryEntity = prdAction.getPRDSecondaryEntity().getEntity();
            boolean elseThenWereAlreadyCreated = true;
            Conditions newCondition = (Conditions) copyPRDCondition(prdAction.getPRDSecondaryEntity().getPRDSelection().getPRDCondition(),
                    prdAction, elseThenWereAlreadyCreated, secondaryEntity, null);
            selection = new Selection(Integer.parseInt(prdAction.getPRDSecondaryEntity().getPRDSelection().getCount()), newCondition);
            //selection.getCondition().setSelection(selection);
            //newCondition.setSelection(selection);
        }
        switch (prdAction.getType()){
            case("calculation"):{
                newAction = copyCalculation(prdAction, secondaryEntity, selection);
            }break;
            case("decrease"):{
                newAction = new Decrease(prdAction.getEntity(), secondaryEntity, selection, prdAction.getBy(), prdAction.getProperty());
            }break;
            case("increase"):{
                newAction = new Increase(prdAction.getEntity(),secondaryEntity,selection, prdAction.getBy(), prdAction.getProperty());
            }break;
            case("condition"): {
                boolean elseThenWereAlreadyCreated = false;
                newAction = copyPRDCondition(prdAction.getPRDCondition(), prdAction, elseThenWereAlreadyCreated,
                        secondaryEntity, selection);
            }break;
            case("set"):{
                newAction = new Set(prdAction.getEntity(), secondaryEntity, selection, prdAction.getValue(), prdAction.getProperty());
            }break;
            case("kill"):{
                newAction = new Kill(prdAction.getEntity(), secondaryEntity, selection);
            }break;
            case("replace"):{
                //primary and secondary supposed to be null
                newAction = new Replace(prdAction.getKill(), prdAction.getCreate(), prdAction.getMode());
            }break;
            case("proximity"):{
                newAction = copyPRDProximity(prdAction);
            }break;
        }
        return newAction;
    }

    public Proximity copyPRDProximity(PRDAction prdAction) {
        int depth;
        String depthExpression = prdAction.getPRDEnvDepth().getOf();
        /*try {
            depth = Integer.parseInt(prdAction.getPRDEnvDepth().getOf());
        }catch (Exception e){
            System.out.println("in copy prd proximity");
            depth = (int) Float.parseFloat(prdAction.getPRDEnvDepth().getOf());
        }*/
        List<Actions> actionsList = new ArrayList<>();
        for(PRDAction currPrdAction: prdAction.getPRDActions().getPRDAction())
            actionsList.add(copyPRDAction(currPrdAction));

        return new Proximity(prdAction.getPRDBetween().getSourceEntity(), prdAction.getPRDBetween().getTargetEntity(),
                depthExpression, actionsList);
    }

    public Actions copyPRDCondition(PRDCondition prdCondition, PRDAction prdAction, boolean elseThenCreated,
                                    String secondaryEntity, Selection selection) {
        String singularity = prdCondition.getSingularity();
        Actions newAction;

        ThenElseImpl thenOp = new ThenElseImpl("then");
        ThenElseImpl elseOp = new ThenElseImpl("else");
        if (!elseThenCreated) {
            //create then action list
            for (PRDAction prdActionInThen : prdAction.getPRDThen().getPRDAction())
                thenOp.addAction(copyPRDAction(prdActionInThen));
            //create else action list
            if (prdAction.getPRDElse() != null) {
                for (PRDAction prdActionInElse : prdAction.getPRDElse().getPRDAction())
                    elseOp.addAction(copyPRDAction(prdActionInElse));
            }
            elseThenCreated = true;
        }

        if (singularity.equals("single")) {
            newAction = new SingleCondition(prdCondition.getEntity(), "", selection, thenOp, elseOp,
                    prdCondition.getProperty(), prdCondition.getOperator(), prdCondition.getValue());
        }
        else { //multiple conditions, need to send next one
            MultipleConditions multipleCondition = new MultipleConditions(prdAction.getEntity(), secondaryEntity, selection,
                    thenOp,elseOp, prdCondition.getLogical());
            for(PRDCondition currCondition: prdCondition.getPRDCondition()) {
                Conditions newCondition = (Conditions) copyPRDCondition(currCondition, prdAction, elseThenCreated,
                        secondaryEntity, selection);
                multipleCondition.addCondition(newCondition);
            }
            newAction = multipleCondition;
        }
        return newAction;
    }

    public Calculation copyCalculation(PRDAction prdAction, String secondaryEntity, Selection selection){
        String arg1, arg2, calcType;
        if(prdAction.getPRDMultiply()!=null){
            arg1 = prdAction.getPRDMultiply().getArg1();
            arg2 = prdAction.getPRDMultiply().getArg2();
            calcType = "multiply";
        }
        else{
            arg1 = prdAction.getPRDDivide().getArg1();
            arg2 = prdAction.getPRDDivide().getArg2();
            calcType = "divide";
        }
        return new Calculation(prdAction.getEntity(), secondaryEntity, selection, prdAction.getResultProp(), calcType, arg1, arg2);
    }

    public void setUpdatedEntitiesPopulation(Map<String, Integer> updatedPopulationList){
        for(Map.Entry<String, EntityDefinitionImpl> entry: entityDefinitionMap.entrySet()){
            entry.getValue().setPopulation(updatedPopulationList.get(entry.getKey()));
        }
    }
    public void copyEntityDefinition(PRDWorld prdWorld){
        for(PRDEntity prdEntity: prdWorld.getPRDEntities().getPRDEntity()){
            EntityDefinitionImpl newEntityDef = new EntityDefinitionImpl(prdEntity.getName());

            for(PRDProperty prdCurrProperty: prdEntity.getPRDProperties().getPRDProperty()){
                PropertyDefinitionImpl newProperty;
                float rangeTop =0, rangeBottom = 0;
                Object value = null;
                PropertyType propertyType = getPropertyTypeFromPRD(prdCurrProperty.getType());
                boolean isRandomVal = false;
                if(prdCurrProperty.getPRDValue().isRandomInitialize())
                    isRandomVal = true;
                else
                    value = getPRDPropertyInitValue(prdCurrProperty.getPRDValue().getInit(), propertyType);
                if(prdCurrProperty.getPRDRange()!=null){
                    rangeTop = (float)prdCurrProperty.getPRDRange().getTo();
                    rangeBottom = (float)prdCurrProperty.getPRDRange().getFrom();
                }
                newProperty = new PropertyDefinitionImpl(prdCurrProperty.getPRDName()
                        , propertyType,rangeTop,rangeBottom,isRandomVal, value);
                newEntityDef.addProperty(newProperty);
            }
            entityDefinitionMap.put(newEntityDef.getName(), newEntityDef);
        }
    }

    public Object getPRDPropertyInitValue(String prdValue, PropertyType propertyType){

        if(propertyType.equals(PropertyType.DECIMAL))
            return Integer.parseInt(prdValue);
        else if(propertyType.equals(PropertyType.FLOAT))
            return Float.parseFloat(prdValue);
        else if(propertyType.equals(PropertyType.BOOLEAN))
            return (prdValue.equalsIgnoreCase("true"));
        else return prdValue;
    }

    public void createEnvManagerMap(List<PropertyDefinitionImpl> envPropertyList, Map<String, PropertyDefinitionImpl> managerMap){
        for(PropertyDefinitionImpl currProperty: envPropertyList)
            managerMap.put(currProperty.getName(), currProperty);
    }

    public PropertyType getPropertyTypeFromPRD(String type){
        PropertyType propertyType = null;
        switch(type){
            case("decimal"):
                propertyType = PropertyType.DECIMAL;
                break;
            case("float"):
                propertyType = PropertyType.FLOAT;
                break;
            case("boolean"):
                propertyType = PropertyType.BOOLEAN;
                break;
            case("string"):
                propertyType = PropertyType.STRING;
                break;
        }
        return propertyType;
    }
    public List<PropertyDefinitionImpl> createEnvProertyList(PRDEnvironment prdEvironment){
        List<PropertyDefinitionImpl> res = new ArrayList<>();
        for(PRDEnvProperty prdEnvProperty: prdEvironment.getPRDEnvProperty()){
            PropertyType propertyType = null;
            float rangeTop = 0;
            float rangeBottom = 0;
            propertyType = getPropertyTypeFromPRD(prdEnvProperty.getType());
            if(prdEnvProperty.getPRDRange()!=null) {
                rangeTop = (float) prdEnvProperty.getPRDRange().getTo();
                rangeBottom = (float) prdEnvProperty.getPRDRange().getFrom();
            }
            //initialize all to random init value to separate after user's input
            PropertyDefinitionImpl newEnvVariable = new PropertyDefinitionImpl(prdEnvProperty.getPRDName(),
                    propertyType, rangeTop, rangeBottom, true, null );
            res.add(newEnvVariable);
        }
        return res;
    }


}
