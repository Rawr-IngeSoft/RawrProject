<?php


/*
 * Dar todos los comentarios de uin post
 * @param post_id, identificador del post
 * @return lista de posts en formato JSON
 * @ requirement 38
*/

include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$post = $_GET['post_id']; // esto debería cambiarse por el id pet
$sql =
    "SELECT *
     FROM Comment
     WHERE idPost = $post
     ORDER BY date";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {

       // Crear un diccionario de Post
        $arreglo = array(
            "idComment"=>$row['idComment'],
            "username"=> $row['username'],
            "text"=> $row['text'],
            "date"=> $row['date']
            );
        array_push($retorno, $arreglo);

    }
    $json = array('pets'=>$retorno, "status"=>"0");
    echo json_encode($json);
 }else{
    echo 'pasó algo muy raro';
 }
