package com.geraldmaloney.elevator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *  I wanted to show code improvement ana introduced a Time-based loop in Main instead of this being prompt driven.
 *  This is in preparation for a tangible standalone unit ran on Raspberry Pi using Pi4J to use Java to control GPIO Pins
 *
 */
public class Main {
    public static void main(String[] args) {
        PassengerElevator elevator = new PassengerElevator();

        System.out.println("Welcome to the BlueStaq Elevator Simulator!\n By: Gerald maloney");
        System.out.println("===============================================");

        boolean running = true;
        long tickRateMs = 1000; // 1-second tick

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (running) {
            // Elevator logic tick
            elevator.goToNextFloor();
            elevator.printStatus();

            try {
                if (reader.ready()) {
                    String input = reader.readLine().trim().toUpperCase();
                    // Yrying to test this. Don't need the whole menu enabled...
                    switch (input) {
                        case "Q", "QUIT", "EXIT" -> {
                            System.out.println("Exiting elevator simulation...");
                            running = false;
                        }
                        case "B" -> elevator.pressFloor(PassengerElevator.MIN_FLOOR_NUMBER);
                        case "L" -> elevator.pressFloor(PassengerElevator.LOBBY_FLOOR_NUMBER);
                        case "R" -> elevator.pressFloor(PassengerElevator.MAX_FLOOR_NUMBER);
                        case "OPEN" -> elevator.openDoor();
                        case "CLOSE" -> elevator.closeDoor();
                        case "BELL" -> elevator.ringBell();
                        case "STOP" -> elevator.emergencyStopPressed();
                        default -> {
                            try {
                                int floor = Integer.parseInt(input);
                                elevator.pressFloor(floor);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Use numbers or commands (B, L, R, OPEN, CLOSE, STOP, QUIT)");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Input error: " + e.getMessage());
            }

            // Delay for next tick
            try {
                Thread.sleep(tickRateMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
