package application.app.tasks.runSimulation;

import simulation.SimulationImpl;

public class RunSimulation implements Runnable{

    //private AppController mainController;
    private SimulationImpl simulation;
    //private Button showResultsButton;
    //private MenuButton entitiesMenuButton;
    //private Button setSimulationButton;
    public RunSimulation(SimulationImpl simulation){
        //this.mainController = mainController;
        this.simulation = simulation;
        //this.showResultsButton = showResultsButton;
        //this.entitiesMenuButton = entitiesMenuButton;
        //this.setSimulationButton = setSimulationButton;
    }

    public SimulationImpl getSimulation(){
        return simulation;
    }

    @Override
    public void run() {
            System.out.println("Start running simulation");
            //showResultsButton.setDisable(true);
            //setSimulationButton.setDisable(true);
            simulation.runSimulation();
            /*if (simulation.getStatus().equals(Status.FINISHED)) {
                statusVBox.getChildren().set(0, new Label("Simulation status: ended."));
                if (simulation.getTerminate().getTerminatedByTicks()) {
                    statusVBox.getChildren().set(0, new Label("Notes: simulation ended due to ticks condition."));
                } else {
                    statusVBox.getChildren().set(0, new Label("Notes: simulation ended due to seconds condition."));
                }
            }*/
            /*else{
                statusVBox.getChildren().set(0, new Label("Simulation status: ended."));
                statusVBox.getChildren().set(1, new Label("Notes: "));
            }*/
            //else ive got stop notes
            //showResultsButton.setDisable(false);

            //get status, is stop - finished by user, if finished - by termination


            /*System.out.println("in run simulation main thread");
            System.out.println(e.getMessage());
            simulation.setErrorMessage(e.getMessage());*/

           /* Platform.runLater(() -> {
                statusVBox.getChildren().set(0, new Label("Simulation status: stopped due to an error."));
                statusVBox.getChildren().set(1, new Label("Notes: " + e.getMessage()));
                //System.out.println(e.getMessage());
            });*/
    }
}
