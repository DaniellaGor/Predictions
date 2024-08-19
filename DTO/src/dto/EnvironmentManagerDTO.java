package dto;

import java.util.Map;

public class EnvironmentManagerDTO {
    private Map<String, PropertyDTO> environmentVarList;

    public EnvironmentManagerDTO(Map<String, PropertyDTO> variablesMap){
        environmentVarList = variablesMap;
    }

    public Map<String, PropertyDTO> getEnvironmentVarMap(){ return environmentVarList;}
}
