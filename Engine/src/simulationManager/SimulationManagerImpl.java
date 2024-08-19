package simulationManager;

import dto.ActiveEnvironmentDTO;
import dto.PastSimulationsDTO;
import dto.SimulationDTO;
import exceptions.*;
import simulation.SimulationImpl;
import world.WorldImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimulationManagerImpl implements SimulationManager {

    Map<Integer, SimulationImpl> simulationMap;
    int counter;
    ExecutorService threadExecutor;

    public SimulationManagerImpl() {
        simulationMap = new LinkedHashMap<>();
        counter = 0;
    }

    public void setThreadPool(int numOfThreads){
        threadExecutor = Executors.newFixedThreadPool(numOfThreads);
    }

    @Override
    public void addSimulation(SimulationImpl simulation) {
        counter++;
        simulationMap.put(counter, simulation);
        simulation.setId(counter);
    }

    public ExecutorService getThreadExecutor(){
        return threadExecutor;
    }
    @Override
    public SimulationImpl getSimulation(int id) {
        return simulationMap.get(id);
    }
    @Override
    public void startSimulation(WorldImpl world, ActiveEnvironmentDTO activeEnvironmentDTO, String dateAndTime, int id) throws VariableDoesNotExistsException, ValueDoesNotMatchPropertyTypeException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {

       /* SimulationImpl simulation = new SimulationImpl(world, guid);
        simulation.setActiveEnvironmentFromUsersInput(activeEnvironmentDTO);
        simulation.setDate(dateAndTime);
        simulation.runSimulation();
        simulationMap.put(guid, simulation);*/
    }

    public PastSimulationsDTO createPastSimulationsDTO(boolean isFinished) {
        Map<Integer, SimulationDTO> simulationsMapDTO = new HashMap<>();
        int count = 0;
        for (Map.Entry<Integer, SimulationImpl> entry : simulationMap.entrySet()) {
            count++;
            simulationsMapDTO.put(entry.getKey(), entry.getValue().createSimulationDTO(isFinished));
        }
        return new PastSimulationsDTO(simulationsMapDTO);
    }


    @Override
    public Map<Integer, SimulationImpl> getSimulationMap() {
        return simulationMap;
    }

    public int getWaitingThreadsNumber(){
        return (int)((ThreadPoolExecutor) threadExecutor).getQueue().size();
    }

    public int getRunningThreadsNumber(){
        return (int)((ThreadPoolExecutor) threadExecutor).getActiveCount();
    }

    public int getCompletedThreadsNumber(){
        return (int)((ThreadPoolExecutor) threadExecutor).getCompletedTaskCount();
    }

    public void clearDataFromMap(){
        simulationMap.clear();
    }
}
