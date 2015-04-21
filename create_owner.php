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

$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

//volver el string en un arreglo


$username = $json_array['username'];
$password = $json_array['password'];
$name = $json_array['name'];
$lastname = $json_array['lastname'];

/* Create connection to mysql database */
$conn = dbConnect();

/* MySql query inseting new row in Owner */
$mysql_query = "INSERT INTO Owner(username, name, lastname)
                        VALUES('$username', '$name', '$lastname')";


/* insert new user in table User*/
$userCreated = createUser($conn, $username, $password);


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
