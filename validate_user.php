<?php

/*
 * Validate a user
 * atributes read from HTTP Post Request
 */

//import db connection file
include 'db_connect.php';

// Create connection to mysql database
$conn = dbConnect();

$username = isset($_POST['username']) ? ($_POST['username']) : "";
$password = isset($_POST['password']) ? ($_POST['password']) : "";

if($username == "" || $password == ""){
  $return_data['status'] = 0;
}else{
  $mysql_query = "SELECT username FROM UserOwner
              WHERE username=$username AND passwd=$password";
  $returnn = conn->query($mysql_query);
  if($returnn == $username){
     $return_data['status'] = 1;
  }else{
     $return_data['status'] = 0;
  }
}

echo json_encode($return_data);

?>
