package application.body.details.actions.replace;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReplaceController {
    @FXML private Label actionNameLabel;
    @FXML private Label killEntityLabel;
    @FXML private Label createEntityLabel;
    @FXML private Label modeLabel;

    public void setReplaceActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setReplaceKillEntityLabel(String primaryEntity){
        killEntityLabel.setText(primaryEntity);
    }

    public void setReplaceCreateEntityLabel(String secondaryEntity){
        createEntityLabel.setText(secondaryEntity);
    }

    public void setReplaceModeLabel(String mode){ modeLabel.setText(mode);}
}
