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
$owner_username = NULL;
$profilePic = NULL;

/* get Json */
$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

/* convert string to array */

$username = $json_array['username'];
$type = $json_array['type'];
$name = $json_array['name'];
$owner_username = $json_array['owner_username'];



/*
if(isset($_POST["username"])) $username = $_POST["username"];
if(isset($_POST["name"])) $name = $_POST["name"];
if(isset($_POST["type"])) $type = $_POST["type"];
if(isset($_POST["owner_username"])) $owner_username = $_POST["owner_username"];
*/



/* Create connection to mysql database */
$conn = dbConnect();

/* MySql query inseting new row in Pet */
$mysql_query = "INSERT INTO Pet(username, name, type, owner_username)
                        VALUES('$username', '$name', '$type','$owner_username')";

/* insert new user in table User*/
$userCreated = createUser($conn, $username, NULL);

if($userCreated){
  if($conn->query($mysql_query) == TRUE){
    $json_return= array('status' => '1');
    echo json_encode($json_return);
  }else{
    $json_return= array('status' => '0');
    echo json_encode($json_return);
  }
}else{
  $json_return= array('status' => '0');
    echo json_encode($json_return);
}

?>
