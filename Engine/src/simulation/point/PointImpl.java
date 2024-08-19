package simulation.point;
import simulation.grid.directions.Directions;

import java.util.Random;

public class PointImpl implements Point{
    private int x;
    private int y;

    public PointImpl(){}

    public PointImpl(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public void setPoint(PointImpl point){
        x = point.x;
        y = point.y;
    }

    public void randomPosition(int rows, int cols){
        Random random = new Random();
        x = random.nextInt(rows);
        y = random.nextInt(cols);
    }

    @Override
    public void move(Directions direction) {
        if(direction.equals(Directions.UP))
            y++;
        else if(direction.equals(Directions.DOWN))
            y--;
        else if(direction.equals(Directions.LEFT))
            x--;
        else x++;
    }
}
