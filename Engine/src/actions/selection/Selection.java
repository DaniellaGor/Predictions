package actions.selection;

import actions.conditions.Conditions;

public class Selection {
    private int count;
    private Conditions condition;
    public Selection(int count, Conditions condition){
        this.count = count;
        this.condition = condition;
    }

    public int getCount(){
        return count;
    }

    public Conditions getCondition(){
        return condition;
    }


}
