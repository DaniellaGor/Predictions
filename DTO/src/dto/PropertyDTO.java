package dto;

import java.util.ArrayList;

public class PropertyDTO {
    private String name;
    private String type;
    private float rangeTop;
    private float rangeBottom;
    private boolean isRandomInitialization;
    private Object value;
    private ArrayList<ChangesInValueDTO> changesInValueDTOList;
    private int lastChange;
    private  int numOfChanges;
    private int sumOfChanges;

    /*public PropertyDTO(String name, String type, float rangeTop, float rangeBottom, boolean isRandom, Object value,
                       ArrayList<ChangesInValueDTO> changesInValueDTOList){
        this.name = name;
        this. type = type;
        this.rangeTop = rangeTop;
        this.rangeBottom = rangeBottom;
        this.isRandomInitialization = isRandom;
        this.value = value;
        this.changesInValueDTOList = changesInValueDTOList;
    }*/

    public PropertyDTO(String name, String type, float rangeTop, float rangeBottom, boolean isRandom, Object value,
                       int lastChange, int sumOfChanges, int numOfChanges){
        this.name = name;
        this. type = type;
        this.rangeTop = rangeTop;
        this.rangeBottom = rangeBottom;
        this.isRandomInitialization = isRandom;
        this.value = value;
        //this.changesInValueDTOList = changesInValueDTOList;
        this.lastChange = lastChange;
        this.sumOfChanges = sumOfChanges;
        this.numOfChanges = numOfChanges;
    }

    public PropertyDTO(String name, String type, float rangeTop, float rangeBottom, boolean isRandom, Object value){
        this.name = name;
        this. type = type;
        this.rangeTop = rangeTop;
        this.rangeBottom = rangeBottom;
        this.isRandomInitialization = isRandom;
        this.value = value;
        this.changesInValueDTOList = null;
    }

    /*public ArrayList<ChangesInValueDTO> getChangesInValueDTOList(){
        return changesInValueDTOList;
    }*/
    public int getLastChange(){ return lastChange;}

    public int getNumOfChanges(){ return numOfChanges;}
    public int getSumOfChanges(){ return sumOfChanges;}

    public String getName(){return name;}

    public String getType(){return type;}
    public float getRangeTop(){return rangeTop;}
    public float getRangeBottom(){return rangeBottom;}
    public boolean getIsRandomInit(){return isRandomInitialization;}

    public Object getValue(){return value;}

}
