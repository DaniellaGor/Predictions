/*
package console.commands;

import dto.*;
import exceptions.*;
import simulationManager.SimulationManagerImpl;
import world.WorldImpl;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.System.exit;

public class CommandsImpl implements Commands{

    private WorldImpl engineWorld;
    private SimulationManagerImpl pastSimulations;

    public CommandsImpl(){
        //pastSimulations = new SimulationManagerImpl();
    }
    @Override
    //command #1
    public void getFile() throws PropertyDoesNotExistException, FileIsNotXMLFileException, JAXBException, FileNotFoundException, ActionOnNonExistingEntityInstanceException, EnvironmentVariablesNameDuplicationException, EntityPropertiesNameDuplicationException, MathActionVariablesAreNotNumericException, ValueDoesNotMatchPropertyTypeException, SpacesInNameException, FileDoesNotExistsException, GridMeasurementsAreNotValidException, ValueDoesNotMatchActionException, EntityDoesNotExistException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a full XML file path:");
        String filePath = scanner.nextLine();
        engineWorld = new WorldImpl(filePath);
    }

    public boolean checkIfWorldExists(){
        return (engineWorld!=null);
    }

    public boolean checkIfThereAreSimulations(){
        return (!pastSimulations.getSimulationMap().isEmpty());
    }
    @Override
    public void presentSimulationData() {
       */
/* Scanner scanner = new Scanner(System.in);
        WorldDTO worldDTO = engineWorld.createWorldDTO();
        int i = 1;
        System.out.println("\nPresenting data from file:\n");
        System.out.println("\tEntities:");
        for(EntityDefinitionDTO entity: worldDTO.getEntitiesList()){
            System.out.println("\t\tEntity number " + i + ":");
            System.out.println("\t\t\tEntity name:" + entity.getName());;
            System.out.println("\t\t\tPopulation amount: " + entity.getPopulation());
            System.out.println("\t\t\tProperties:");
            int j = 1;
            for(PropertyDTO property: entity.getPropertiesList()){
                System.out.println("\t\t\t\tProperty number " + j +":");
                System.out.println("\t\t\t\t\tName: "+ property.getName());
                System.out.println("\t\t\t\t\tType: " + property.getType());
                if(property.getRangeTop()!=0)
                {
                    if(property.getType().equalsIgnoreCase("decimal")){
                        System.out.println("\t\t\t\t\tRange: from " + (int)property.getRangeBottom() + " to " +
                                (int)property.getRangeTop());
                    }
                    else  System.out.println("\t\t\t\t\tRange: from " + property.getRangeBottom() + " to " + property.getRangeTop());
                }
                if(property.getIsRandomInit())
                    System.out.println("\t\t\t\t\tValue will be initialized randomly.\n");
                else System.out.println("\t\t\t\t\tValue will not be initialized randomly.\n");
                j++;
            }
        }
        System.out.println("\tRules:");
        for(RulesDTO rule: worldDTO.getRulesList()){
            System.out.println("\t\tName: " + rule.getName());
            System.out.println("\t\tActivation conditions: ticks - " + rule.getActivation().getTicks() + ", probability - " +
                    rule.getActivation().getProbability());
            System.out.println("\t\tActions amount: " + rule.getActionsAmount());
            System.out.println("\t\tActions names: ");

            List<String> actionNames = rule.getActionsNamesList();
            i=1;
            for(String name: actionNames)
                System.out.println("\t\t\t" + i + "." + name);
        }
        System.out.println("\n\tTermination conditions:");
        if(worldDTO.getTermination().getTicks()!=0)
            System.out.println("\t\tTicks amount: " + worldDTO.getTermination().getTicks());
        if(worldDTO.getTermination().getSeconds()!=0)
            System.out.println("\t\tSeconds amount: " + worldDTO.getTermination().getSeconds());
        //scanner.close();*//*


    }

    @Override
    public void runSimulation() {
        WorldDTO worldDTO = engineWorld.createWorldDTO();
        Scanner scanner = new Scanner(System.in);

        //get user's input
        Map<String, PropertyDTO> envVarsMap = new HashMap<>();
       //userEnvironmentVariableInput(envVarsMap, worldDTO);
        for(Map.Entry<String,PropertyDTO> entry: worldDTO.getEnvironmentManager().getEnvironmentVarMap().entrySet()){
            if(envVarsMap.get(entry.getKey())==null){
                Object newVal = randomValue(entry.getValue().getRangeTop(),
                        entry.getValue().getRangeBottom(), entry.getValue().getType());
                envVarsMap.put(entry.getKey(), new PropertyDTO(entry.getKey(), entry.getValue().getType(),
                        entry.getValue().getRangeTop(), entry.getValue().getRangeBottom(), true, newVal));
            }
        }
        ActiveEnvironmentDTO activeEnvironmentDTO = new ActiveEnvironmentDTO(envVarsMap);

        //present all environment vars to user
        System.out.println("\nPresenting updated environment variables:");
        for(Map.Entry<String, PropertyDTO> entry:
                activeEnvironmentDTO.getEnvironmentVariables().entrySet()){
            System.out.println("\tName: " + entry.getKey());
            System.out.println("\tValue: " + entry.getValue().getValue());
            System.out.println();
        }
        //new simulation
        */
