<?php
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
    "SELECT p.username, p.owner_username, p.name, p.type, ph.path
     FROM Pet p, Photo ph
     WHERE p.username = ph.username AND p.username = '$username' ";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {
    
       // Crear un diccionario de Post
        $arreglo = array(
            "owner"=>$row['owner_username'],
            "name"=> $row['name'],
            "type"=> $row['type'],
            "path"=> $row['path']
            );
        array_push($retorno, $arreglo);

    }
    $json = array('pets'=>$retorno, "status"=>"0");
    echo json_encode($json);
 }else{
    echo 'pasó algo muy raro';
 }