
<?php

/*
 * Dar todos los mensajes enviados y recibidos por un usuario
 * @param username, identificador de nombre de usuario
 * @return Lista de posts en formato JSON
*/

error_reporting(E_ALL);
ini_set('display_errors', 1);
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
    "SELECT m.username_sender, m.username_receiver, m.text, m.date, m.status
     FROM Message m
     WHERE m.username_sender='$username' OR m.username_receiver='$username'
     ORDER BY m.date DESC";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {

       // Crear un diccionario de Post
        $arreglo = array(
            "sender"=>$row['username_sender'],
            "receiver"=> $row['username_receiver'],
            "text"=> $row['text'],
            "date"=> $row['date'],
            "status"=> $row['status']
            );
        array_push($retorno, $arreglo);

    }
    $json = array('messages'=>$retorno, "status"=>"1");
    echo json_encode($json);
 }else{
    $json = array("status"=>"0");
    echo json_encode($json);
 }
