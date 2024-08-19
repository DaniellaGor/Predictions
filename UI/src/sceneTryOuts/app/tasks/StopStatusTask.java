package application.app.tasks;

import simulation.SimulationImpl;
import simulation.status.Status;

public class StopStatusTask implements Runnable{
    private SimulationImpl simulation;
    public StopStatusTask(SimulationImpl simulation){
        this.simulation = simulation;
    }
    @Override
    public void run() {
        System.out.println("updating stop");
        //update data
        simulation.setStatus(Status.STOP);
        simulation.setTerminationByUser();
    }
}
