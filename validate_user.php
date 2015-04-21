<?php
ini_set('display_errors', true);
 error_reporting(E_ALL);

include 'db_connect.php';

$conn = dbConnect();

$username=NULL;
$password=NULL;
if(isset($_POST["username"])) $username = $_POST["username"];
if(isset($_POST["password"])) $password = $_POST["password"];


$request_body = @file_get_contents('php://input'); // coger el contenido del body del request
$json_array = json_decode($request_body, true);//volver el string en un arreglo
$username= $json_array['username'];
$password= $json_array['password'];

$mysql_query = "SELECT * 
				FROM User u, Owner o
            	WHERE u.username='$username' AND u.password='$password'" AND u.username=o.username;


$returnn = $conn->query($mysql_query);

$row_cnt = $returnn->num_rows;
$json_return= array();
if($row_cnt == 1){
     // echo "Successful";
	$row = $result->fetch_assoc();
	$json_return['status']='1';
	$user = array(
			'name' => $row['name'] ,
			'lastname'=>$row['lastname'],
			'picture'=>$row['profilePicture'],
			'address'=>$row['address']
			);
	$json_array['user']=$user;
	echo json_encode($json_array);
}else{
     // echo "Not Successfull";
	$json_return['status']='0';
	echo json_encode($json_array);
}

$conn->close();
?>
