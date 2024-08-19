package execution.instances.property;

import definition.properties.PropertyDefinitionImpl;

public interface PropertyInstance {
    void createValue(PropertyDefinitionImpl propertyDefinition, int currTick);
    Object getValue();
    void setValue(Object newValue, int curr);
    String getName();

}
