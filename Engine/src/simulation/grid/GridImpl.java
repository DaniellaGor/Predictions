package simulation.grid;

import execution.instances.entity.EntityInstanceImpl;
import simulation.grid.directions.Directions;
import simulation.point.PointImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridImpl implements Grid{

    private int rows;
    private int cols;
    private EntityInstanceImpl[][] positionGrid;

    public GridImpl(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        positionGrid = new EntityInstanceImpl[rows][cols];
    }
    @Override
    public void setStartingPosition(EntityInstanceImpl entityInstance) {
        PointImpl newPoint = new PointImpl();
        newPoint.randomPosition(rows, cols);
        checkBoundaries(newPoint);
        while(positionGrid[newPoint.getX()][newPoint.getY()] != null)
            newPoint.randomPosition(rows, cols);

        entityInstance.setPosition(newPoint);
        positionGrid[newPoint.getX()][newPoint.getY()] = entityInstance;
    }

    public void setPosition(PointImpl position, EntityInstanceImpl entityInstance){
        positionGrid[position.getX()][position.getY()] = entityInstance;
        if(entityInstance != null)
            entityInstance.setPosition(position);
    }

    @Override
    public void checkBoundaries(PointImpl point) {
        int num;
        if(point.getX() >= rows) {
            point.setX(0);
        }
        else if(point.getX() <0)
            point.setX(rows-1);

        if(point.getY() >= cols) {
            point.setY(0);
        }
        else if(point.getY() < 0)
            point.setY(cols-1);
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public EntityInstanceImpl getEntityInPoint(int row, int col) {
        return positionGrid[row][col];
    }

    public void moveEntity(EntityInstanceImpl entityInstance){
        //int newDirectionInteger = direction.ordinal();
        Random random = new Random();
        int newDirection = random.nextInt(4);
        Directions direction = Directions.UP;

        //set random direction
        direction = direction.getDirection(newDirection);
        //try to move
        PointImpl tempPosition = new PointImpl(entityInstance.getPosition().getX(), entityInstance.getPosition().getY());
        tempPosition.move(direction);
        checkBoundaries(tempPosition);

        int firstTryDirection = direction.ordinal();
        if(positionGrid[tempPosition.getX()][tempPosition.getY()] == null) {
            //remove from grid
            positionGrid[entityInstance.getPosition().getX()][entityInstance.getPosition().getY()] = null;
            //move entity
            entityInstance.getPosition().setPoint(tempPosition);
            //set on grid
            positionGrid[entityInstance.getPosition().getX()][entityInstance.getPosition().getY()] = entityInstance;
        }
        else { //this position is taken
            Directions currDirection = direction;
            tempPosition.setPoint(entityInstance.getPosition());
            List<Directions> validDirectionsList = new ArrayList<>();
            //gather all valid positions
            for(int i=0; i<4; i++) {
                if(i!=firstTryDirection) {
                    currDirection = currDirection.getDirection(i);
                    tempPosition.move(currDirection);
                    checkBoundaries(tempPosition);
                    if (positionGrid[tempPosition.getX()][tempPosition.getY()] == null)
                        validDirectionsList.add(currDirection);

                    tempPosition.setPoint(entityInstance.getPosition());
                }
            }

            //if list is not empty - move to random direction
            if(!validDirectionsList.isEmpty()){
                //int size = validDirectionsList.size();
                //int directionInteger = random.nextInt(size);
                //currDirection = currDirection.getDirection(directionInteger);
                //PointImpl tempPos = entityInstance.getPosition();
                //tempPos.move(validDirectionsList.get(0));
                positionGrid[entityInstance.getPosition().getX()][entityInstance.getPosition().getY()] = null;
                entityInstance.getPosition().move(validDirectionsList.get(0));
                checkBoundaries(entityInstance.getPosition());
                positionGrid[entityInstance.getPosition().getX()][entityInstance.getPosition().getY()] = entityInstance;
                //entityInstance.setPosition(currPosition);
            }
        }

    }


   /* public void move(Directions direction, EntityInstanceImpl entityInstance, PointImpl currPosition){
        //int newDirectionInteger = direction.ordinal();
        PointImpl tempPosition = currPosition;
        Directions newDirection = direction;
        List<Directions> validDirectionsList = new ArrayList<>();
        for(int i=0; i<4; i++){
            newDirection = newDirection.getDirection(i);
            tempPosition.move(newDirection);
            checkBoundaries(tempPosition);
            if(positionGrid[tempPosition.getX()][tempPosition.getY()] == null)
                validDirectionsList.add(newDirection);

            tempPosition = currPosition;
        }

        if(!validDirectionsList.isEmpty()){
            Random random = new Random();
            int size = validDirectionsList.size();
            int directionInteger = random.nextInt(size);
            newDirection = newDirection.getDirection(directionInteger);
            currPosition.move(newDirection);
            positionGrid[currPosition.getX()][currPosition.getY()] = entityInstance;
            entityInstance.setPosition(currPosition);
        }
    }*/

    /*public boolean moveAndCheck(Directions direction, EntityInstanceImpl entityInstance, PointImpl currPoint, boolean[] checkedDirection){

        Directions newDirection = direction;
        Random random = new Random();
        int num=direction.ordinal();

        if(checkedDirection[Directions.UP.ordinal()] && checkedDirection[Directions.DOWN.ordinal()] &&
                checkedDirection[Directions.RIGHT.ordinal()] && checkedDirection[Directions.LEFT.ordinal()])
            return false;

        else {
            if (direction.equals(Directions.UP) && !checkedDirection[Directions.UP.ordinal()]){
                if(positionGrid[currPoint.getX()][currPoint.getY()] != null) {
                    checkedDirection[Directions.UP.ordinal()] = true;
                    while (num == Directions.UP.ordinal()) {// && !valid) {
                        num = random.nextInt(4);
                    }
                    newDirection = newDirection.getDirection(num);
                    return moveAndCheck(newDirection, entityInstance, currPoint, checkedDirection);
                }
                else {
                    PointImpl point = new PointImpl(currPoint.getX(),currPoint.getY());
                    setPosition(point, entityInstance);
                    return true;
                }
            } else if (direction.equals(Directions.DOWN) && !checkedDirection[Directions.DOWN.ordinal()]){
                if(positionGrid[currPoint.getX()][currPoint.getY()] != null) {
                    checkedDirection[Directions.DOWN.ordinal()] = true;
                    while (num == Directions.DOWN.ordinal()) {// && !valid) {
                        num = random.nextInt(4);
                    }
                    newDirection = newDirection.getDirection(num);
                    return moveAndCheck(newDirection, entityInstance, currPoint, checkedDirection);
                }
                else {
                    PointImpl point = new PointImpl(currPoint.getX(),currPoint.getY());
                    setPosition(point, entityInstance);
                    return true;
                }
            }
            else if (direction.equals(Directions.RIGHT) && !checkedDirection[Directions.RIGHT.ordinal()]){
                if(positionGrid[currPoint.getX()][currPoint.getY()] != null) {
                    checkedDirection[Directions.RIGHT.ordinal()] = true;
                    while (num == Directions.RIGHT.ordinal()) {// && !valid) {
                        num = random.nextInt(4);
                    }
                    newDirection = newDirection.getDirection(num);
                    return moveAndCheck(newDirection, entityInstance, currPoint, checkedDirection);
                }
                else {
                    PointImpl point = new PointImpl(currPoint.getX(),currPoint.getY());
                    setPosition(point, entityInstance);
                    return true;
                }
            }
            else if (direction.equals(Directions.LEFT) && !checkedDirection[Directions.LEFT.ordinal()]){
                if(positionGrid[currPoint.getX()][currPoint.getY()] != null) {
                    checkedDirection[Directions.LEFT.ordinal()] = true;
                    while (num == Directions.LEFT.ordinal()) {// && !valid) {
                        num = random.nextInt(4);
                    }
                    newDirection = newDirection.getDirection(num);
                    return moveAndCheck(newDirection, entityInstance, currPoint, checkedDirection);
                }
                else {
                    PointImpl point = new PointImpl(currPoint.getX(),currPoint.getY());
                    setPosition(point, entityInstance);
                    return true;
                }
            }

        }
        return false;
    }*/

    /*public void moveEntity(EntityInstanceImpl currEntity){
        Random random = new Random();
        int newDirection = random.nextInt(4);
        Directions direction = Directions.UP;
        direction = direction.getDirection(newDirection);
        move(direction, currEntity);

        //newPosition.move(direction);
        //checkBoundaries(currEntity.getPosition());
        //sending to check new position - if it is valid - updates in function
        //if not valid - nothing changes
        //moveAndCheck(direction, currEntity, newPosition, checkedPositions);
    }
*/
    /*public void moveEntity(){
        Random random = new Random();
        boolean[] checkedPositions = {false};
        EntityInstanceImpl currEntity = positionGrid[indX][indY];
        PointImpl currPosition = currEntity.getPosition();
        move(direction, currEntity);

        //newPosition.move(direction);
       //checkBoundaries(currEntity.getPosition());
        //sending to check new position - if it is valid - updates in function
        //if not valid - nothing changes
        //moveAndCheck(direction, currEntity, newPosition, checkedPositions);
    }*/

    /*public void moveEntity(int indX, int indY, Directions direction){
        Random random = new Random();
        boolean[] checkedPositions = {false};
        EntityInstanceImpl currEntity = positionGrid[indX][indY];
        PointImpl currPosition = currEntity.getPosition();
        move(direction, currEntity, currPosition);

        //newPosition.move(direction);
        //checkBoundaries(currEntity.getPosition());
        //sending to check new position - if it is valid - updates in function
        //if not valid - nothing changes
        //moveAndCheck(direction, currEntity, newPosition, checkedPositions);
    }*/
}
