package definition.entities;

import definition.properties.PropertyDefinitionImpl;
import dto.EntityDefinitionDTO;
import dto.PropertyDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityDefinitionImpl implements EntityDefinition {
    private String name = "";
    private int population;

    //private List<Integer> populationChangesList;
    //private List<Pair<Integer, Integer>> populationChangesList;
    //private Map<Integer, Integer> populationChangesMap;
    private int lastChange;


    private List<PropertyDefinitionImpl> propertyDefinitionList;
    public EntityDefinitionImpl(String name){
        this.name = name;
        propertyDefinitionList = new ArrayList<>();
        //populationChangesMap = new LinkedHashMap<>();
        //populationChangesList = new ArrayList<>();
        lastChange = 0;
    }

    public int getLastChange(){
        return lastChange;
    }

    public void setLastChange(int num){
        lastChange = num;
    }
    public EntityDefinitionImpl(String name, int population){
        this.name = name;
        propertyDefinitionList = new ArrayList<>();
    }

    public void setPopulation(int population){
        this.population = population;
    }

    /*public Map<Integer, Integer> getPopulationChangesMap(){
        return populationChangesMap;
    }*/

    /*public List<Pair<Integer, Integer>> getPopulationChangesList(){
        return populationChangesList;
    }*/

    /*public void addPopulationChangesToList(int tick, int change){
        populationChangesMap.put(tick, change);
    }*/

    public EntityDefinitionDTO createEntityDefinitionDTO(){
        Map<String,PropertyDTO> propertyDTOMap = new HashMap<>();
        for(PropertyDefinitionImpl propertyDefinition: propertyDefinitionList){
            propertyDTOMap.put(propertyDefinition.getName(), propertyDefinition.createPropertyDefinitionDTO());
        }
            //return new EntityDefinitionDTO(name, population, propertyDTOMap, populationChangesMap);
            return new EntityDefinitionDTO(name, population, propertyDTOMap);
    }

   /* public EntityDefinitionDTO createEntityDefinitionDTO(int population){
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        for(PropertyDefinitionImpl propertyDefinition: propertyDefinitionList){
            propertyDTOList.add(propertyDefinition.createPropertyDTO());
        }
            return new EntityDefinitionDTO(name,population, propertyDTOList, populationChangesMap);
    }*/

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getPopulation(){
        return population;
    }

    @Override
    public List<PropertyDefinitionImpl> getProperties() {
        return propertyDefinitionList;
    }

    @Override
    public void addProperty(PropertyDefinitionImpl propertyDefinition) {
        propertyDefinitionList.add(propertyDefinition);
    }
}
