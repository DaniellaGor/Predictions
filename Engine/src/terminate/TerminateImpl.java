package terminate;

import dto.TerminationDTO;

public class TerminateImpl implements Terminate{
    private boolean ticksCondition;
    private boolean secondsCondition;
    private int seconds;
    private int ticks;
    private boolean terminatedByUser;
    private boolean terminatedByTicks;
    private boolean allowUserToTerminate;

    public TerminateImpl(int ticks, int seconds, boolean secondsCondition, boolean ticksCondition, boolean allowUserToTerminate){
        this.ticks = ticks;
        this.seconds = seconds;
        this.ticksCondition = ticksCondition;
        this.secondsCondition = secondsCondition;
        terminatedByTicks = false;
        this.terminatedByUser = false;
        this.allowUserToTerminate = allowUserToTerminate;
    }

    public void setTerminatedByUser(boolean set){
        terminatedByUser = set;
    }

    @Override
    public boolean getTicksCondition() {
        return ticksCondition;
    }

    @Override
    public boolean getSecondsCondition() {
        return secondsCondition;
    }

    @Override
    public int getTicksAmount() {
        return ticks;
    }

    @Override
    public int getSecondsAmount() {
        return seconds;
    }

    @Override
    public boolean getTerminateByUser() {
        return terminatedByUser;
    }

    public void setTerminatedByTicks(boolean set){
        terminatedByTicks = set;
    }

    public boolean getIfUserIsAllowedToTerminate(){
        return allowUserToTerminate;
    }


    public boolean getTerminatedByTicks(){
        return terminatedByTicks;
    }

    public TerminationDTO createTerminateDTO(){
        return new TerminationDTO(ticks, seconds, terminatedByTicks, terminatedByUser, allowUserToTerminate);
    }
}
