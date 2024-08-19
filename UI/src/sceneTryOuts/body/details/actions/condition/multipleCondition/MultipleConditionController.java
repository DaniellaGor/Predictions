package application.body.details.actions.condition.multipleCondition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MultipleConditionController {
    @FXML
    private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label numOfThenLabel;
    @FXML private Label numOfElseLabel;
    @FXML private Label logicLabel;
    @FXML private Label numOfConditionsLabel;

    public void setMultipleConditionActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setMultipleConditionPrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setMultipleConditionSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setMultipleConditionLogicLabel(String logic){
        logicLabel.setText(logic);
    }

    public void setMultipleConditionNumOfConditionsLabel(String numOfConditions){
        numOfConditionsLabel.setText(numOfConditions);
    }

    public void setMultipleConditionNumOfThenLabel(String numOfThen){
        numOfThenLabel.setText(numOfThen);
    }

    public void setMultipleConditionNumOfElseLabel(String numOfElse){
        numOfElseLabel.setText(numOfElse);
    }
}