/*try {
            UUID thisSimulationGUID = UUID.randomUUID();
            pastSimulations.startSimulation(engineWorld, activeEnvironmentDTO, getTimeAndDate(), thisSimulationGUID);
            System.out.println("\nPresenting simulation results:");
            System.out.println("\tSimulation GUID - " + thisSimulationGUID);
            TerminationDTO terminationDTO = new TerminationDTO(worldDTO.getTermination().getTicks(),
                    worldDTO.getTermination().getSeconds(), worldDTO.getTermination().getIfEndedByTicks());
            if(terminationDTO.getIfEndedByTicks())
                System.out.println("\tSimulation ended due to ticks amount.");
            else System.out.println("\tSimulation ended due to seconds amount.");

        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("\nProgram will return to menu now.\n");
            //return to menu!
        }*//*

        //scanner.close();
    }

    public String getTimeAndDate(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss");
        return currentDateTime.format(formatter);
    }
    */
/*public void userEnvironmentVariableInput(Map<String, PropertyDTO> envVarsMap,WorldDTO worldDTO) {
        Scanner scanner = new Scanner(System.in);
        int i = 1;
        String res = null;
        Object value = null;
        System.out.println("\nPresenting environment variables by name:");
        String answer;

        for(Map.Entry<String,PropertyDTO> entry: worldDTO.getEnvironmentManager().getEnvironmentVarMap().entrySet()){
            System.out.println(i+". " + entry.getKey());
            i++;
        }
        System.out.println("\nWould you like to insert a value into a certain variable? Please enter Y if yes, N if no.");
        answer = scanner.nextLine();

        while(!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"))){
            System.out.println("Wrong keys have been entered. " +
                    "Please enter Y for setting a new value, N for moving to the next variable.");
            answer = scanner.nextLine();
        }
        i--;
        while((answer).equalsIgnoreCase("y")){

            System.out.println("\nPlease enter the number of the wanted variable: ");
            answer = scanner.nextLine();
            int num=0;
            boolean valid = false;

            while(!checkUsersInput(answer, i)){
                answer = scanner.nextLine();
            }
            num = Integer.parseInt(answer);
            PropertyDTO usersChosenProperty = worldDTO.getEnvironmentManager()
                    .getEnvironmentVarList().get(num-1);
            System.out.println("Inserting value for variable " + usersChosenProperty.getName() + " :");
            System.out.println("Please enter a value of type " + usersChosenProperty.getType() +":");
            if(usersChosenProperty.getRangeTop()!=0){
                System.out.print("***Please note the value must be between ");
                if(usersChosenProperty.getType().equalsIgnoreCase("decimal"))
                    System.out.print((int) usersChosenProperty.getRangeBottom() + " and " +
                            (int) usersChosenProperty.getRangeTop() + " ***\n");
                else
                    System.out.print(usersChosenProperty.getRangeBottom() + " and " +
                            usersChosenProperty.getRangeTop() + " ***\n");
            }
            value = getValueFromString(usersChosenProperty);
            envVarsMap.put(usersChosenProperty.getName(),new PropertyDTO(usersChosenProperty.getName(),
                    usersChosenProperty.getType(), usersChosenProperty.getRangeTop(),
                    usersChosenProperty.getRangeBottom(), false, value));

            System.out.println("\nWould you like to change anything else? Please enter 'Y' if yes, 'N' if not: ");
            answer = scanner.nextLine();
            i=1;
            if(answer.equalsIgnoreCase("y")) {
                System.out.println("\nPresenting environment variables by name:\n");
                for(Map.Entry<String,PropertyDTO> entry: worldDTO.getEnvironmentManager().getEnvironmentVarMap().entrySet()){
                    System.out.println(i+". " + entry.getKey());
                    i++;
                }
            }
        }

    }*//*


    public Object randomValue(float rangeTop, float rangeBottom, String type) {
        Object randomObject = null;
        Random random = new Random();
        if (type.equalsIgnoreCase("decimal")) {
            int randomInt = random.nextInt((int) rangeTop - (int) rangeBottom) + (int) rangeBottom;
            randomObject = randomInt;
        } else if (type.equalsIgnoreCase("float")) {
            float randomFloat = random.nextFloat() * (rangeTop - rangeBottom) + rangeBottom;
            randomObject = randomFloat;
        } else if (type.equalsIgnoreCase("boolean")) {
            boolean randomBoolean = random.nextBoolean();
            randomObject = randomBoolean;
        } else if (type.equalsIgnoreCase("string")) {
            int randomLength = random.nextInt(51) + 1;
            String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ?!_-,.()";
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < randomLength; i++) {
                char c = characters.charAt(random.nextInt(characters.length()));
                randomString.append(c);
            }
            randomObject = randomString;
        }

        return randomObject;
    }

        public Object getValueFromString(PropertyDTO property) {
        Scanner scanner = new Scanner(System.in);
        Object res = null;
        boolean isValid = false;
        String val = scanner.nextLine();
        switch (property.getType().toLowerCase()){
            case("decimal"):{
                while(!isValid){
                    try {
                        res = Integer.parseInt(val);
                        if(property.getRangeTop()!=0) {
                            int topRange = (int)property.getRangeTop();
                            int bottomRange = (int)property.getRangeBottom();
                            if (!((int)res > topRange) && !((int)res < bottomRange))
                                isValid = true;
                            else {
                                System.out.println("Value is not in the property range! Please enter a valid " +
                                        "value between " + bottomRange + " and " + topRange +".");
                                val = scanner.nextLine();
                            }
                        }
                    }catch (Exception e) {
                        System.out.println("Property type is decimal, but value in condition" +
                                "is not decimal. Please enter a valid value.");
                        val = scanner.nextLine();
                    }
                }
            }break;
            case("float"):{
                while(!isValid) {
                    try {
                        res = Float.parseFloat(val);
                        if (property.getRangeTop() != 0) {
                            if (!((float) res > property.getRangeTop()) && !((float) res < property.getRangeBottom()))
                                isValid = true;
                            else {
                                System.out.println("Value is not in the property range! Please enter a valid " +
                                        "value between " + property.getRangeBottom() + " and" + property.getRangeTop() + ".");
                                val = scanner.nextLine();
                            }
                        }
                    }catch (Exception e) {
                        System.out.println("Property type is float, but value in condition" +
                                "is not float. Please enter a valid value.");
                        val = scanner.nextLine();
                    }
                }
            }break;
            case("boolean"):{
                while(!isValid) {
                    if (checkIfStringMeansBooleanVal(val)) {
                        res = Boolean.parseBoolean(val);
                        isValid = true;
                    }
                    else {
                        System.out.println("Property type is boolean, but value in condition" +
                                "does not match 'true' or 'false'. Please enter a valid value.");

                        val = scanner.nextLine();
                    }
                }
            }break;
            default:
                res = val; //type = string
        }
        return res;
    }

    public boolean checkIfStringMeansBooleanVal(String expression){
        return((expression.toLowerCase()).equals("true") || (expression.toUpperCase()).equals("false"));
    }

    public boolean checkUsersInput(String userNum, int ind){
        int num=0;
        boolean res = true;
        try {
            num = Integer.parseInt(userNum);
        } catch (Exception e) {
            System.out.println("Wrong keys have been entered. " +
                    "Please enter a number between 1 and " + ind);
        }
        if(num< 0 || num > ind) {
            System.out.println("Wrong keys have been entered. " +
                    "Please enter a number between 1 and " + ind);
            res = false;
        }
        else res = true;

        return res;
    }
    @Override
    public void presentPastSimulation() {
        */
