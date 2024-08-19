package simulation.point;

import simulation.grid.directions.Directions;

public interface Point {
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
    void move(Directions direction);

}
