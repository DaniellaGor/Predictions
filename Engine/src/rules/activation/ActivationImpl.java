package rules.activation;

public class ActivationImpl implements Activation{

    private int ticks;
    private double probability;

    public ActivationImpl(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }
    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    @Override
    public void setTicks(int num) {
        ticks = num;
    }

    @Override
    public void setProbability(double num) {
        probability = num;
    }
}
