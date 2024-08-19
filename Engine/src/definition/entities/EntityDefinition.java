package definition.entities;

import definition.properties.PropertyDefinitionImpl;

import java.util.List;

public interface EntityDefinition {
    String getName();
    int getPopulation();

    List<PropertyDefinitionImpl> getProperties();

    void addProperty(PropertyDefinitionImpl propertyDefinition);
}
