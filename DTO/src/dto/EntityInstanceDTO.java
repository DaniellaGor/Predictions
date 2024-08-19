package dto;

import java.util.Map;

public class EntityInstanceDTO {

    private EntityDefinitionDTO entityDefinitionDTO;
    private Map<String, PropertyDTO> propertyDTOMap;
    private int id;

    public EntityInstanceDTO(EntityDefinitionDTO entityDefinitionDTO, Map<String, PropertyDTO> propertyDTOMap, int id){
        this.entityDefinitionDTO = entityDefinitionDTO;
        this.propertyDTOMap = propertyDTOMap;
        this.id = id;
    }

    public EntityDefinitionDTO getEntityDefinitionDTO(){ return entityDefinitionDTO;}
    public Map<String, PropertyDTO> getPropertyDTOMap(){ return propertyDTOMap;}
    public int getId(){ return id;}
}
