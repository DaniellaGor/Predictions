package application.body.newExecution;

import dto.ActiveEnvironmentDTO;
import dto.EntityDefinitionDTO;
import dto.PropertyDTO;
import dto.WorldDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import application.app.AppController;

import java.util.*;

public class NewExecutionController {
    private AppController mainController;
    @FXML private VBox entitiesVBox;
    @FXML private Button startButton;
    @FXML private Button clearButton;
    @FXML private VBox envVarVBox;
    @FXML private Button setSimulationVarsButton;
    @FXML private VBox errorVBox;
    public void setMainController(AppController mainController){
        this.mainController = mainController;
    }

    @FXML public void setSimulationVarsButtonListener(ActionEvent event){
        envVarVBox.setSpacing(10);
        entitiesVBox.setSpacing(10);
        setSimulationVarsButton.setDisable(true);
        setEntitiesPopulation();
        setEnvironmentVars();
        startButton.setDisable(false);
    }

    public void setEnvironmentVars(){
        WorldDTO worldDTO = mainController.createWorldDTO();
        for(Map.Entry<String,PropertyDTO> entry: worldDTO.getEnvironmentManager().getEnvironmentVarMap().entrySet()){
            VBox internVBox = new VBox();
            internVBox.getChildren().add(new Label(entry.getKey()));
            internVBox.getChildren().add(new Label("Please enter value of type " + entry.getValue().getType() + ":"));
            if(entry.getValue().getRangeTop() != 0){
                internVBox.getChildren().add(new Label("Please enter a number between " + entry.getValue().getRangeBottom()
                        + " and + " + entry.getValue().getRangeTop() + ":"));
            }
            TextField textField = new TextField();
            textField.setPromptText("enter environment variable value..");
            internVBox.getChildren().add(textField);
            internVBox.getChildren().add(new Label());
            internVBox.getChildren().add(new Label());
            envVarVBox.getChildren().add(internVBox);
        }
    }
    public void setDisableStartButton(boolean press){
        startButton.setDisable(press);
    }

    public void setRerunExecution(Map<String, Integer> population, ActiveEnvironmentDTO reRunActiveEnv) {
        entitiesVBox.getChildren().clear();
        envVarVBox.getChildren().clear();

        //set population
        for (Map.Entry<String, Integer> entry : population.entrySet()) {
            VBox internVBox = new VBox();
            internVBox.getChildren().add(new Label(entry.getKey()));
            TextField populationText = new TextField(Integer.toString(entry.getValue()));
            populationText.setDisable(true);
            internVBox.getChildren().add(populationText);
            entitiesVBox.getChildren().add(internVBox);
        }

        //set environment

        for (Map.Entry<String, PropertyDTO> entry : reRunActiveEnv.getEnvironmentVariables().entrySet()) {
            VBox internVBox = new VBox();
            internVBox.getChildren().add(new Label(entry.getKey()));
            internVBox.getChildren().add(new Label("Value type: " + entry.getValue().getType()));
            if(entry.getValue().getRangeTop() != 0){
                internVBox.getChildren().add(new Label("Range is between " + entry.getValue().getRangeBottom()
                        + " and + " + entry.getValue().getRangeTop() + ":"));
            }
            TextField textField = new TextField();
            if(entry.getValue().getIsRandomInit())
                textField.setPromptText("Value will be initialized randomly");
            else{
                if(entry.getValue().getType().equalsIgnoreCase("decimal"))
                    textField = new TextField(Integer.toString((int)entry.getValue().getValue()));
                else if(entry.getValue().getType().equalsIgnoreCase("float"))
                    textField = new TextField(Float.toString((float)entry.getValue().getValue()));
                else if(entry.getValue().getType().equalsIgnoreCase("boolean"))
                    textField = new TextField(Boolean.toString((boolean) entry.getValue().getValue()));
                else textField = new TextField((String)entry.getValue().getValue());
            }
            textField.setDisable(true);
            internVBox.getChildren().add(textField);
            internVBox.getChildren().add(new Label());
            internVBox.getChildren().add(new Label());
            envVarVBox.getChildren().add(internVBox);

        }

    }
    public void setEntitiesPopulation(){
        WorldDTO worldDTO = mainController.createWorldDTO();
        List<EntityDefinitionDTO> tempDTOEntitiesList = new ArrayList<>(worldDTO.getEntitesMap().values());
        Collections.reverse(tempDTOEntitiesList);
        for(EntityDefinitionDTO entityDefinitionDTO: tempDTOEntitiesList){
            VBox internVBox = new VBox();
            internVBox.getChildren().add(new Label(entityDefinitionDTO.getName()));
            TextField textField = new TextField();
            textField.setPromptText("enter population amount..");
            internVBox.getChildren().add(textField);

            entitiesVBox.getChildren().add(internVBox);
        }
    }

