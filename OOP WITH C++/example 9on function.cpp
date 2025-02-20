#include <iostream>
using namespace std;
int add(int d, int e) {
    return d + e;
}

int main() {
    int sum = add(45, 15);  
    cout << "The sum is: " << sum << endl;
    return 0;
}
