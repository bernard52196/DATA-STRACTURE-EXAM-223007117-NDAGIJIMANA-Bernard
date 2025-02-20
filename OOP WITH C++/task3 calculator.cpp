#include <iostream>

using namespace std;


int add(int a, int b);
int subtract(int a, int b);
int multiply(int a, int b);
double divide(int a, int b);

int main() {
    int choice, num1, num2;
    
    cout << "===============================" << endl;
    cout << "      BASIC CALCULATOR        " << endl;
    cout << "===============================" << endl;
    cout << "Select operation:" << endl;
    cout << "1. Addition" << endl;
    cout << "2. Subtraction" << endl;
    cout << "3. Multiplication" << endl;
    cout << "4. Division" << endl;
    cout << "===============================" << endl;
    cout << "Enter choice (1-4): ";
    cin >> choice;
    
    if (choice < 1 || choice > 4) {
        cout << "Invalid choice! Please restart the program and enter a valid option." << endl;
        return 1;
    }
    
    cout << "Enter two numbers: ";
    cin >> num1 >> num2;
    
    cout << "===============================" << endl;
    cout << "Result: ";
    
    switch (choice) {
        case 1:
            cout << add(num1, num2) << endl;
            break;
        case 2:
            cout << subtract(num1, num2) << endl;
            break;
        case 3:
            cout << multiply(num1, num2) << endl;
            break;
        case 4:
            cout << divide(num1, num2) << endl;
            break;
    }
    
    cout << "===============================" << endl;
    return 0;
}

int add(int a, int b) {
    return a + b;
}

int subtract(int a, int b) {
    return a - b;
}

int multiply(int a, int b) {
    return a * b;
}

double divide(int a, int b) {
    if (b != 0)
        return (double)a / b;
    else {
        cout << "Error! Division by zero." << endl;
        return 0;
    }
}
