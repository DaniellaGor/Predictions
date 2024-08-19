package dto;

public class ChangesInValueDTO {
    private int start;
    private int end;
    private Object value;

    public ChangesInValueDTO(int start, int end, Object value){
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }
    /*
    public Object getValue(){
        return value;
    }*/
}
