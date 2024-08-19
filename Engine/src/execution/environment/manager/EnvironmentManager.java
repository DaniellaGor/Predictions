package execution.environment.manager;

import definition.properties.PropertyDefinitionImpl;
import execution.environment.active.ActiveEnvironmentImpl;

import java.util.Map;

public interface EnvironmentManager {
    ActiveEnvironmentImpl createEnvironmentActive();
    Map<String, PropertyDefinitionImpl> getEnvDefinitionsMap();
    void addEnvironmentToManager(PropertyDefinitionImpl propertyDefinition);
}
