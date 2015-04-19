<?php

/*
 * Create a new pet row
 * atributes read from HTTP Post Request
 */

/* import user creation */
include 'create_user.php';
/* import db connection file */
include 'db_connect.php';


$username = NULL;
$name = NULL;
$type = NULL;
$owner = NULL;
$profilePic = NULL;


if(isset($_POST["username"])) $username = $_POST["username"];
if(isset($_POST["name"])) $name = $_POST["name"];
if(isset($_POST["type"])) $type = $_POST["type"];
if(isset($_POST["owner"])) $owner = $_POST["owner"];




/* Create connection to mysql database */
$conn = dbConnect();

/* MySql query inseting new row in Pet */
$mysql_query = "INSERT INTO Pet(username, name, type, owner_username)
                        VALUES('$username', '$name', '$type','$owner')";

/* insert new user in table User*/
$userCreated = createUser($conn, $username, NULL);

if($userCreated){
  if($conn->query($mysql_query) == TRUE){
    echo "New Pet created";
  }else{
    echo "Error inserting new Pet in database";
  }
}else{
  header('status : 0');
  echo "Error inserting new User in database";
}

?>
