<?php


include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$sql =
"SELECT * 
 FROM POST 
 WHERE idPet in 
 			(
 			SELECT idPet 
 			FROM Friends 
 			WHERE idPet_friend = '$username' 
 			UNION  
 			SELECT idPet_friend 
 			FROM Friends 
 			WHERE idPet = '$username') LIMIT 100";
// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
       // Crear un diccionario de Post
    	$arreglo = array('id'=>$row['idPost'], 'text'=>$row['text'], 'date'=>$row['date'], 'idPet'=>$row['idPet'], 'Business'=>$row['Business_username'],'photo'=>$row['photo']);
    	echo json_encode($arr);
    }
 }

