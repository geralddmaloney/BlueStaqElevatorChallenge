package com.geraldmaloney.elevator;

import java.util.*;
import java.lang.Thread;

/**
 * First crack at non-abstract Elevator code. Deprecated this since I implemented an AbstractElevator base
 * and extended it to a PassengerElevator class.
 */
@Deprecated
public class Elevator {

    public static final int MIN_FLOOR_NUMBER = 0;
    public static final int LOBBY_FLOOR_NUMBER = 1;
    public static final int MAX_FLOOR_NUMBER = 12;

    private int primaryDestination = -1;                // Holds primary destination
    private int currentFloor;                           // Current Floor Elevator is on
    private ElevatorStatus elevatorStatus;              // Is elevator moving up? down? Stopped?
    private DoorStatus doorStatus;                      // Are elevator doors Open? Closed? Locked?
    private ElevatorRunMode runMode;                    // What mode are we running in? ON = Normal run mode
    private Queue<Integer> requestQueue;          // Requests queue that can receive multiple floor presses at once;

    /**
     * Elevator Constructor. Elevator starts at floor Lobby.
     * Is stopped. Door is closed. And a new requestQueue is initialized
     */
    public Elevator() {
        this.currentFloor = LOBBY_FLOOR_NUMBER ;        // Default to Lobby
        this.elevatorStatus = ElevatorStatus.STOPPED;   // Elevator starts not moving
        this.doorStatus = DoorStatus.CLOSED;            // Doors Default to close
        this.runMode = ElevatorRunMode.ON;              // Run Mode is ON
        this.requestQueue = new LinkedList<>();         //
    }

    /**
     * Press a button for a floor. That floor is added to the queue
     * @param floor - the requested floor
     */
    public void pressFloor(int floor) {
        // Validate the requested floor is within building range
        if (floor < MIN_FLOOR_NUMBER || floor > MAX_FLOOR_NUMBER) {
            System.out.println("Invalid Floor Number! Basement is Floor " + MIN_FLOOR_NUMBER +
                    ", Roof is " + MAX_FLOOR_NUMBER + ".\nPlease Try Again!");
            return;
        }

        // Reject duplicate requests
        if (requestQueue.contains(floor) || floor == currentFloor) {
            System.out.println("Floor already requested or you are already on that floor.");
            return;
        }

        // Determine if there is no active mission in progress
        boolean noActiveMission = (primaryDestination == -1 || requestQueue.isEmpty());

        // If stopped with no active destination, assign this floor as the new mission
        if (noActiveMission) {
            primaryDestination = floor;
        } else if (elevatorStatus == ElevatorStatus.MOVING_UP) {
            // Only update if the new floor is further up
            if (floor > currentFloor && floor > primaryDestination) {
                primaryDestination = floor;
            }
        } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
            // Only update if the new floor is further down
            if (floor < currentFloor && floor < primaryDestination) {
                primaryDestination = floor;
            }
        }

        // Rebuild the queue with updated comparator (if needed)
        PriorityQueue<Integer> rebuiltQueue = new PriorityQueue<>(getFloorComparator());
        rebuiltQueue.addAll(requestQueue);
        rebuiltQueue.add(floor);
        requestQueue = rebuiltQueue;

