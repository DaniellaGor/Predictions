/*
package console.menu;

import console.commands.CommandsImpl;

import java.util.Scanner;

import static java.lang.System.exit;

public class MenuImpl implements Menu{

    private CommandsImpl commands;
    public MenuImpl(){
        commands = new CommandsImpl();
    }

    public void printMenu(){
        System.out.println("\nPlease choose one of the options below:");
        System.out.println("1. Insert a new XML file (please note that with no file, system will not continue)");
        System.out.println("2. Watch the simulation data (before activation)");
        System.out.println("3. Run a new simulation");
        System.out.println("4. Watch the details of a past simulation (please note that if no simulation was activated program will go back to menu)");
        System.out.println("5. Exit program");
        System.out.println("Please choose the number of the wanted option:");

    }
    public void runMenu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Predictions program!");
        printMenu();
        String choice = scanner.nextLine();
        while(!choice.equals("5")) {
            try {
                switch (choice) {
                    case ("1"): {
                        if(commands.checkIfWorldExists())
                            commands.clearDate();
                        commands.getFile();
                        System.out.println("\nFile is valid. Program will continue to menu.");
                    }
                    break;
                    case ("2"): {
                        if (commands.checkIfWorldExists())
                            commands.presentSimulationData();
                        else {
                            System.out.println("\nCan not present simulation if file was not uploaded.");
                            System.out.println("Program will return to main menu now -");
                            System.out.println("Please choose option 1 to upload a file.\n");
                        }
                    }
                    break;
                    case ("3"): {
                        if (commands.checkIfWorldExists())
                            commands.runSimulation();
                        else {
                            System.out.println("\nCan not run simulation if file was not uploaded.");
                            System.out.println("Program will return to main menu now -");
                            System.out.println("Please choose option 1 to upload a file.\n");
                        }
                    }
                    break;
                    case ("4"): {
                        if (commands.checkIfThereAreSimulations())
                            commands.presentPastSimulation();
                        else {
                            System.out.println("\nCan not present past simulation since no simulation has been activated.");
                            System.out.println("Program will return to main menu now -");
                            System.out.println("If file was not uploaded yet - please choose option 1 to upload a file.");
                            System.out.println("Else, if file was uploaded but program did not run a simulation - ");
                            System.out.println("Please choose option 2.\n");
                        }
                    }
                    break;
                    default:
                        System.out.println("Wrong key, please try again.\n");
                }
                printMenu();
                //scanner.nextLine();
                choice = scanner.nextLine();

            } catch (Exception e) {
                System.out.println("\nProblem occurred. Exception message:");
                System.out.println(e.getMessage() + "\n");
                System.out.println("Program will continue to menu.\n");
                printMenu();
                choice = scanner.nextLine();
            }
        }

        if(choice.equals("5")){
            System.out.println("Program will terminate now.");
            System.out.println("\n\n****** Goodbye! ******");
            exit(1);
        }

        //scanner.close();
    }
}
*/