/*Scanner scanner = new Scanner(System.in);
        System.out.println("\nPresenting past simulations:");

        PastSimulationsDTO pastSimulationsDTO = pastSimulations.createPastSimulationsDTO();
        int i = 1;
        for (Map.Entry<Integer, SimulationDTO> entry : pastSimulationsDTO.getSimulationDTOMap().entrySet()) {
            System.out.println("\n\t" + i +". Simulation running date: "+ entry.getValue().getDate() +
                    ", GUID: " + entry.getKey());
            i++;
        }
        i--;
        System.out.println("\nPlease enter the number of simulation for the program to show its data.");
        String simulationChoice = scanner.nextLine();
        while(!checkUsersInput(simulationChoice, i)){
            simulationChoice = scanner.nextLine();
        }
        int simulationChoiceInNum = Integer.parseInt(simulationChoice)-1;
        SimulationDTO currSimulationDTO = getChosenSimulation(simulationChoiceInNum, pastSimulationsDTO);

        System.out.println("\nPlease choose the way you would like to see the data:");
        System.out.println("\t1.By entities amount (what was the population in" +
                "the beginning and how many there are now)");
        System.out.println("\t2.By a property of a specific entity (then see the value of this property in all entities)");
        String wayOfPresentingChoice = scanner.nextLine();
        while(!checkUsersInput(wayOfPresentingChoice, 2)){
            wayOfPresentingChoice = scanner.nextLine();
        }
        List<EntityDefinitionDTO> entitiesDefList = engineWorld.createEntityDefDTOList();
        if (wayOfPresentingChoice.equals("1")) //print entities by population and current size
            printByEntitiesAmount(currSimulationDTO, entitiesDefList);

        else  //print properties value for all entities
            printByPropertyHistogram(entitiesDefList, currSimulationDTO);


            //scanner.close();*//*

    }

    public SimulationDTO getChosenSimulation(int ind, PastSimulationsDTO pastSimulationsDTO){
        int currInd = 0;
        SimulationDTO res = null;
        for(Map.Entry<Integer, SimulationDTO> entry: pastSimulationsDTO.getSimulationDTOMap().entrySet()) {
            if (ind == currInd) {
                res = entry.getValue();
                break;
            }
            currInd++;
        }

        return res;
    }

    public void printByEntitiesAmount(SimulationDTO currSimulationDTO, List<EntityDefinitionDTO> entitiesDefList) {
        int i = 1;
        System.out.println("\nPrinting entities original population and current population:");
        for (EntityDefinitionDTO currEntityDTO: entitiesDefList) {
            System.out.println("\tEntity number " + i);
            System.out.println("\t\tOriginal population: " + currEntityDTO.getPopulation());
            System.out.println("\t\tCurrent population: " +
                    currSimulationDTO.getEntitiesMap().get(currEntityDTO.getName()).size());
            i++;
        }
    }

    public void printByPropertyHistogram(List<EntityDefinitionDTO> entitiesDefList, SimulationDTO currSimulationDTO){
        */
