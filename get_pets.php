<?php
/*
 * Obtener todos los pets de un usuario
 * @param username, nombre de usuario dueño
 * @return Lista de mascotas del usuario dueño en formato JSON
 * @ requirement 29
*/

include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
    "SELECT p.username, p.owner_username, p.name, p.type, ph.path, p.birth_date, p.gender
     FROM Pet p LEFT JOIN Photo ph ON p.username = ph.username
     WHERE  p.owner_username = '$username'
     GROUP BY p.username";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {

       // Crear un diccionario de Post
        $arreglo = array(
            "username"=>$row['username'],
            "owner"=>$row['owner_username'],
            "name"=> $row['name'],
            "type"=> $row['type'],
            "path"=> $row['path'],
            "birth_date"=> $row['birth_date'],
            "gender"=> $row['gender'],

            );
        array_push($retorno, $arreglo);

    }
    $json = array('pets'=>$retorno, "status"=>"1");
    echo json_encode($json);
 }else{
    $json = array("status"=>"0");
    echo json_encode($json);
 }
