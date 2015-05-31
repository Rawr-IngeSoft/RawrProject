<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
    "SELECT p.username, p.owner_username, p.name, p.type, p.race, p.birth_date, p.gender, ph.path
     FROM Friends f, Pet p, User u LEFT JOIN Photo ph on ph.idPhoto = u.idPhoto_profile
     WHERE f.username_friend='$username' AND f.username=p.username AND u.username ='$username'";
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
            "type"=> $row['type'],
            "race"=> $row['race'],
            "birth_date"=> $row['birth_date'],
            "gender"=> $row['gender'],
            "path"=>$row['path']
            );
        array_push($retorno, $arreglo);
    }
    $json = array('friends'=>$retorno, "status"=>"1");
    echo json_encode($json);
 }else{
    $json = array("status"=>"0");
    echo json_encode($json);
 }