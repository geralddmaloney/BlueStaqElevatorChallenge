package com.geraldmaloney.elevator;
/**
 * Tracks the motion status of the elevator.
 * Used in safety logic to determine valid operations (e.g., blocking door open while moving).
 */
enum ElevatorStatus{
    STOPPED("[X]STOPPED[X]"),                // Elevator is stopped; thus waiting for next instruction
    MOVING_UP("[^]UP[^]"),              // Elevator is moving up. May also be passed as a Request from another floor. Someone on floor N presses the Up button
    MOVING_DOWN("[V]DOWN[V]");            // Elevator is moving down. May also be passed as a Request from another floor. Someone on floor N presses the Down button

    private final String label;

    ElevatorStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}

