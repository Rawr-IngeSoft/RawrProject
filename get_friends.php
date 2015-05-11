<?php
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
    "SELECT f.username, f.owner_username, f.name, f.type, f.race, f.birth_date, f.gender 
     FROM Friends f, Pet p
     WHERE f.username_friend='$username'
     AND f.username=p.username";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {
    
       // Crear un diccionario de Post
        $arreglo = array(
            "username"=>$row['username'],
            "owner"=> $row['owner_username'],
            "name"=> $row['name'],
            "type"=> $row['type']
            );
        array_push($retorno, $arreglo);

    }
    $json = array('friends'=>$retorno, "status"=>"0");
    echo json_encode($json);
 }else{
    echo 'pasó algo muy raro';
 }