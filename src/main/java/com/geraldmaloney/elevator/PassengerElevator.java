package com.geraldmaloney.elevator;

import java.util.PriorityQueue;
import java.util.Comparator;

public class PassengerElevator extends AbstractElevator {

    public PassengerElevator() {
        super();
    }

    /**
     * Handles user floor requests.
     * Validates input, updates destination logic, and adds to queue.
     */
    @Override
    public void pressFloor(int floor) {
        if (floor < MIN_FLOOR_NUMBER || floor > MAX_FLOOR_NUMBER) {
            System.out.println("Invalid Floor Number! Basement is Floor " + MIN_FLOOR_NUMBER +
                    ", Roof is " + MAX_FLOOR_NUMBER + ".\nPlease Try Again!");
            return;
        }

        if (requestQueue.contains(floor) || floor == currentFloor) {
            System.out.println("Floor already requested or you are already on that floor.");
            return;
        }

        boolean noActiveMission = (primaryDestination == -1 || requestQueue.isEmpty());

        if (noActiveMission) {
            primaryDestination = floor;
        } else if (elevatorStatus == ElevatorStatus.MOVING_UP) {
            if (floor > currentFloor && floor > primaryDestination) {
                primaryDestination = floor;
            }
        } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
            if (floor < currentFloor && floor < primaryDestination) {
                primaryDestination = floor;
            }
        }

        PriorityQueue<Integer> rebuiltQueue = new PriorityQueue<>(getFloorComparator());
        rebuiltQueue.addAll(requestQueue);
        rebuiltQueue.add(floor);
        requestQueue = rebuiltQueue;

        String label = getCurrentFloorString(floor);
        System.out.println("Floor " + label + " pressed and added to floor request queue!");
    }

    /**
     * Handles elevator motion logic.
     * Moves toward next floor, manages arrivals, and state transitions.
     */
    @Override
    public void goToNextFloor() {
        if (requestQueue.isEmpty()) {
            elevatorStatus = ElevatorStatus.STOPPED;
            primaryDestination = -1;
            System.out.println("No floor requests in queue");
            return;
        }

        Integer nextFloor = findNextFloorOnTheWay();
        if (nextFloor == null) {
            elevatorStatus = ElevatorStatus.STOPPED;
            System.out.println("No valid next floor to move to.");
            return;
        }
        if (nextFloor == currentFloor) {
            elevatorStatus = ElevatorStatus.STOPPED;

            if (requestQueue.contains(currentFloor)) {
                requestQueue.remove(currentFloor);
            }

            System.out.println("Arrived at floor " + getCurrentFloorString(currentFloor) + " (" + elevatorStatus.getLabel() + ")");
            goToSleep(2500);
            openDoor();

            if (currentFloor == primaryDestination || requestQueue.isEmpty()) {
                primaryDestination = -1;
                reassignPrimaryDestination();
            }

            return;
        }

        if (nextFloor > currentFloor) {
            elevatorStatus = ElevatorStatus.MOVING_UP;
            System.out.println("Moving up toward floor " + getCurrentFloorString(nextFloor));
        } else if (nextFloor < currentFloor) {
            elevatorStatus = ElevatorStatus.MOVING_DOWN;
            System.out.println("Moving down toward floor " + getCurrentFloorString(nextFloor));
        }
        System.out.println("==================================");
        goToSleep(1500);

        if (elevatorStatus == ElevatorStatus.MOVING_UP) {
            currentFloor++;
        } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
            currentFloor--;
        }

        if (requestQueue.contains(currentFloor)) {
            elevatorStatus = ElevatorStatus.STOPPED;
            requestQueue.remove(currentFloor);

            System.out.println("Arrived at floor " + getCurrentFloorString(currentFloor) + " (" + elevatorStatus.getLabel() + ")");
            goToSleep(2500);
            openDoor();

            if (currentFloor == primaryDestination || requestQueue.isEmpty()) {
                primaryDestination = -1;
            }

            reassignPrimaryDestination();
        }
    }

    /**
     * Displays current elevator state.
     * Includes floor location, next stop, final destination, and run mode.
     */
    @Override
    public void printStatus() {
        System.out.println("======== Elevator Status =========");

        String floorLabel = getCurrentFloorString(currentFloor);
        Integer liveNextFloor = findNextFloorOnTheWay();

        String destinationLabel = (liveNextFloor == null || requestQueue.isEmpty() || liveNextFloor == currentFloor)
                ? "*Idle*"
                : getCurrentFloorString(liveNextFloor);

        String finalDestinationLabel = (primaryDestination != -1)
                ? getCurrentFloorString(primaryDestination)
                : (requestQueue.isEmpty() ? "None" : getCurrentFloorString(findNextFloorOnTheWay()));

        System.out.println("Floor: " + floorLabel);
        System.out.println("Moving: " + elevatorStatus.getLabel() + " | Next Stop: " + destinationLabel);
        System.out.println("Final Destination: " + finalDestinationLabel);
        System.out.println("Doors: " + doorStatus);

        if (runMode != ElevatorRunMode.ON) {
            System.out.println("Mode: " + runMode);
        }

        System.out.println("==================================");
    }

    /**
     * Determines the next floor on the way to the current primary destination.
     * Prioritizes closest matching direction.
     */
    private Integer findNextFloorOnTheWay() {
        Integer bestFloor = null;

        // Determine active direction if currently stopped
        ElevatorStatus effectiveDirection = elevatorStatus;
        if (elevatorStatus == ElevatorStatus.STOPPED && primaryDestination != -1) {
            if (primaryDestination > currentFloor) {
                effectiveDirection = ElevatorStatus.MOVING_UP;
            } else if (primaryDestination < currentFloor) {
                effectiveDirection = ElevatorStatus.MOVING_DOWN;
            }
        }

        for (int floor : requestQueue) {
            if (effectiveDirection == ElevatorStatus.MOVING_UP && floor > currentFloor) {
                if (bestFloor == null || floor < bestFloor) {
                    bestFloor = floor;
                }
            } else if (effectiveDirection == ElevatorStatus.MOVING_DOWN && floor < currentFloor) {
                if (bestFloor == null || floor > bestFloor) {
                    bestFloor = floor;
                }
            }
        }

        // If no valid floor in current direction, return primary destination
        if (bestFloor == null && primaryDestination != -1 && primaryDestination != currentFloor) {
            bestFloor = primaryDestination;
        }

        return bestFloor;
    }


    /**
     * Creates comparator to order the floor request queue.
     * Sorting is direction-aware or fallback to proximity.
     */
    private Comparator<Integer> getFloorComparator() {
        return (a, b) -> {
            if (elevatorStatus == ElevatorStatus.MOVING_UP) {
                return Integer.compare(a, b);
            } else if (elevatorStatus == ElevatorStatus.MOVING_DOWN) {
                return Integer.compare(b, a);
            } else {
                int distA = Math.abs(a - currentFloor);
                int distB = Math.abs(b - currentFloor);
                return Integer.compare(distA, distB);
            }
        };
    }
}
