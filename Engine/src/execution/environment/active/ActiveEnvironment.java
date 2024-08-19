package execution.environment.active;

import definition.properties.PropertyDefinitionImpl;
import execution.instances.property.PropertyInstanceImpl;

import java.util.Map;

public interface ActiveEnvironment {
    PropertyInstanceImpl getProperty(String propertyName);
    void addProperty(PropertyDefinitionImpl propertyDefinition);
    Map<String, PropertyInstanceImpl> getEnvVarsMap();
}
