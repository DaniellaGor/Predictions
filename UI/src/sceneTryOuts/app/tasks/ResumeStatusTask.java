package application.app.tasks;

import simulation.SimulationImpl;
import simulation.status.Status;

public class ResumeStatusTask implements Runnable{
    //private Object pauseDummyObject;
    private SimulationImpl simulation;
    private Object pauseDummyObject;
    public ResumeStatusTask(SimulationImpl simulation, Object pauseDummyObject){
        this.pauseDummyObject = pauseDummyObject;
        this.simulation = simulation;
    }

    @Override
    public void run() {
        synchronized (pauseDummyObject) {
            System.out.println("resume");
            simulation.setIsPaused(false);
            simulation.setStatus(Status.ON_GOING);
            System.out.println("resumed");
            pauseDummyObject.notifyAll();
        }
    }
}
