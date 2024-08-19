package execution.instances.entity;

import actions.Actions;
import actions.context.ContextImpl;
import exceptions.*;
import execution.instances.property.PropertyInstanceImpl;

import java.util.List;
import java.util.Map;

public interface EntityInstance {

    Map<String, PropertyInstanceImpl> getPropertiesMap();
    void addProperty(PropertyInstanceImpl property);

    int getId();
    boolean doNotKill();
    void setDoNotKill(boolean kill);
    void invokeActions(List<Actions> actionsList, ContextImpl context) throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException;
}
