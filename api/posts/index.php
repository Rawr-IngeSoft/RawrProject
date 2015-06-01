<?php

/*
 * Create a new post row
 * atributes read from HTTP Post Request as json
 */
require '../../db_connect.php';


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



if($_SERVER['REQUEST_METHOD']=="GET"){
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
    	$json_return= array('status' => '1', 'id'=> ''.$conn->insert_id);
    	echo json_encode($json_return);
 	}else{
    	$json_return= array('status' => '0');
    	echo json_encode($json_return);
  	}
	

}elseif ($_SERVER['REQUEST_METHOD']=="PUT") {
	echo "hicieron un request PUT";
}elseif($_SERVER['REQUEST_METHOD']=="DELETE"){
	echo "hicieron un request DELETE";
}