/*int i = 1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nThe entities of the file will be presented now:");

        for (EntityDefinitionDTO currEntityDefDTO : entitiesDefList) {
            System.out.println("\t" + i + ".Entity name: " + currEntityDefDTO.getName());
            i++;
        }
        i--;
        System.out.println("\nPlease enter the number of the entity that you would like to choose a property from:");
        String chosenEntity = scanner.nextLine();
        while(!checkUsersInput(chosenEntity, i)){
            chosenEntity = scanner.nextLine();
        }
        EntityDefinitionDTO entity = entitiesDefList.get(Integer.parseInt(chosenEntity)-1);
        System.out.println("The properties of the entity '" + entity.getName() + "' will be presented now:");
        i = 1;
        *//*
*/
/*for (PropertyDTO propertyDTO : entity.getPropertiesList()) {
            System.out.println("\t" + i + ".Property name: " + propertyDTO.getName());
            i++;
        }
        i--;*//*
*/
/*

        System.out.println("\nPlease enter the number of the wanted property:");
        String chosenProperty = scanner.nextLine();
        while(!checkUsersInput(chosenProperty, i)){
            chosenProperty = scanner.nextLine();
        }
       *//*
*/
/* PropertyDTO property = entity.getPropertiesList().get(Integer.parseInt(chosenProperty)-1);
        System.out.println("Presenting property '" + property.getName() + "' histogram:");*//*
*/
/*

        Map<Object, Long> countByPropertyValue = currSimulationDTO.getEntitiesMap().get(entity.getName())
                .stream().collect(Collectors.groupingBy(instance -> instance.getPropertyDTOMap().get(property.getName())
                        .getValue(), Collectors.counting()));

        List<Map.Entry<Object, Long>> sortedEntries = countByPropertyValue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toList());

        if(sortedEntries.isEmpty())
            System.out.println("No data to present.\n");
        else {
            System.out.println("Presenting in a format of - Occurrence : Value of Property");
            // Print the sorted entries
            for (Map.Entry<Object, Long> entry : sortedEntries) {
                System.out.println("\t" + entry.getValue() + ":" + entry.getKey());
            }
        }*//*

    }
    @Override
    public void exitProgram() {
        System.out.println("\nProgram will terminate now. Goodbye.");
        exit(1);
    }

    @Override
    public void clearDate() {
        pastSimulations.clearDataFromMap();
    }
}
*/
