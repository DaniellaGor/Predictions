package actions;

import actions.context.ContextImpl;
import actions.selection.Selection;
import execution.instances.entity.EntityInstanceImpl;

public class Kill extends Actions{
    //private int id;

    public Kill(String givenEntityName, String secondaryEntity, Selection selection){
        super(givenEntityName, secondaryEntity, "Kill", selection);
        //this.id = id;

    }
    @Override
    public void applyAction(ContextImpl context) {
        EntityInstanceImpl currentEntity = null;
        if(context.getPrimaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
            currentEntity = context.getPrimaryEntity();
        else if(context.getSecondaryEntity() != null) {
            if (context.getSecondaryEntity().getEntityDefinition().getName().equals(primaryEntityName))
                currentEntity = context.getSecondaryEntity();
        }

        if(currentEntity!=null) {
            currentEntity.setDoNotKill(false);
        }
    }

    @Override
    public String getActionName() {
        return actionName;
    }
}
