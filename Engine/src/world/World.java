package world;

import definition.entities.EntityDefinitionImpl;
import execution.environment.manager.EnvironmentManagerImpl;
import generated.PRDWorld;
import rules.Rules;
import terminate.TerminateImpl;

import java.util.List;
import java.util.Map;

public interface World {

    //void presentSimulationData();
    //void runSimulation();
    void copyDataFromValidXML(PRDWorld prdWorld);
    Map<String, EntityDefinitionImpl> getEntitiesDefinitionMap();
    List<Rules> getRulesList();

    EnvironmentManagerImpl getEnvironmentManager();
    TerminateImpl getTerminateConditions();




    //void addEntityDefinition(EntityDefinitionImpl entityDefinition);

    //void addRule(Rules rule);

    //void addEntityInstance(EntityInstanceImpl entityInstance);



    //EntityInstancesManagerImpl getEntityInstancesManager();
}
