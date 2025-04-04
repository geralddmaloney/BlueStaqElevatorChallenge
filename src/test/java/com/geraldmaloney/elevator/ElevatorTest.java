package com.geraldmaloney.elevator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {

    private AbstractElevator elevator;

    /**
     * Setup a fresh elevator instance before each test.
     */
    @BeforeEach
    public void setup() {
        elevator = new PassengerElevator();
    }

    /**
     * Test that a valid floor press adds the floor to the queue.
     */
    @Test
    public void testValidFloorEntersQueue() {
        elevator.pressFloor(3);
        assertFalse(isRequestQueueEmpty(elevator), "Floor 3 should be added to the queue.");
    }

    /**
     * Test that an invalid floor below the minimum is rejected.
     */
    @Test
    public void testRejectsInvalidFloorBelowMin() {
        elevator.pressFloor(-1);
        assertTrue(isRequestQueueEmpty(elevator), "Invalid floor -1 should not be added.");
    }

    /**
     * Test that an invalid floor above the maximum is rejected.
     */
    @Test
    public void testRejectsInvalidFloorAboveMax() {
        elevator.pressFloor(99);
        assertTrue(isRequestQueueEmpty(elevator), "Invalid floor 99 should not be added.");
    }

    /**
     * Test that duplicate floor requests are ignored.
     */
    @Test
    public void testDuplicateFloorIgnored() {
        elevator.pressFloor(5);
        elevator.pressFloor(5); // duplicate
        assertEquals(1, getQueueSize(elevator), "Only one entry for floor 5 should exist.");
    }

    /**
     * Test that label mapping correctly returns friendly floor names.
     */
    @Test
    public void testFloorLabelMapping() {
        assertEquals("Basement", elevator.getCurrentFloorString(AbstractElevator.MIN_FLOOR_NUMBER));
        assertEquals("Lobby*", elevator.getCurrentFloorString(AbstractElevator.LOBBY_FLOOR_NUMBER));
        assertEquals("Roof", elevator.getCurrentFloorString(AbstractElevator.MAX_FLOOR_NUMBER));
    }

    /**
     * Helper method to check if the elevator's request queue is empty.
     */
    private boolean isRequestQueueEmpty(AbstractElevator elevator) {
        try {
            Field field = AbstractElevator.class.getDeclaredField("requestQueue");
            field.setAccessible(true);
            Queue<Integer> queue = (Queue<Integer>) field.get(elevator);
            return queue.isEmpty();
        } catch (Exception e) {
            fail("Reflection failed to access requestQueue.");
            return true;
        }
    }

    /**
     * Helper method to check the number of elements in the request queue.
     */
    private int getQueueSize(AbstractElevator elevator) {
        try {
            Field field = AbstractElevator.class.getDeclaredField("requestQueue");
            field.setAccessible(true);
            Queue<Integer> queue = (Queue<Integer>) field.get(elevator);
            return queue.size();
        } catch (Exception e) {
            fail("Reflection failed to access requestQueue.");
            return -1;
        }
    }
}
