package dto.actionsDTO;

public class SetDTO extends ActionDTO{
    private String property;
    private String value;
    public SetDTO(String name, String primaryEntity, String secondaryEntity, String property, String value){
        super(name, primaryEntity, secondaryEntity);
        this.value = value;
        this.property = property;
    }

    public String getProperty(){ return property;}
    public String getValue() {return value;}
}
