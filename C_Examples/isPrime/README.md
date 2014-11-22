Prime number detector
(isPrime)

 Author	:	Yosub Lee
 Date 	:	11/21/2014

 How it works?
  1. get SQRT
  2. Round up   ex) 4.2 --> 5
  3. divide 'num' with numbers between 3 and rounded number.
  4. if it divides the 'num' then it's not prime number.

 
@param
	(int) num	: a target integer to examine
@return	
	bool		: Retruns True if it is a prime number,
			otherwise, False
