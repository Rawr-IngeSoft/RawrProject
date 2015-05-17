<?php
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
    "SELECT * FROM Request 
     WHERE  username_receiver = '$username'";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {
    
       // Crear un diccionario de Post
        $arreglo = array(
            "username_sender"=>$row['username_sender'],
            "username_receiver"=>$row['username_receiver'],
            "status"=>$row['status']
            );
        array_push($retorno, $arreglo);

    }
    $json = array('requests'=>$retorno, "status"=>"1");
    echo json_encode($json);
 }else{
    $json = array("status"=>"0");
    echo json_encode($json);
 }
