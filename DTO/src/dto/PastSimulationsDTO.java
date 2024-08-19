package dto;

import java.util.Map;

public class PastSimulationsDTO {
    private Map<Integer, SimulationDTO> simulationDTOMap;

    public PastSimulationsDTO(Map<Integer, SimulationDTO> simulationDTOMap){
        this.simulationDTOMap = simulationDTOMap;
    }

    public Map<Integer, SimulationDTO> getSimulationDTOMap() {
        return simulationDTOMap;
    }
}
