package application.app;

import dto.GridDTO;
import dto.WorldDTO;
import exceptions.*;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import application.app.tasks.GettingInfoTask;
import application.body.BodyController;
import application.body.details.DetailsController;
import application.body.newExecution.NewExecutionController;
import application.body.results.ResultsController;
import application.header.HeaderController;
import simulationManager.SimulationManagerImpl;
import world.WorldImpl;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class AppController {

    private WorldImpl engineWorld;
    private SimulationManagerImpl simulationManager;

    @FXML private Pane headerComponent;
    @FXML private HeaderController headerComponentController;

    @FXML private Pane bodyComponent;
    @FXML private BodyController bodyComponentController;
    private Stage primaryStage;

    @FXML private DetailsController detailsComponentController;
    @FXML private NewExecutionController newExecutionComponentController;
    @FXML private ResultsController resultsComponentController;
    @FXML private Pane detailsComponent;
    @FXML private Pane newExecutionComponent;
    @FXML private Pane resultsComponent;

    private GettingInfoTask gettingInfoTask;

    public AppController() throws PropertyDoesNotExistException, FileIsNotXMLFileException, JAXBException, FileNotFoundException, ActionOnNonExistingEntityInstanceException, EnvironmentVariablesNameDuplicationException, EntityPropertiesNameDuplicationException, MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, SpacesInNameException, FileDoesNotExistsException {
        //engineWorld = new WorldImpl(headerComponentController.getFilePath());
        simulationManager = new SimulationManagerImpl();
    }

    public GettingInfoTask getTask(){
        return gettingInfoTask;
    }

    public void setEngineWorld(WorldImpl engineWorld) {
        this.engineWorld = engineWorld;
    }

    @FXML
    public void initialize() {
        if (headerComponentController != null && bodyComponentController != null) {
            headerComponentController.setMainController(this);
            bodyComponentController.setMainController(this);
        }
    }

//move to results controller??
   /* public void startBottomHasBeenPressed(){
        UUID thisSimulationGUID = UUID.randomUUID();
        SimulationImpl simulation = new SimulationImpl(getEngineWorld(), thisSimulationGUID);
        simulation.setActiveEnvironmentFromUsersInput(getActiveEnvDTO());
        simulation.setDate(getTimeAndDate());
        simulationManager.addSimulation(simulation, thisSimulationGUID);

        RunSimulation runSimulation = new RunSimulation(simulation);
        simulationManager.getThreadExecutor().execute(runSimulation);
    }*/

    public int getMaxPopulation(){
        GridDTO gridDTO = engineWorld.getGridDTODimensions();
        return gridDTO.getCols()*gridDTO.getRows();
    }
   /* public void start() throws VariableDoesNotExistsException, PropertyDoesNotExistException, ValueDoesNotMatchPropertyTypeException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException {
        UUID thisSimulationGUID = UUID.randomUUID();
        simulationManager.startSimulation(engineWorld, newExecutionComponentController.sendEnvironmentVarsToEngine()
                    , getTimeAndDate(), thisSimulationGUID);
    }
*/
    /*public ActiveEnvironmentDTO getActiveEnvDTO(){
        return bodyComponentController.getNewExecutionComponentController().sendEnvironmentVarsToEngine();
    }*/

    public WorldDTO createWorldDTO(){
        return engineWorld.createWorldDTO();
    }

    public BodyController getBodyComponentController(){
        return bodyComponentController;
    }

    public HeaderController getHeaderComponentController(){ return headerComponentController;}



    public void setHeaderComponentController(HeaderController headerComponentController){
        this.headerComponentController = headerComponentController;
        headerComponentController.setMainController(this);
    }

    public void setBodyComponentController(BodyController bodyComponentController){
        this.bodyComponentController = bodyComponentController;
        bodyComponentController.setMainController(this);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    public WorldImpl getEngineWorld(){ return engineWorld;}
    public SimulationManagerImpl getSimulationManager() { return simulationManager;}
}
