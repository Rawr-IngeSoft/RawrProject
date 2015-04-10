<?php

/*
 * Create a new user row
 * atributes read from HTTP Post Request
 */

//import db connection file
include 'db_connect.php';


$username = $_POST["username"];
$password = $_POST["password"];

$name = NULL;
$lastname = NULL;


if(isset($_POST["name"])) $name = $_POST["name"];
if(isset($_POST["lastname"])) $lastname = $_POST["lastname"];




// Create connection to mysql database
$conn = dbConnect();

//MySql query insert new row in UserOwner
$mysql_query = "INSERT INTO UserOwner(username, password, name, lastname)
                        VALUES('$username', '$password','$name','$lastname')";



if($conn->query($mysql_query) == TRUE){
   header('status : 1');
}else{
   header('status : 0');
}

?>
