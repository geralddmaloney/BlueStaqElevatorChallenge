# Elevator Simulation — Gerald D. Maloney Senior Software Engineer Candidate

Elevator simulation for an interactive console-driven system.  
Developed in response to a BlueStaq software engineering challenge.

---

## 1. Project Title & Candidate Info

- Project: Elevator Control System Simulation
- Author: Gerald Maloney
- Position: Senior Software Engineer candidate
- Challenge Context: 2–3 day focused build window outside normal working hours
- Time Used: ~9-10 hours from start to finish. 3 hours Research, Setup, Design, Prototyping methods. 
4.5-5.5 hours of coding the complete product, and 1.5 hours for testing and README.    

---

## 2. Overview

This project simulates a real-world elevator system using Java. 
It models stateful behavior including direction-aware traversal, queued floor requests, emergency protocols, and modular architecture. 
The system utilizes a SCAN-style elevator scheduler, prioritizing direction-first travel and intelligent mid-route floor stops.
User input is captured via a console interface with stepwise cycle logic. 
All state is printed clearly for user validation. The system has been tested interactively and is supported with basic unit tests for key input logic.

---

## 3. Architecture

- AbstractElevator: Defines shared elevator state, control logic, and emergency handling
- PassengerElevator: Implements public passenger elevator traversal and request queue prioritization
- Main: Console-based interface and control loop
- Enums:
    - ElevatorStatus: Movement state (up, down, stopped)
    - DoorStatus: Physical door state
    - ElevatorRunMode: Emergency and operational modes
- *DEPRECATED* Elevator: First attempt at Elevator class. Got it working, but then opted to use an abstract class instead.

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
- All user input is validated through a single entry point and converted to numerical floors
- Door logic includes distinct handling for emergency states, such as overload and E-STOP

System behavior mimics real elevator logic—requests do not cause direction reversal unless the mission is complete. Interrupts are queued but honored only when direction permits.

---

## 6. How to Run

1. Install Java (version 17+) and Gradle
2. Clone or download the repository
3. From the project root, run:


Alternatively, you can run Main.java directly in IntelliJ or another IDE with Gradle support.

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
their code prior to QA/Testing takes over. And I wanted to show I can adapt to new tools on the fly. I am used to NUnit in 
VS2022, but IntelliJ's IDEA and JUnit have been a very easy switch for me once I understood the difference between Java ByteCode
and C-based languages Machine Code and how they differ in compilation and processing. Kind of wild you can treat enums like objects in Java,
but I digress. 

---

## 8. Known Limitations

- Only one elevator is supported; no dispatcher or multi-elevator coordination
- No GUI or visualization beyond console output
- No logging

---

## 9. What I'd Do Next

- Get rid of magic string and magic number code smells with strict data typing
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

From there, I began by doing some research on elevators and their features and safety protocols. From there, I began 
reverse engineering requirements. That was when I started to really think about how elevators make decisions regarding what floor to go
to next out of a queue or options. From there I looked to see what industry practiced algorithms are used for elevators and 
came across SCAN-style tree traversal algorithms. After getting a point to point elevator and door system working, I began 
implementing the SCAN-style scheduler into the elevator. Once testing was complete for that, I moved on to implementing a 
safety menu, because key requirements for elevators revolve around passenger or payload safety. I researched common protocols:
Fire, Earthquake, Hurricane, et al. and implemented simulated safety protocols to control various run modes and door status states.
Once that was complete, I implemented simple JUnit tests to showcase I can perform JUnit automated testing.
Finally, I thought in terms of modularity and scalability and swapped the deprecated Elevator class with an Abstract Elevator Class,
which I inherited into a Passenger Elevator. But with the Abstract Elevator Base Class other elevator types such as Freight 
Elevators, Service elevators, et al. can be created from the Abstract Elevator Class. 


From my perspective, this project was developed to demonstrate my ability to model real-world systems and design modular solutions.
It was built across 2 days in focused working sessions outside of full-time responsibilities.

My goal was not to over-engineer the solution, but to build something representative of how I approach system problems:
- Solve what matters first
- Prove function under constraints
- Leave room for evolution and expansion

Thank you for taking the time to review this submission. The opportunities BlueStaq is offering are truly exciting to me!
I look forward to meeting with you all and proving my worth for a place on Your Team! 

– Gerald Maloney


