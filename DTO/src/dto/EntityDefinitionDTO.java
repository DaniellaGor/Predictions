package dto;

import java.util.Map;

public class EntityDefinitionDTO {
    private String name;
    private int population;
    //private List<PropertyDTO> propertiesList;
    private Map<String, PropertyDTO> propertiesMap;
   // private List<Pair<Integer, Integer>> populationHistoryList;
    //private Map<Integer, Integer> populationHistoryMap;

   /* public EntityDefinitionDTO(String name, Map<String, PropertyDTO> propertiesMap, Map<Integer, Integer> populationHistoryMap){
        this.name = name;
        this.propertiesMap = propertiesMap;
        this.population = population;
        this.populationHistoryMap = populationHistoryMap;
    }*/

    public EntityDefinitionDTO(String name, int population, Map<String, PropertyDTO> propertiesMap){
        this.name = name;
        this.propertiesMap = propertiesMap;
        this.population = population;
        //this.populationHistoryList = populationHistoryList;
    }

    /*public EntityDefinitionDTO(String name, int population, Map<String, PropertyDTO> propertiesMap, Map<Integer, Integer> populationHistoryMap){
        this.name = name;
        this.propertiesMap = propertiesMap;
        this.population = population;
        this.populationHistoryMap = populationHistoryMap;
    }*/

   /* public List<Pair<Integer, Integer>> getPopulationHistoryList(){
        return populationHistoryList;
    }*/
    /*public Map<Integer, Integer> getPopulationHistoryMap(){
        return populationHistoryMap;
    }*/

   /* public void setPopulation(int population){
        this.population = population;
    }*/

    public String getName(){ return name;}

    public int getPopulation(){ return population;}

    public Map<String, PropertyDTO> getPropertiesMap(){ return propertiesMap;}
}
