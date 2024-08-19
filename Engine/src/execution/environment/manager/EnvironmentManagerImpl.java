package execution.environment.manager;

import definition.properties.PropertyDefinitionImpl;
import dto.EnvironmentManagerDTO;
import dto.PropertyDTO;
import execution.environment.active.ActiveEnvironmentImpl;

import java.util.*;

public class EnvironmentManagerImpl implements EnvironmentManager {
    Map<String, PropertyDefinitionImpl> environmentVarsDefenitionMap;

    public EnvironmentManagerImpl(){
        environmentVarsDefenitionMap = new HashMap<>();
    }
    @Override
    public ActiveEnvironmentImpl createEnvironmentActive() {
        return new ActiveEnvironmentImpl();
    }

    public EnvironmentManagerDTO createEnvironmentManagerDTO(){
        Map<String, PropertyDTO> envPropertiesDTO = new HashMap<>();
        for(Map.Entry<String, PropertyDefinitionImpl> entry: environmentVarsDefenitionMap.entrySet()){
            envPropertiesDTO.put(entry.getKey(),entry.getValue().createPropertyDefinitionDTO());
        }
        return new EnvironmentManagerDTO(envPropertiesDTO);
    }
    @Override
    public Map<String, PropertyDefinitionImpl> getEnvDefinitionsMap() {
        return environmentVarsDefenitionMap;
    }

    @Override
    public void addEnvironmentToManager(PropertyDefinitionImpl propertyDefinition) {
        environmentVarsDefenitionMap.put(propertyDefinition.getName(), propertyDefinition);
    }
}