    //check input function? listener
    public VBox getEntitiesVBox(){
        return entitiesVBox;
    }


    @FXML void startButtonListener(ActionEvent event){
        WorldDTO worldDTO = mainController.createWorldDTO();
        Map<String, PropertyDTO> envVarsMap = new HashMap<>();
        Map<String, Integer> updatedPopulationMap = new HashMap<>();
        boolean isEntitiesValid, isEnvVarsValid;

        //check if reRun has been pressed
        if(!mainController.getBodyComponentController().getResultsComponentController().getReRunButtonHasBeenPressed()) {
            //check population and env variables
            isEntitiesValid = checkPopulationInput(updatedPopulationMap, mainController.getMaxPopulation());
            isEnvVarsValid = checkEnvironmentVariables(worldDTO, envVarsMap);

            if (isEntitiesValid && isEnvVarsValid) {
                if (errorVBox.getChildren().size() > 0)
                    errorVBox.getChildren().clear();

                //send updated entities to engine
                mainController.getEngineWorld().setUpdatedEntitiesPopulation(updatedPopulationMap);

                //create active environment
                ActiveEnvironmentDTO activeEnvironmentDTO = new ActiveEnvironmentDTO(envVarsMap);

                //start a new simulation + update reRunActiveEnv
                mainController.getBodyComponentController().getResultsComponentController().startButtonHasBeenPressed(activeEnvironmentDTO, updatedPopulationMap);
            } else {
                if (errorVBox.getChildren().size() == 0)
                    errorVBox.getChildren().add(new Label("Wrong input. Please look for error notes."));
            }
        }
        else{
            mainController.getBodyComponentController().getResultsComponentController().setReRunButtonHasBeenPressed(false);
            //send updated entities to engine
            updatedPopulationMap = mainController.getBodyComponentController().getResultsComponentController().getPopulationReRun();
            ActiveEnvironmentDTO activeEnvironmentDTO = mainController.getBodyComponentController()
                    .getResultsComponentController().getReRunEnv();

            mainController.getEngineWorld().setUpdatedEntitiesPopulation(updatedPopulationMap);
            mainController.getBodyComponentController().getResultsComponentController().startButtonHasBeenPressed(activeEnvironmentDTO, updatedPopulationMap);
        }



    }

    public Button getSetSimulationButton(){
        return setSimulationVarsButton;
    }

    boolean checkPopulationInput(Map<String, Integer> updatedPopulationList, int populationAllowed){
        boolean isEntitiesValid = true;
        VBox currEntity;

        for(int i=0; i<entitiesVBox.getChildren().size(); i++){
            currEntity = (VBox)entitiesVBox.getChildren().get(i);
            Label entityLabelName = (Label)currEntity.getChildren().get(0);
            TextField populationTextField = (TextField) currEntity.getChildren().get(1);
            Label newError;
            //Label populationLabel = (Label)currEntity.getChildren().get(1);
            if(populationTextField.getText().equals(""))
                updatedPopulationList.put(entityLabelName.getText(), 0);
            else {
                try {
                    if (currEntity.getChildren().size() > 2) {
                        currEntity.getChildren().remove(2);
                    }
                    int population = Integer.parseInt(populationTextField.getText());
                    if(populationAllowed - population < 0) {
                        newError = new Label("Population valid range: " + populationAllowed);
                        currEntity.getChildren().add(newError);
                        currEntity.getChildren().add(new Label());
                        isEntitiesValid = false;
                    }
                    populationAllowed -= population;
                    updatedPopulationList.put(entityLabelName.getText(), population);
                } catch (Exception e) {
                    System.out.println("in execution controller");
                    //check how this looks
                    newError = new Label("Input must be of decimal type.");
                    currEntity.getChildren().add(newError);
                    currEntity.getChildren().add(new Label());
                    isEntitiesValid = false;
                }
            }

            /*if(i == entitiesVBox.getChildren().size() && !isEntitiesValid)
                i=0;*/
        }
        return isEntitiesValid;
    }

