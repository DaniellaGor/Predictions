package execution.environment.active;

import definition.properties.PropertyDefinitionImpl;
import dto.ActiveEnvironmentDTO;
import dto.PropertyDTO;
import execution.instances.property.PropertyInstanceImpl;

import java.util.HashMap;
import java.util.Map;

public class ActiveEnvironmentImpl implements ActiveEnvironment {
    Map<String, PropertyInstanceImpl> environmentVariables;

    public ActiveEnvironmentImpl(){
        environmentVariables = new HashMap<>();
    }
    @Override
    public PropertyInstanceImpl getProperty(String propertyName) {
        return environmentVariables.get(propertyName);
    }

    @Override
    public void addProperty(PropertyDefinitionImpl propertyDefinition) {
        boolean saveRandomInitVal = propertyDefinition.getIfRandomInit();
        //value already exists from user, or got already random - do not random again
        if(saveRandomInitVal)
            propertyDefinition.setRandomInit(false);
        PropertyInstanceImpl propertyInstance = new PropertyInstanceImpl(propertyDefinition, 0);
        propertyDefinition.setRandomInit(saveRandomInitVal);
        propertyInstance.getPropertyDefinition().setRandomInit(saveRandomInitVal);
        environmentVariables.put(propertyInstance.getName(), propertyInstance);
    }

    @Override
    public Map<String, PropertyInstanceImpl> getEnvVarsMap() {
        return environmentVariables;
    }

    public ActiveEnvironmentDTO createActiveEnvironmentDTO(boolean isFinished){
        Map<String, PropertyDTO> envPropertyMapDTO = new HashMap<>();
        for(Map.Entry<String, PropertyInstanceImpl> entry: environmentVariables.entrySet()){
            envPropertyMapDTO.put(entry.getKey(), entry.getValue().createPropertyDTOFromPropertyInstance(isFinished));
        }
        return new ActiveEnvironmentDTO(envPropertyMapDTO);
    }
}
