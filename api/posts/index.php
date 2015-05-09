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
	$conn = dbConnect();
	$request_body = file_get_contents('php://input');
	$json_array = json_decode($request_body, true);
	//sacar del JSON array los datos que necesito
	$username= $json_array['username'];
	$text= $json_array['text'];
	$path= $json_array['path'];
	$type= $json_array['type'];
	$id= obtenerIdPhoto($username, $path);
	$mysql_query= "INSERT INTO Post (username, text, idPhoto, type) 
							VALUES ('$username', '$text', '$id', '$type')";

	if($conn->query($mysql_query) == TRUE){
    		$json_return= array('status' => '1');
    		echo json_encode($json_return);
 	}else{
    	$json_return= array('status' => '0');
    	echo json_encode($json_return);
  	}
	echo "Se crea el post con id " . $conn->insert_id;

}elseif ($_SERVER['REQUEST_METHOD']=="PUT") {
	echo "hicieron un request PUT";
}elseif($_SERVER['REQUEST_METHOD']=="DELETE"){
	echo "hicieron un request DELETE";
}