        // Label the floor for nicer output
        String label = getCurrentFloorString(floor);
        System.out.println("Floor " + label + " pressed and added to floor request queue!");
    }

    /**
     * Returns a Comparator that sorts floors based on the current elevator direction.
     * If moving up- lower floors come first (ascending order)
     * If moving down - higher floors come first (descending order)
     * If stopped - prioritize floors closest to current floor
     */
    private Comparator<Integer> getFloorComparator() {
        return (a, b) -> {
            if (elevatorStatus == ElevatorStatus.MOVING_UP) {
                return Integer.compare(a, b); // Ascending order
            } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
                return Integer.compare(b, a); // Descending order
            } else {
                int distA = Math.abs(a - currentFloor);
                int distB = Math.abs(b - currentFloor);
                return Integer.compare(distA, distB); // Closest floor wins
            }
        };
    }

    /**
     * Checks Door Status, Elevator Status, and Run Mode.
     * If doors are closed, elevator stopped, and run mode is on; then doors can open
     */
    public void openDoor() {
        // If the door is already open, say so and return.
        if(doorStatus == DoorStatus.OPEN) {
            System.out.println("Door Status is already Open!");
            return;
        }

        // If the door is closed, the elevator is stopped, and the run mode is on; then simulate the door opening and closing
        // with delays
        if(doorStatus == DoorStatus.CLOSED && elevatorStatus == ElevatorStatus.STOPPED && runMode == ElevatorRunMode.ON) {
            System.out.println("Door is opening...");
            goToSleep(1500);
            doorStatus = DoorStatus.OPEN;
            System.out.println("Door is open!\n Please Exit Now. And Have a Nice Day!");
            goToSleep(1500);
            closeDoor();
        }
        // Allow doors to open in a temp emergency stop state, then the run mode is shifted to the desired protocol
        else if(doorStatus == DoorStatus.LOCKED && elevatorStatus == ElevatorStatus.STOPPED && runMode == ElevatorRunMode.ESTOP) {
            System.out.println("EMERGENCY! Door is opening...");
            goToSleep(1500);
            doorStatus = DoorStatus.OPEN;
            System.out.println("EMERGENCY! Door is open.\n Please quickly Exit for your safety!");
            goToSleep(1500);
            closeDoor();
        }
        // If OVERLOAD protocol is engaged keep door open
        else if(runMode == ElevatorRunMode.OVERLOAD)
        {
            System.out.println("WEIGHT OVERLOAD DETECTED!");
            System.out.println("Door is opening...");
            goToSleep(1500);
            doorStatus = DoorStatus.OPEN;
            System.out.println("Door is open.\n Please remove excess weight!");
            goToSleep(1500);
        }
        else {
            System.out.println("Door cannot be opened at this time.");
        }
    }

    /**
     *  Checks Door Status, Elevator Status, and Run Mode.
     *  If doors are open, elevator stopped, and run mode is on; then doors can open
     */
    public void closeDoor() {
        // If the door is already closed, say so and return.
        if(doorStatus == DoorStatus.CLOSED){
            System.out.println("Door is already closed!");
            return;
        }

        // or if the door is open, the elevator is stopped, and the run mode is on; then close the door with delays
        if(doorStatus == DoorStatus.OPEN && elevatorStatus == ElevatorStatus.STOPPED && runMode == ElevatorRunMode.ON) {
            System.out.println("Door is closing...");
            goToSleep(1500);
            doorStatus = DoorStatus.CLOSED;
            System.out.println("Door is closed!");
            goToSleep(1500);
            System.out.println("==================================");
        }
        else if(doorStatus == DoorStatus.OPEN && elevatorStatus == ElevatorStatus.STOPPED && runMode != ElevatorRunMode.ON){
            System.out.println("Door is closing...");
            goToSleep(1500);
            doorStatus = DoorStatus.CLOSED;
            System.out.println("Door is closed!");
            goToSleep(1500);
            System.out.println("==================================");
        }
        else {
            System.out.println("Door cannot be closed at this time.");
        }
    }

    /**
     * Emergency Stop Button
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
                    fireEmergencyMode();
                    runEstopMenu = false;
                    break;
                case "2":
                case "EARTHQUAKE":
                    // Call method to init Earthquake Emergency Mode
                    earthquakeMode();
                    runEstopMenu = false;
                    break;
                case "3":
                case "OVERLOAD":
                    // Call method to init Weight Overload Emergency Mode
                    weightOverloadMode();
                    break;
                case "4":
                case "HURRICANE":
                    // Call method to init Hurricane Emergency Mode
                    break;
                case "5":
                case "QUIT":
                case "RESCUE":
                    // Method to init Fire Rescue Mode
                    exitEmergencyMode();
                    runEstopMenu = false;
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
     * Displays Emergency Stop menu which let the user select an emergency protocol in lieu of active sensors.
     */
    public void displayEmergencyStopMenu(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!! Emergency System Activated !!!");
        System.out.println("Please Enter the reason for the Emergency:");
        System.out.println("[1] - Fire");
        System.out.println("[2] - Earthquake");
        System.out.println("[3] - Overload");
        System.out.println("[4] - Hurricane");
        System.out.println("[5] - Fire Rescue Mode (Quit Emergency Stop Mode)");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     *  Goes to the next floor that is on the way to the primary destination.
     */
    public void goToNextFloor() {
        // If the floor request queue is empty, stop and reset destination
        if (requestQueue.isEmpty()) {
            elevatorStatus = ElevatorStatus.STOPPED;
            primaryDestination = -1; // Reset primary destination when no requests remain
            System.out.println("No floor requests in queue");
            return;
        }


        // Determine next logical floor to stop at based on direction and requested stops
        int nextFloor = findNextFloorOnTheWay();

        // If the elevator is already at the target floor
        if (nextFloor == currentFloor) {
            elevatorStatus = ElevatorStatus.STOPPED;

            if (requestQueue.contains(currentFloor)) {
                requestQueue.remove(currentFloor);
            }

            System.out.println("Arrived at floor " + getCurrentFloorString(currentFloor) + " (" + elevatorStatus.getLabel() + ")");
            goToSleep(2500);
            openDoor();

            // Always reset primaryDestination if it has just been reached
            if (currentFloor == primaryDestination || requestQueue.isEmpty()) {
                primaryDestination = -1;
            }
            reassignPrimaryDestination();
            return;
        }

        // Determine direction and announce
        if (nextFloor > currentFloor) {
            elevatorStatus = ElevatorStatus.MOVING_UP;
            System.out.println("Moving up toward floor " + getCurrentFloorString(nextFloor));
        } else if (nextFloor < currentFloor) {
            elevatorStatus = ElevatorStatus.MOVING_DOWN;
            System.out.println("Moving down toward floor " + getCurrentFloorString(nextFloor));
        }
        System.out.println("==================================");

        // Simulate travel time
        goToSleep(1500);

        // Step elevator one floor in current direction
        if (elevatorStatus == ElevatorStatus.MOVING_UP) {
            currentFloor++;
        } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
            currentFloor--;
        }

        // Check if the new current floor is a requested stop
        if (requestQueue.contains(currentFloor)) {
            elevatorStatus = ElevatorStatus.STOPPED;
            requestQueue.remove(currentFloor);

            System.out.println("Arrived at floor " + getCurrentFloorString(currentFloor) + " (" + elevatorStatus.getLabel() + ")");
            goToSleep(2500);
            openDoor();

            // Always reset primaryDestination if it has just been reached
            if (currentFloor == primaryDestination || requestQueue.isEmpty()) {
                primaryDestination = -1;
            }
            reassignPrimaryDestination();
        }
    }

    /**
     * Returns the next floor on the way to the primary destination.
     * Stops at intermediate floors if they are in the queue and in the current direction of travel.
     * If none found, returns the primary destination.
     */
    private Integer findNextFloorOnTheWay() {
        Integer bestFloor = null;

        // Loop through requestQueue and determine nearest floor according to the direction the elevator is moving.
        for (int floor : requestQueue) {
            if (elevatorStatus == ElevatorStatus.MOVING_UP &&
                    floor > currentFloor) {
                if (bestFloor == null || floor < bestFloor) {
                    bestFloor = floor; // Closest upward floor
                }
            } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN &&
                    floor < currentFloor) {
                if (bestFloor == null || floor > bestFloor) {
                    bestFloor = floor; // Closest downward floor
                }
            }
        }

        // Fallback if direction cannot find anything
        return bestFloor != null ? bestFloor : (!requestQueue.isEmpty() ? requestQueue.peek() : null);
    }

    /**
     * Rings the Alarm Bell - can be used as a delay to go to next floor
     */
    public void ringBell(){
        System.out.println("***Ring!***");
    }

    /**
     * Prints the Elevator's status.
     * - Current Floor
     * - Destination Floor
     * - Next Stop
     * - Elevator status (moving [direction], stopped, idle)
     * - Door Status (open,closed, locked)
     * - Mode displays when not on normal ON mode.
     */
    public void printStatus() {
        System.out.println("======== Elevator Status =========");

        // Get human-readable label for current floor
        String floorLabel = getCurrentFloorString(currentFloor);

        // Get the next floor on the way to primaryDestination
        Integer liveNextFloor = findNextFloorOnTheWay();
        String destinationLabel = (liveNextFloor == null || requestQueue.isEmpty() || liveNextFloor == currentFloor)
                ? "*Idle*"
                : getCurrentFloorString(liveNextFloor);

        // Get final destination label if applicable
        String finalDestinationLabel = (primaryDestination != -1)
                ? getCurrentFloorString(primaryDestination)
                : (requestQueue.isEmpty() ? "None" : getCurrentFloorString(findNextFloorOnTheWay()));


        System.out.println("Floor: " + floorLabel);
        System.out.println("Moving: " + elevatorStatus.getLabel() + " | Next Stop: " + destinationLabel);
        System.out.println("Final Destination: " + finalDestinationLabel);
        System.out.println("Doors: " + doorStatus);

        // Show run mode only if it’s not ON
        if (runMode != ElevatorRunMode.ON) {
            System.out.println("Mode: " + runMode);
        }

        System.out.println("==================================");
    }

    /**
     *  Translates Basement, Lobby*, and Roof values to string labels.
     * @param currentFloor - the integer of the floor
     * @return - the string value of the integer to handle Lobby, Basement, and Roof labels.
     */
    public String getCurrentFloorString(int currentFloor) {
        return switch (currentFloor) {
            case MIN_FLOOR_NUMBER -> "Basement";        // Basement
            case LOBBY_FLOOR_NUMBER -> "Lobby*";        // Lobby.Don't forget the safety star to indicate ground-floor!
            case MAX_FLOOR_NUMBER -> "Roof";            // Roof
            default -> String.valueOf(currentFloor);
        };
    }

    /**
     * Just a sleep method to condense the try/catch
     * @param ms time in millisecond
     */
    public void goToSleep(int ms) {
        try {
            Thread.sleep(ms);  // pause for half a second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // restore the interrupt flag
            System.out.println("Sleep was interrupted.");
        }
    }

    /**
     * Pushes blank lines so Display Console is more readable.
     */
    public static void clearConsole(){
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Primary destination is the first button pushed.Every following push is tested.
     * If we are going up  we accept all new values in that direction that are larger than the current target floor as the new
     * primary destination
     * If we are going down we accept all new values in that direction that are samller than the current target floor as
     * the new primary destination
     */
    private void reassignPrimaryDestination() {
        if (primaryDestination != -1 || requestQueue.isEmpty()) {
            return; // No need to reassign if already active or nothing to assign
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
            // Elevator is stopped—choose the farthest away floor in either direction
            for (int floor : requestQueue) {
                if (Math.abs(floor - currentFloor) > Math.abs(target - currentFloor)) {
                    target = floor;
                }
            }
        }

        primaryDestination = target;
    }

    /**
     * Actions taken when FIRE_SAFETY Run Mode is activated.
     * - Door opens and emergency warning plays
     * - Doors closed and are locked.
     * - User is prompted in a loop to ask to end the Fire Safety protocol.
     */
    public void fireEmergencyMode(){
        System.out.println("***Fire Emergency Mode***");
        openDoor(); // open doors
        runMode = ElevatorRunMode.FIRE_SAFETY;      // Turn Run Mode to Fire Safety
        doorStatus = DoorStatus.LOCKED;             // Lock the Door until Fire Rescue Arrives

        Scanner scanner = new Scanner(System.in);
        while(runMode == ElevatorRunMode.FIRE_SAFETY){
            printStatus();
            System.out.println("End Fire Safety Mode? [Y] or [N]?");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                closeDoor();
                runMode = ElevatorRunMode.ON;
                doorStatus = DoorStatus.CLOSED;
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
        sendElevatorToBasement();               // Send elevator to basement
        // Show restricted main menu? Give option to wait for rescue? Set Run Mode back to Normal (ON)?
        Scanner scanner = new Scanner(System.in);
        while(runMode == ElevatorRunMode.EARTHQUAKE){
            printStatus();
            System.out.println("End Earthquake Mode? [Y] or [N]?");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
                closeDoor();
                runMode = ElevatorRunMode.ON;
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
     * Have the elevator go straight to the basement.
     */
    public void sendElevatorToBasement(){
        System.out.println("***Sending Elevator to Basement***");
        elevatorStatus = ElevatorStatus.MOVING_DOWN;               // Move elevator down
        requestQueue.clear();                                      // clear requestQueue
        requestQueue.add(MIN_FLOOR_NUMBER);                        // Add Basement to request Queue
        while(currentFloor > MIN_FLOOR_NUMBER){                    // Loop until we reach the Basement
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
        System.out.println("*Elevator weight overloaded!*\n *Opening Doors.* \n*Remove excess weight before proceeding!*");
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
        doorStatus = DoorStatus.CLOSED;
        goToSleep(1500);
        System.out.println("Door is unlocked!");

    }
}
