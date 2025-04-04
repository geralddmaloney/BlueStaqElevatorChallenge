package com.geraldmaloney.elevator;

import java.util.*;

public abstract class AbstractElevator {

    // Constants for floor numbers
    public static final int MIN_FLOOR_NUMBER = 0;
    public static final int LOBBY_FLOOR_NUMBER = 1;
    public static final int MAX_FLOOR_NUMBER = 12;

    // Elevator system state
    protected int currentFloor;
    protected int primaryDestination = -1;

    protected ElevatorStatus elevatorStatus;
    protected DoorStatus doorStatus;
    protected ElevatorRunMode runMode;
    protected Queue<Integer> requestQueue;

    // Constructor - initializes elevator at Lobby with system defaults
    public AbstractElevator() {
        this.currentFloor = LOBBY_FLOOR_NUMBER;
        this.elevatorStatus = ElevatorStatus.STOPPED;
        this.doorStatus = DoorStatus.CLOSED;
        this.runMode = ElevatorRunMode.ON;
        this.requestQueue = new LinkedList<>();
    }

    /**
     * Called every cycle to move elevator one floor in current direction.
     * Handles state changes and stops at valid requested floors.
     */
    public abstract void goToNextFloor();

    /**
     * Called when a user presses a floor button. Adds valid floors to the queue.
     */
    public abstract void pressFloor(int floor);

    /**
     * Called to display current elevator state and user interface.
     */
    public abstract void printStatus();

    /**
     * Prints user-friendly label for special floors.
     */
    public String getCurrentFloorString(int floor) {
        return switch (floor) {
            case MIN_FLOOR_NUMBER -> "Basement";
            case LOBBY_FLOOR_NUMBER -> "Lobby*";
            case MAX_FLOOR_NUMBER -> "Roof";
            default -> String.valueOf(floor);
        };
    }

    /**
     * Utility method for simulating timed actions (doors, movement, etc.).
     */
    protected void goToSleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Clears the console by printing empty lines for visual separation.
     */
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Opens the elevator doors if system conditions allow it.
     */
    protected void openDoor() {
        if (doorStatus == DoorStatus.OPEN) {
            System.out.println("Door is already open.");
            return;
        }

        if (doorStatus == DoorStatus.CLOSED &&
                elevatorStatus == ElevatorStatus.STOPPED &&
                runMode == ElevatorRunMode.ON) {
            System.out.println("Door is opening...");
            goToSleep(1500);
            doorStatus = DoorStatus.OPEN;
            System.out.println("Door is open. Please Exit Now.");
            goToSleep(1500);
            closeDoor();
        } else if (doorStatus == DoorStatus.LOCKED &&
                elevatorStatus == ElevatorStatus.STOPPED &&
                runMode == ElevatorRunMode.ESTOP) {
            System.out.println("EMERGENCY: Door is opening...");
            goToSleep(1500);
            doorStatus = DoorStatus.OPEN;
            System.out.println("EMERGENCY: Door is open. Please evacuate.");
            goToSleep(1500);
            closeDoor();
        } else if (runMode == ElevatorRunMode.OVERLOAD) {
            System.out.println("OVERLOAD: Door is opening...");
            goToSleep(1500);
            doorStatus = DoorStatus.OPEN;
            System.out.println("OVERLOAD: Door is open. Please remove excess weight.");
            goToSleep(1500);
        } else {
            System.out.println("Door cannot be opened at this time.");
        }
    }

    /**
     * Closes the elevator doors if system conditions allow it.
     */
    protected void closeDoor() {
        if (doorStatus == DoorStatus.CLOSED) {
            System.out.println("Door is already closed.");
            return;
        }

        if (doorStatus == DoorStatus.OPEN &&
                elevatorStatus == ElevatorStatus.STOPPED &&
                (runMode == ElevatorRunMode.ON)) {
            System.out.println("Door is closing...");
            goToSleep(1500);
            doorStatus = DoorStatus.CLOSED;
            System.out.println("Door is closed.");
            goToSleep(1500);
            System.out.println("==================================");
        }
        else if(doorStatus == DoorStatus.OPEN && elevatorStatus == ElevatorStatus.STOPPED && runMode != ElevatorRunMode.ON){
            System.out.println("EMERGENCY: Door is closing...");
            goToSleep(1500);
            doorStatus = DoorStatus.CLOSED;
            System.out.println("EMERGENCY: Door is closed!");
            goToSleep(1500);
            System.out.println("==================================");
        }
        else {
            System.out.println("Door cannot be closed at this time.");
        }
    }

