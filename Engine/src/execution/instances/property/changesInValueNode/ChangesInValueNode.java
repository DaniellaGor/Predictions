package execution.instances.property.changesInValueNode;

public interface ChangesInValueNode {
    void setStart(int start);
    void setEnd(int end);
    void setValueInArray(Object newValue);
    int getStart();
    int getEnd();
    Object getValueFromArray();
}
