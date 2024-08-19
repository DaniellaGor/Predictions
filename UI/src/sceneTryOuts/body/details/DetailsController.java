package application.body.details;

import dto.*;
import dto.actionsDTO.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import application.app.AppController;
import application.body.details.actions.calculation.CalculationController;
import application.body.details.actions.condition.multipleCondition.MultipleConditionController;
import application.body.details.actions.condition.singleCondition.SingleConditionController;
import application.body.details.actions.decrease.DecreaseController;
import application.body.details.actions.increase.IncreaseController;
import application.body.details.actions.kill.KillController;
import application.body.details.actions.proximity.ProximityController;
import application.body.details.actions.replace.ReplaceController;
import application.body.details.actions.set.SetController;
import application.commonResourcePaths.CommonResourcePaths;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DetailsController {
    private AppController mainController;
    @FXML
    private TreeView treeView;
    @FXML
    private VBox detailsVbox;

    public void setTreeView() {
        WorldDTO worldDTO = mainController.createWorldDTO();

        TreeItem<String> rootItem = new TreeItem<>("Simulation Details");
        TreeItem<String> entities = new TreeItem<>("Entities");
        TreeItem<String> rules = new TreeItem<>("Rules");
        TreeItem<String> termination = new TreeItem<>("Termination");
        TreeItem<String> environmentVars = new TreeItem<>("Environment Variables");

        //add entities
        List<TreeItem<String>> tempList = new ArrayList<>();
        for (Map.Entry<String, EntityDefinitionDTO> entry : worldDTO.getEntitesMap().entrySet()) {
            tempList.add(new TreeItem<>(entry.getKey()));
        }

        Collections.reverse(tempList);
        for (TreeItem<String> entity : tempList) {
            for (Map.Entry<String, PropertyDTO> propEntry : worldDTO.getEntitesMap().get(entity.getValue()).getPropertiesMap().entrySet()) {
                entity.getChildren().add(new TreeItem<>(propEntry.getKey()));
            }
        }
        entities.getChildren().addAll(tempList);
        //add rules and actions
        for (RulesDTO rule : worldDTO.getRulesList()) {
            TreeItem<String> singleRule = new TreeItem<>(rule.getName());
            int i=1;
            for (ActionDTO action : rule.getActionsNamesList()) {
                singleRule.getChildren().add(new TreeItem<>(i+". " + action.getName()));
                i++;
            }
            rules.getChildren().add(singleRule);
        }
        //add environment variables
        for (Map.Entry<String, PropertyDTO> envEntry : worldDTO.getEnvironmentManager().getEnvironmentVarMap().entrySet()) {
            environmentVars.getChildren().add(new TreeItem<>(envEntry.getKey()));
        }

        rootItem.getChildren().addAll(entities, rules, termination, environmentVars);
        treeView.setRoot(rootItem);
    }

    public void selectItem() {
        if (detailsVbox == null)
            detailsVbox = new VBox();
        else if (!detailsVbox.getChildren().isEmpty())
            detailsVbox.getChildren().clear();

        WorldDTO worldDTO = mainController.createWorldDTO();
        TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            String selected = item.getValue();
            //check if property of entity
            String parent=null;
            if(item.getParent()!=null)
                parent = item.getParent().getValue();
            detailsVbox.setSpacing(10);
            //check if parent an entity
            if (worldDTO.getEntitesMap().get(parent) != null) {
                detailsVbox.getChildren().add(new Label("Presenting entity '" + parent + "' details:"));
                PropertyDTO currProperty = worldDTO.getEntitesMap().get(parent).getPropertiesMap().get(selected);
                HBox propertyHBox = new HBox();
                propertyHBox.setSpacing(15);
                propertyHBox.getChildren().add(new Label("Property '" + selected + "' details: "));
                VBox tempdetailsVBox = new VBox();
                tempdetailsVBox.setSpacing(15);

                tempdetailsVBox.getChildren().add(new Label("Type: " + currProperty.getType()));
                if (currProperty.getRangeTop() != 0) {
                    tempdetailsVBox.getChildren().add(new Label("Range: from " + currProperty.getRangeBottom() + " to " +
                            currProperty.getRangeTop()));
                }
                if (currProperty.getIsRandomInit())
                    tempdetailsVBox.getChildren().add(new Label("Property value will be initialized randomly."));
                else tempdetailsVBox.getChildren().add(new Label("Property value will not be initialized randomly."));
                propertyHBox.getChildren().add(tempdetailsVBox);
                detailsVbox.getChildren().add(propertyHBox);

            }
            //check if environment
            else if (worldDTO.getEnvironmentManager().getEnvironmentVarMap().get(selected) != null) {
                detailsVbox.getChildren().add(new Label("Presenting environment variables details:"));
                PropertyDTO currEnvVar = worldDTO.getEnvironmentManager().getEnvironmentVarMap().get(selected);
                HBox propertyHBox = new HBox();
                propertyHBox.setSpacing(15);
                propertyHBox.getChildren().add(new Label("Variable '" + selected + "' details: "));
                VBox tempdetailsVBox = new VBox();
                tempdetailsVBox.setSpacing(15);

                tempdetailsVBox.getChildren().add(new Label("Type: " + currEnvVar.getType()));
                if (currEnvVar.getRangeTop() != 0) {
                    tempdetailsVBox.getChildren().add(new Label("Range: from " + currEnvVar.getRangeBottom() + " to " +
                            currEnvVar.getRangeTop()));
                }

                propertyHBox.getChildren().add(tempdetailsVBox);
                detailsVbox.getChildren().add(propertyHBox);
            }
            //check if termination
            else if (selected.equalsIgnoreCase("Termination")) {
                detailsVbox.getChildren().add(new Label("Presenting termination details:"));
                HBox tempHB = new HBox();
                VBox tempVB = new VBox();
                tempHB.getChildren().add(tempVB);
                VBox tempdetailsVBox = new VBox();
                tempdetailsVBox.setSpacing(15);
                TerminationDTO terminationDTO = worldDTO.getTermination();
                if (terminationDTO.getSeconds() <= 0 && terminationDTO.getTicks() <= 0)
                    tempdetailsVBox.getChildren().add(new Label("Program will be terminated by user."));
                else if (terminationDTO.getSeconds() <= 0 && terminationDTO.getTicks() > 0) {
                    if (terminationDTO.getIfUserAllowedToTerminate()) {
                        tempdetailsVBox.getChildren().add(new Label("Program will terminate when ticks amount is" +
                                terminationDTO.getTicks()));
                        tempdetailsVBox.getChildren().add(new Label("or if user chooses to terminate."));
                    } else {
                        tempdetailsVBox.getChildren().add(new Label("Program will terminate when ticks amount is" +
                                terminationDTO.getTicks()));
                    }
                } else if (terminationDTO.getSeconds() > 0 && terminationDTO.getTicks() <= 0) {
                    if (terminationDTO.getIfUserAllowedToTerminate()) {
                        tempdetailsVBox.getChildren().add(new Label("Program will terminate when seconds amount is" +
                                terminationDTO.getSeconds()));
                        tempdetailsVBox.getChildren().add(new Label("or if user chooses to terminate."));
                    } else {
                        tempdetailsVBox.getChildren().add(new Label("Program will terminate when seconds amount is" +
                                terminationDTO.getSeconds()));
                    }
                } else {
                    if (terminationDTO.getIfUserAllowedToTerminate()) {
                        tempdetailsVBox.getChildren().add(new Label("Program will terminate when seconds amount is" +
                                terminationDTO.getSeconds()));
                        tempdetailsVBox.getChildren().add(new Label("or when ticks amount is " + terminationDTO.getTicks()));
                        tempdetailsVBox.getChildren().add(new Label("or if user chooses to terminate."));
                    } else {
                        tempdetailsVBox.getChildren().add(new Label("Program will terminate when seconds amount is" +
                                terminationDTO.getSeconds()));
                        tempdetailsVBox.getChildren().add(new Label("or when ticks amount is " + terminationDTO.getTicks()));
                    }
                }
                tempHB.getChildren().add(tempdetailsVBox);
                detailsVbox.getChildren().add(tempHB);
            }

            //else actions
            else if(checkIfAction(selected)){
                RulesDTO currRule = null;
                ActionDTO currAction = null;
                for (RulesDTO ruleDTO : worldDTO.getRulesList()) {
                    if (ruleDTO.getName().equalsIgnoreCase(parent)) {
                        currRule = ruleDTO;
                        int i = 1;
                        for (ActionDTO actionDTO : currRule.getActionsNamesList()) {
                            if (selected.toLowerCase().endsWith(actionDTO.getName().toLowerCase()) && selected.startsWith(Integer.toString(i))) {
                                currAction = actionDTO;
                                break;
                            }
                            i++;
                        }
                        if (currAction != null)
                            break;
                    }
                }

                presentActionScreen(currAction);
            }
        }
    }

    public boolean checkIfAction(String expression){
        return (expression.toLowerCase().endsWith("increase") || expression.toLowerCase().endsWith("decrease") ||
                expression.toLowerCase().endsWith("calculation") || expression.toLowerCase().endsWith("condition")
        || expression.toLowerCase().endsWith("replace") || expression.toLowerCase().endsWith("proximity")
        || expression.toLowerCase().endsWith("set") || expression.toLowerCase().endsWith("kill"));
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void presentActionScreen(ActionDTO actionDTO) {
        detailsVbox.getChildren().clear();
        switch (actionDTO.getName()) {
            case ("Increase"): {
                createIncreaseController(actionDTO);
            }
            break;
            case ("Decrease"): {
                createDecreaseController(actionDTO);
            }
            break;
            case ("Calculation"): {
                createCalculationController(actionDTO);
            }
            break;
            case ("Condition"): {
                ConditionDTO conditionDTO = (ConditionDTO) actionDTO;
                if(conditionDTO.getSingularity().equalsIgnoreCase("single"))
                    createSingleConditionController(actionDTO);
                else createMultipleConditionController(actionDTO);

            }
            break;
            case ("Set"): {
                createSetController(actionDTO);
            }
            break;
            case ("Replace"): {
                createReplaceController(actionDTO);
            }
            break;
            case ("Proximity"): {
                createProximityController(actionDTO);
            }
            break;
            case ("Kill"): {
                createKillController(actionDTO);
            }
            break;
        }
    }

    public void createSetController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.SET_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            SetController actionController = loader.getController();
            SetDTO setDTO = (SetDTO) actionDTO;

            actionController.setSetActionNameLabel(setDTO.getName());
            actionController.setSetPrimaryEntityLabel(setDTO.getPrimaryEntity());

            if (setDTO.getSecondaryEntity().equals(""))
                actionController.setSetSecondaryEntityLabel("none");
            else actionController.setSetSecondaryEntityLabel(setDTO.getSecondaryEntity());
            actionController.setSetPropertyLabel(setDTO.getProperty());
            actionController.setSetValueLabel(setDTO.getValue());

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createIncreaseController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.INCREASE_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            IncreaseController actionController = loader.getController();
            IncreaseDTO increaseDTO = (IncreaseDTO) actionDTO;

            actionController.setIncreaseActionNameLabel(increaseDTO.getName());
            actionController.setIncreasePrimaryEntityLabel(increaseDTO.getPrimaryEntity());
            if (increaseDTO.getSecondaryEntity().equals(""))
                actionController.setIncreaseSecondaryEntityLabel("none");
            else actionController.setIncreaseSecondaryEntityLabel(increaseDTO.getSecondaryEntity());
            actionController.setIncreasePropertyLabel(increaseDTO.getProperty());
            actionController.setIncreaseByValueLabel(increaseDTO.getByExpression());

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createDecreaseController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.DECREASE_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            DecreaseController actionController = loader.getController();
            DecreaseDTO decreaseDTO = (DecreaseDTO) actionDTO;

            actionController.setDecreaseActionNameLabel(decreaseDTO.getName());
            actionController.setDecreasePrimaryEntityLabel(decreaseDTO.getPrimaryEntity());
            if (decreaseDTO.getSecondaryEntity().equals(""))
                actionController.setDecreaseSecondaryEntityLabel("none");
            else actionController.setDecreaseSecondaryEntityLabel(decreaseDTO.getSecondaryEntity());
            actionController.setDecreasePropertyLabel(decreaseDTO.getProperty());
            actionController.setDecreaseByValueLabel(decreaseDTO.getByExpression());

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createCalculationController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.CALCULATION_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            CalculationController actionController = loader.getController();
            CalculationDTO calculationDTO = (CalculationDTO) actionDTO;

            actionController.setCalculationActionNameLabel(calculationDTO.getName());
            actionController.setCalculationPrimaryEntityLabel(calculationDTO.getPrimaryEntity());
            if (calculationDTO.getSecondaryEntity().equals(""))
                actionController.setCalculationSecondaryEntityLabel("none");
            else actionController.setCalculationSecondaryEntityLabel(calculationDTO.getSecondaryEntity());
            actionController.setCalculationResultPropLabel(calculationDTO.getResultProp());
            actionController.setCalculationModeLabel(calculationDTO.getCalculationType());
            actionController.setCalculationArg1Label(calculationDTO.getArg1());
            actionController.setCalculationArg2Label(calculationDTO.getArg2());

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createKillController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.KILL_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            KillController actionController = loader.getController();
            KillDTO killDTO = (KillDTO) actionDTO;

            actionController.setKillActionNameLabel(killDTO.getName());
            actionController.setKillPrimaryEntityLabel(killDTO.getPrimaryEntity());
            if (killDTO.getSecondaryEntity().equals(""))
                actionController.setKillSecondaryEntityLabel("none");
            else actionController.setKillSecondaryEntityLabel(killDTO.getSecondaryEntity());

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createProximityController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.PROXIMITY_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            ProximityController actionController = loader.getController();
            ProximityDTO proximityDTO = (ProximityDTO) actionDTO;

            actionController.setProximityActionNameLabel(proximityDTO.getName());
            actionController.setProximitySourceEntityLabel(proximityDTO.getSource());
            actionController.setProximityTargetEntityLabel(proximityDTO.getTarget());
            actionController.setProximityDepthLabel(proximityDTO.getDepth());
            actionController.setProximityActionsAmountLabel(Integer.toString(proximityDTO.getNumOfActions()));

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createReplaceController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.REPLACE_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            ReplaceController actionController = loader.getController();
            ReplaceDTO replaceDTO = (ReplaceDTO) actionDTO;

            actionController.setReplaceActionNameLabel(replaceDTO.getName());
            actionController.setReplaceKillEntityLabel(replaceDTO.getKill());
            actionController.setReplaceCreateEntityLabel(replaceDTO.getCreate());
            actionController.setReplaceModeLabel(replaceDTO.getMode());


            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }

    public void createSingleConditionController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.SINGLE_CONDITION_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            SingleConditionController actionController = loader.getController();
            ConditionDTO conditionDTO = (ConditionDTO) actionDTO;

            actionController.setSingleConditionActionNameLabel("Single condition");
            actionController.setSingleConditionPrimaryEntityLabel(conditionDTO.getPrimaryEntity());
            actionController.setSingleConditionSecondaryEntityLabel(conditionDTO.getSecondaryEntity());
            actionController.setSingleConditionPropertyLabel(conditionDTO.getProperty());
            actionController.setSingleConditionOperatorLabel(conditionDTO.getOperator());
            actionController.setSingleConditionValueLabel(conditionDTO.getValue());
            actionController.setSingleConditionNumOfThenLabel(Integer.toString(conditionDTO.getNumOfThen()));
            actionController.setSingleConditionNumOfElseLabel(Integer.toString(conditionDTO.getNumOfElse()));

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }
    public void createMultipleConditionController(ActionDTO actionDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(CommonResourcePaths.MULTIPLE_CONDITION_FXML_RESOURCE);
            loader.setLocation(url);
            Node title = loader.load();
            MultipleConditionController actionController = loader.getController();
            ConditionDTO conditionDTO = (ConditionDTO) actionDTO;

            actionController.setMultipleConditionActionNameLabel("Multiple condition");
            actionController.setMultipleConditionPrimaryEntityLabel(conditionDTO.getPrimaryEntity());
            actionController.setMultipleConditionSecondaryEntityLabel(conditionDTO.getSecondaryEntity());
            actionController.setMultipleConditionLogicLabel(conditionDTO.getLogic());
            actionController.setMultipleConditionNumOfConditionsLabel(Integer.toString(conditionDTO.getNumOfCondInLogic()));
            actionController.setMultipleConditionNumOfThenLabel(Integer.toString(conditionDTO.getNumOfThen()));
            actionController.setMultipleConditionNumOfElseLabel(Integer.toString(conditionDTO.getNumOfElse()));

            detailsVbox.getChildren().add(title);
        } catch (IOException e) {
            System.out.println("in details controller");
            e.printStackTrace();
        }
    }


}

