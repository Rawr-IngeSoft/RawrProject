<?php
function obtenerIdPhoto($username, $path) {
   	$conn = dbConnect();
   	$sql =
    	"SELECT idPhoto
     	 FROM Photo
     	 WHERE username='$username' AND path ='$path'";
	$result = $conn->query($sql);
    $row = $result->fetch_assoc();
    return $row['idPhoto'];
}

error_reporting(E_ALL);
ini_set('display_errors', 1);
require '../../db_connect.php';

if($_SERVER['REQUEST_METHOD']=="GET"){
	$conn = dbConnect();
	echo "hicieron un request GET";
}elseif($_SERVER['REQUEST_METHOD']=="POST"){
	$request_body = file_get_contents('php://input');
	$json_array = json_decode($request_body, true);
	//sacar del JSON array los datos que necesito
	$username= $json_array['username'];
	$text= $json_array['text'];
	$path= $json_array['path'];
	$type= $json_array['type'];
	$id= obtenerIdPhoto($username, $path);
	echo "el id de la foto es ". $id;

}elseif ($_SERVER['REQUEST_METHOD']=="PUT") {
	echo "hicieron un request PUT";
}elseif($_SERVER['REQUEST_METHOD']=="DELETE"){
	echo "hicieron un request DELETE";
}