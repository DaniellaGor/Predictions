package actions.conditions.thenAndElse;

import actions.Actions;

import java.util.List;

public interface DoAfterCondition {
    public void addAction(Actions action);
    public List<Actions> getActionsList();
}
