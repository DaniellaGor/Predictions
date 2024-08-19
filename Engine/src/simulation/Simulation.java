package simulation;

import dto.ActiveEnvironmentDTO;
import exceptions.*;
import execution.instances.entity.EntityInstanceImpl;
import execution.instances.entity.manager.EntityInstancesManagerImpl;

public interface Simulation {
   // void validateXMLData(File xmlFIle, PRDWorld world) throws FileIsNotXMLFileException, FileNotFoundException, EnvironmentVariablesNameDuplicationException, SpacesInNameException, EntityPropertiesNameDuplicationException, ActionOnNonExistingEntityInstanceException, ActionOnNonExistingPropertyException, MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException;
    void runSimulation() throws VariableDoesNotExistsException, BooleanCanNotBeHigherOrLowerException, ValueDoesNotMatchPropertyTypeException, ValueDoesNotMatchFunctionException, EvaluateFunctionRequiresAnEntityAndAPropertyException, EntityDoesNotMatchFunctionException, PropertyDoesNotExistException;
   // void presentSimulationData();
    //WorldImpl copyDataFromValidXML(PRDWorld someWorld);

    void setActiveEnvironmentFromUsersInput(ActiveEnvironmentDTO activeEnvironmentDTO);
    //ActiveEnvironmentDTO createUpdatedActiveEnvToShowUser();
    void setEntitiesInstancesForSimulation();
    void setDate(String date);
    String getDate();
    EntityInstancesManagerImpl getEntityInstancesManager();

    void addEntityInstance(EntityInstanceImpl entityInstance);
}
