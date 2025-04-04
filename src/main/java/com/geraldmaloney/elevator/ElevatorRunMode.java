package com.geraldmaloney.elevator;

public enum ElevatorRunMode {
    ON,                             // Elevator Power ON
    OFF,                            // Elevator Power OFF. Cannot move or do anything.
    OVERLOAD,                       // Alarm Sounds. Elevator Refuses to Move. 2000lbs Max.
    ESTOP,                          // E-Stop Button Pressed. Stop Elevator on current floor. Prompt asks user what is wrong
    FIRE_SAFETY,                    // Look at last floor and ElevatorStatus for direction. Close door. Lock Door
    FIRE_RESCUE,                    // E-Stop Unlock. Activates rescue menu: what happened question prompts which state to move to
    EARTHQUAKE,                     // User Triggered by E-Stop question in lieu of an onboard seismograph: Stops at nearest floor, Opens door if safe, Power OFF.
    HURRICANE,                      // User Triggered by E-Stop question in lieu of an onboard hazardous weather sensors
}
