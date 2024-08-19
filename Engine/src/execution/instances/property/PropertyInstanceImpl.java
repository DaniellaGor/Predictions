package execution.instances.property;

import definition.properties.PropertyDefinitionImpl;
import definition.properties.PropertyType;
import dto.PropertyDTO;
import execution.instances.property.changesInValueNode.ChangesInValueNodeImpl;

import java.util.ArrayList;
import java.util.Random;

public class PropertyInstanceImpl implements PropertyInstance {

    private PropertyDefinitionImpl propertyDefinition;
    private ArrayList<ChangesInValueNodeImpl> valueChanges;

    private int lastChangeInValue;
    private int sumOfChanges;
    private int numOfChanges;
    private Object value;

    public PropertyInstanceImpl(PropertyDefinitionImpl propertyDefinition, int currTick){
        this.propertyDefinition = propertyDefinition;
        valueChanges = new ArrayList<ChangesInValueNodeImpl>();
        createValue(propertyDefinition, currTick);
        lastChangeInValue = 0;
        sumOfChanges = 0;
        numOfChanges = 0;

        //valueChanges.add(new ChangesInValueNodeImpl(0, value));
    }
    public void createValue(PropertyDefinitionImpl propertyDefinition, int currTick) {
        if (propertyDefinition.getIfRandomInit())
            //this.value = randomValue(propertyDefinition.getRangeTop(), propertyDefinition.getRangeBottom());
            setValue(randomValue(propertyDefinition.getRangeTop(), propertyDefinition.getRangeBottom()), currTick);
        else  setValue(propertyDefinition.getValue(), currTick);
            //this.value = propertyDefinition.getValue();
    }

    public Object getValue(){
        return value;
    }

    /*public Object setRandomPropertyValue(){
            return getType().randomValue(getRangeTop(), getRangeBottom());
    }*/
    public void setValue(Object newValue, int curr){
        if(value == null){ //value changes list is empty
            //valueChanges.add(new ChangesInValueNodeImpl(curr, newValue));
            //value = newValue;
        }
        else if(!value.equals(newValue)) {
            //valueChanges.get(valueChanges.size() - 1).setEnd(curr);
           // valueChanges.add(new ChangesInValueNodeImpl(curr, newValue));
            //value = newValue;
            sumOfChanges += (curr - lastChangeInValue);
            lastChangeInValue = curr;
            numOfChanges++;
        }

        value = newValue;
    }

   /* public void setValue(Object newValue, int curr){
        if(value == null){ //value changes list is empty
            valueChanges.add(new ChangesInValueNodeImpl(curr, newValue));
            //value = newValue;
        }
        else if(!value.equals(newValue)) {
            valueChanges.get(valueChanges.size() - 1).setEnd(curr);
            valueChanges.add(new ChangesInValueNodeImpl(curr, newValue));
            //value = newValue;
        }

        value = newValue;
    }*/

    /*public ArrayList<ChangesInValueNodeImpl> getValueChangesArray(){
        return valueChanges;
    }*/

    public String getName(){
        return propertyDefinition.getName();
    }

    public Object randomValue(float rangeTop, float rangeBottom) {
        Object randomObject = null;
        Random random = new Random();
        Object type = propertyDefinition.getType();
        if (type.equals(PropertyType.DECIMAL)) {
            int randomInt = random.nextInt((int) rangeTop - (int) rangeBottom) + (int) rangeBottom;
            randomObject = randomInt;
        } else if (type.equals(PropertyType.FLOAT)) {
            float randomFloat = random.nextFloat() * (rangeTop - rangeBottom) + rangeBottom;
            randomObject = randomFloat;
        } else if (type.equals(PropertyType.BOOLEAN)) {
            boolean randomBoolean = random.nextBoolean();
            randomObject = randomBoolean;
        } else if(type.equals(PropertyType.STRING)){
            int randomLength = random.nextInt(51) + 1;
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ?!_-,.()";
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < randomLength; i++) {
                char c = characters.charAt(random.nextInt(characters.length()));
                randomString.append(c);
            }
            randomObject = randomString;
        }

        return randomObject;

    }

    /*public ArrayList<ChangesInValueDTO> createChangesInValueDTOList(){
        ArrayList<ChangesInValueDTO> dtoList = new ArrayList<>();
        if(!valueChanges.isEmpty()) {
            for (ChangesInValueNodeImpl curr : valueChanges) {
                dtoList.add(new ChangesInValueDTO(curr.getStart(), curr.getEnd(), curr.getValueFromArray()));
            }
        }
        return dtoList;
    }*/
    public void setLastChangeInValue(int lastChange){
        lastChangeInValue = lastChange;
    }

    public int getSumOfChanges(){ return sumOfChanges;}
    public int getNumOfChanges(){ return numOfChanges;}
    public int getLastChangeInValue(){ return lastChangeInValue;}
    public PropertyDTO createPropertyDTOFromPropertyInstance(boolean isFinished){
        //if(isFinished)
            /*return new PropertyDTO(propertyDefinition.getName(),
                    propertyDefinition.getType().name(),
                    propertyDefinition.getRangeTop(), propertyDefinition.getRangeBottom(),
                    propertyDefinition.getIfRandomInit(), value, createChangesInValueDTOList());*/
        //else
        /*return new PropertyDTO(propertyDefinition.getName(),
                propertyDefinition.getType().name(),
                propertyDefinition.getRangeTop(), propertyDefinition.getRangeBottom(),
                propertyDefinition.getIfRandomInit(), value);*/
        return new PropertyDTO(propertyDefinition.getName(),
                propertyDefinition.getType().name(),
                propertyDefinition.getRangeTop(), propertyDefinition.getRangeBottom(),
                propertyDefinition.getIfRandomInit(), value, lastChangeInValue, sumOfChanges, numOfChanges);
    }
    public PropertyDefinitionImpl getPropertyDefinition(){
        return propertyDefinition;
    }
}
