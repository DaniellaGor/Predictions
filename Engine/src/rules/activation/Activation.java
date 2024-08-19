package rules.activation;

public interface Activation {
    int getTicks();
    double getProbability();
    void setTicks(int num);
    void setProbability(double num);
}
