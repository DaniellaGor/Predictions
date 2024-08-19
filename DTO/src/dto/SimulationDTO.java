package dto;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class SimulationDTO {

    private int id;
    private String date;
    private boolean isPaused;
    private WorldDTO worldDTO;
    private ActiveEnvironmentDTO activeEnvironmentDTO;
    private Map<String, List<EntityInstanceDTO>> entitiesMap;
    private int currentTick;
    private float currentTimeInSeconds;
    private String errorMessage;
    //private String status;
    private boolean showResultsWasClicked;
    private Map<String,List<Pair<Integer, Integer>>> populationHistoryList;

    private TerminationDTO terminationDTO;
    //private EntityInstancesManagerImpl entityInstancesManager;\
    public SimulationDTO(String date, WorldDTO worldDTO, ActiveEnvironmentDTO activeEnvironmentDTO,
                         Map<String, List<EntityInstanceDTO>> entitiesMap, int id, int currentTick,
                         float currentTimeInSeconds, TerminationDTO terminationDTO, boolean isPaused,
                         String errorMessage, boolean showResultsWasClicked, Map<String, List<Pair<Integer, Integer>>> populationHistoryList){
        this.date = date;
        this.worldDTO = worldDTO;
        this.activeEnvironmentDTO = activeEnvironmentDTO;
        this.entitiesMap = entitiesMap;
        this.id = id;
        this.currentTick = currentTick;
        this.currentTimeInSeconds = currentTimeInSeconds;
        this.terminationDTO = terminationDTO;
        this.isPaused = isPaused;
        this.errorMessage = errorMessage;
        //this.status = status;
        this.showResultsWasClicked = showResultsWasClicked;
        this.populationHistoryList = populationHistoryList;
    }

    public Map<String,List<Pair<Integer, Integer>>> getPopulationHistoryList(){
        return populationHistoryList;
    }

    public int getCurrentTick(){
        return currentTick;
    }

    public int getId(){
        return id;
    }

    /*public String getStatus(){
        return status;
    }*/

    public TerminationDTO getTerminationDTO(){ return terminationDTO;}

    public String getErrorMessage(){ return errorMessage;}

    public boolean getIsPaused(){
        return isPaused;
    }

    public float getCurrentTimeInMillis(){
        return currentTimeInSeconds;
    }

    public String getDate(){
        return date;
    }

    public WorldDTO getWorldDTO() {
        return worldDTO;
    }

    public ActiveEnvironmentDTO getActiveEnvironmentDTO() {
        return activeEnvironmentDTO;
    }

    public Map<String, List<EntityInstanceDTO>> getEntitiesMap() {
        return entitiesMap;
    }
}
