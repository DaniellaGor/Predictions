package application.app.tasks;

import dto.SimulationDTO;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import application.body.results.entityPopulationForTableView.EntityPopulation;
import simulation.SimulationImpl;
import simulation.status.Status;

import static java.lang.Thread.sleep;

public class GettingInfoTask implements Runnable{
    private SimulationImpl simulation;
    private VBox simulationIds;
    private VBox statusVBox;
    private IntegerProperty currentTickResultProperty;
    private IntegerProperty currentTimeInSecondsResultProperty;

    private TableView<EntityPopulation> tableView;
    private Button reReunButton;
    private Button showResultsButton;
    private Button stopButton;
    private Button resumeButton;
    private Button pauseButton;
    private HBox currSimulationHBox;



    public GettingInfoTask(SimulationImpl simulation, VBox simulationIds,
                           IntegerProperty currentTickResultProperty,
                           IntegerProperty currentTimeInSecondsResultProperty, TableView<EntityPopulation> tableView,
                           Button reRunButton, Button showResultsButton, VBox statusVBox, Button stopButton, Button resumeButton,
                           Button pauseButton, HBox currSimulationHBox){
        this.simulation = simulation;
        this.simulationIds = simulationIds;
        this.currentTickResultProperty = currentTickResultProperty;
        this.currentTimeInSecondsResultProperty = currentTimeInSecondsResultProperty;
        this.tableView = tableView;
        this.reReunButton = reRunButton;
        this.showResultsButton = showResultsButton;
        this.statusVBox = statusVBox;
        this.stopButton = stopButton;
        this.resumeButton = resumeButton;
        this.pauseButton = pauseButton;
        this.currSimulationHBox = currSimulationHBox;
        //simulation.setCurrentTick(currTicksInUI);
    }

    @Override
    public void run() {

        /*SimulationDTO simulationDTO = simulation.createSimulationDTO(false);
        String status = simulationDTO.getStatus();*/
        ///String status = simulation.getStatus().toString();
        do {
            //get data
            SimulationDTO simulationDTO = simulation.createSimulationDTO(false);
            //boolean isFinished = false;
            System.out.println(simulationDTO.getId() + " is selected");
            //System.out.println("");

            Platform.runLater(() -> {
                System.out.println("entered run later");
                System.out.println("ENTERED WITH "+ simulationDTO.getId());

                //update data
                //currentTimeInSecondsResultProperty.set((int) simulationDTO.getCurrentTimeInMillis() / 1000);
                currentTimeInSecondsResultProperty.set((int) simulation.getCurrentTimeInMillis() / 1000);
                System.out.println(currentTimeInSecondsResultProperty);
                currentTickResultProperty.set(simulationDTO.getCurrentTick());
                System.out.println(currentTickResultProperty);

                //check when killed
                for (EntityPopulation row : tableView.getItems()) {
                    row.populationProperty().set(simulationDTO.getEntitiesMap().get(row.entityNameProperty().getValue()).size());
                    System.out.println(simulationDTO.getEntitiesMap().get(row.entityNameProperty().getValue()).size());
                }
                /*try {
                    sleep(200);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }*/
            });

            try {
                sleep(200);
            } catch (Exception e) {
                System.out.println("in sleep getting info");
                System.out.println(e.getMessage());
            }
            /*if(status.equalsIgnoreCase("finished")) {
                status = "stop";
            }*/


        } while (simulation.getStatus().equals(Status.ON_GOING) && simulation.getIsSelected());
        //} while ((!status.equalsIgnoreCase("stop") && !status.equalsIgnoreCase("pause")) && simulation.getIsSelected());
        //} while ((!simulation.getStatus().equals(Status.STOP) || !simulation.getStatus().equals(Status.PAUSE)) && simulation.getIsSelected());


        SimulationDTO simulationDTO = simulation.createSimulationDTO(true);
        if(simulation.getStatus().equals(Status.FINISHED)) {

            Platform.runLater(() ->{
                //check status
                stopButton.setDisable(true);
                resumeButton.setDisable(true);
                pauseButton.setDisable(true);

                if (!statusVBox.getChildren().isEmpty())
                    statusVBox.getChildren().clear();
                if (!simulationDTO.getErrorMessage().equals("")) {
                    statusVBox.getChildren().add(new Label(simulationDTO.getErrorMessage()));
                } else {
                    if (simulationDTO.getTerminationDTO().getIfEndedByTicks())
                        statusVBox.getChildren().add(new Label("Simulation terminated due to ticks."));
                    else statusVBox.getChildren().add(new Label("Simulation terminated due to seconds."));
                }
                //HBox temp = (HBox) simulationIds.getChildren().get(simulationDTO.getId());
                //Label temp = (Label) simulationIds.getChildren().get(simulationDTO.getId());
                //temp.setText("ended");
               /* HBox currHBox = (HBox)simulationIds.getChildren().get(simulationDTO.getId()+1);
                currHBox.getChildren().set(1, new Label("ended"));*/
                currSimulationHBox.getChildren().set(1, new Label("ended"));
                //temp.getChildren().set(1, new Label("ended"));
                showResultsButton.setDisable(false);
                reReunButton.setDisable(false);
            });

           // if (simulationDTO.getStatus().equalsIgnoreCase("finished")) {
                //if (status.equalsIgnoreCase("finished") || status.equalsIgnoreCase("stop")) {
                /*if (!simulationDTO.getErrorMessage().equals("")) {
                    statusVBox.getChildren().add(new Label(simulationDTO.getErrorMessage()));*/
                /*} else {
                    if (simulationDTO.getTerminationDTO().getIfEndedByTicks())
                        statusVBox.getChildren().add(new Label("Simulation terminated due to ticks."));
                    else statusVBox.getChildren().add(new Label("Simulation terminated due to seconds."));
                }
                HBox temp = (HBox) simulationIds.getChildren().get(simulationDTO.getId());
                temp.getChildren().set(1, new Label("ended"));
                showResultsButton.setDisable(false);
                reReunButton.setDisable(false);
            }*/

        }

        System.out.println("FINISHED WITH "+ simulation.getId());



        /*if (simulation.getIsSelected()) {
            SimulationDTO simulationDTO = simulation.createSimulationDTO(true);
            Platform.runLater(() -> {
                if (!statusVBox.getChildren().isEmpty())
                    statusVBox.getChildren().clear();
                if (simulationDTO.getStatus().equalsIgnoreCase("finished") || simulationDTO.getStatus().equalsIgnoreCase("stop")) {
                    if (!simulationDTO.getErrorMessage().equals("")) {
                        statusVBox.getChildren().add(new Label(simulationDTO.getErrorMessage()));
                    } else if (simulationDTO.getStatus().equalsIgnoreCase("finished")) {
                        if (simulationDTO.getTerminationDTO().getIfEndedByTicks())
                            statusVBox.getChildren().add(new Label("Simulation terminated due to ticks."));
                        else statusVBox.getChildren().add(new Label("Simulation terminated due to seconds."));
                    }
                    HBox temp = (HBox) simulationIds.getChildren().get(simulationDTO.getId());
                    temp.getChildren().set(1, new Label("ended"));
                    showResultsButton.setDisable(false);
                    reReunButton.setDisable(false);
                }
            });*/
        }
}