    /**
     * Triggers ESTOP logic and activates emergency run mode.
     */
    public void emergencyStopPressed(){
        runMode = ElevatorRunMode.ESTOP;
        elevatorStatus = ElevatorStatus.STOPPED;
        doorStatus = DoorStatus.LOCKED;
        System.out.println("**EMERGENCY STOP BUTTON PRESSED**");
        printStatus();                                      // Show elevator status
        boolean runEstopMenu = true;
        Scanner scanner = new Scanner(System.in);

        while (runEstopMenu) {
            displayEmergencyStopMenu();                     // Show emergency menu
            System.out.print("Enter your choice: ");        // Ask User for choice.
            String input = scanner.nextLine();              // Get User input value

            // method to process the choice.
            switch (input.toUpperCase()) {
                case "1":
                case "FIRE":
                    // Call method to init Fire Emergency Mode
                    runEstopMenu = false;
                    fireEmergencyMode();
                    exitEmergencyMode();
                    break;
                case "2":
                case "EARTHQUAKE":
                    // Call method to init Earthquake Emergency Mode
                    runEstopMenu = false;
                    earthquakeMode();
                    exitEmergencyMode();
                    break;
                case "3":
                case "OVERLOAD":
                    // Call method to init Weight Overload Emergency Mode
                    runEstopMenu = false;
                    weightOverloadMode();
                    exitEmergencyMode();
                    break;
                case "4":
                case "HURRICANE":
                    // Call method to init Hurricane Emergency Mode
                    runEstopMenu = false;
                    hurricaneMode();
                    exitEmergencyMode();
                    break;
                case "5":
                case "QUIT":
                case "RESCUE":
                    // Method to init Fire Rescue Mode
                    runEstopMenu = false;
                    exitEmergencyMode();
                    goToSleep(2500);
                    clearConsole();
                    break;
                default:
                    runEstopMenu = false;
                    break;
            }
        }
    }

    /**
     * Simulates ringing the bell—used for idle looping or delay in console.
     */
    public void ringBell() {
        System.out.println("***Ring!***");
        goToSleep(1000);
    }

