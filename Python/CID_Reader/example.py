from cid import CID


example = CID()

print "Generate CID without no input"
example.generateCID()
print "CID: ", example.getCID()

# Read CPU serial number from '/proc/cpuinfp'
print "\nRead the CPU Serial number from /proc/cpuinfo"
example.readCPUInfo()
print "CPU Serial \t=",example.getCPUserial()

# Read CID register from '/sys/block/mmcblk0/device/cid'
print "\nRead the CID register from /sys/block/mmcblk0/device/cid"
example.readCIDInfo()

# GenrateCID() will call generaCRC()
print "\nGenerate CID based on current values"
example.generateCID()
print "CID Value \t=",example.getCID()

# Generate Json file
print "\nGenerate Json File"
example.toJson()
print "Json file has been created"

# parse CID. 
# 1. it has to be a hex str
# 2. its length must be 32 (128 bits)
print "\nTest1 - ParseCID(\"1111\")"
example.parseCID("1111")	#should print an error message

print "\nTest2 - ParseCID(123)" 
example.parseCID(123)		#should print an error message

print "\nTest3 - ParseCOD(\"1ab24809418558BBECDR0524e9903000\")"
example.parseCID("1ab24809418558BBECDR0524e9903000") #it's just random value I typed..

#print the Hex values of each field
print "CID: ",example.getCID()
print "MID: ",example.getMID()
print "OID: ",example.getOID()
print "PNM: ",example.getPNM()
print "PRV: ",example.getPRV()
print "PSN: ",example.getPSN()
print "MDT: ",example.getMDT()
