package terminate;

public interface Terminate {
    boolean getTicksCondition();
    boolean getSecondsCondition();
    int getTicksAmount();
    int getSecondsAmount();
    boolean getTerminateByUser();
}
