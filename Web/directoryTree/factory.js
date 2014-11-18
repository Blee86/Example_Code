
/*
 * Factory Class Definition
 */
function Factory(name, startNum, endNum, children) {
	this.name = name;
	this.startNum = startNum;
	this.endNum = endNum;
	this.collapsed = false;		// to remember whether it was expanded or collapsed
	
	if (children != null) {
		this.children = children.nodeValue.split(",");
	}
	else {
		this.children = null;
	}
}


/*
 * Global Variable
 */
var factoryArray = new Array();	// Global Array will hold all Factory objects.


/*
 * Make a html List 
 * @param factory	-	Factory object
 */
function displayFactory(factory) {
	// Set Parent
	var number = factory.name.split(" ")[1];
	var list = document.createElement("ul");
	var parent = document.createElement("li");
	var parentImage = document.createElement("img");
	parentImage.setAttribute("src", "images/folder.png");
	parent.setAttribute("id", "factory");
	
	var span = document.createElement("span");
	span.setAttribute("oncontextmenu","ShowMenu('contextMenu', event)")
	span.setAttribute("id", number);
	span.style.display = "inline-block";
		
	var text = document.createTextNode(" " + factory.name + ": (" + factory.startNum + " - " + factory.endNum + ")");
	span.appendChild(parentImage);
	span.appendChild(text);
	parent.appendChild(span);
	list.appendChild(parent);
	
	// Set Children
	if( factory.children != null){
		var childList = document.createElement("ul");

		for (var j=0; j < factory.children.length; j++) {
			var child = document.createElement("li");
			var span = document.createElement("span");
			span.style.display = "inline-block";
			var image = document.createElement("img");
			image.setAttribute("src", "images/document.png");
			child.setAttribute("id", "child");
			
			child.style.listStyleType = "none";
			child.style.padding = '0';
			if ( j == factory.children.length-1 ) {
				child.style.listStyleImage = "url('images/l_line.png')";
			}
			else {
				child.style.listStyleImage = "url('images/i_line.png')";
			}
			var text = document.createTextNode(factory.children[j]);
			span.appendChild(image);
			span.appendChild(text);
			child.appendChild(span);
			childList.appendChild(child);
			parent.appendChild(childList);
		}
	}
	
	// Append this to 'div root'			
	var top = document.getElementById("top");
	top.appendChild(list);
}

/* Generate numbers 
 * elem		: 	factory number
 */
function generateOutput(elem, opt) {
	// Check Selector Value (Number to generate)
	var select = document.getElementById("selector");
	
	
	if (window.XMLHttpRequest) {
	// For IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} 
	else {
		xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
	}
				
	if (xmlhttp.overrideMimeType)
		xmlhttp.overrideMimeType('text');
		
	xmlhttp.onreadystatechange = function() {
		parser = new DOMParser();
		if ( xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			// Parse XML reponse
			var docXML = xmlhttp.responseText;
			
		}
	}
	
	// Somehow POST doesn't work on my laptop..
	
	if ( opt == 0) {
		xmlhttp.open("GET","factory.php?action=addchild&factory=" + elem + "&numGen=" + select.value, true);
	}
	else if ( opt == 1) {
		xmlhttp.open("GET","factory.php?action=addfactory" , true);
	}
	else {
		xmlhttp.open("GET","factory.php?action=reset" , true);
	}
	
	xmlhttp.send();
	
	// Set the select back to index 0
	select.selectedIndex = -1;
	
}


/*
 * Request XML from the Server, and Update a html List on the page
 */
function getListFromServer(){
	if (window.XMLHttpRequest) {
		// For IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
		} 
	else {
		xmlhttp = new ActiveObject("Microsoft.XMLHTTP");
	}
				
	if (xmlhttp.overrideMimeType)
		xmlhttp.overrideMimeType('text/xml');
		
	xmlhttp.onreadystatechange = function() {
		parser = new DOMParser();
		if ( xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			// Parse XML reponse
			var docXML = xmlhttp.responseXML;
			var factories = docXML.documentElement.getElementsByTagName("factory");

			// Create HTML List here. Dynamically!
			for( var i = 0; i < factories.length; i++) {
				// Get information 
				var thisFactory = factories.item(i);	
				var name = thisFactory.getElementsByTagName("name").item(0).firstChild.nodeValue;
				var startNum = thisFactory.getElementsByTagName("startNum").item(0).firstChild.nodeValue;
				var endNum = thisFactory.getElementsByTagName("endNum").item(0).firstChild.nodeValue;
				var children = thisFactory.getElementsByTagName("Children").item(0).firstChild;
				var factory = new Factory(name, startNum, endNum, children);			
				factoryArray.push(factory);
				displayFactory(factory);			
			}
		}
	}
			
	xmlhttp.open("GET", "factoryTree.xml?action=refresh", true);
	xmlhttp.send();
}
