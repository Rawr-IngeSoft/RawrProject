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
$profilePic = NULL;


if(isset($_POST["name"])) $name = $_POST["name"];
if(isset($_POST["type"])) $type = $_POST["type"];
if(isset($_POST["owner"])) $owner = $_POST["owner"];




// Create connection to mysql database
$conn = dbConnect();

//MySql query inseting new row in Pet
$mysql_query = "INSERT INTO Pet(name, type, owner_username)
                        VALUES('$name', '$type','$owner')";

if($conn->query($mysql_query) == TRUE){
   echo "New Pet created";
}else{
  echo "Error inserting new Pet in database";
}

?>
