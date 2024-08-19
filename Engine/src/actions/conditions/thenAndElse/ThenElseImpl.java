package actions.conditions.thenAndElse;

import actions.Actions;

import java.util.ArrayList;
import java.util.List;

public class ThenElseImpl implements DoAfterCondition {
    private String whichOne;
    private List<Actions> actionsList;
    public ThenElseImpl(String name){
        whichOne = name;
        actionsList = new ArrayList<>();
    }
    public void addAction(Actions action){
        actionsList.add(action);
    }
    public List<Actions> getActionsList(){
        return actionsList;
    }
}
