<?php
/*
 * Create a new Owner row
 * atributes read from HTTP Post Request
 */

/* import user creation */
include 'create_user.php';
/* import db connection file */
include 'db_connect.php';


$username = NULL;
$password = NULL;
$name = NULL;
$lastname = NULL;


if(isset($_POST["username"])) $username = $_POST["username"];
if(isset($_POST["password"])) $password = $_POST["password"];
if(isset($_POST["name"])) $name = $_POST["name"];
if(isset($_POST["lastname"])) $lastname = $_POST["lastname"];


$name = $name != NULL ? "'$name'" : "NULL";
$lastname = $lastname != NULL ? "'$lastname'" : "NULL";

/* Create connection to mysql database */
$conn = dbConnect();

/* MySql query inseting new row in Owner */
$mysql_query = "INSERT INTO Owner(username, name, lastname)
                        VALUES('$username', $name, $lastname)";

/* insert new user in table User*/
$userCreated = createUser($conn, $username, $password);


if($userCreated){
  if($conn->query($mysql_query) == TRUE){
    header('status : 1');
    echo "New Owner created";
  }else{
    /* TODO
     * roll back in user
     */
     header('status : 0');
     echo "Error inserting new Owner in database";
  }
}else{
  header('status : 0');
  echo "Error inserting new User in database";
}

?>
