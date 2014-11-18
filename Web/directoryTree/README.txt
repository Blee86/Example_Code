Author	: Yosub Lee
Email	: yosubl86@vt.edu
Date	: 2014/11/17

//////////////////////// Description ////////////////////////

It uses MySql. to see the webpage, you need to change the variables
relating to MySql such as ‘host’, ‘user’, ‘pass’,and ‘database’ (found in line 235)
  
Once a client sends a request, ‘factory.php’ will create database ‘nodeTree’ and
table ‘factories.’

/////////////////////////////////////////////////////////////
I worked on the following environment

Apache 		- 2.4.9
PHP		- 5.5.14
Database	- MySQL

//////////////////////// Files ////////////////////////
- Client Side
	index.html	
	factory.js	// Processes responses from the Server side
	/css/style.css
	/images/*.*	// Image files for a list

- Server Side
	factory.php	// Processes requests from the client side
			// Sends a XML file

			
