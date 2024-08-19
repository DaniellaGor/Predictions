package application.body.results;

import dto.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import application.app.AppController;
import application.app.tasks.GettingInfoTask;
import application.app.tasks.PauseStatusTask;
import application.app.tasks.ResumeStatusTask;
import application.app.tasks.StopStatusTask;
import application.app.tasks.runSimulation.RunSimulation;
import application.body.results.entityPopulationForTableView.EntityPopulation;
import simulation.SimulationImpl;
import simulation.status.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ResultsController {

    private AppController mainController;
    @FXML private Label currentTimeInSecondsResult;
    @FXML private Label currentTickResult;
    @FXML private Button stopButton;
    @FXML private Button resumeButton;
    @FXML private Button pauseButton;
    @FXML private VBox tableViewVBox;
    @FXML private VBox simulationIds;
    @FXML private VBox detailsVBoxResults;
    @FXML private VBox statusVBox;
    @FXML private Button showResultsButton;
    @FXML private HBox menuButtonsHBox;
    @FXML private HBox lineChartHBox;
    @FXML private HBox populationHistogramHBox;
    @FXML private HBox propertyConsistencyHBox;
    @FXML private HBox averagePropertyValueHBox;
    @FXML private VBox chosenEntityAndPropVBox;
    @FXML private Button reRunButton;
    private MenuButton entitiesMenuButton;
    private MenuButton propertiesMenuButton;
    private String chosenEntityInHistogram;
    private String chosenPropertyInHistogram;
    private Button currIdPressed;
    private HBox currSimulationHBox;
    private SimulationImpl currentSimulation;
    private boolean reRunButtonHasBeenPressed;
    private IntegerProperty currentTickResultProperty;
    private IntegerProperty currentTimeInSecondsResultProperty;
    private boolean firstSimulation;
    public ResultsController(){
        simulationIds = new VBox();
        simulationIds.setSpacing(5.00);
        detailsVBoxResults = new VBox();
        currentTickResultProperty = new SimpleIntegerProperty();
        currentTimeInSecondsResultProperty = new SimpleIntegerProperty();
        tableViewVBox = new VBox();
        reRunButtonHasBeenPressed = false;

        /*currentTickResult.textProperty().bind(currentTickResultProperty.asString());
        currentTimeInSecondsResult.textProperty().bind(currentTimeInSecondsResultProperty.asString());*/
    }
    public void setCurrentSimulation(SimulationImpl simulation){
        currentSimulation = simulation;
    }

    public boolean getReRunButtonHasBeenPressed(){
        return reRunButtonHasBeenPressed;
    }

    public Map<String, Integer> getPopulationReRun(){
        return currentSimulation.getRerunPopulation();
    }

    public ActiveEnvironmentDTO getReRunEnv(){
        return currentSimulation.getRerunActiveEnv();
    }

    @FXML public void reRunButtonListener(ActionEvent event){
        reRunButtonHasBeenPressed = true;
        Tab newExecutionTab = mainController.getBodyComponentController().getNewExecutionTab();
        mainController.getBodyComponentController().getProgramTabs().getSelectionModel().select(newExecutionTab);
        mainController.getBodyComponentController().getNewExecutionComponentController()
                .setRerunExecution(currentSimulation.getRerunPopulation(), currentSimulation.getRerunActiveEnv());
        mainController.getBodyComponentController().getNewExecutionComponentController().setDisableStartButton(false);

    }
    @FXML public void stopButtonListener(ActionEvent event){
        //send status update to program
        //currentSimulation.setStatus(Status.STOP);
        showResultsButton.setDisable(false);
        reRunButton.setDisable(false);
        stopButton.setDisable(true);
        resumeButton.setDisable(true);
        pauseButton.setDisable(true);
        new Thread(new StopStatusTask(currentSimulation)).start();
        statusVBox.getChildren().clear();
        statusVBox.setSpacing(10);
        statusVBox.getChildren().add(new Label("Simulation status: simulation ended."));
        statusVBox.getChildren().add(new Label("Notes: simulation ended by user."));
        /*HBox currHBox = (HBox)simulationIds.getChildren().get(currentSimulation.getId()+1);
        currHBox.getChildren().set(1, new Label("ended"));*/
        currSimulationHBox.getChildren().set(1, new Label("ended"));
    }
    @FXML public void pauseButtonListener(ActionEvent event){
        //send status update to program
        //currentSimulation.setStatus(Status.PAUSE);
        resumeButton.setDisable(false);
        pauseButton.setDisable(true);
        stopButton.setDisable(false);

        new Thread(new PauseStatusTask(currentSimulation)).start();
        statusVBox.getChildren().clear();
        statusVBox.setSpacing(10);
        statusVBox.getChildren().add(new Label("Simulation status: simulation is paused."));
        /*HBox currHBox = (HBox)simulationIds.getChildren().get(currentSimulation.getId()+1);
        currHBox.getChildren().set(1, new Label("paused"));*/
        currSimulationHBox.getChildren().set(1, new Label("paused"));

    }
    @FXML public void resumeButtonListener(ActionEvent event){
        //SimulationDTO currSimulationDTO = currentSimulation.createSimulationDTO();
        //currentSimulation.setStatus(Status.ON_GOING);
        //send status update to program
        pauseButton.setDisable(false);
        resumeButton.setDisable(true);
        new Thread(new ResumeStatusTask(currentSimulation, currentSimulation.getPauseDummyObject())).start();
        statusVBox.getChildren().clear();
        statusVBox.setSpacing(10);
        statusVBox.getChildren().add(new Label("Simulation status: simulation in process."));
        //HBox currHBox = (HBox)simulationIds.getChildren().get(currentSimulation.getId()+1);
        //currHBox.getChildren().set(1, new Label("in process.."));
        currSimulationHBox.getChildren().set(1, new Label("in process.."));
        /*int currTickResultNum = 0;
        String cleanCurrTick = currentTickResult.getText().replaceAll(",", "");
        try{
            currTickResultNum = Integer.parseInt(cleanCurrTick);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/
        new Thread(new GettingInfoTask(currentSimulation, simulationIds,
                currentTickResultProperty, currentTimeInSecondsResultProperty,
                (TableView<EntityPopulation>)tableViewVBox.getChildren().get(0), reRunButton, showResultsButton,
                statusVBox, stopButton, resumeButton, pauseButton, currSimulationHBox)).start();
    }

    @FXML public void initialize(){
       /* currentTickResult.textProperty().bind(currentTickResultProperty.asString());
        currentTimeInSecondsResult.textProperty().bind(currentTimeInSecondsResultProperty.asString());*/

        currentTickResult.textProperty().bind(Bindings.format("%,d", currentTickResultProperty));
        currentTimeInSecondsResult.textProperty().bind(Bindings.format("%,d", currentTimeInSecondsResultProperty));
    }
    public void setMainController(AppController mainController){
        this.mainController = mainController;
    }

    public void setReRunButtonHasBeenPressed(boolean set){
        reRunButtonHasBeenPressed = set;
    }
    public void setGraph(WorldDTO worldDTO){
        lineChartHBox.getChildren().clear();
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Ticks");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Entities");

        //lineChart = new LineChart<xAxis, yAxis>;

        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChartHBox.getChildren().add(lineChart);

        SimulationDTO simulationDTO = currentSimulation.createSimulationDTO(true);

        /*for(Map.Entry<String, EntityDefinitionDTO> entityDefinitionDTOEntry: worldDTO.getEntitesMap().entrySet()){
            XYChart.Series series = new XYChart.Series();
            series.setName(entityDefinitionDTOEntry.getKey());
            int ind = 0;
            for (Pair<Integer, Integer> curr : entityDefinitionDTOEntry.getValue().getPopulationHistoryList()){
                series.getData().add(new XYChart.Data(curr.getKey(), curr.getValue()));
                //series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                ind++;
            }
            lineChart.getData().add(series);
        }*/

        for(Map.Entry<String, List<Pair<Integer, Integer>>> entry: currentSimulation.getPopulationChangesList().entrySet()){
            XYChart.Series series = new XYChart.Series();
            series.setName(entry.getKey());
            int ind = 0;
            for (Pair<Integer, Integer> curr : entry.getValue()){
                series.getData().add(new XYChart.Data(curr.getKey(), curr.getValue()));
                //series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
                ind++;
            }
            lineChart.getData().add(series);
        }

    }
    public void startButtonHasBeenPressed(ActiveEnvironmentDTO activeEnvironmentDTO, Map<String, Integer> reRunPopulation){

        //if its the first simulation in this file
        if(mainController.getHeaderComponentController().getFirstUseOfFile()){
        //if(simulationIds.getChildren().isEmpty()){
            mainController.getHeaderComponentController().setFirstUseOfFile(false);
            firstSimulation = true;

        }
        else firstSimulation = false;

        if(!menuButtonsHBox.getChildren().isEmpty())
            menuButtonsHBox.getChildren().clear();
        if(currIdPressed!=null){
            currIdPressed.setStyle("    -fx-border-color: rgba(0,178,208,0);" +
                        "    -fx-stroke-width: 6;");
        }

        showResultsButton.setDisable(true);
        stopButton.setDisable(false);
        //resumeButton.setDisable(false);
        pauseButton.setDisable(false);
        reRunButton.setDisable(true);

        //every time we enter after first time
        if(currentSimulation!=null)
            currentSimulation.setIsSelected(false);

        Tab resultsTab = mainController.getBodyComponentController().getResultsTab();;
        mainController.getBodyComponentController().getProgramTabs().getSelectionModel().select(resultsTab);
        SimulationImpl simulation = new SimulationImpl(mainController.getEngineWorld());
        simulation.setActiveEnvironmentFromUsersInput(activeEnvironmentDTO);
        // simulation.setActiveEnvironmentFromUsersInput(mainController.getActiveEnvDTO());
        simulation.setDate(getTimeAndDate());
        mainController.getSimulationManager().addSimulation(simulation);
        simulation.setReRunPopulation(reRunPopulation);

        RunSimulation runSimulation = new RunSimulation(simulation);

        //update current simulation
        if(currentSimulation!=null)
            currentSimulation.setIsSelected(false);

        currentSimulation = simulation;
        simulation.setEntitiesInstancesForSimulation();
        currentSimulation.setIsSelected(true);

        mainController.getSimulationManager().getThreadExecutor().execute(runSimulation);
        //queue manager
        mainController.getHeaderComponentController().runThreadPoolInfoTask();

        currIdPressed = presentSimulations(simulation.getId(), simulation.createSimulationDTO(false));

        currIdPressed.fire();


        statusVBox.getChildren().clear();
        statusVBox.setSpacing(10);

        statusVBox.getChildren().add(new Label("Simulation status: simulation in process."));
    }

    public void showResultsButtonListener(ActionEvent event){
        //currentSimulation.setShowResultWasClicked(true);
        setGraph(mainController.createWorldDTO());
        setHistogramMenuBars();
    }

    public void setHistogramMenuBars(){
        entitiesMenuButton = null;
        propertiesMenuButton = null;
        menuButtonsHBox.getChildren().clear();
        if(!menuButtonsHBox.getChildren().isEmpty())
            menuButtonsHBox.getChildren().clear();

        entitiesMenuButton = new MenuButton("Entities");
        entitiesMenuButton.setMinSize(70, 25.6);
        menuButtonsHBox.getChildren().add(entitiesMenuButton);
        propertiesMenuButton = new MenuButton("Properties");
        propertiesMenuButton.setMinSize(80, 25.6);
        setEntitiesMenuBer();
    }

    public void setEntitiesMenuBer(){

        SimulationDTO simulationDTO = currentSimulation.createSimulationDTO(true);
        //WorldDTO worldDTO = mainController.createWorldDTO();
        List<MenuItem> tempList = new ArrayList<>();
        for(Map.Entry<String, List<EntityInstanceDTO>> entry: simulationDTO.getEntitiesMap().entrySet()){
            tempList.add(new MenuItem(entry.getKey()));
        }

        Collections.reverse(tempList);
        entitiesMenuButton.getItems().addAll(tempList);
        entitiesMenuButton.setMinSize(60, 25.6);

        for(MenuItem menuItem: entitiesMenuButton.getItems()){
            entitiesMenuButton.setMinSize(70, 25.6);
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //propertiesMenuButton.setDisable(true);
                    entitiesMenuButton.setMinSize(70, 25.6);
                    if(!chosenEntityAndPropVBox.getChildren().isEmpty())
                        chosenEntityAndPropVBox.getChildren().clear();

                    if(!populationHistogramHBox.getChildren().isEmpty())
                        populationHistogramHBox.getChildren().clear();

                    if(!propertyConsistencyHBox.getChildren().isEmpty())
                        propertyConsistencyHBox.getChildren().clear();

                    if(!averagePropertyValueHBox.getChildren().isEmpty())
                        averagePropertyValueHBox.getChildren().clear();

                    chosenEntityInHistogram = menuItem.getText();
                    chosenEntityAndPropVBox.getChildren().add(new Label("Chosen entity: " + chosenEntityInHistogram));
                   // propertiesMenuButton.setDisable(false);
                    propertiesMenuButton.getItems().clear();
                    setPropertiesMenuBar();
                }
            });
        }
    }

    public void setPropertiesMenuBar(){
        //menuButtonsHBox.getChildren().clear();
        SimulationDTO simulationDTO = currentSimulation.createSimulationDTO(true);
        WorldDTO worldDTO = mainController.createWorldDTO();

        propertiesMenuButton.getItems().clear();


        EntityDefinitionDTO entityDefinitionDTO = worldDTO.getEntitesMap().get(chosenEntityInHistogram);
        //simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).get(0).getEntityDefinitionDTO();

        if(menuButtonsHBox.getChildren().size()>1)
            menuButtonsHBox.getChildren().set(1,propertiesMenuButton);
        else menuButtonsHBox.getChildren().add(propertiesMenuButton);

         for(Map.Entry<String, PropertyDTO> entry: entityDefinitionDTO.getPropertiesMap().entrySet()){
            propertiesMenuButton.getItems().add(new MenuItem(entry.getKey()));
        }
        propertiesMenuButton.setMinSize(80, 25.6);

        for(MenuItem menuItem: propertiesMenuButton.getItems()){
            propertiesMenuButton.setMinSize(80, 25.6);
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    propertiesMenuButton.setMinSize(80, 25.6);
                    chosenPropertyInHistogram = menuItem.getText();
                    if(chosenEntityAndPropVBox.getChildren().size()>1)
                        chosenEntityAndPropVBox.getChildren().set(1, new Label("Chosen property: " + chosenPropertyInHistogram));
                    else chosenEntityAndPropVBox.getChildren().add(new Label("Chosen property: " + chosenPropertyInHistogram));
                    populationHistogramHBox.getChildren().clear();
                    propertyConsistencyHBox.getChildren().clear();
                    averagePropertyValueHBox.getChildren().clear();
                    //do histogram
                    histogramData();
                }
            });
        }
    }

    public void histogramData(){

        SimulationDTO simulationDTO = currentSimulation.createSimulationDTO(true);
        WorldDTO worldDTO = mainController.createWorldDTO();
        //population histogram

        //populationHistogramHBox.getChildren().add(new Label("Population Histogram:"));
        VBox populationVBoxTemp = new VBox();
        populationVBoxTemp.getChildren().add(new Label("Original Population: "
                + worldDTO.getEntitesMap().get(chosenEntityInHistogram).getPopulation()));
        populationVBoxTemp.getChildren().add(new Label("Current Population: "
                + simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).size()));

        populationHistogramHBox.getChildren().add(populationVBoxTemp);
        //else populationHistogramHBox.getChildren().add(populationVBoxTemp);

        //average amount of ticks without changing property value
        float sumChangesInSingleEntity = 0, entityAvg = 0, sumAverages = 0, average = 0;
        //for every entity - check average change
        //then check average of all entities together
        for(EntityInstanceDTO entityInstanceDTO: simulationDTO.getEntitiesMap().get(chosenEntityInHistogram)) {
           /* ArrayList<ChangesInValueDTO> tempList = entityInstanceDTO.getPropertyDTOMap().
                    get(chosenPropertyInHistogram).getChangesInValueDTOList();*/
            /*if (!tempList.isEmpty()) {
                for (ChangesInValueDTO changesInValueNode : tempList) {
                    //sum all ticks amount changes in this entity's property
                    sumChangesInSingleEntity += (changesInValueNode.getEnd() - changesInValueNode.getStart());
                }
                //sum all ticks amount without changing and calculate average
                entityAvg = sumChangesInSingleEntity/tempList.size();*/

           /* }
            else entityAvg = currentSimulation.getCurrentTick();*/
            PropertyDTO relevantProperty = entityInstanceDTO.getPropertyDTOMap().get(chosenPropertyInHistogram);
            if(relevantProperty.getNumOfChanges() == 0)
                entityAvg = relevantProperty.getLastChange();
            else
                entityAvg = (float) relevantProperty.getSumOfChanges() /relevantProperty.getNumOfChanges();

            //entityAvg = sumChangesInSingleEntity/simulationDTO.getCurrentTick();
            sumAverages += entityAvg;
        }
        //sum all averages and calculate total average
        average = sumAverages/simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).size();

        if(propertyConsistencyHBox.getChildren().size()>0)
            propertyConsistencyHBox.getChildren().set(0,new Label("Property Consistency: " + Float.toString(average)));
        else propertyConsistencyHBox.getChildren().add(new Label("Property Consistency: " + Float.toString(average)));

        //calculate average property value if arithmetic
        PropertyDTO chosenProperty = worldDTO.getEntitesMap().get(chosenEntityInHistogram).getPropertiesMap().get(chosenPropertyInHistogram);
        if(chosenProperty.getType().equalsIgnoreCase("decimal") || chosenProperty.getType().equalsIgnoreCase("float")){

                //for each entity in entity def list
            if(chosenProperty.getType().equalsIgnoreCase("decimal")) {
                int sumValues = 0;
                for (EntityInstanceDTO entityInstanceDTO : simulationDTO.getEntitiesMap().get(chosenEntityInHistogram)) {
                    sumValues += (int) (entityInstanceDTO.getPropertyDTOMap().get(chosenPropertyInHistogram).getValue());
                }
                if(simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).size() > 0)
                    averagePropertyValueHBox.getChildren().add(new Label("Average Property Value is: "
                            + sumValues / simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).size()));
                else averagePropertyValueHBox.getChildren().add(new Label("Average Property Value is: "
                        + sumValues));
                //}
            }
            else{
                float sumValues = 0;
                for (EntityInstanceDTO entityInstanceDTO : simulationDTO.getEntitiesMap().get(chosenEntityInHistogram)) {
                    sumValues += (float) (entityInstanceDTO.getPropertyDTOMap().get(chosenPropertyInHistogram).getValue());
                }
                if(simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).size() > 0)
                    averagePropertyValueHBox.getChildren().add(new Label("Average Property Value is: "
                            + sumValues / simulationDTO.getEntitiesMap().get(chosenEntityInHistogram).size()));
                else averagePropertyValueHBox.getChildren().add(new Label("Average Property Value is: "
                        + sumValues));
            }

        }
    }


    public void setTableViewVBox(SimulationDTO simulationDTO){
        // Create a TableView
        /*if(tableViewVBox.getChildren().size()>0)
            */tableViewVBox.getChildren().clear();

        TableView<EntityPopulation> tableView = new TableView<>();
        ObservableList<TableColumn<EntityPopulation, ?>> columns = tableView.getColumns();

        TableColumn<EntityPopulation, String> entityColumn = new TableColumn<>("Entity");
        entityColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        columns.add(entityColumn);

        TableColumn<EntityPopulation, Integer> populationColumn = new TableColumn<>("Population");
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        columns.add(populationColumn);

        ObservableList<EntityPopulation> items = FXCollections.observableArrayList();
        List<EntityPopulation> tempList = new ArrayList<>();
        for(Map.Entry<String, List<EntityInstanceDTO>> entry: simulationDTO.getEntitiesMap().entrySet()){
            tempList.add(new EntityPopulation(entry.getKey(), entry.getValue().size()));
            //items.add(new EntityPopulation(entry.getKey(), entry.getValue().size()));
        }
        //reverse list so table will be in right order
        Collections.reverse(tempList);
        for(EntityPopulation entityPopulation: tempList)
            items.add(entityPopulation);

        tableView.setItems(items);
        tableView.setEditable(true);

        tableViewVBox.getChildren().add(tableView);
    }

    public String getTimeAndDate(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        return currentDateTime.format(formatter);
    }

    public Button presentSimulations(int id, SimulationDTO simulationDTO){
        HBox currentSimulationLine = new HBox();
        //setTableViewVBox(simulationDTO);
        currentSimulationLine.setSpacing(10.00);
        //Label entityId = new Label("Simulation number " + Integer.toString(id));
        Button idButton = new Button("Simulation #" + Integer.toString(id));
        //add progress!!!!
        Label progress = new Label("In process..");
        currentSimulationLine.getChildren().addAll(idButton, progress);

        simulationIds.getChildren().add(currentSimulationLine);

        idButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currSimulationHBox = currentSimulationLine;
                if(currentSimulation!=null && currentSimulation!=mainController.getSimulationManager().getSimulation(id)){
                    currentSimulation.setIsSelected(false);
                }

                Button clicked = (Button) event.getSource();
                if(currIdPressed!=null && currIdPressed!=clicked){
                    currIdPressed.setStyle("    -fx-border-color: rgba(0,178,208,0.01);" +
                            "    -fx-stroke-width: 6;");
                }

                currIdPressed = clicked;
                currIdPressed.setStyle(
                        "    -fx-border-color: #00b2d0;" +
                                "    -fx-stroke-width: 6;");// +);

                setTableViewVBox(simulationDTO);
                //get the number of simulation
                //int idOfCurrSim = simulationIds.getChildren().indexOf(clicked) + 1;
                currentSimulation = mainController.getSimulationManager().getSimulation(id);
                currentSimulation.setIsSelected(true);
                setButtonsForCurrentSimulation();
                //clear show results of past one
                menuButtonsHBox.getChildren().clear();
                chosenEntityAndPropVBox.getChildren().clear();
                populationHistogramHBox.getChildren().clear();
                propertyConsistencyHBox.getChildren().clear();
                averagePropertyValueHBox.getChildren().clear();
                lineChartHBox.getChildren().clear();

                /*if(currentSimulation.getShowResultsWasClicked()){
                    showResultsButton.fire();
                }*/
                //mainController.setCurrentSimulation(currentSimulation);
                /*int currTickResultNum = 0;
                //String cleanCurrTick = currentTickResult.getText().replaceAll(",", "");
                int cleanCurrTick = currentTickResultProperty.getValue();*/
                /*try{
                    currTickResultNum = Integer.parseInt(cleanCurrTick);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }*/

                new Thread(new GettingInfoTask(currentSimulation, detailsVBoxResults,
                        currentTickResultProperty, currentTimeInSecondsResultProperty,
                        (TableView<EntityPopulation>)(tableViewVBox.getChildren().get(0)), reRunButton, showResultsButton, statusVBox, stopButton, resumeButton, pauseButton, currSimulationHBox)).start();

            }
        });
        return idButton;
    }

    public void setButtonsForCurrentSimulation() {
        if (currentSimulation.getStatus().equals(Status.STOP) || currentSimulation.getStatus().equals(Status.FINISHED)) {
            stopButton.setDisable(true);
            resumeButton.setDisable(true);
            pauseButton.setDisable(true);
            reRunButton.setDisable(false);
        } else if (currentSimulation.getStatus().equals(Status.PAUSE)) {
            resumeButton.setDisable(false);
            pauseButton.setDisable(true);
            stopButton.setDisable(false);
            reRunButton.setDisable(true);
        } else { //(currentSimulation.getStatus().equals(Status.ON_GOING))
            resumeButton.setDisable(true);
            pauseButton.setDisable(false);
            stopButton.setDisable(false);
            reRunButton.setDisable(true);
        }
    }

    public void clearAllData(){
        mainController.getBodyComponentController().getNewExecutionComponentController().clearNewExecutionData();
        simulationIds.getChildren().clear();
        statusVBox.getChildren().clear();
        tableViewVBox.getChildren().clear();
        menuButtonsHBox.getChildren().clear();
        chosenEntityAndPropVBox.getChildren().clear();
        populationHistogramHBox.getChildren().clear();
        propertyConsistencyHBox.getChildren().clear();
        averagePropertyValueHBox.getChildren().clear();
        lineChartHBox.getChildren().clear();
        currentSimulation = null;
        currIdPressed = null;
        reRunButtonHasBeenPressed = false;
        chosenEntityInHistogram = "";
        chosenPropertyInHistogram = "";
        /*currentTickResult.setText("");
        currentTimeInSecondsResult.setText("");*/
        currentTimeInSecondsResultProperty.set(0);
        currentTickResultProperty.set(0);
    }

    public void disableResultsButtons(){
        stopButton.setDisable(true);
        showResultsButton.setDisable(true);
        pauseButton.setDisable(true);
        resumeButton.setDisable(true);
        reRunButton.setDisable(true);
    }
}
