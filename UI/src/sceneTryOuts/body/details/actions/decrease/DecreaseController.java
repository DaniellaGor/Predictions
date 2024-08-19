package application.body.details.actions.decrease;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DecreaseController {
    @FXML
    private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label propertyLabel;
    @FXML private Label byValueLabel;

    public void setDecreaseActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setDecreasePrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setDecreaseSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setDecreasePropertyLabel(String property){
        propertyLabel.setText(property);
    }

    public void setDecreaseByValueLabel(String value){
        byValueLabel.setText(value);
    }
}
