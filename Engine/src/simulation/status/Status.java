package simulation.status;

public enum Status {
    ON_GOING, PAUSE, STOP, FINISHED;

   /* @Override
    public String toString(){

    }*/
    public String getStatus(Status status){
        int num = status.ordinal();
        switch(num){
            case(0):
                return "on going";
            case(1):
                return "pause";
            case(2):
                return "stop";
            case(3):
                return "finished";
        }
        return "on going";
    }

}
