package application.app.tasks;

import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import application.header.HeaderController;
import simulationManager.SimulationManagerImpl;

public class ThreadInfoTask implements Runnable{

    private SimulationManagerImpl simulationManager;
    private SimpleLongProperty threadPoolQueueProperty;
    private SimpleLongProperty threadPoolActiveProperty;
    private SimpleLongProperty threadPoolCompletedProperty;

    private HeaderController headerController;

    public ThreadInfoTask(SimulationManagerImpl simulationManager, SimpleLongProperty threadPoolQueueProperty, SimpleLongProperty threadPoolActiveProperty, SimpleLongProperty threadPoolCompletedProperty,
                          HeaderController headerController){
        this.simulationManager = simulationManager;
        this.threadPoolQueueProperty = threadPoolQueueProperty;
        this.threadPoolActiveProperty = threadPoolActiveProperty;
        this.threadPoolCompletedProperty = threadPoolCompletedProperty;
        this.headerController = headerController;
    }

    @Override
    public void run() {
        System.out.println("threadPool updater task IN");
        do{
           int queueThreads = simulationManager.getWaitingThreadsNumber();
           int activeThreads = simulationManager.getRunningThreadsNumber();
           int completedThreads = simulationManager.getCompletedThreadsNumber();

            Platform.runLater(()->{
                threadPoolQueueProperty.set(queueThreads);
                threadPoolActiveProperty.set(activeThreads);
                threadPoolCompletedProperty.set(completedThreads);
            });

            try{
                Thread.sleep(200);
            } catch (InterruptedException e) {
            // Handle the InterruptedException if needed
            }

        }while(!headerController.getStopThreadPoolInfoTask());

        System.out.println("ThreadInfoTask END!");

    }
}
