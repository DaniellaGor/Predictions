package definition.properties;

public interface PropertyDefinition {
    String getName();
    PropertyType getType();

    float getRangeTop();

    float getRangeBottom();

    boolean getIfRandomInit();

    void setRandomInit(boolean set);

    void setValue(Object value);

    Object getValue();

}
