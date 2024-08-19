package simulation.grid;

import execution.instances.entity.EntityInstanceImpl;
import simulation.point.PointImpl;

public interface Grid {
    void setStartingPosition(EntityInstanceImpl entityInstance);
    void checkBoundaries(PointImpl point);
    int getRows();
    int getCols();

    EntityInstanceImpl getEntityInPoint(int row, int col);
}
