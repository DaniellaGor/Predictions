package application.body.details.actions.proximity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProximityController {
    @FXML private Label actionNameLabel;
    @FXML private Label sourceEntityLabel;
    @FXML private Label targetEntityLabel;
    @FXML private Label depthLabel;
    @FXML private Label actionsAmountLabel;

    public void setProximityActionNameLabel(String actionName){
        actionNameLabel.setText(actionName);
    }

    public void setProximitySourceEntityLabel(String primaryEntity){
        sourceEntityLabel.setText(primaryEntity);
    }

    public void setProximityTargetEntityLabel(String secondaryEntity){
        targetEntityLabel.setText(secondaryEntity);
    }
    public void setProximityDepthLabel(String depth){ depthLabel.setText(depth);}
    public void setProximityActionsAmountLabel(String numOfActions){ actionsAmountLabel.setText(numOfActions);}
}
