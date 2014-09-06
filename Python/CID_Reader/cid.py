#	ECE 4564 - Assignment 0
#	Name: Yosub Lee
#	Date: 9/5/2014
#
#	for Cyclic Redundancy Code (CRC) calculation
#	I used the module implemented by TA, Thaddeus Czauski

import os.path
import json
import re
import calendar
import BitVector
import sdcard.crc7

class CID:
	"""CID Class for Parsing and Generating CID Register

	It parse and store CID Register values and CPU(Raspberry Pi) serial number.
	It can write all information to Json file.
	The name of the Json file is 'HW0.json'
	"""
	__cpu_serial = 0
	__output = None

	# CID Variables
	__CID = None
	__MID = None
	__OID = None
	__PNM = None
	__PRV = None
	__PSN = None
	__MDT = None
	__reserved = None

	def __init__(self):
		self.__output = open('HW0.json', 'w')
		self.setMID("00")
		self.setOID("0000")
		self.setPNM("0000000000")
		self.setPRV("00")
		self.setPSN("00000000")
		self.setReserved("0")
		self.setMDT("000")
		

	def __del__(self):
		self.__output.close()

	def readCPUInfo(self, cpu_location = "/proc/cpuinfo"):
		""" Reads CPU information and store Serial Number
	
		:param cpu_location: (str) Location of cpuinfo file
		:return: None
		:raises IOError: if the location of cpuinfo file does not exist
		:raises TypeError: if the variable cpu_location is not str type
		"""
		try:
			cpu_file = open(cpu_location,'r')
		except IOError:
			print "File Does not Exist: ", self.cpuinfo_location
		except TypeError:
			print "cpu_location is not string type"
		else:
			cpu_info = cpu_file.readlines()
			serial_raw = None	
			# Get rid of Tabs, WhiteSpace and Line Breakers
			for x in cpu_info:
				if "Serial" in x:
					serial_raw = re.sub('[\t\n\ ]','',x)

			# If there is no Serial Infomation	
			if serial_raw == None:
				print "Can't find CPU Serial!"
				return

			temp = serial_raw.split(':')	
			#data = {"CPU":[temp[0]:temp[1]]}
			self.setCPUserial(temp[1])

	def readCIDInfo(self, cid_location = "/sys/block/mmcblk0/device/cid"):
		"""Reads CID Register from the given cid_location

		It parses the CID register and store to the member variables

		:param cid_location: (str) The location of CID register file
		:return: None
		:raises IOError: if CID register file does not exist at the given location
		:raises TypeError: if cid_location is not str type
		"""
		
		try:
			cid_file = open(cid_location, 'r')
		except IOError:
			print "File Does not Exist: ", cid_location

		else:
			# Read CID register
			cid_raw = cid_file.readline()
			cid_raw = re.sub('[\t\n\ ]', '', cid_raw)

			self.setCID(cid_raw)
			
			# Read MID
			self.setMID(cid_raw[0] + cid_raw[1])
			
			# Read OID
			oid_temp = ''
			for x in range(2,6):
				oid_temp += cid_raw[x]
			self.setOID(oid_temp)	
		
			# Read PNM
			pnm_temp = ''
			for x in range(6, 16):
				pnm_temp += cid_raw[x]
			self.setPNM(pnm_temp)	

			# Read PRV
			prv_temp = cid_raw[16] + cid_raw[17]
			self.setPRV(prv_temp)

			# Read PSN
			psn_temp = ''
			for x in range(18,26):
				psn_temp += cid_raw[x]
			self.setPSN(psn_temp)

			# Read Reserved Bit
			self.setReserved(cid_raw[26])

			# Read MDT
			mdt_temp = cid_raw[27] + cid_raw[28] + cid_raw[29]			
			self.setMDT(mdt_temp)	

	def parseCID(self, cid):
		"""Parses the given cid register value and store to the member variables
	
		cid value should be hex numbers in string type

		:param cid: (str) CID Register Hex value
		:return: None
		:raises TypeError: if cid is not a str type
		:raises ValueError: if cid is not hex number with correct length
		"""
		try:
			if type(cid) is not str:
				raise TypeError('"{arg}" is not str type'.format(arg=cid))
			if len(cid) != 32:
				raise ValueError('"{arg}" is not correct value. Can\'t parse "{arg}"'.format(arg=cid))
		except ValueError as detail:
			print detail
		except TypeError as detail:
			print detail
		else:
			self.setCID(cid)
			
			# Read MID
			self.setMID(cid[0] + cid[1])
			
			# Read OID
			oid_temp = ''
			for x in range(2,6):
				oid_temp += cid[x]
			self.setOID(oid_temp)	
		
			# Read PNW
			pnm_temp = ''
			for x in range(6, 16):
				pnm_temp += cid[x]
			self.setPNM(pnm_temp)	

			# Read PRV
			prv_temp = cid[16] + cid[17]
			self.setPRV(prv_temp)

			# Read PSN
			psn_temp = ''
			for x in range(18,26):
				psn_temp += cid[x]
			self.setPSN(psn_temp)
			
			# Read Reserved Bit
			self.setReserved(cid[26])

			# Read MDT
			mdt_temp = cid[27] + cid[28] + cid[29]			
			self.setMDT(mdt_temp)	


	# Write SD card information to Json File
	def toJson(self):	
		""" Write all information to Json File
		
		The name of the Json file is 'HW0.json'
		it will contain CPU_Serial, MDT, MID, OID, PNM, PRV and PSN in alphabetical order.
		if there is a field that does not have value, it will be "NULL"
		:return: None	
		"""
    
        	# Formatting all values
        	try:
			mid = "{0:08b}".format(int(self.getMID(),16))
        	except:
			mid = None

		try:
			oid = self.getOID().decode("hex")
        	except:
			oid = None

		try:
			pnm = self.getPNM().decode("hex")
		except:
			pnm = None	        

		try:
			prv = self.getPRV()
        		prv = prv[0] + '.' + prv[1]
        	except:
			prv = None

		try:
			psn = "{0:08b}".format(int(self.getPSN(),16))
        	except:
			psn = None	

		try:
			mdt_temp = self.getMDT()
        		year = 2000 + int(mdt_temp[0] + mdt_temp[1], 16)
			month = calendar.month_name[int(mdt_temp[2],16)]
        		mdt = month + ' ' + str(year)
        	except:
			mdt = None
        	# Store the values to Json file
		data =  {"CPU_Serial":self.getCPUserial(), "MID":mid, "OID":oid, "PNM":pnm, "PRV":prv, "PSN":psn, "MDT":mdt, "CRC":self.generateCRC()}
		self.__output.write(json.dumps(data, self.__output, indent=4, sort_keys=True))	

	def generateCID(self):
		"""Generate CID using the value stored in the class

		The value has not been set will be set as 0s

		:return:(str) CID register value
		"""
		# crc is a hex string. which is supposed to be 7-bit.
		# since the LSB should be always 1, I will shift crc value and add 1 to make it 8-bit value
		try:
			crc = int(self.generateCRC(),16)
		except ValueError:
			last8bit = "01"
		else:
			last8bit = hex((crc << 1) + 1)
			last8bit = re.sub('[0x]','',last8bit)
		
		# combine all values. 
		CID = self.getMID() + self.getOID() + self.getPNM() + self.getPRV() + self.getPSN() + self.getReserved() + self.getMDT() + last8bit 
		
		self.setCID(CID)
		return CID

	
	def generateCRC(self):
		"""Generate CRC using the most 120 bits of the stored CID
		
		if there are fields set as None, then It will be treated as 0s
		:return: (str) CRC Value
		"""
		mid = self.getMID()
		oid = self.getOID()
		pnm = self.getPNM()
		prv = self.getPRV()
		psn = self.getPSN()
		reserved = self.getReserved()
		mdt = self.getMDT()

		if mid is not None:
			mid = "{0:08b}".format(int(mid,16))
		else:
			mid = "00000000"

		if oid is not None:
			oid = "{0:016b}".format(int(oid,16))
		else:
			for i in xrange(16):
				oid += "0"

		if pnm is not None:
			pnm = "{0:040b}".format(int(pnm,16))
		else:
			for i in xrange(40):
				pnm += "0"

		if prv is not None:
			prv = "{0:08b}".format(int(prv,16))
		else:
			prv += "00000000"

		if psn is not None:
			psn = "{0:032b}".format(int(psn,16))
		else:
			for i in xrange(32):
				psn += "0"

		if reserved is not None:
			reserved = "{0:04b}".format(int(reserved,16))
		else:
			reserved = "0000"

		if mdt is not None:
			mdt = "{0:012b}".format(int(mdt,16))	
		else:
			for i in xrange(12):
				mdt += "0"		 
	
		targetBit = mid + oid + pnm + prv + psn + reserved + mdt
		vectorBit = BitVector.BitVector(bitstring=targetBit)
		checkSum = sdcard.crc7.generate_crc(vectorBit).int_val()
	
		crc = hex(checkSum)
		
		crc = re.sub('[0x]','',crc)
		return crc
	
	
	# Setter
	# Parameter should be Hex values in String form
	def setCPUserial(self, serial):
		"""Set CPU Serial Number
		
		serial should be a str type
		:param serial: (str) the serial number to set
		:return: None
		:raise TypeError: if serial is not str
		"""
		try:
			if type(serial) is not str:
				raise TypeError('[CPU Serial] "{arg}" is not str type'.format(arg=serial))
		except TypeError as detail:
			print detail
		else:
			self.__cpu_serial = serial
		
	def setCID(self, cid):
		"""Set CID Register Number
		
		cid should be hex number in str type
		:param cid: (str) the cid number to set
		:return: None
		:raise TypeError: if cid is not str
		:raise ValueError: if cid does not have correct length
		"""
		try:
			if type(cid) is not str:
				raise TypeError('[CID] "{arg}" is not str type'.format(arg=cid))
			if len(cid) is not 32:
				raise ValueError('[CID] "{arg}" is not correct value'.format(arg=cid))
		except TypeError as detail:
			print detail
		except ValueError as detail:
			print detail
		else:
			self.__CID = cid

	def setMID(self, mid):
		"""The value for the Manufacturer ID (MID) field

		mid should be 8-bit hex number in a str type
		:param mid: (str) the manufacturer ID to set
		:return: None
		:raise TypeError: if mid is not str
		:raise ValueError: if mid does not have correct length
		"""
		try:
			if type(mid) is not str:
				raise TypeError('[MID] "{arg}" is not str type'.format(arg=mid))
			if len(mid) is not 2:
				raise ValueError('[MID] "{arg}" is not correct value'.format(arg=mid))
		except TypeError as detail:
			print detail
		except ValueError as detail:
			print detail
		else:
			self.__MID = mid

	def setOID(self, oid):
		"""The value for the OEM/Application ID (OID) field
            
       		oid should be 16-bit hex number in a str type
      		:param oid: (str) the OEM/Application ID to set
      		:return: None
        	:raise TypeError: if oid is not str
        	:raise ValueError: if oid does not have correct length
        	"""
		try:
			if type(oid) is not str:
				raise TypeError('[OID] "{arg}" is not str type'.format(arg=oid))
			if len(oid) is not 4:
				raise ValueError('[OID] "{arg}" is not correct value'.format(arg=oid))
		except TypeError as detail:
			print detail
		except ValueError as detail:
			print detail
		else:
            		self.__OID = oid

	def setPNM(self, pnm):
		"""The value for the Product Name (PNM) field
            
        	pnm should be 16-bit hex number in a str type
        	:param pnm: (str) the Product Name to set
        	:return: None
        	:raise TypeError: if pnm is not str
        	:raise ValueError: if pnm does not have correct length
        	"""
		try:
			if type(pnm) is not str:
				raise TypeError('[PNM] "{arg}" is not str type'.format(arg=pnm))
			if len(pnm) is not 10:
				raise ValueError('[PNM] "{arg}" is not correct value'.format(arg=pnm))
		except TypeError as detail:
			print detail
		except ValueError as detail:
			print detail
		else:
           		self.__PNM = pnm

	def setPRV(self, prv):
		"""The value for the Product revision (PRV) field
          
 		prv should be 8-bit hex number in a str type
		:param prv: (str) the Product revision to set
     	 	:return: None
        	:raise TypeError: if prv is not str
        	:raise ValueError: if prv does not have correct length
        	"""
		try:
    	       		if type(prv) is not str:
   	          		raise TypeError('[PRV] "{arg}" is not str type'.format(arg=prv))
   	         	if len(prv) is not 2:
   	       			raise ValueError('[PRV] "{arg}" is not correct value'.format(arg=prv))
  	      	except TypeError as detail:
                	print detail
        	except ValueError as detail:
            		print detail
        	else:
       		     self.__PRV = prv

	def setPSN(self, psn):
        	"""The value for the Product serial number (PSN) field
            
        	psn should be 32-bit hex number in a str type
        	:param psn: (str) the Product serial number to set
        	:return: None
        	:raise TypeError: if psn is not str
        	:raise ValueError: if psn does not have correct length
        	"""
        	try:
        	    if type(psn) is not str:
        	        raise TypeError('[PSN] "{arg}" is not str type'.format(arg=psn))
        	    if len(psn) is not 8:
        	        raise ValueError('[PSN] "{arg}" is not correct value'.format(arg=psn))
        	except TypeError as detail:
        	    print detail
        	except ValueError as detail:
        	    print detail
        	else:
			self.__PSN = psn
	
	def setMDT(self, mdt):
        	"""The value for the Manufacturing date (MDT) field
            
        	mdt should be 12-bit hex number in a str type
        	:param mdt: (str) the Product Name to set
        	:return: None
        	:raise TypeError: if mdt is not str
        	:raise ValueError: if mdt does not have correct length
        	"""
        	try:
        	    if type(mdt) is not str:
        	        raise TypeError('[MDT] "{arg}" is not str type'.format(arg=mdt))
        	    if len(mdt) is not 3:
        	        raise ValueError('[MDT] "{arg}" is not correct value'.format(arg=mdt))
        	except TypeError as detail:
        	    print detail
        	except ValueError as detail:
        	    print detail
        	else:
            		self.__MDT = mdt
	
	def setReserved(self, reserved):
		"""The value for the reserved bits field

		It might have a value or not. (4 bit)
		:param reserved: (str) Reserved 4 bit in hex
		:return: None
		:raise TypeError: if reserved is not str
		:raise ValueError: if reserved does not have correct length
		"""
		try:
			if type(reserved) is not str:
				raise TypeError('[Reserved_bit] "{arg}" is not str type'.format(arg=reserved))
			if len(reserved) is not 1:
				raise ValueError('[Reserved_bit] "{arg}" is not correct value'.format(arg=reserved))
		except TypeError as detail:
			print detail
		except ValueError as detail:
			print detail
		else:
			self.__reserved = reserved

	# Getter
	def getCPUserial(self):
		"""Return the CPU serial number in a str type
	
		If it has not been set yet, then return None	
		:return: (str) CPU Serial Number in hex
		"""
		return self.__cpu_serial

	def getCID(self):
		"""Return the CID Register number in a str type

		If it has not been set yet, then return None	
		:return: (str) CID Register number in hex
		""" 
		return self.__CID

	def getMID(self):
		"""Retrun the Manufacturer ID in a str type

		If it has not been set yet, then return None	
		:return: (str) the Manufacturer ID in hex
		"""
		return self.__MID

	def getOID(self):
		"""Return the OEM/Application ID in a str type

		If it has not been set yet, then return None	
		:return: (str) OEM/Application ID in hex
		"""
		return self.__OID

	def getPNM(self):
		"""Return the Product Name in a str type

		If it has not been set yet, then return None	
		:return: (str) Product Name in Hex
		"""
		return self.__PNM

	def getPRV(self):
		"""Return the Product Revision in a str type

		If it has not been set yet, then return None	
		:return: (str) Product Revision in hex
		"""
		return self.__PRV

	def getPSN(self):
		"""Return the Product serial number in a str type

		If it has not been set yet, then return None	
		:return: (str) Product serial number in hex
		"""
		return self.__PSN

	def getMDT(self):
		"""Return the Manufacturing date in a str type
	
		If it has not been set yet, then return None	
		:return: (str) Manufacturing date in hex
		"""
		return self.__MDT
	def getReserved(self):
		"""Return the Reserved 4-bit in a str type

		If it has not been set yet, then rethrn None
		:return: (str) Reserved bit in hex
		"""
		return self.__reserved 



