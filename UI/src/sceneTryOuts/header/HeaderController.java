package application.header;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import application.app.AppController;
import application.app.tasks.ThreadInfoTask;
import world.WorldImpl;

import java.io.File;

public class HeaderController {

    private AppController mainController;
    @FXML private Button loadFileButton;
    @FXML private VBox queueManagementVBox;
    @FXML private Label threadPoolQueueLabel;
    @FXML private Label threadPoolActiveLabel;
    @FXML private Label threadPoolCompletedLabel;
    @FXML private Label filePath;
    @FXML private HBox fileValidationHBox;

    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private SimpleLongProperty threadPoolQueueProperty;
    private SimpleLongProperty threadPoolActiveProperty;
    private SimpleLongProperty threadPoolCompletedProperty;

    private boolean stopThreadPoolInfoTask;
    private boolean firstUseOfFile;


    public HeaderController(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        threadPoolActiveProperty = new SimpleLongProperty();
        threadPoolCompletedProperty = new SimpleLongProperty();
        threadPoolQueueProperty = new SimpleLongProperty();
        stopThreadPoolInfoTask = false;
        firstUseOfFile = false;
    }

    @FXML public void initialize(){
       /* currentTickResult.textProperty().bind(currentTickResultProperty.asString());
        currentTimeInSecondsResult.textProperty().bind(currentTimeInSecondsResultProperty.asString());*/
        threadPoolQueueLabel.textProperty().bind(Bindings.format("%,d", threadPoolQueueProperty));
        threadPoolActiveLabel.textProperty().bind(Bindings.format("%,d", threadPoolActiveProperty));
        threadPoolCompletedLabel.textProperty().bind(Bindings.format("%,d", threadPoolCompletedProperty));

        threadPoolQueueProperty.set(0);
        threadPoolActiveProperty.set(0);
        threadPoolCompletedProperty.set(0);
    }

    public VBox getQueueManagementVBox(){ return queueManagementVBox;}

    public void setQueueManager(){

    }

    public boolean getFirstUseOfFile(){
        return firstUseOfFile;
    }

    public void setFirstUseOfFile(boolean set){
        firstUseOfFile = set;
    }

    public void runThreadPoolInfoTask(){
        new Thread(new ThreadInfoTask(mainController.getSimulationManager(), threadPoolQueueProperty, threadPoolActiveProperty, threadPoolCompletedProperty, this)).start();
    }

    public boolean getStopThreadPoolInfoTask(){
        return stopThreadPoolInfoTask;
    }

    @FXML
    void loadFileButtonListener(ActionEvent event) {
        //threadPool updating task was running up until now
        if(!stopThreadPoolInfoTask){
            stopThreadPoolInfoTask = true;
            threadPoolCompletedProperty.set(0);
            threadPoolQueueProperty.set(0);
            threadPoolActiveProperty.set(0);
            //firstUseOfFile = true;
        }
        //if it's not the first file
        if(!filePath.getText().equals("")) {
            mainController.getBodyComponentController().getResultsComponentController().clearAllData();
            //filePath.setText("");
            fileValidationHBox.getChildren().clear();
        }

        mainController.getBodyComponentController().getResultsComponentController().disableResultsButtons();

        firstUseOfFile = true;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(mainController.getPrimaryStage());
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        filePath.setText(absolutePath);

        try{
            WorldImpl world = new WorldImpl(filePath.getText());
            mainController.setEngineWorld(world);
            fileValidationHBox.getChildren().add(new Label("File uploaded successfully!"));
            isFileSelected.set(true);
            //set thread pool once
            mainController.getSimulationManager().setThreadPool(world.createWorldDTO().getNumOfThreads());

            mainController.getBodyComponentController().enableTabs();
            mainController.getBodyComponentController().getDetailsComponentController().setTreeView();
        }catch (Exception e){
            System.out.println("in header controller");
            System.out.println(e.getMessage());
            fileValidationHBox.getChildren().add(new Label(e.getMessage()));
        }

    }

    public String getFilePath(){
        return filePath.getText();
    }

    @FXML
    void queueManagementButtonListener(ActionEvent event){

    }

    public void setMainController(AppController mainController){
        this.mainController = mainController;
    }

}
