package world.fileValidation;

import exceptions.*;
import generated.PRDEntities;
import generated.PRDRules;
import generated.PRDWorld;

import java.io.File;
import java.io.FileNotFoundException;

public interface FileValidation {
    void checkIfFileExistsAndOfTypeXML(File xmlFile) throws FileNotFoundException, FileIsNotXMLFileException, FileDoesNotExistsException;
    void checkSpaces(PRDWorld prdWorld) throws SpacesInNameException;
    void checkIfEnvironmentVariableNamesAreUnique(PRDWorld prdWorld) throws EnvironmentVariablesNameDuplicationException;
    void checkIfEntitiesPropertiesNamesAreUnique(PRDEntities prdEntities) throws EntityPropertiesNameDuplicationException;
    void checkIfEntityInvokingActionExist(PRDEntities prdEntities, PRDRules prdRules) throws ActionOnNonExistingEntityInstanceException;

    void checkIfValueMatchesPropertyType(PRDWorld prdWorld) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException;
    void checkIfPropertyExistsWhenInvokingActions(PRDEntities prdEntities, PRDRules prdRules) throws PropertyDoesNotExistException, ValueDoesNotMatchActionException, EntityDoesNotExistException;
    void checkIfMathActionVariablesAreNumericHelper(PRDWorld prdWorld) throws MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, PropertyDoesNotExistException, EntityDoesNotExistException;

}
