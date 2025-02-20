#include <iostream>

using namespace std;

int main() {
    int numbers[5] = {1, 2, 3, 4, 5};
    
    cout << "Array elements: ";
    for (int i = 0; i < 5; i++) {
        cout << numbers[i] << " ";
    }
    cout << endl;
    
    return 0;
}
