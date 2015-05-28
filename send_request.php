<?php
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();

$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$sender= $json_array['sender'];
$receiver=$json_array['receiver'];
$text= $json_array['text'];
$sql="INSERT INTO Request(username_sender, username_receiver,text) VALUES ('$sender', '$receiver', '$text')";
if($conn->query($sql) == TRUE){
  $json_return= array('status' => '1');
  echo json_encode($json_return);
}else{
  $json_return= array('status' => '0');
  echo json_encode($json_return);
}