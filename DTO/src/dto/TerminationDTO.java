package dto;


public class TerminationDTO {
    private int ticks;
    private int seconds;
    private boolean endedDueToTicks;
    private boolean terminatedByUser;

    private boolean allowUserToTerminate;

    public TerminationDTO(int ticks, int seconds, boolean endedDueToTicks, boolean terminateByUser, boolean allowUserToTerminate){
        this.ticks = ticks;
        this.seconds = seconds;
        this.endedDueToTicks = false;
        this.terminatedByUser = terminateByUser;
        this.allowUserToTerminate = allowUserToTerminate;
    }

    public int getTicks(){ return ticks;}
    public int getSeconds(){ return seconds;}

    public boolean getIfEndedByTicks(){
        return endedDueToTicks;
    }

    public boolean getIfEndedByUser(){
        return terminatedByUser;
    }

    public boolean getIfUserAllowedToTerminate(){
        return allowUserToTerminate;
    }
}
