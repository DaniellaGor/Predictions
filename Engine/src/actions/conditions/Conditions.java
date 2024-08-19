package actions.conditions;

import actions.Actions;
import actions.conditions.thenAndElse.ThenElseImpl;
import actions.context.ContextImpl;
import actions.selection.Selection;
import exceptions.*;

public abstract class Conditions extends Actions {
    private String singularity;
    private ThenElseImpl thenOp;
    private ThenElseImpl elseOp;

    public Conditions(String givenEntityName, String secondaryEntity, Selection selection,  String singularity, ThenElseImpl thenOp, ThenElseImpl elseOp){
        super(givenEntityName, secondaryEntity, "Condition", selection);
        this.singularity = singularity;
        this.thenOp = thenOp;
        this.elseOp = elseOp;
    }

    public String getSingularity(){
        return singularity;
    }
    public ThenElseImpl getThenOp(){
        return thenOp;
    }
    public ThenElseImpl getElseOp(){
        return elseOp;
    }

    public void setSelection(Selection selection){
        this.selection = selection;
    }

    public abstract void setRes(boolean res);

    public abstract boolean getRes();
    @Override
    public abstract void applyAction(ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException;
   /* public abstract void addCondition(Condition condition);
    public abstract List<Condition> getConditionList();*/
}
