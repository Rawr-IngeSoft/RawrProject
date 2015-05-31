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
"SELECT idPost, username, text, date, path, type, status, price, likes
 FROM (
	 (
	  SELECT p.idPost, p.username, p.text, p.date,  ph.path, p.type, p.status, p.price, p.likes, pet.name
	  FROM Post p, Photo ph, Pet pet  
      WHERE pet.username=p.username AND p.idPhoto = ph.idPhoto AND p.username in
         	 (SELECT username_friend FROM Friends WHERE username = '$username'
		  UNION
		  SELECT '$username'
                 ) ORDER BY p.date LIMIT 100
	 )
	 UNION
	 (
	  SELECT p.idPost, p.username, p.text, p.date, null as path, p.type, p.status, p.price, p.likes, pet.name
	  FROM Post p, Pet pet
      WHERE pet.username = p.username  AND p.username in
		 ( SELECT username_friend FROM Friends WHERE username = '$username'
	           UNION
		   SELECT '$username'
                 ) ORDER BY p.date LIMIT 100
	 )
      ) t
GROUP BY idPost";

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {

        $usernamePost = $row['username'];
        $sql2 = "SELECT p.path
                 FROM User u, Photo p
                 WHERE u.username = '$usernamePost'
                 AND u.idPhoto_profile = p.idPhoto";
        $result2 = $conn->query($sql2);
        $pathProfile = 'null';
        if($result2->num_rows > 0){
          $resultrow = $result2->fetch_assoc();
          $pathProfile = $resultrow['path'];
        }

       // Crear un diccionario de Post
        $arreglo = array('id'=>$row['idPost'], 'text'=>$row['text'],
        'date'=>$row['date'], 'idPet'=>$row['username'], 'photo'=>$row['path'],
        'likes'=>$row['likes'], 'photoProfile'=>$pathProfile, 'name'=>$row['path']);
        array_push($retorno, $arreglo);

    }
    $json = array('posts'=>$retorno);
    echo json_encode($json);
 }else{
    echo 'pasó algo muy raro';
 }
