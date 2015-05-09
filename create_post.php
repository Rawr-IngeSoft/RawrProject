<?php

/*
 * Create a new post row
 * atributes read from Json
 */

/* import db connection file */
include 'db_connect.php';


$username = NULL;
$text = NULL;
$idPhoto = NULL;
$type = 'Post';

/* get Json */
$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

/* convert string to array */

$username = $json_array['username'];
$text = $json_array['text'];
$idPhoto = $json_array['idPhoto'];

$idPhoto = $idPhoto == '' ? 'Null' : "'$idPhoto'";


/* Create connection to mysql database */
$conn = dbConnect();

/* MySql query inseting new row in Post */
$mysql_query = "INSERT INTO Post(username, text, type, idPhoto)
                        VALUES('$username', '$text','$type', $idPhoto)";


if($conn->query($mysql_query) == TRUE){
  $json_return= array('status' => '1');
  echo json_encode($json_return);
}else{
  $json_return= array('status' => '0');
  echo json_encode($json_return);
}

?>