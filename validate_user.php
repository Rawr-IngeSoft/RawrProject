<?php
ini_set('display_errors', true);
 error_reporting(E_ALL);

include 'db_connect.php';

$conn = dbConnect();

$username = $_POST["username"];
$password = $_POST["password"];

if($username == "" || $password == ""){
  header('Response : 0');
}else{

  $mysql_query = "SELECT username FROM UserOwner
              WHERE username='$username' AND password='$password'";

  $returnn = $conn->query($mysql_query);
$row_cnt = $returnn->num_rows;

  if($row_cnt == 1){
	header('Response : 1');
  }else{
	header('Response : 0');
  }
}
?>
