package simulationManager;

import dto.ActiveEnvironmentDTO;
import exceptions.*;
import simulation.SimulationImpl;
import world.WorldImpl;

import java.util.Map;

public interface SimulationManager {

    void addSimulation(SimulationImpl simulation);
    SimulationImpl getSimulation(int id);

    void startSimulation(WorldImpl world, ActiveEnvironmentDTO activeEnvironmentDTO, String dateAndTime, int id) throws VariableDoesNotExistsException, ValueDoesNotMatchPropertyTypeException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException;

    Map<Integer, SimulationImpl> getSimulationMap();

}
