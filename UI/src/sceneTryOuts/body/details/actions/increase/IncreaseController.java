package application.body.details.actions.increase;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IncreaseController {
    @FXML
    private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label propertyLabel;
    @FXML private Label byValueLabel;

    public void setIncreaseActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setIncreasePrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setIncreaseSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setIncreasePropertyLabel(String property){
        propertyLabel.setText(property);
    }

    public void setIncreaseByValueLabel(String value){
        byValueLabel.setText(value);
    }
}
