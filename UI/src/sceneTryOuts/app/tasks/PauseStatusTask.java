package application.app.tasks;

import simulation.SimulationImpl;
import simulation.status.Status;

public class PauseStatusTask implements Runnable{
    //private Object pauseDummyObject;
    private SimulationImpl simulation;
    /*private VBox idsVBox;
    private Button currIdButton;*/
    public PauseStatusTask(SimulationImpl simulation){
        //this.pauseDummyObject = pauseDummyObject;
        this.simulation = simulation;
    }

    @Override
    public void run() {
        simulation.setStatus(Status.PAUSE);
        simulation.setIsPaused(true);
    }
}
