#include <iostream>
#include <cstring>
#include <limits>
#include <thread>
#include <chrono>

int getValidInput();

// State structure
struct State {
    char color[10];
    int duration;
    bool isWarning;
};

// Abstract base class
class TrafficLightBase {
protected:
    State* states;
    int numStates;
    int currentState;

public:
    TrafficLightBase() : states(nullptr), numStates(0), currentState(0) {}
    virtual ~TrafficLightBase() {
        delete[] states;
    }

    virtual void cycle() = 0;
    const State& getCurrentState() const {
        return *(states + currentState);
    }
    virtual void displayState() const = 0;
};

// Standard Traffic Light
class StandardTrafficLight : public TrafficLightBase {
public:
    StandardTrafficLight(int redDuration = 30, int yellowDuration = 5, int greenDuration = 25) {
        numStates = 3;
        states = new State[numStates];

        strcpy(states[0].color, "RED");
        states[0].duration = redDuration;
        states[0].isWarning = false;

        strcpy(states[1].color, "YELLOW");
        states[1].duration = yellowDuration;
        states[1].isWarning = true;

        strcpy(states[2].color, "GREEN");
        states[2].duration = greenDuration;
        states[2].isWarning = false;
    }

    void cycle() override {
        displayState();
        std::this_thread::sleep_for(std::chrono::seconds((states + currentState)->duration));
        currentState = (currentState + 1) % numStates;
    }

    void displayState() const override {
        std::cout << "\nStandard Traffic Light Status:\n";
        std::cout << "-----------------------------\n";
        std::cout << "Current State: " << (states + currentState)->color
                  << " (" << (states + currentState)->duration << " seconds)\n";
        if ((states + currentState)->isWarning)
            std::cout << "WARNING: Prepare to stop!\n";
    }
};

class PedestrianTrafficLight : public TrafficLightBase {
public:
    PedestrianTrafficLight(int dontWalkDuration = 30, int walkDuration = 20) {
        numStates = 2;
        states = new State[numStates];

        strcpy(states[0].color, "DONT_WALK");
        states[0].duration = dontWalkDuration;
        states[0].isWarning = false;

        strcpy(states[1].color, "WALK");
        states[1].duration = walkDuration;
        states[1].isWarning = false;
    }

    void cycle() override {
        displayState();
        std::this_thread::sleep_for(std::chrono::seconds((states + currentState)->duration));
        currentState = (currentState + 1) % numStates;
    }

    void displayState() const override {
        std::cout << "\nPedestrian Light Status:\n";
        std::cout << "------------------------\n";
        std::cout << "Current State: " << (states + currentState)->color
                  << " (" << (states + currentState)->duration << " seconds)\n";
    }
};

class TrafficLightManager {
private:
    TrafficLightBase** controllers;
    int numControllers;
    int capacity;

public:
    TrafficLightManager() : controllers(nullptr), numControllers(0), capacity(0) {}

    ~TrafficLightManager() {
        for (int i = 0; i < numControllers; ++i)
            delete controllers[i];
        delete[] controllers;
    }

    void addController(TrafficLightBase* controller) {
        if (numControllers >= capacity) {
            int newCapacity = (capacity == 0) ? 1 : capacity * 2;
            TrafficLightBase** newArr = new TrafficLightBase*[newCapacity];
            for (int i = 0; i < numControllers; ++i)
                newArr[i] = controllers[i];
            delete[] controllers;
            controllers = newArr;
            capacity = newCapacity;
        }
        controllers[numControllers++] = controller;
    }

    void removeController(int index) {
        if (index < 0 || index >= numControllers) {
            std::cout << "Invalid index!\n";
            return;
        }

        delete controllers[index];
        for (int i = index; i < numControllers - 1; ++i)
            controllers[i] = controllers[i + 1];
        --numControllers;
    }

    void displayControllers() const {
        if (numControllers == 0) {
            std::cout << "No traffic lights are currently managed.\n";
            return;
        }

        std::cout << "\nCurrent Traffic Lights:\n";
        std::cout << "------------------------\n";
        for (int i = 0; i < numControllers; ++i) {
            std::cout << i + 1 << ". ";
            if (dynamic_cast<StandardTrafficLight*>(controllers[i]))
                std::cout << "Standard Traffic Light\n";
            else
                std::cout << "Pedestrian Traffic Light\n";
        }
    }

    void cycleAll() {
        for (int i = 0; i < numControllers; ++i)
            controllers[i]->cycle();
    }

    int getNumControllers() const {
        return numControllers;
    }

    void addCustomStandardTrafficLight() {
        std::cout << "Enter RED duration: ";
        int red = getValidInput();
        std::cout << "Enter YELLOW duration: ";
        int yellow = getValidInput();
        std::cout << "Enter GREEN duration: ";
        int green = getValidInput();
        addController(new StandardTrafficLight(red, yellow, green));
        std::cout << "Custom Standard Traffic Light added.\n";
    }

    void addCustomPedestrianTrafficLight() {
        std::cout << "Enter DONT WALK duration: ";
        int dontWalk = getValidInput();
        std::cout << "Enter WALK duration: ";
        int walk = getValidInput();
        addController(new PedestrianTrafficLight(dontWalk, walk));
        std::cout << "Custom Pedestrian Traffic Light added.\n";
    }
};

int getValidInput() {
    int input;
    while (!(std::cin >> input) || input <= 0) {
        std::cout << "Invalid input. Please enter a positive number: ";
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    }
    return input;
}

//  Main menu
void displayMenu() {
    std::cout << "\nTraffic Light Manager Menu\n";
    std::cout << "--------------------------\n";
    std::cout << "1. Add Custom Standard Traffic Light\n";
    std::cout << "2. Add Custom Pedestrian Traffic Light\n";
    std::cout << "3. Remove Traffic Light\n";
    std::cout << "4. Display All Traffic Lights\n";
    std::cout << "5. Cycle All Traffic Lights\n";
    std::cout << "6. Exit\n";
    std::cout << "Enter your choice (1 -6):";
}

int main() {
    TrafficLightManager manager;
    bool running = true;

    std::cout << "Welcome to Traffic Light Manager!\n";
    std::cout << "=================================\n";

    while (running) {
        displayMenu();
        int choice = getValidInput();

        switch (choice) {
            case 1:
                manager.addCustomStandardTrafficLight();
                break;
            case 2:
                manager.addCustomPedestrianTrafficLight();
                break;
            case 3:
                if (manager.getNumControllers() == 0) {
                    std::cout << "No controllers to remove.\n";
                    break;
                }
                manager.displayControllers();
                std::cout << "Enter the number to remove: ";
                manager.removeController(getValidInput() - 1);
                std::cout << "Traffic Light removed.\n";
                break;
            case 4:
                manager.displayControllers();
                break;
            case 5:
                if (manager.getNumControllers() == 0) {
                    std::cout << "No controllers to cycle.\n";
                    break;
                }
                std::cout << "Cycling all controllers...\n";
                manager.cycleAll();
                break;
            case 6:
                running = false;
                std::cout << "Exiting program...\n";
                break;
            default:
                std::cout << "Invalid choice. Please enter 1–6.\n";
        }
    }

    return 0;
}
