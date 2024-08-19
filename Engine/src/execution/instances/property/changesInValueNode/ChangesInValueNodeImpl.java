package execution.instances.property.changesInValueNode;

public class ChangesInValueNodeImpl implements ChangesInValueNode{
    private int start;
    private int end;
    private Object value;

    public ChangesInValueNodeImpl(int start, Object value){
        this.start = start;
        this.value = value;
        this.end = 0;
    }
    @Override
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public void setValueInArray(Object newValue) {
        this.value = newValue;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public Object getValueFromArray() {
        return value;
    }
}
