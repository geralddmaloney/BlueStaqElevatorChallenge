# Elevator Simulation — Gerald D. Maloney, Senior Software Engineer Candidate

Elevator simulation for an interactive console-driven system.  
Developed in response to a BlueStaq software engineering challenge.

---

## 1. Project Title & Candidate Info

- Project: Elevator Control System Simulation
- Author: Gerald Maloney
- Position: Senior Software Engineer candidate
- Challenge Context: 2–3 day focused build window outside normal working hours
- Time Used: ~9–10 hours total  
  • 3 hours on research, environment setup, design, and prototyping  
  • 4.5–5.5 hours coding the complete product  
  • 1.5 hours testing, README writing, and polish

---

## 2. Overview

This project simulates a real-world elevator system using Java. 
It models stateful behavior including direction-aware traversal, queued floor requests, emergency protocols, and modular architecture. 
The system utilizes a SCAN-style elevator scheduler, prioritizing direction-first travel and intelligent mid-route floor stops.
User input is captured via a console interface with stepwise cycle logic. 
All system states are clearly displayed at runtime for user visibility and validation. 
The system has been tested interactively and is supported with very basic unit tests for key input logic and proof of use.

---

## 3. Architecture

- AbstractElevator: Defines shared elevator state, control logic, and emergency handling
- PassengerElevator: Implements public passenger elevator traversal and request queue prioritization
- Main: Console-based interface and control loop
- Enums:
    - ElevatorStatus: Movement state (up, down, stopped)
    - DoorStatus: Physical door state
    - ElevatorRunMode: Emergency and operational modes
- *DEPRECATED* Elevator: First attempt at Elevator class. Fully functional, but later replaced by an abstract class to support extensibility.

System is organized around modular OOP principles. 
The core elevator is decoupled from its interface, and future elevator types (freight, express) can be derived from AbstractElevator.

---

## 4. Core Features

- Directional SCAN-style traversal
- Request queue prioritization based on movement direction
- Primary destination tracking with in-path intermediate stops
- Dynamic reassignment of final destination post-arrival
- Emergency Stop (E-Stop) menu with selectable emergency modes
- Safety protocol handling (fire, earthquake, overload)
- Locked door logic for emergency states
- Door open/close simulation with safeguards
- Duplicate and invalid input rejection
- Console interface with clean loop and menu refresh
- Unit test coverage for core input and validation behaviors

---

## 5. Design Decisions

- Request queue is managed as a PriorityQueue with custom comparator to match elevator direction
- Final destination is tracked separately from intermediate stops to maintain directional integrity
- AbstractElevator defines the full FSM and base system behavior; PassengerElevator only implements unique subclass behavior
- All user input is normalized through a single entry point, then validated and parsed into actionable floor values.
- Door logic includes distinct handling for emergency states, such as overload and E-STOP

System behavior mimics real elevator logic—requests do not cause direction reversal unless the mission is complete. Interrupts are queued but honored only when direction permits.

---

## 6. How to Run

1. Install Java (version 17+) and Gradle
2. Clone or download the repository
3. Run Main.java directly in IntelliJ or another IDE with Gradle support or via Command Line.

---

## 7. How to Test

JUnit tests are located in: ElevatorTest.java

Or right-click `ElevatorTest` in your IDE and select "Run Tests."

Tests cover:
- Valid floor input
- Invalid floor rejection
- Queue deduplication
- Floor label mapping

Automated testing was not part of the assignment, but it is best practice for software engineers to write Unit Tests for 
their code prior to QA/Testing takes over. And I wanted to show I can adapt to new tools on the fly. 
I’m experienced with NUnit in Visual Studio, but transitioning to IntelliJ IDEA and JUnit was straightforward after understanding the differences between Java bytecode and C-based machine code compilation. 
It was a fun shift—especially discovering how Java treats enums like objects. Kind of wild you can treat enums like objects in Java, but I digress. 

---

## 8. Known Limitations

- Only one elevator is supported; no dispatcher or multi-elevator coordination
- No GUI or visualization beyond console output
- No logging

---

## 9. What I'd Do Next

- Replace magic strings and numbers with enums or constants for maintainability and readability.
- Clean up door lock, unlock, open, close, sequence and tie states together logically.
- Extract safety protocols (fire, earthquake, etc.) into dedicated strategy modules, so they can be swapped 
according to elevator type. This would amend some duplicate control path logic in the code.
- Implement an elevator controller  
- Implement dispatcher logic for multi-elevator systems
- Implement random Passengers on random floors requesting a ride
- Add logging
- Build a GUI visualization of transitions and events
- Expand testing to cover emergency protocols and more sequenced floor testing
- Consider multithreading the elevator for passive state updates and input decoupling; make this time driven instead of loop
driven
- General readability cleanup after above list is completed
- Beef up JUnit testing. I just demo'ed a sample to show I can.

---

## 10. Personal Reflection

Upon receiving this challenge, I recognized it as a deliberately open-ended test of problem-solving and execution 
under undefined constraints. 

So, starting with my environment. I installed a trial license of IntelliJ's IDEA, all needed transitive libraries, 
configured the environment, and got a Hello World running.

From there, I began by doing some research on elevators and their features and safety protocols and began 
reverse engineering requirements. That’s when I began thinking critically about how elevators prioritize queued floor requests in real time.
Next, I looked to see what industry practiced algorithms are used for elevators and came across SCAN-style tree traversal algorithms. 
After getting a point-to-point elevator and door system working, I began implementing the SCAN-style scheduler into the elevator. 
Once testing was complete for that, I moved on to implementing a safety menu, because key requirements for elevators revolve around passenger or payload safety. 
I researched common protocols: Fire, Earthquake, Hurricane, et al. and implemented simulated safety protocols to control various run modes and door status states.
Once that was complete, I implemented simple JUnit tests to showcase I can perform JUnit automated testing.
Finally, I focused on modularity and scalability—refactoring the original `Elevator` class into an `AbstractElevator` base, which `PassengerElevator` inherits from. 
This provides a clean foundation for future elevator types (freight, express, etc.).



From my perspective, this project was developed to demonstrate my ability to model real-world systems and design modular solutions.
It was built across 2 days in focused working sessions outside of full-time responsibilities.

My goal was not to over-engineer the solution, but to build something representative of how I approach system problems:
- Solve what matters first
- Prove function under constraints
- Leave room for evolution and expansion


## About Me

Hi, I’m Gerald Maloney. I moved from the Chicagoland area to Colorado Springs in 2018 with my wife, Alyssa. 
Together, we’re raising two amazing and bright daughters— Harper (5) and Nora (3). 

### My Hobbies:
In no particular order:  
3D Printing, AI, Laser Engraving, 
All Things Games (PC, tabletop, cards, board), Drone flying & building, 
Marksmanship, Cooking, Whiskey Collecting, Boxing/MMA, Books(philosophy, tech manuals, some classics), 
Old Kung Fu Movies, and Sci-Fi TV.

### What Drives Me:
- My Family.
- Challenge and growth—if I’m not building, I’m not breathing
- Working with cutting-age tech and modernizing legacy systems  
- Serving the Warfighter—*“To serve those that serve. One Team; One Fight.”*

This project was more than a challenge—it was a chance to model systems, explore modular design under time constraints, and showcase the kind of engineer I am:  
Fast. Precise. Adaptable. Real.

If you’ve read this far—thank you for your time and consideration.  
Looking forward to our conversation!

—
**Gerald Maloney**

