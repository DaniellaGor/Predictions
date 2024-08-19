package dto;

import dto.actionsDTO.ActionDTO;

import java.util.List;

public class RulesDTO {
    private String name;
    private RuleActivationDTO activation;
    private int actionsAmount;
    private List<ActionDTO> actionsList;

    public RulesDTO(String name, RuleActivationDTO activation, int actionsAmount, List<ActionDTO> actions){
        this.name = name;
        this.activation = activation;
        this.actionsAmount = actionsAmount;
        this.actionsList = actions;
    }

    public String getName(){return name;}
    public RuleActivationDTO getActivation(){return activation;}
    public int getActionsAmount(){return actionsAmount;}
    public List<ActionDTO> getActionsNamesList(){return actionsList;}
}
