ECE4564 Assignment 0
Author	: Yosub Lee
Date 	: 9/5/2014

[Submitssion Files]
* cid.py	- CID class. Parses & stores CID register value.
* example.py	- Example Usage of CID class
* BitVector.py 	- External Library to calculate CRC()
* /sdcard	- CRC calculation method implemented by Thaddeus Czauski

//////// CID Class /////////
1. Variables
	private members
	- __cpu_serial = a str of CPU Serial number
	- __output = a filestream to write 'HW0.json'
	- __CID = a str of a CID register value. hex
	- __MID = a str of the manufacturer ID. hex
	- __OID = a str of the OEM/Application ID. hex
	- __PNM = a str of the Product name. hex
	- __PRV = a str of the Product revision. hex
	- __PSN = a str of the Product serial number. hex
	- __MDT = a str of the Manufacturing date. hex

	# Initial value is zeros in str
	# All private member variables are hex numbers in str type
 
2. Methods
* readCPUInfo(cpu_location = "/proc/cpuinfo")
	- Reads cpu information from '/proc/cpuinfo' or a given location
	and store cpu serial number.

* readCIDInfo(cid_location = "/sys/block/mmcblk0/device/cid")
	- Reads the CID register value from '/sys/block/mmcblk0/device/cid' or
	a given location and parse it. 
	- It does the same thing as what parseCID() method does but it takes a value from cid register file.

* parseCID(cid)
	- cid must be a hex str with length 32 (128-bit)
	- if it is not a str type or length is not 32
	it will print an error message 

* toJson()
	- Generates 'HW0.json' file with the stored values
	- If CID has not been read yet, then all fields will be None
	- The value for each field will be translated into the correct form
	
	ex) CID Register = 03534453553136478030a7e30700e500

	Json output
	{
	    "CPU_Serial": "0000000002f6cc0d", 
	    "CRC": "6d", 
	    "MDT": "May 2014", 
	    "MID": "00000011", 
	    "OID": "SD", 
	    "PNM": "SU16G", 
	    "PRV": "8.0", 
	    "PSN": "110000101001111110001100000111"
	}

* generateCID()
	- Generates a CID str with the stored values
	- This method calls generateCRC() to generate CRC7 checksum value

* generateCRC()
	- If there is a field with None, then the filed will be treated as zeros.
	 (First time, I set all member variables as None, didn't initialize them. then later I added initializing them as zeros.	If/else parts were made before I added the initialization)


 Setters
* setCPUserial(serial)
* setCID(cid)
* setMID(mid)
* setOID(oid)
* setPNM(pnm)
* setPRV(prv)
* setPSN(psn)
* setMDT(mdt)
* setReserved(reserved)	/* I made this just in case.. */

	- All Setters take hex numbers in str. the Length of the parameter should be matched to the requirement

 Getters
* getCPUserial()
* getCID()
* getMID()
* getOID()
* getPNM()
* getPRV()
* getPSN()
* getMDT()
* getReserved() 

	- All getters return hex numbers in str.  
