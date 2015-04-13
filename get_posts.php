<?php


include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
"SELECT * 
 FROM Post 
 WHERE idPet in 
 			(
 			SELECT idPet 
 			FROM Friends 
 			WHERE idPet_friend = $username
 			UNION  
 			SELECT idPet_friend 
 			FROM Friends 
 			WHERE idPet = $username) LIMIT 100";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {
    
       // Crear un diccionario de Post
    	$arreglo = array('id'=>$row['idPost'], 'text'=>$row['text'], 'date'=>$row['date'], 'idPet'=>$row['idPet'], 'Business'=>$row['Business_username'],'photo'=>$row['photo']);
    	array_push($retorno, json_encode($arreglo));

    }
    $json = array('posts'=>$retorno);
    echo json_encode($json);
 }else{
 	echo 'pasó algo muy raro';
 }


 {"posts":"["{"id":"1","text":"Post de pruba","date":"2015-04-13 15:16:56","idPet":"20","Business":null,"photo":"null"}","{"id":"2","text":"Post #2","date":"2015-04-13 15:23:10","idPet":"20","Business":null,"photo":"null"}"]"}