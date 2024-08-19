package rules;

import actions.Actions;
import rules.activation.ActivationImpl;


import java.util.ArrayList;
import java.util.List;

public class Rules {
    private String name;
    private int actionsAmount;
    private List<Actions> actionsList;
    private ActivationImpl activation;

    public Rules(String name, int actionsAmount, ActivationImpl activation){
        this.name = name;
        this.actionsAmount = actionsAmount;
        actionsList = new ArrayList<>(actionsAmount);
        this.activation = activation;
    }
    public String getName(){
        return name;
    }

    public ActivationImpl getActivation(){
        return activation;
    }

    public List<Actions> getActionsList(){
        return actionsList;
    }

    public void addAction(Actions newAction){
        actionsList.add(newAction);
    }
}
