<?php

/*
 * Create a new pet row
 * atributes read from HTTP Post Request
 */

//import db connection file
include 'db_connect.php';



$name = NULL;
$type = NULL;
$owner = NULL;


if(isset($_POST["name"])) $name = $_POST["name"];
if(isset($_POST["type"])) $type = $_POST["type"];
if(isset($_POST["owner"])) $owner = $_POST["owner"];



// Create connection to mysql database
$conn = dbConnect();

$mysql_query = "INSERT INTO Pet(name, type, owner_username)
                        VALUES('$name', '$type','$owner')";

/*$mysql_query = "INSERT INTO Pet(idPet, name, type, owner_username)
                        VALUES(NULL, 'Dug', 'Dog','user')";
                        */
                        
echo 'conecte';
if($conn->query($mysql_query) == TRUE){
   echo "New Pet created";
}else{
  echo "Error inserting new Pet in database";
}

?>
