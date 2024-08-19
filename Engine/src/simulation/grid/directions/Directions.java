package simulation.grid.directions;

public enum Directions {
    UP, DOWN, LEFT, RIGHT;

    public Directions getDirection(int num){
        if(num == UP.ordinal())
            return UP;
        else if(num == DOWN.ordinal())
            return DOWN;
        else if(num == RIGHT.ordinal())
            return RIGHT;
        else return LEFT;
    }
}