    boolean checkEnvironmentVariables(WorldDTO worldDTO, Map<String, PropertyDTO> envVarsMap){
        boolean isEnvVarsValid = true;
        boolean isRandomInit = false;
        //int j =0;
        for(int j = 0; j<worldDTO.getEnvironmentManager().getEnvironmentVarMap().size(); j++){
            isRandomInit = false;
        //for(Map.Entry<String, PropertyDTO> entry: worldDTO.getEnvironmentManager().getEnvironmentVarMap().entrySet()){
            VBox currEnvVar = (VBox) envVarVBox.getChildren().get(j);
            Label currEnvVarName = (Label)currEnvVar.getChildren().get(0);
            currEnvVar.getChildren().set(currEnvVar.getChildren().size()-2, new Label()); //  clear past errors
            Object value;

            PropertyDTO currProperty = worldDTO.getEnvironmentManager().getEnvironmentVarMap().get(currEnvVarName.getText());
            TextField valueTextField = (TextField) currEnvVar.getChildren().get(currEnvVar.getChildren().size() -3);
            String valueString = valueTextField.getText();
            if(valueString.equals("")) { // user did not insert value, random value
                value = randomValue(currProperty.getRangeTop(), currProperty.getRangeBottom(), currProperty.getType());
                isRandomInit = true;
            }
            else { //get value
                value = getValueFromString(currProperty, valueString, currEnvVar);
            }
            //check if there's an error message
            Label errorLabel = (Label)currEnvVar.getChildren().get(currEnvVar.getChildren().size()-2);
            if(!errorLabel.getText().equals("")) // there's an error message
                isEnvVarsValid = false;
            else envVarsMap.put(currProperty.getName(),new PropertyDTO(currProperty.getName(),
                    currProperty.getType(), currProperty.getRangeTop(),
                    currProperty.getRangeBottom(), isRandomInit, value));
            //j++;
        }

        return isEnvVarsValid;
    }

    public Object randomValue(float rangeTop, float rangeBottom, String type) {
        Object randomObject = null;
        Random random = new Random();
        if (type.equalsIgnoreCase("decimal")) {
            int randomInt = random.nextInt((int) rangeTop - (int) rangeBottom) + (int) rangeBottom;
            randomObject = randomInt;
        } else if (type.equalsIgnoreCase("float")) {
            float randomFloat = random.nextFloat() * (rangeTop - rangeBottom) + rangeBottom;
            randomObject = randomFloat;
        } else if (type.equalsIgnoreCase("boolean")) {
            boolean randomBoolean = random.nextBoolean();
            randomObject = randomBoolean;
        } else if (type.equalsIgnoreCase("string")) {
            int randomLength = random.nextInt(51) + 1;
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ?!_-,.()";
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < randomLength; i++) {
                char c = characters.charAt(random.nextInt(characters.length()));
                randomString.append(c);
            }
            randomObject = randomString;
        }

        return randomObject;
    }

    public Object getValueFromString(PropertyDTO property, String val, VBox currEnvVar) {
        Object res = null;
        boolean isValid = false;
        switch (property.getType().toLowerCase()){
            case("decimal"):{
                //while(!isValid){
                    try {
                        res = Integer.parseInt(val);
                        if(property.getRangeTop()!=0) {
                            int topRange = (int)property.getRangeTop();
                            int bottomRange = (int)property.getRangeBottom();
                            if (!((int)res > topRange) && !((int)res < bottomRange))
                                isValid = true;
                            else {
                                currEnvVar.getChildren().set(currEnvVar.getChildren().size()-2,
                                        new Label("Value is not in the property range!"));
                            }
                        }
                    }catch (Exception e) {
                        System.out.println("in execution controller");
                        currEnvVar.getChildren().set(currEnvVar.getChildren().size()-2,
                                new Label("Value is not decimal."));
                    }
               // }
            }break;
            case("float"):{
                //while(!isValid) {
                    try {
                        res = Float.parseFloat(val);
                        if (property.getRangeTop() != 0) {
                            if (!((float) res > property.getRangeTop()) && !((float) res < property.getRangeBottom()))
                                isValid = true;
                            else {
                                currEnvVar.getChildren().set(currEnvVar.getChildren().size()-2,
                                        new Label("Value is not in the property range!"));
                            }
                        }
                    }catch (Exception e) {
                        System.out.println("in execution controller");
                        currEnvVar.getChildren().set(currEnvVar.getChildren().size() - 2,
                                new Label("Value is not of type float."));
                    }
                //}
            }break;
            case("boolean"):{
                //while(!isValid) {
                    if (checkIfStringMeansBooleanVal(val)) {
                        res = Boolean.parseBoolean(val);
                        isValid = true;
                    }
                    else {
                        currEnvVar.getChildren().set(currEnvVar.getChildren().size()-2,
                                new Label("Value does not match 'true' or 'false'."));
                    }
             //   }
            }break;
            default:
                res = val; //type = string
        }
        return res;
    }

    public boolean checkIfStringMeansBooleanVal(String expression){
        return(expression.equalsIgnoreCase("true") || (expression.equalsIgnoreCase("false")));
    }

    @FXML void clearButtonListener(ActionEvent event){
        envVarVBox.getChildren().clear();
        entitiesVBox.getChildren().clear();
        setSimulationVarsButton.setDisable(false);
    }

    public void clearNewExecutionData(){
        clearButton.fire();
    }
}
