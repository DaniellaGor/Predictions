package dto;

import java.util.List;
import java.util.Map;

public class WorldDTO {
    //private List<EntityDefinitionDTO> entitiesList;
    private Map<String, EntityDefinitionDTO> entitesMap;
    private EnvironmentManagerDTO environmentManager;
    private List<RulesDTO> rulesList;
    private TerminationDTO termination;
    private int numOfThreads;

 /*   public WorldDTO(List<EntityDefinitionDTO> entitiesList, EnvironmentManagerDTO environmentManager,
                    List<RulesDTO> rulesList, TerminationDTO termination, int numOfThreads){
        this.entitiesList = entitiesList;
        this.environmentManager = environmentManager;
        this.rulesList = rulesList;
        this.termination = termination;
        this.numOfThreads = numOfThreads;
    }*/
    public WorldDTO(Map<String, EntityDefinitionDTO> entitiesMap, EnvironmentManagerDTO environmentManager,
                        List<RulesDTO> rulesList, TerminationDTO termination, int numOfThreads){
            this.entitesMap = entitiesMap;
            this.environmentManager = environmentManager;
            this.rulesList = rulesList;
            this.termination = termination;
            this.numOfThreads = numOfThreads;
        }

    public int getNumOfThreads(){
        return numOfThreads;
    }
    //public List<EntityDefinitionDTO> getEntitiesList(){ return entitiesList;}
    public Map<String, EntityDefinitionDTO> getEntitesMap(){
        return entitesMap;
    }
    public EnvironmentManagerDTO getEnvironmentManager(){ return environmentManager;}
    public List<RulesDTO> getRulesList(){ return rulesList;}
    public TerminationDTO getTermination(){return termination;}
}
