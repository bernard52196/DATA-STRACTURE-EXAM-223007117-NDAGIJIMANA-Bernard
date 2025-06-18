# Traffic Light Manager

## Assigned Task
Create a C++ program that simulates a traffic light management system with the following requirements:

- Define a `State` structure to hold traffic light state information
- Create an abstract base class for traffic lights
- Implement two types of traffic lights (Standard and Pedestrian)
- Manage multiple traffic lights using dynamic arrays
- Demonstrate inheritance and polymorphism
- Implement proper memory management
- Provide a menu-driven interface for user interaction
- Annotate code with comments explaining each line

---

## How the Task Was Completed

The program is implemented in C++ and uses object-oriented programming principles. It features a menu-driven interface for managing standard and pedestrian traffic lights, with dynamic memory management and detailed code comments.

---

## Fully Annotated Code (Every Line Commented)

```cpp
#include <iostream> // Include for input/output stream operations
#include <cstring> // Include for C-style string functions (strcpy)
#include <limits> // Include for numeric limits (input validation)
#include <thread> // Include for thread sleep functionality
#include <chrono> // Include for time durations

// Forward declaration for input validation function
int getValidInput(); // Declares a function to get a valid integer from user

// Structure to represent a traffic light state
struct State {
    char color[10];      // Stores the color name (e.g., "RED", "GREEN")
    int duration;        // Stores the duration in seconds for this state
    bool isWarning;      // Indicates if this state is a warning (e.g., yellow)
}; // End of State struct

// Abstract base class for all traffic lights
class TrafficLightBase {
protected: // Protected members accessible by derived classes
    State* states;       // Pointer to dynamically allocated array of states
    int numStates;       // Number of states in the traffic light
    int currentState;    // Index of the current state

public: // Public interface
    TrafficLightBase() : states(nullptr), numStates(0), currentState(0) {} // Constructor initializes members
    virtual ~TrafficLightBase() { delete[] states; } // Virtual destructor for cleanup

    virtual void cycle() = 0; // Pure virtual function to cycle states
    const State& getCurrentState() const { return *(states + currentState); } // Returns current state
    virtual void displayState() const = 0; // Pure virtual function to display state
}; // End of TrafficLightBase class

// Class for standard (vehicle) traffic lights
class StandardTrafficLight : public TrafficLightBase {
public: // Public interface
    StandardTrafficLight(int redDuration = 30, int yellowDuration = 5, int greenDuration = 25) { // Constructor with default durations
        numStates = 3; // There are three states: Red, Yellow, Green
        states = new State[numStates]; // Allocate memory for states

        strcpy(states[0].color, "RED"); // Set color to RED
        states[0].duration = redDuration; // Set duration for RED
        states[0].isWarning = false; // RED is not a warning

        strcpy(states[1].color, "YELLOW"); // Set color to YELLOW
        states[1].duration = yellowDuration; // Set duration for YELLOW
        states[1].isWarning = true; // YELLOW is a warning

        strcpy(states[2].color, "GREEN"); // Set color to GREEN
        states[2].duration = greenDuration; // Set duration for GREEN
        states[2].isWarning = false; // GREEN is not a warning
    } // End of constructor

    void cycle() override { // Override cycle method
        displayState(); // Show current state
        std::this_thread::sleep_for(std::chrono::seconds((states + currentState)->duration)); // Wait for duration
        currentState = (currentState + 1) % numStates; // Move to next state (wrap around)
    } // End of cycle

    void displayState() const override { // Override displayState method
        std::cout << "\nStandard Traffic Light Status:\n"; // Print header
        std::cout << "-----------------------------\n"; // Print separator
        std::cout << "Current State: " << (states + currentState)->color // Print color
                  << " (" << (states + currentState)->duration << " seconds)\n"; // Print duration
        if ((states + currentState)->isWarning) // If current state is a warning
            std::cout << "WARNING: Prepare to stop!\n"; // Print warning message
    } // End of displayState
}; // End of StandardTrafficLight class

// Class for pedestrian traffic lights
class PedestrianTrafficLight : public TrafficLightBase {
public: // Public interface
    PedestrianTrafficLight(int dontWalkDuration = 30, int walkDuration = 20) { // Constructor with default durations
        numStates = 2; // There are two states: DONT_WALK, WALK
        states = new State[numStates]; // Allocate memory for states

        strcpy(states[0].color, "DONT_WALK"); // Set color to DONT_WALK
        states[0].duration = dontWalkDuration; // Set duration for DONT_WALK
        states[0].isWarning = false; // Not a warning

        strcpy(states[1].color, "WALK"); // Set color to WALK
        states[1].duration = walkDuration; // Set duration for WALK
        states[1].isWarning = false; // Not a warning
    } // End of constructor

    void cycle() override { // Override cycle method
        displayState(); // Show current state
        std::this_thread::sleep_for(std::chrono::seconds((states + currentState)->duration)); // Wait for duration
        currentState = (currentState + 1) % numStates; // Move to next state (wrap around)
    } // End of cycle

    void displayState() const override { // Override displayState method
        std::cout << "\nPedestrian Light Status:\n"; // Print header
        std::cout << "------------------------\n"; // Print separator
        std::cout << "Current State: " << (states + currentState)->color // Print color
                  << " (" << (states + currentState)->duration << " seconds)\n"; // Print duration
    } // End of displayState
}; // End of PedestrianTrafficLight class

// Manager class for multiple traffic lights
class TrafficLightManager {
private: // Private members
    TrafficLightBase** controllers; // Array of pointers to traffic light controllers
    int numControllers;             // Number of controllers
    int capacity;                   // Capacity of the array

public: // Public interface
    TrafficLightManager() : controllers(nullptr), numControllers(0), capacity(0) {} // Constructor

    ~TrafficLightManager() { // Destructor
        for (int i = 0; i < numControllers; ++i) // Loop through controllers
            delete controllers[i]; // Delete each controller
        delete[] controllers; // Delete the array
    } // End of destructor

    void addController(TrafficLightBase* controller) { // Add a new controller
        if (numControllers >= capacity) { // If array is full
            int newCapacity = (capacity == 0) ? 1 : capacity * 2; // Double capacity or start at 1
            TrafficLightBase** newArr = new TrafficLightBase*[newCapacity]; // Allocate new array
            for (int i = 0; i < numControllers; ++i) // Copy old controllers
                newArr[i] = controllers[i]; // Copy pointer
            delete[] controllers; // Delete old array
            controllers = newArr; // Update pointer
            capacity = newCapacity; // Update capacity
        } // End if
        controllers[numControllers++] = controller; // Add new controller and increment count
    } // End of addController

    void removeController(int index) { // Remove controller at index
        if (index < 0 || index >= numControllers) { // Validate index
            std::cout << "Invalid index!\n"; // Print error
            return; // Exit function
        } // End if

        delete controllers[index]; // Delete controller
        for (int i = index; i < numControllers - 1; ++i) // Shift controllers
            controllers[i] = controllers[i + 1]; // Move next controller up
        --numControllers; // Decrement count
    } // End of removeController

    void displayControllers() const { // Display all controllers
        if (numControllers == 0) { // If none
            std::cout << "No traffic lights are currently managed.\n"; // Print message
            return; // Exit function
        } // End if

        std::cout << "\nCurrent Traffic Lights:\n"; // Print header
        std::cout << "------------------------\n"; // Print separator
        for (int i = 0; i < numControllers; ++i) { // Loop through controllers
            std::cout << i + 1 << ". "; // Print index
            if (dynamic_cast<StandardTrafficLight*>(controllers[i])) // If standard
                std::cout << "Standard Traffic Light\n"; // Print type
            else // Otherwise
                std::cout << "Pedestrian Traffic Light\n"; // Print type
        } // End for
    } // End of displayControllers

    void cycleAll() { // Cycle all controllers
        for (int i = 0; i < numControllers; ++i) // Loop through controllers
            controllers[i]->cycle(); // Cycle each controller
    } // End of cycleAll

    int getNumControllers() const { return numControllers; } // Return number of controllers

    void addCustomStandardTrafficLight() { // Add custom standard light
        std::cout << "Enter RED duration: "; // Prompt
        int red = getValidInput(); // Get RED duration
        std::cout << "Enter YELLOW duration: "; // Prompt
        int yellow = getValidInput(); // Get YELLOW duration
        std::cout << "Enter GREEN duration: "; // Prompt
        int green = getValidInput(); // Get GREEN duration
        addController(new StandardTrafficLight(red, yellow, green)); // Add new controller
        std::cout << "Custom Standard Traffic Light added.\n"; // Confirm
    } // End of addCustomStandardTrafficLight

    void addCustomPedestrianTrafficLight() { // Add custom pedestrian light
        std::cout << "Enter DONT WALK duration: "; // Prompt
        int dontWalk = getValidInput(); // Get DONT WALK duration
        std::cout << "Enter WALK duration: "; // Prompt
        int walk = getValidInput(); // Get WALK duration
        addController(new PedestrianTrafficLight(dontWalk, walk)); // Add new controller
        std::cout << "Custom Pedestrian Traffic Light added.\n"; // Confirm
    } // End of addCustomPedestrianTrafficLight
}; // End of TrafficLightManager class

// Function to get a valid positive integer from user
int getValidInput() {
    int input; // Variable to store input
    while (!(std::cin >> input) || input <= 0) { // While input is invalid or not positive
        std::cout << "Invalid input. Please enter a positive number: "; // Prompt again
        std::cin.clear(); // Clear error state
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n'); // Ignore bad input
    } // End while
    return input; // Return valid input
} // End of getValidInput

// Function to display the main menu
void displayMenu() {
    std::cout << "\nTraffic Light Manager Menu\n"; // Print header
    std::cout << "--------------------------\n"; // Print separator
    std::cout << "1. Add Custom Standard Traffic Light\n"; // Option 1
    std::cout << "2. Add Custom Pedestrian Traffic Light\n"; // Option 2
    std::cout << "3. Remove Traffic Light\n"; // Option 3
    std::cout << "4. Display All Traffic Lights\n"; // Option 4
    std::cout << "5. Cycle All Traffic Lights\n"; // Option 5
    std::cout << "6. Exit\n"; // Option 6
    std::cout << "Enter your choice (1 -6):"; // Prompt for choice
} // End of displayMenu

// Main function: entry point of the program
int main() {
    TrafficLightManager manager; // Create manager object
    bool running = true; // Program running flag

    std::cout << "Welcome to Traffic Light Manager!\n"; // Welcome message
    std::cout << "=================================\n"; // Separator

    while (running) { // Main loop
        displayMenu(); // Show menu
        int choice = getValidInput(); // Get user choice

        switch (choice) { // Handle user choice
            case 1: // If 1
                manager.addCustomStandardTrafficLight(); // Add standard light
                break; // Exit case
            case 2: // If 2
                manager.addCustomPedestrianTrafficLight(); // Add pedestrian light
                break; // Exit case
            case 3: // If 3
                if (manager.getNumControllers() == 0) { // If none to remove
                    std::cout << "No controllers to remove.\n"; // Print message
                    break; // Exit case
                } // End if
                manager.displayControllers(); // Show controllers
                std::cout << "Enter the number to remove: "; // Prompt
                manager.removeController(getValidInput() - 1); // Remove selected
                std::cout << "Traffic Light removed.\n"; // Confirm
                break; // Exit case
            case 4: // If 4
                manager.displayControllers(); // Show all controllers
                break; // Exit case
            case 5: // If 5
                if (manager.getNumControllers() == 0) { // If none to cycle
                    std::cout << "No controllers to cycle.\n"; // Print message
                    break; // Exit case
                } // End if
                std::cout << "Cycling all controllers...\n"; // Print message
                manager.cycleAll(); // Cycle all
                break; // Exit case
            case 6: // If 6
                running = false; // Set running to false to exit
                std::cout << "Exiting program...\n"; // Print exit message
                break; // Exit case
            default: // For any other input
                std::cout << "Invalid choice. Please enter 1–6.\n"; // Print error
        } // End switch
    } // End while

    return 0; // Return 0 to indicate successful execution
} // End of main
```

---

## How to Use

2. Run the executable.
3. Use the menu to add, remove, display, and cycle traffic lights.

---

## Author
Bernard 
