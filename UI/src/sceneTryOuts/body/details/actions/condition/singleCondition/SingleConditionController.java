package application.body.details.actions.condition.singleCondition;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleConditionController {
    @FXML
    private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label numOfThenLabel;
    @FXML private Label numOfElseLabel;
    @FXML private Label propertyLabel;
    @FXML private Label valueLabel;
    @FXML private Label operatorLabel;

    public void setSingleConditionActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setSingleConditionPrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setSingleConditionSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setSingleConditionPropertyLabel(String property){
        propertyLabel.setText(property);
    }

    public void setSingleConditionOperatorLabel(String operator){
        operatorLabel.setText(operator);
    }

    public void setSingleConditionValueLabel(String value){
        valueLabel.setText(value);
    }
    public void setSingleConditionNumOfThenLabel(String numOfThen){
        numOfThenLabel.setText(numOfThen);
    }

    public void setSingleConditionNumOfElseLabel(String numOfElse){
        numOfElseLabel.setText(numOfElse);
    }
}
