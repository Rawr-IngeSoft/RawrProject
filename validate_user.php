<?php

/*
 * Validar un usuario
 * @param username, nombre de usuario dueño
 * @param password, contraseña
 * @ requirement 08
*/

ini_set('display_errors', true);
 error_reporting(E_ALL);

include 'db_connect.php';

$conn = DB::dbConnect();

$username=NULL;
$password=NULL;
if(isset($_POST["username"])) $username = $_POST["username"];
if(isset($_POST["password"])) $password = $_POST["password"];


$request_body = file_get_contents('php://input'); // coger el contenido del body del request
$json_array = json_decode($request_body, true);//volver el string en un arreglo
$username= $json_array['username'];
$password= $json_array['password'];

$mysql_query = "SELECT *
				FROM Owner o , User u LEFT JOIN Photo p ON p.idPhoto = u.idPhoto_profile
            	WHERE  u.username=o.username AND u.username='$username' AND u.password='$password' ";


$returnn = $conn->query($mysql_query);

$row_cnt = $returnn->num_rows;
$json_return= array();

if($row_cnt == 1){
	$row = $returnn->fetch_assoc();
	$json_return['status']='1';
	$user = array(
			'name' => $row['name'] ,
			'lastname'=>$row['lastname'],
			'picture'=>$row['path'],
			'address'=>$row['address']
			);
	$json_return['user']=$user;
	echo json_encode($json_return);
}else{
	$json_return['status']='0';
	echo json_encode($json_return);
}

$conn->close();
?>
