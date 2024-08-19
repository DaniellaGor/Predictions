package application.body.details.actions.calculation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CalculationController {
    @FXML
    private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label resultPropLabel;
    @FXML private Label modeLabel;
    @FXML private Label arg1Label;
    @FXML private Label arg2Label;

    public void setCalculationActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setCalculationPrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setCalculationSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setCalculationResultPropLabel(String resultProp){
        resultPropLabel.setText(resultProp);
    }

    public void setCalculationModeLabel(String mode){
        modeLabel.setText(mode);
    }

    public void setCalculationArg1Label(String arg1){
        arg1Label.setText(arg1);
    }

    public void setCalculationArg2Label(String arg2){
        arg2Label.setText(arg2);
    }
}
