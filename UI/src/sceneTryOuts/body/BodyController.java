package application.body;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import application.app.AppController;
import application.body.details.DetailsController;
import application.body.newExecution.NewExecutionController;
import application.body.results.ResultsController;

public class BodyController {
    @FXML private DetailsController detailsComponentController;
    @FXML private NewExecutionController newExecutionComponentController;
    @FXML private ResultsController resultsComponentController;
    @FXML private Pane detailsComponent;
    @FXML private Pane newExecutionComponent;
    @FXML private Pane resultsComponent;
    @FXML private Tab detailsTab;
    @FXML private Tab newExecutionTab;
    @FXML private Tab resultsTab;
    @FXML private TabPane programTabs;

    private AppController mainController;
    public TabPane getProgramTabs(){
        return programTabs;
    }

    public Tab getResultsTab(){
        return resultsTab;
    }

    public Tab getNewExecutionTab(){
        return newExecutionTab;
    }
    public void enableTabs(){
        programTabs.setDisable(false);
    }

    public void setMainController(AppController mainController){
        this.mainController = mainController;
        setTabsControllers();
        programTabs.setDisable(true);
    }

    public NewExecutionController getNewExecutionComponentController(){
        return newExecutionComponentController;
    }

    public DetailsController getDetailsComponentController(){ return detailsComponentController;}
    public void setTabsControllers(){
        detailsComponentController.setMainController(this.mainController);
        newExecutionComponentController.setMainController(this.mainController);
        resultsComponentController.setMainController(this.mainController);
    }

   /* @FXML
    public void initialize() {
        if (detailsComponentController != null && newExecutionComponentController != null && resultsComponentController != null) {
            detailsComponentController.setMainController(this.mainController);
            newExecutionComponentController.setMainController(this.mainController);
            resultsComponentController.setMainController(this.mainController);
        }
    }
*/
    @FXML public void detailsTabListener(Event event){

    }

    @FXML public void newExecutionTabListener(Event event){
        newExecutionComponentController.setDisableStartButton(true);
    }

    @FXML public void resultsTabListener(Event event){

    }

    public void setDetailsComponentController(DetailsController detailsComponentController){
        this.detailsComponentController = detailsComponentController;
        detailsComponentController.setMainController(this.mainController);
    }
    public void setNewExecutionComponentController(NewExecutionController newExecutionComponentController){
        this.newExecutionComponentController = newExecutionComponentController;
        newExecutionComponentController.setMainController(this.mainController);
    }
    public void setResultsComponentController(ResultsController resultsComponentController){
        this.resultsComponentController = resultsComponentController;
        resultsComponentController.setMainController(this.mainController);
    }

    public ResultsController getResultsComponentController(){
        return resultsComponentController;
    }
}
