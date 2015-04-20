<?php
ini_set('display_errors', true);
 error_reporting(E_ALL);

include 'db_connect.php';

$conn = dbConnect();

$username = $_POST["username"];
$password = $_POST["password"];

$mysql_query = "SELECT * FROM Owner
            WHERE username='$username' AND password='$password'"; //esto debería ser owner

$returnn = $conn->query($mysql_query);

$row_cnt = $returnn->num_rows;

if($row_cnt == 1){
	$row = $result->fetch_assoc();

	$json = array(
			'username'=>$row['username'],
			'name'=>$row['name'],
			'lastname'=>$row['lastname'],
			'profilePicture'=>$row['profilePicture'],
			'address'=>$row['address']);

     echo  json_encode($json);
}else{
      echo "Not Successfull";
}

$conn->close();
?>
