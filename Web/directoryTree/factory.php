<?php 	header('Content-type: text/xml; charset=utf-8'); ?>

<?php
   				
    		// Function Declaration & Definition
    		
    		/* Count a number of factories in Database
    		 * $sqlConnection	:	MySQL link identifier
    		 * Return			: 	(int) a number of factories in DB
    		
			 */
			 function count_factory($sqlConnection) {
    			$query = "SELECT COUNT(*) from factories";
				
				$result = mysql_query($query, $sqlConnection);
				
				if (!$result) {
					echo "<br /> Could not run query: ". mysql_error(). "<br/>";
					exit;
				}
				
				$row = mysql_fetch_row($result);
				$factoryNumber = intval($row[0]);
				return $factoryNumber;
    		}
			
			/* Clear Children
			 * $factory		:	Factory Number
			*/
			function clear_children($sqlConnection, $factory) {
				$factoryName = "Factory ".$factory;
				$query = "UPDATE factories SET children=null WHERE name='". $factoryName."'";
				
				$result = mysql_query($query, $sqlConnection);
				
				if (!$result) {
					echo "<br /> Deletion Failed! <br />";
				}
			}
			
			/* Add new Factory
			 * $sqlConnection	:	MySQL link identifier
			 * Return			: 	(int) 0 - Failed,	1 - Success
			 */
    		function add_factory($sqlConnection) {
				$randNum1 = rand(0, 5000);
				$randNum2 = 0;
				while ( ($randNum2 - $randNum1) < 1) {
					$randNum2 = rand(0, 5000);
				}	    		
				
				$factoryNum = count_factory($sqlConnection) + 1;
				$factoryname = "Factory ".$factoryNum;
				$query = sprintf("INSERT INTO factories (Name, StartNumber, EndNumber) VALUES ('%s', '%d', '%d')",
				$factoryname, $randNum1, $randNum2);
				
				$result = mysql_query($query, $sqlConnection);
				
				if (!$result) {
					echo "<br /> Insertion Failed! <br />";
				}
				
				return $result;
    		}
    		
			/* Get data of a specific factory 
			 * $sqlConnection	:	MySQL link identifier
			 * $factoryNum		:	(int) Factory number
			 * Return			: 	data(row) of the target factory
			 */
			function get_factory($sqlConnection, $factoryNum) {
				$factoryName = sprintf("Factory %d",$factoryNum);
				$query = sprintf("SELECT * FROM factories WHERE name='%s'", $factoryName);
				
				$result = mysql_query($query, $sqlConnection);			
				if($result){
				$row = mysql_fetch_assoc($result);
					if ($row) {
						return $row;
					}
				}
				else {
					return NULL;
				}
			}
			
			function reset_db($sqlConnection) {
				$query = "truncate table factories";
				$result = mysql_query($query, $sqlConnection);		
			}
			/* Get data of a specific factory 
			 * $sqlConnection	:	MySQL link identifier
			 * $factoryNum		:	(int) Factory number
			 * Return			: 	(int) 0 - failed. 1 - Success
			 */
			function add_child($sqlConnection, $factoryNum, $childNum) {
				$row = get_factory($sqlConnection, $factoryNum);
				
				if (($row != NULL)) {
					$startNumber = intval($row['startNumber']);
					$endNumber = intval($row['endNumber']);
				
					//  Children sholud be in the range of this row
					if ( $childNum < $startNumber || $childNum > $endNumber) {
						echo "Wrong!";
						return 0;
					}
					
					$arr = explode(",", $row['children']);
					
					// If first one is empty then..
					if ( $arr[0] == null ) {
						$query = sprintf("UPDATE factories SET children='%d' WHERE name = 'Factory %d'", $childNum, $factoryNum);
						mysql_query($query, $sqlConnection);
						return 1;
					}
				
					// No duplicate child! 
					$length = count($arr);
					for($i = 0; $i < $length -1; $i++) {
						if ( $childNum == intval($arr[$i])) return 0;
					}
					
					array_push($arr, $childNum);
					print_r($arr);
					$children= implode(',', $arr);
					$query = sprintf("UPDATE factories SET children='%s' WHERE name = 'Factory %d'", $children, $factoryNum);
					mysql_query($query, $sqlConnection);
					return 1;	
				}
				else {
					echo "no";	
					return 0;		
				}
			}	
			
			/* Generate Multiple Children
			 * 
			 * 
			 */
			 function generate_children($sqlConnection, $factoryNum, $numOfChildren) {
			 	clear_children($sqlConnection, $factoryNum);
				
				$i = 0;
				$row = get_factory($sqlConnection, $factoryNum);
				if (($row != NULL)) {
					$startNumber = intval($row['startNumber']);
					$endNumber = intval($row['endNumber']);
				}
				
				while($i < $numOfChildren) {
					if (add_child($sqlConnection, $factoryNum, rand($startNumber, $endNumber))) {
						$i++;
					}
				} 
			 }
			/* Get data of a specific factory 
			 * $sqlConnection	:	MySQL link identifier
			 * $factoryNum		:	(int) Factory number
			 * Return			: 	dict array with startnumber and endnumber. Otherwise, Null
			 */
			function get_factory_numRange($sqlConnection, $factoryNum) {
				$row = get_factory($sqlConnection, $factoryNum);
				
				if ( $row != NULL ) {
					$startNumber = intval($row['startNumber']);
					$endNumber = intval($row['endNumber']);
					
					$arr = array('startNumber' => $startNumber,
								'endNumber' => $endNumber);
								
					return $arr;
				}
				else {
					return NULL;
				}
			}
				
			/* Get an int array of children 
			 * $sqlConnection	:	MySQL link identifier
			 * $factoryNum		:	(int) Factory number
			 * Return			: 	Int array of children at the target factory. Otherwise, Null
			 */			
			function get_facotry_children($sqlConnection, $factoryNum) {
	
				$row = get_factory($sqlConnection, $factoryNum);
				if ( $row != NULL) {
					$children = explode(",", $row['children']);
					
					if ( count($children) == 1 && $children[0] == NULL) {
						return NULL;
					}
					return $children;
				}
				else {
	
					return NULL;
				}	
			}
		
			function refresh_tree($sqlConnection) {
				// produce XML	
				//header("Content-type: text/xml");
				
				$dom = new domDocument('1.0');
				$dom->formatOutput = true;
				$root = $dom->appendChild($dom->createElement("factories"));
				$xml = simplexml_import_dom($dom);
				
				// Get data from SQL server
				$numOfFactories = count_factory($sqlConnection);
				$i = 1;
				
				for ($i = 1; $i <= $numOfFactories; $i++) {
					try {
						$ret = get_factory($sqlConnection, $i);
						$factory = $xml->addChild("factory");
						$factory->addChild("name", $ret['name']);
						$factory->addChild("startNum", $ret['startNumber']);
						$factory->addChild("endNum", $ret['endNumber']);
						$factory->addChild("Children", $ret['children']);
						echo $factory->asXML();
					}
					catch (Exception $e) {
						echo $e->getMessage();
					}
					
				}
				
				//echo $dom->save(); // put string in test1
    			echo $dom->save('factoryTree.xml');
				
			}		
			
			$host		= "localhost";
			$user		= "root";
			$pass		= "rumandcoke";
			$database	= "nodeTree";
			
			// Connect to SQL Server and Select DB
			$connect = mysql_connect($host, $user,$pass) or die(mysql_error());
			$db = mysql_select_db($database, $connect);
			if (!$db) {
				$query = "CREATE DATABASE nodeTree; 
						  USE nodeTree;";
						  ;
				mysql_query($query, $connect);
			}
			/////////////////////////////////////////////////////////////
			
			if(mysql_num_rows(mysql_query("SHOW TABLES LIKE factories")) != 1) {
				$query = "CREATE TABLE IF NOT EXISTS factories (
						  name VARCHAR(20),
						  startNumber INT(255),
						  endNumber INT(255),
						  children VARCHAR(255));";
				mysql_query($query, $connect);
			}
			
			// Handling Request from users
			if ($_SERVER['REQUEST_METHOD'] == 'GET'){
					$action = $_GET['action'];
					if ( $action == 'reset'){
						reset_db($connect);
					}
					else if($action == "addchild"){
						$factory = intval($_GET['factory']);
						$number = intval($_GET['numGen']);
						generate_children($connect, $factory, $number);
					}
					else if($action == "addfactory"){
						add_factory($connect);
					}
					
			}

			refresh_tree($connect);
			mysql_close($connect);
?>
	