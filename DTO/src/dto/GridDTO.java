package dto;

public class GridDTO {
    private int rows;
    private int cols;
    //the grid it self?
    public GridDTO(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
    }
    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }
}
