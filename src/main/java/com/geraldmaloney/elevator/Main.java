package com.geraldmaloney.elevator;

import java.util.Scanner;
import com.geraldmaloney.elevator.AbstractElevator;
import com.geraldmaloney.elevator.PassengerElevator;


public class Main {
    public static void main(String[] args) {

       PassengerElevator elevator = new PassengerElevator();
       Scanner scanner = new Scanner(System.in);
       boolean running = true;


       System.out.println("Welcome to the Elevator Simulator!");
       System.out.println("==================================");

                                                                // Default Run Mode is ON. Alternate Modes Triggered Via E-Stop Menu
       while (running) {
           elevator.goToNextFloor();                            // Proceed to next floor in queue
           elevator.printStatus();                              //Show current status
           displayNormalMenu();                                 // Add enum Check for run mode

           System.out.print("Enter your choice: ");             // Ask User for choice.
           String input = scanner.nextLine();

           PassengerElevator.clearConsole();                             // clear console for clean display

           String buttonName = displayButtonLabel(input);       // Gets button function
           System.out.println("************************");
           System.out.println("YOU PRESSED: " + buttonName);
           System.out.println("************************");

           // Make Method to House Switch Case
           switch (input) {
               case "1":
                   System.out.print("Enter floor number to request [" + elevator.MIN_FLOOR_NUMBER + " thru " + elevator.MAX_FLOOR_NUMBER + " ](or B = Basement, L = Lobby, R = Roof): ");
                   String inputFloor = scanner.nextLine().trim().toUpperCase();

                   int floor;
                   try {
                       switch (inputFloor) {
                           case "B":
                           case "BASEMENT":
                               floor = Elevator.MIN_FLOOR_NUMBER;
                               break;
                           case "L":
                           case "LOBBY":
                               floor = Elevator.LOBBY_FLOOR_NUMBER;
                               break;
                           case "R":
                           case "ROOF":
                               floor = Elevator.MAX_FLOOR_NUMBER;
                               break;
                           default:
                               floor = Integer.parseInt(inputFloor);  // Try parsing as number
                       }

                       elevator.pressFloor(floor);

                   } catch (NumberFormatException e) {
                       System.out.println("Invalid input. Please enter a number or B, L, R.");
                   }
                   break;
               case "2":
                   elevator.openDoor();
                   break;

               case "3":
                   elevator.closeDoor();
                   break;

               case "4":
                   elevator.ringBell();
                   break;

               case "5":
                   elevator.emergencyStopPressed();
                   break;

               case "6":
                   // wait for next floor method here.
                   System.out.println("Waiting for elevator to reach next stop...");
                   while (elevator.elevatorStatus != ElevatorStatus.STOPPED) {
                       elevator.goToNextFloor();
                       elevator.printStatus();
                       elevator.goToSleep(1000); // Small delay between cycles
                   }
                   break;
               case "7":
                   System.out.println("Exiting elevator simulation...");
                   running = false;
                   break;

               default:
                   System.out.println("Invalid input. Try again.");
                   break;
           }

           System.out.println();            // line break for readability
       }
        scanner.close();
    }

    private static void displayNormalMenu(){
        System.out.println("********* MENU *********");
        System.out.println("Select an option:");
        System.out.println("[1] Press floor button");
        System.out.println("[2] Open door");
        System.out.println("[3] Close door");
        System.out.println("[4] Ring Bell (Wait/Delay a floor)");
        System.out.println("[5] Emergency Stop (Change Run Mode)");
        System.out.println("[6] Wait until Next Floor");
        System.out.println("[7] Quit");
        System.out.println("************************");
    }

    private static String displayButtonLabel(String button){
        String label = "";
        switch (button){
            case "1":
                label = "The Select Floor Button";
            break;
            case "2":
                label = "The Open Door Button";
            break;
            case "3":
                label = "The Close Door Button";
            break;
            case "4":
                label = "The Ring Bell/Wait Button";
                break;
            case "5":
                label = "The Emergency Stop Button";
                break;
            case "6":
                label = "Wait until Next Floor Button";
                break;
            default:
                label = "Invalid Button";
                break;
        }
        return label;
    }

}