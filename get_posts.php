<?php


include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
"SELECT p.idPost, p.username, p.text, p.date,  ph.path, p.type, p.status, p.price 
 FROM Post p, Photo ph
 WHERE  p.idPhoto = ph.idPhoto AND p.username in 
            (
            SELECT username_friend 
            FROM Friends 
            WHERE username = '$username'
            UNION  
            SELECT username
            FROM Friends 
            WHERE username_friend = '$username'
            UNION
            SELECT '$username'
            ) ORDER BY p.date LIMIT 100";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {
    
       // Crear un diccionario de Post
        $arreglo = array('id'=>$row['idPost'], 'text'=>$row['text'], 'date'=>$row['date'], 'idPet'=>$row['idPet'], 'Business'=>$row['Business_username'],'photo'=>$row['path']);
        array_push($retorno, $arreglo);

    }
    $json = array('posts'=>$retorno);
    echo json_encode($json);
 }else{
    echo 'pasó algo muy raro';
 }