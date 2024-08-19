package application.body.details.actions.set;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SetController {
    @FXML private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;
    @FXML private Label propertyLabel;
    @FXML private Label valueLabel;

    public void setSetActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setSetPrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setSetSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }

    public void setSetPropertyLabel(String property){
        propertyLabel.setText(property);
    }

    public void setSetValueLabel(String value){
        valueLabel.setText(value);
    }

}
