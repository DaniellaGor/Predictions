package dto;

import java.util.Map;

public class ActiveEnvironmentDTO {
    private Map<String, PropertyDTO> environmentVariables;
    public ActiveEnvironmentDTO(Map<String, PropertyDTO> environmentVariables){
        this.environmentVariables = environmentVariables;
    }

    public Map<String, PropertyDTO> getEnvironmentVariables(){ return environmentVariables;}
}
