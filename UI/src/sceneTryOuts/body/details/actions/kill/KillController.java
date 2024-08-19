package application.body.details.actions.kill;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KillController {
    @FXML
    private Label actionNameLabel;
    @FXML private Label primaryEntityLabel;
    @FXML private Label secondaryEntityLabel;

    public void setKillActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setKillPrimaryEntityLabel(String primaryEntity){
        primaryEntityLabel.setText(primaryEntity);
    }

    public void setKillSecondaryEntityLabel(String secondaryEntity){
        secondaryEntityLabel.setText(secondaryEntity);
    }
}
