#include <iostream>
#include <iomanip>
#include <cmath>
using namespace std;

/*
* How it works?
* 1. get SQRT
* 2. Round up	ex) 4.2 --> 5
* 3. divide 'num' with numbers between 3 and rounded number.
* 4. if it divides the 'num' then it's not prime number.
*/
bool isPrime(int num) {
	if (num == 1 || num == 2 || num == 3) return true;
	
	float sq = sqrt(num);
 	int round = int(ceil(sq));

	for (int i=2; i <= round; i++) {
		if ( (num % i) == 0) return false;
	}

	return true;
}

int main() {
	// Range
	int lower = 5;
	int upper = 50;

	for(int i = lower; i < upper; i++){	
		if ( isPrime(i) ){
			cout << i <<  "\t is prime \n";
		}
	}
	

	return 0;
}
