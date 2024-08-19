package actions.conditions;

import actions.Actions;
import actions.conditions.thenAndElse.ThenElseImpl;
import actions.context.ContextImpl;
import actions.selection.Selection;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultipleConditions extends Conditions{
    private List<Conditions> conditions;
    private String logical;
    private boolean res;

    public MultipleConditions(String entityName, String secondaryEntity, Selection selection, ThenElseImpl thenOp, ThenElseImpl elseOp, String logical){
        super(entityName, secondaryEntity, selection,"multiple", thenOp, elseOp);
        this.logical = logical;
        conditions = new ArrayList<>();
        res = true;
    }

    @Override
    public void setRes(boolean res) {
        this.res = res;
    }

    @Override
    public boolean getRes() {
        return res;
    }

    public String getLogical(){ return logical;}

    @Override
    public void applyAction(ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException {
        EntityInstanceImpl currentEntity = null;
        if (context.getPrimaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
            currentEntity = context.getPrimaryEntity();
        else if (context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
                currentEntity = context.getSecondaryEntity();
        }

        if (currentEntity != null) {
            boolean resPrev;
            boolean resCurr;
            //boolean logicalConditionRes = true;
            Iterator<Conditions> iterator = conditions.iterator();
            Conditions currCondition = iterator.next();

            //first condition, at least 2 conditions in multiple list of conditions
            currCondition.applyAction(context);
            resPrev = currCondition.getRes();
            currCondition = iterator.next();

            do {
                currCondition.applyAction(context);
                resCurr = currCondition.getRes();
                if (logical.equals("or")) {
                    if (!resPrev && !resCurr)
                        //logicalConditionRes = false;
                        res = false;
                    else
                        res = true;
                } else if (logical.equals("and")) {
                    if (!(resPrev && resCurr))
                        //logicalConditionRes = false;
                        res = false;
                    else res = true;
                }
                resPrev = resCurr;
                if (iterator.hasNext())
                    currCondition = iterator.next();
            } while (res && iterator.hasNext());

            if (res)
                for (Actions action : getThenOp().getActionsList())
                    action.applyAction(context);
            else // condition is false
                if (getElseOp() != null)
                    for (Actions action : getElseOp().getActionsList())
                        action.applyAction(context);
            //else do nothing
        }
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    public void addCondition(Conditions condition){
        conditions.add(condition);
    }

    public List<Conditions> getConditionList(){
        return conditions;
    }

}

