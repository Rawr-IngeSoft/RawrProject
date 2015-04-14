<?php
ini_set('display_errors', true);
 error_reporting(E_ALL);
/*
 * Validate a user
 * atributes read from HTTP Post Request
 */

//import db connection file
include 'db_connect.php';

// Create connection to mysql database
$conn = dbConnect();

$username = $_POST["username"];
$password = $_POST["password"];

//echo $username;
//header('username : \$username');
//header('password : \$password');

if($username == "" || $password == ""){
  header('status : 0');
  $return_data['status'] = 0;
}else{

  $mysql_query = "SELECT username FROM UserOwner
              WHERE username='$username' AND password='$password'";

/*
$mysql_query = "SELECT username FROM UserOwner
              WHERE username='user' AND password='user'";
*/
//echo $mysql_query;

  $returnn = $conn->query($mysql_query);
$row_cnt = $returnn->num_rows;
echo $row_cnt;
//echo mysql_num_rows ($returnn);
//	echo $returnn;

  if($row_cnt == 1){
	header('status : 1');
  }else{
	header('status : 0');
  }
}
//echo json_encode($return_data);
?>
