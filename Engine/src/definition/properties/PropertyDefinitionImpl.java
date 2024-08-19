package definition.properties;

import dto.PropertyDTO;

public class PropertyDefinitionImpl implements PropertyDefinition {
    private String name;
    private PropertyType type;
    private float rangeTop;
    private float rangeBottom;
    private boolean isRandomInitialization;

    private Object initValue;

    public PropertyDefinitionImpl(String name, PropertyType type, float rangeTop, float rangeBottom, boolean isRandomInitialization, Object initValue){

        this.name = name;
        this.type = type;
        this.rangeTop = rangeTop;
        this.rangeBottom = rangeBottom;
        this.isRandomInitialization = isRandomInitialization;
        this.initValue = initValue;
    }

    public String getName(){
        return name;
    }

    public PropertyType getType(){
        return type;
    }

    public float getRangeTop(){
        return rangeTop;
    }

    public float getRangeBottom(){
        return rangeBottom;
    }

    public boolean getIfRandomInit(){
        return isRandomInitialization;
    }

    public PropertyDTO createPropertyDefinitionDTO(){
        String propertyType = type.name();
        return new PropertyDTO(name, propertyType, rangeTop,
                rangeBottom, isRandomInitialization, initValue);
    }
    @Override
    public void setRandomInit(boolean set) {
        isRandomInitialization = set;
    }

    @Override
    public void setValue(Object value) {
        initValue = value;
    }

    @Override
    public Object getValue() {
        return initValue;
    }


}