    /**
     * Displays emergency stop menu choices (delegated in subclass).
     */
    public void displayEmergencyStopMenu() {
        System.out.println("!!!!!!!!!! EMERGENCY MENU !!!!!!!!!!");
        System.out.println("[1] - Fire");
        System.out.println("[2] - Earthquake");
        System.out.println("[3] - Overload");
        System.out.println("[4] - Hurricane");
        System.out.println("[5] - Fire Rescue Mode (Exit)");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * Triggers fire emergency mode, locking the door after opening.
     */
    public void fireEmergencyMode(){
        System.out.println("***Fire Emergency Mode***");
        openDoor(); // open doors
        runMode = ElevatorRunMode.FIRE_SAFETY;      // Turn Run Mode to Fire Safety
        doorStatus = DoorStatus.LOCKED;             // Lock the Door until Fire Rescue Arrives
        System.out.println("Doors are Locked!");


        Scanner scanner = new Scanner(System.in);
        while(runMode == ElevatorRunMode.FIRE_SAFETY){
            printStatus();
            System.out.println("End Fire Safety Mode? [Y] or [N]?");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                runMode = ElevatorRunMode.ON;
                closeDoor();

                System.out.println("Doors are Unlocked!");
                goToSleep(1500);
                System.out.println("Resuming Normal Operations...");
                goToSleep(1500);

            }
            else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                System.out.println("Waiting for Fire Safety mode to be ended...");
            }
            else {
                System.out.println("Invalid input! Select [Y] or [N]!");
            }
        }
    }

    /**
     * Actions taken when EARTHQUAKE Run Mode is activated
     * - Door opens and emergency warning plays
     * - Doors close
     * - Elevator is sent to the basement
     * - User is asked in a loop if they wish to end Earthquake mode
     */
    public void earthquakeMode() {
        System.out.println("***Earthquake Mode***");
        openDoor();                             // Open doors
        runMode = ElevatorRunMode.EARTHQUAKE;   // Set Earthquake Run Mode
        doorStatus = DoorStatus.LOCKED;         // Lock the Door until Fire Rescue Arrives
        sendElevatorToBasement();               // Send elevator to basement

        Scanner scanner = new Scanner(System.in);
        while(runMode == ElevatorRunMode.EARTHQUAKE){
            printStatus();
            System.out.println("End Earthquake Mode? [Y] or [N]?");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                runMode = ElevatorRunMode.ON;
                closeDoor();

                System.out.println("Doors are Unlocked!");
                goToSleep(1500);
                System.out.println("Resuming Normal Operations...");
                goToSleep(1500);

            }
            else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                System.out.println("Waiting for Earthquake mode to be ended...");
            }
            else {
                System.out.println("Invalid input! Select [Y] or [N]!");
            }
        }
    }

    /**
     * Sends elevator to basement under emergency logic.
     */
    public void sendElevatorToBasement() {
        System.out.println("*** Sending Elevator to Basement ***");
        elevatorStatus = ElevatorStatus.MOVING_DOWN;
        requestQueue.clear();
        requestQueue.add(MIN_FLOOR_NUMBER);

        while (currentFloor > MIN_FLOOR_NUMBER) {
            goToNextFloor();
            printStatus();
            goToSleep(1500);
        }
    }

    /**
     * Weight Overload Safety Protocol - in lieu of sensors, users trigger via E-stop menu
     *  - Run mode changed to OVERLOAD
     *  - Doors Open and stay open
     *  - User is asked in a loop if excess weight has been removed
     */
    public void weightOverloadMode(){
        System.out.println("***Weight Overload Mode***");
        System.out.println("*Elevator weight overloaded!*");
        runMode = ElevatorRunMode.OVERLOAD;                                         // Run mode set to OVERLOAD
        openDoor();                                                                 // doors open and keeps them open due to run mode.
        Scanner scanner = new Scanner(System.in);                                   // loop to ask user if excess weight was removed
        while(runMode == ElevatorRunMode.OVERLOAD){
            printStatus();
            System.out.print("Is Excess Weight Removed? [Y] or [N]");               // Ask User for choice.
            String input = scanner.nextLine();
            // Reset run mode if excess weight was removed
            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                closeDoor();
                runMode = ElevatorRunMode.ON;
            }
            // Otherwise stay in loop
            else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                System.out.println("Please remove excess weight before proceeding!");
            }
            // Handle invalid input
            else {
                System.out.println("Invalid input! Select [Y] or [N]!");
            }
        }
    }

    /**
     * Upon exiting Emergency Mode the run mode will change back to ON and the door will be unlocked by switching it back to closed.
     */
    public void exitEmergencyMode(){
        System.out.println("***Exiting Emergency Mode***");
        System.out.println("Returning to Normal Operations...");
        runMode = ElevatorRunMode.ON;
        goToSleep(1500);
        System.out.println("Door is unlocking...");
        doorStatus = DoorStatus.OPEN;
        goToSleep(1500);
        System.out.println("Door is unlocked!");
        closeDoor();
    }

    /**
     * Reassigns a new primaryDestination based on queue and movement direction.
     */
    protected void reassignPrimaryDestination() {
        if (primaryDestination != -1 || requestQueue.isEmpty()) {
            return;
        }

        int target = currentFloor;

        if (elevatorStatus == ElevatorStatus.MOVING_UP) {
            for (int floor : requestQueue) {
                if (floor > target) {
                    target = floor;
                }
            }
        } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
            for (int floor : requestQueue) {
                if (floor < target) {
                    target = floor;
                }
            }
        } else {
            for (int floor : requestQueue) {
                if (Math.abs(floor - currentFloor) > Math.abs(target - currentFloor)) {
                    target = floor;
                }
            }
        }

        primaryDestination = target;
    }


    /**
     * Actions taken when HURRICANE Run Mode is activated
     * - Door opens and emergency warning plays
     * - Doors close
     * - Elevator is sent to the roof
     * - User is asked in a loop if they wish to end Hurrican mode
     */
    public void hurricaneMode() {
        System.out.println("***Hurricane Mode***");
        openDoor();                             // Open doors
        runMode = ElevatorRunMode.HURRICANE;    // Set HURRICANE Run Mode
        doorStatus = DoorStatus.LOCKED;         // Lock the Door until Fire Rescue Arrives
        sendElevatorToRoof();                   // Send elevator to Roof

        Scanner scanner = new Scanner(System.in);
        while(runMode == ElevatorRunMode.HURRICANE){
            printStatus();
            System.out.println("End HURRICANE Mode? [Y] or [N]?");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                runMode = ElevatorRunMode.ON;
                closeDoor();

                System.out.println("Doors are Unlocked!");
                goToSleep(1500);
                System.out.println("Resuming Normal Operations...");
                goToSleep(1500);

            }
            else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
                System.out.println("Waiting for Earthquake mode to be ended...");
            }
            else {
                System.out.println("Invalid input! Select [Y] or [N]!");
            }
        }
    }

    /**
     * Sends elevator to basement under emergency logic.
     */
    public void sendElevatorToRoof() {
        System.out.println("*** Sending Elevator to Roof ***");
        elevatorStatus = ElevatorStatus.MOVING_UP;
        requestQueue.clear();
        requestQueue.add(MAX_FLOOR_NUMBER);

        while (currentFloor < MAX_FLOOR_NUMBER) {
            goToNextFloor();
            printStatus();
            goToSleep(1500);
        }
    }
}
