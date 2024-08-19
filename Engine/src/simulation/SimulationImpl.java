package simulation;

import actions.Actions;
import actions.context.ContextImpl;
import actions.context.ReplaceNode;
import definition.entities.EntityDefinitionImpl;
import dto.ActiveEnvironmentDTO;
import dto.EntityInstanceDTO;
import dto.PropertyDTO;
import dto.SimulationDTO;
import execution.environment.active.ActiveEnvironmentImpl;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.entity.manager.EntityInstancesManagerImpl;
import execution.instances.property.PropertyInstanceImpl;
import javafx.util.Pair;
import rules.Rules;
import simulation.grid.GridImpl;
import simulation.status.Status;
import terminate.TerminateImpl;
import world.WorldImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimulationImpl implements Simulation {
    private int id;
    private String date;
    private WorldImpl world;
    private ActiveEnvironmentImpl activeEnvironment;
    private EntityInstancesManagerImpl entityInstancesManager;
    private Map<String, List<Pair<Integer, Integer>>> populationChangesList;
    private GridImpl positionGrid;
    private int currentTick;
    private long currentTimeInMillis;
    private Status status;
    private boolean userTerminated;
    private Map<String, Integer> reRunPopulation;
    private ActiveEnvironmentImpl reRunActiveEnv;

    private Object pauseDummyObject;

    private TerminateImpl terminate;
    private boolean isPaused;
    private boolean isSelected;

    private String errorMessage;
    private boolean showResultWasClicked;
    private int lastChange;
    // List<ReplaceNode> replaceEntities;

    public SimulationImpl(WorldImpl world) {
        this.world = world;
        activeEnvironment = world.getEnvironmentManager().createEnvironmentActive();
        entityInstancesManager = new EntityInstancesManagerImpl();
        positionGrid = new GridImpl(world.getRows(), world.getCols());
        currentTick = 0;
        //userTerminated = false;
        pauseDummyObject = new Object();
        isPaused = false;
        terminate = world.getTerminateConditions();
        isSelected = true;
        currentTimeInMillis = 0;
        errorMessage = "";
        //status = Status.ON_GOING;
        status = Status.STOP;
        showResultWasClicked = false;
        //populationChangesList = new ArrayList<>();
        //replaceEntities = new ArrayList<>();
        populationChangesList = new HashMap<>();
        lastChange = 0;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }
    public void setIsSelected(boolean set){
        isSelected = set;
    }

    //public List<ReplaceNode> getReplaceEntities(){ return replaceEntities;}
    public Map<String, List<Pair<Integer, Integer>>> getPopulationChangesList(){
        return populationChangesList;
    }
    public boolean getIsSelected(){
        return isSelected;
    }
    public void setUserTerminated(boolean terminated){
        this.userTerminated = terminated;
    }

    public TerminateImpl getTerminate(){
        return terminate;
    }

    public boolean getUserTerminated(){
        return userTerminated;
    }

    public long getCurrentTimeInMillis(){ return currentTimeInMillis;}

    public boolean getShowResultsWasClicked(){ return showResultWasClicked;}
    public void setShowResultWasClicked(boolean set){ showResultWasClicked = set;}

    public boolean checkIfRuleIsOperable(Rules rule) {
        Random random = new Random();
        double randomDouble = random.nextDouble(); // Generates a random double between 0 (inclusive) and 1 (exclusive)
        randomDouble = randomDouble + Double.MIN_VALUE; // Include 0
        randomDouble = randomDouble * (1.0 - Double.MIN_VALUE) + Double.MIN_VALUE; // Include 1
        return ((currentTick % rule.getActivation().getTicks() == 0) && (randomDouble < rule.getActivation().getProbability()));
    }

    public void setId(int id){
        this.id = id;
    }

    public void setActiveEnvironmentFromUsersInput(ActiveEnvironmentDTO activeEnvironmentDTO) {
        reRunActiveEnv = new ActiveEnvironmentImpl();
        for (Map.Entry<String, PropertyDTO> entry : activeEnvironmentDTO.getEnvironmentVariables().entrySet()) {
            world.getEnvironmentManager().getEnvDefinitionsMap().get(entry.getKey()).setRandomInit(entry.getValue().getIsRandomInit());
            world.getEnvironmentManager().getEnvDefinitionsMap().get(entry.getKey()).setValue(entry.getValue().getValue());

            //set reRun active env
            //PropertyDefinitionImpl reRunProp = new PropertyDefinitionImpl(entry.getKey(), )
            reRunActiveEnv.addProperty(world.getEnvironmentManager().getEnvDefinitionsMap().get(entry.getKey()));
            //add to active environment
            activeEnvironment.addProperty(world.getEnvironmentManager().getEnvDefinitionsMap().get(entry.getKey()));
        }

    }
    public void setReRunPopulation(Map<String, Integer> rerunPopulation){
        this.reRunPopulation = rerunPopulation;
    }
    public void setEntitiesInstancesForSimulation() {
        for (Map.Entry<String, EntityDefinitionImpl> entry: world.getEntitiesDefinitionMap().entrySet()) {
            if (entityInstancesManager.getEntitiesMap().get(entry.getKey())==null) {
                entityInstancesManager.getEntitiesMap().put(entry.getKey(), new ArrayList<>());
            }
            entityInstancesManager.createEntityInstances(entry.getValue(), positionGrid, 0);
            //entry.getValue().getPopulationChangesMap().put(0, entry.getValue().getPopulation());
            //entry.getValue().getPopulationChangesList().add(new Pair<>(0,entry.getValue().getPopulation()));
            List<Pair<Integer, Integer>> temp = new ArrayList<>();
            temp.add(new Pair<>(0,entry.getValue().getPopulation()));
            populationChangesList.put(entry.getKey(), temp);

        }
    }

    public Map<String, Integer> getRerunPopulation(){
        return reRunPopulation;
    }

    public ActiveEnvironmentDTO getRerunActiveEnv(){
        return reRunActiveEnv.createActiveEnvironmentDTO(false);
    }
    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getDate() {
        return date;
    }

    public WorldImpl getWorld() {
        return world;
    }

    public int getId() {
        return id;
    }

   /* public void setCurrentTick(int tick){
        currentTick = tick;
    }*/

    public SimulationDTO createSimulationDTO(boolean isFinished) {
        Map<String, List<EntityInstanceDTO>> entitiedDefMap = new HashMap<>();
        for (Map.Entry<String, List<EntityInstanceImpl>> entityInstanceList : entityInstancesManager
                .getEntitiesMap().entrySet()) {
            List<EntityInstanceDTO> instancesDTOList = new ArrayList<>();
            //int entityCount = 0;
            for (EntityInstanceImpl entityInstance : entityInstanceList.getValue()) {
               // entityCount++;
                instancesDTOList.add(entityInstance.createEntityInstanceDTO(entityInstance.getId(), isFinished));
            }
            entitiedDefMap.put(entityInstanceList.getKey(), instancesDTOList);
        }
        ActiveEnvironmentDTO activeEnvironmentDTO = activeEnvironment.createActiveEnvironmentDTO(isFinished);
        //String newStatus = status.toString();
        /*return new SimulationDTO(date, world.createWorldDTO(), activeEnvironmentDTO,
                entitiedDefMap, getId(), currentTick, currentTimeInMillis, world.getTerminateConditions().createTerminateDTO(),
                isPaused, errorMessage, showResultWasClicked, populationChangesList);*/
        return new SimulationDTO(date, world.createWorldDTO(), activeEnvironmentDTO,
                entitiedDefMap, getId(), currentTick, currentTimeInMillis, world.getTerminateConditions().createTerminateDTO(),
                isPaused, errorMessage, showResultWasClicked, populationChangesList);
    }

    public Status getStatus(){
        return status;
    }

    public boolean getIsPaused(){
        return isPaused;
    }

    public Object getPauseDummyObject(){
        return pauseDummyObject;
    }
    public void setIsPaused(boolean setPause){
        isPaused = setPause;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void runSimulation() {
        status = Status.ON_GOING;
        boolean activateKill = false;
        //tick++;
        //setEntitiesInstancesForSimulation();
        int durationInSeconds = world.getTerminateConditions().getSecondsAmount();
        long startTime = System.currentTimeMillis(), endTime = durationInSeconds * 1000 + startTime;
        int count = 0;
        long pauseTimeInMillis = 0;
        try {
            while (!checkIfNeedToTerminate(endTime, startTime)) {
                //if pause - manage
                //if(status.equals(Status.PAUSE)) {

                synchronized (pauseDummyObject) {
                    while (isPaused) {
                        try {
                            System.out.println("pause");
                            pauseDummyObject.wait();
                            pauseTimeInMillis = (System.currentTimeMillis() - startTime) - currentTimeInMillis;
                            System.out.println("resume");
                        } catch (Exception e) {
                            System.out.println("in while loop of synchronized run simulation");
                            System.out.println(e.getMessage());
                        }
                    }
                }

                currentTimeInMillis = (System.currentTimeMillis() - pauseTimeInMillis) - startTime;

                List<Actions> allActions = new ArrayList<>();
                for (Rules item : world.getRulesList()) {
                    //create list of all action of all operable rules
                    if (checkIfRuleIsOperable(item)) {
                        allActions.addAll(item.getActionsList());
                    }
                }
                List<ReplaceNode> replaceEntities = new ArrayList<>();
                //go through all entities of all types, apply al action on each entity (if possible)
                for (Map.Entry<String, List<EntityInstanceImpl>> entry : entityInstancesManager.getEntitiesMap().entrySet()) {
                    for (EntityInstanceImpl instance : entry.getValue()) {

                        ContextImpl context = new ContextImpl(instance, entityInstancesManager, activeEnvironment, positionGrid,
                                world.getEntitiesDefinitionMap(), currentTick, replaceEntities);
                        //try {
                            /*if (instance.invokeActions(allActions, context))
                                activateKill = true;*/
                        instance.invokeActions(allActions, context);
                        if (!instance.doNotKill())
                            activateKill = true;

                        //move entity
                        positionGrid.moveEntity(instance);
                        /*} catch (VariableDoesNotExistsException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (BooleanCanNotBeHigherOrLowerException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (ValueDoesNotMatchPropertyTypeException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (ValueDoesNotMatchFunctionException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (EvaluateFunctionRequiresAnEntityAndAPropertyException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (EntityDoesNotMatchFunctionException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        } catch (PropertyDoesNotExistException e) {
                            System.out.println("in while loop exception of the simulation it self");
                            System.out.println(e.getMessage());
                            throw new RuntimeException(e);
                        }*/

                    /*if (!instance.doNotKill()) //check secondary also?
                        activateKill = true;*/
                        //set current tick as temporary end for value changes, so that in the last iteration every one is updated
                        for (Map.Entry<String, PropertyInstanceImpl> propertyEntry : instance.getPropertiesMap().entrySet()) {
                            /*if (!propertyEntry.getValue().getValueChangesArray().isEmpty())
                                propertyEntry.getValue().getValueChangesArray().
                                        get(propertyEntry.getValue().getValueChangesArray().size() - 1).setEnd(currentTick);*/
                            propertyEntry.getValue().setLastChangeInValue(currentTick);
                        }
                    }
                }

                ContextImpl context = new ContextImpl(null, entityInstancesManager, activeEnvironment, positionGrid,
                        world.getEntitiesDefinitionMap(), currentTick, replaceEntities);
                entityInstancesManager.replaceEntities(context);

                for (Map.Entry<String, List<EntityInstanceImpl>> entry : entityInstancesManager.getEntitiesMap().entrySet()) {
                    entityInstancesManager.killEntities(entry.getKey(), context.getGrid());

                    //activateKill = false;
                }

                //create history of population for graph in ui
                /*for (Map.Entry<String, EntityDefinitionImpl> entry : world.getEntitiesDefinitionMap().entrySet()) {
                    int currPopulation = entityInstancesManager.getEntitiesMap().get(entry.getKey()).size();
                    //int lastChange = entry.getValue().getPopulationChangesMap().get(entry.getValue().getLastChange());
                    int lastInd = entry.getValue().getPopulationChangesList().size()-1;
                    int lastChange = entry.getValue().getPopulationChangesList().get(lastInd).getValue();
                    //if (lastChange != currPopulation || currentTick % 10000 == 0) {
                    if (lastChange != currPopulation) {
                        entry.getValue().setLastChange(currentTick);
                        //entry.getValue().getPopulationChangesMap().put(currentTick, currPopulation);
                        entry.getValue().getPopulationChangesList().add(new Pair<>(currentTick, currPopulation));
                    }
                    //entry.getValue().setLastChange(currentTick);
                }*/
                for (Map.Entry<String, List<Pair<Integer, Integer>>> entry : populationChangesList.entrySet()) {

                    int currPopulation = entityInstancesManager.getEntitiesMap().get(entry.getKey()).size();
                    //int lastChange = entry.getValue().getPopulationChangesMap().get(entry.getValue().getLastChange());
                    int lastInd = entry.getValue().size()-1;
                    int lastChange = entry.getValue().get(lastInd).getValue();
                    //if (lastChange != currPopulation || currentTick % 10000 == 0) {
                    if (lastChange != currPopulation) {
                        lastChange = currentTick;

                        //entry.getValue().getPopulationChangesMap().put(currentTick, currPopulation);
                        entry.getValue().add(new Pair<>(currentTick, currPopulation));
                    }
                    //entry.getValue().setLastChange(currentTick);
                }

                currentTick++;
            }
       } catch (Exception e){
            System.out.println("in while loop exception of the simulation it self");
            status = Status.FINISHED;
            errorMessage = e.getMessage();
        }

        for (Map.Entry<String, List<EntityInstanceImpl>> entry : entityInstancesManager.getEntitiesMap().entrySet()) {
            //world.getEntitiesDefinitionMap().get(entry.getKey()).getPopulationChangesMap().put(currentTick, entry.getValue().size());
            populationChangesList.get(entry.getKey()).add(new Pair<>(currentTick,entry.getValue().size()));
            //world.getEntitiesDefinitionMap().get(entry.getKey()).getPopulationChangesList().add(new Pair<>(currentTick,entry.getValue().size()));
            //world.getEntitiesDefinitionMap().get(entry.getKey()).getPopulationChangesList().add(new Pair<>(currentTick,entry.getValue().size()));
        }
    }

    public int getCurrentTick(){
        return currentTick;
    }

    public void setTerminationByUser(){
        terminate.setTerminatedByTicks(false);
        terminate.setTerminatedByUser(true);
    }

    //true stands for ending simulation
    public boolean checkIfNeedToTerminate(long endTime, long startTime){
        boolean res=false;

        if(status.equals(Status.STOP) || status.equals(Status.FINISHED)) {
            res = true;
        }

        //ticks and seconds are not initialized do nothing
        else if(!terminate.getTicksCondition() && !terminate.getSecondsCondition());

        //ticks are not initialized
        else if(!terminate.getTicksCondition()) {
            //terminate.setTerminatedByTicks(false);
            if(System.currentTimeMillis() > endTime){
                status = Status.FINISHED;
                res = true;
            }
        }
        //seconds are not initialized
        else if(!terminate.getSecondsCondition()) {
            if(currentTick > world.getTerminateConditions().getTicksAmount()){
                status = Status.FINISHED;
               // terminate.setTerminatedByUser(false);
                terminate.setTerminatedByTicks(true);
                res = true;
            }
        }

        else if((System.currentTimeMillis() > endTime) && (currentTick<terminate.getTicksAmount())) {
            //terminate.setTerminatedByTicks(false);
            //terminate.setTerminatedByUser(false);
            res = true;
        }
        else if((System.currentTimeMillis() < endTime) &&
                (currentTick>world.getTerminateConditions().getTicksAmount())){
            terminate.setTerminatedByTicks(true);
            //terminate.setTerminatedByUser(false);
            return true;
        }
        return res;
    }
    public String getTimeAndDate(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        return currentDateTime.format(formatter);
    }

    @Override
    public EntityInstancesManagerImpl getEntityInstancesManager() {
        return entityInstancesManager;
    }

    @Override
    public void addEntityInstance(EntityInstanceImpl entityInstance) {
        entityInstancesManager.getEntitiesMap().get(entityInstance.getEntityDefinition().getName()).add(entityInstance);
    }

}
