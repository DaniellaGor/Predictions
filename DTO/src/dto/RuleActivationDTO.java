package dto;

public class RuleActivationDTO {
    private int ticks;
    private double probability;

    public RuleActivationDTO(int tick, double probability){
        this.ticks = tick;
        this.probability = probability;
    }

    public int getTicks(){ return ticks;}

    public double getProbability() { return probability;}
}
