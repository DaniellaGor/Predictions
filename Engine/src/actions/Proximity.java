package actions;

import actions.context.ContextImpl;
import definition.properties.PropertyType;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;
import simulation.point.PointImpl;

import java.util.List;

public class Proximity extends Actions{
    private String sourceEntity;
    private String targetEntity;
    private String depth;
    private List<Actions> actionsList;

    public Proximity(String sourceEntity, String targetEntity, String depth, List<Actions> actionsList){
        super("", "", "Proximity", null);
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.depth = depth;
        this.actionsList = actionsList;
    }

    public String getSourceEntity(){ return sourceEntity;}

    public String getTargetEntity(){ return targetEntity;}
    public String getDepth(){ return depth;}

    public List<Actions> getActionsList(){ return actionsList;}

    @Override
    public void applyAction(ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        EntityInstanceImpl currentEntity = null;

        if (context.getPrimaryEntity().getEntityDefinition().getName().equals(sourceEntity))
            currentEntity = context.getPrimaryEntity();
        else if (context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(sourceEntity))
                currentEntity = context.getSecondaryEntity();
        }

        if (currentEntity != null) {
            Object checkDepth = checkExpression(depth, PropertyType.DECIMAL, currentEntity, context);
            float decimalDepth;
            if(checkDepth instanceof String)
                decimalDepth = Float.parseFloat((String)checkDepth);
            else {
                /*if(checkDepth instanceof Integer) {
                    checkDepth = (int) checkDepth;
                    decimalDepth = (float)checkDepth;
                }*/
                if(checkDepth instanceof  Integer)
                    decimalDepth= ((Integer) checkDepth).floatValue();
                else
                    decimalDepth = (float)checkDepth;
                //else decimalDepth = checkDepth;
            }
            //else decimalDepth = (float) checkDepth;

            EntityInstanceImpl targetEntity = checkPointProximity(currentEntity.getPosition(), decimalDepth, context);
            if (targetEntity != null && !targetEntity.equals(currentEntity)) {
                for (Actions action : actionsList) {
                    ContextImpl newContext = new ContextImpl(currentEntity, targetEntity, context.getEntityInstancesManager(),
                            context.getActiveEnvironment(), context.getGrid(),
                            context.getEntityDefinitionMap(), context.getCurrentTick(), context.getReplaceEntities());
                    action.applyAction(newContext);
                }
            }
        }
    }

    public EntityInstanceImpl checkPointProximity(PointImpl source, float proximityCircle, ContextImpl context) {
        int maxX = context.getGrid().getRows();
        int maxY = context.getGrid().getCols();

        //go over all circles until max circle
        for (int radius = 1; radius <= proximityCircle; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (dx * dx + dy * dy <= radius * radius) {
                        /*int nx = x + dx;
                        int ny = y + dy;*/
                        int nx = (source.getX() % maxX) + dx;
                        int ny = source.getY() % maxY + dy;

                        // Check if the coordinates are within the grid boundaries
                        if (nx >= 0 && nx < maxX && ny >= 0 && ny < maxY) {
                            EntityInstanceImpl ret = context.getGrid().getEntityInPoint(nx, ny);
                            if (ret != null && ret.getEntityDefinition().getName().equalsIgnoreCase(targetEntity)) {
                                return ret;
                            }
                        }
                    }
                }

            }

        }
        return null;
    }

    @Override
    public String getActionName() {
        return actionName;
    }
}
