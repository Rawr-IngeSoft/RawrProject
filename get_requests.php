<?php
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet

$sql =
    "SELECT r.text, r.status, p.*, o.name as ownerName,
     o.lastname, o.address, o.idLocation, o.email,
     o.birth_date as ownerBirth, o.gender as ownerGender
     , p2.path AS owner_picture, p1.path AS pet_picture
     FROM Request r, 
          (( Pet p INNER JOIN User u 
                ON u.username = p.username
                )
            LEFT JOIN Photo p1 ON u.idPhoto_profile = p1.idPhoto
            )      
        , (( Owner o INNER JOIN User u2 
                ON u2.username = o.username
                )
            LEFT JOIN Photo p2 ON u2.idPhoto_profile = p2.idPhoto
            )
     WHERE r.username_receiver = '$username' AND
     p.username = r.username_sender AND  o.username = p.owner_username";
//echo $username;

// ahora toca recorrer el query
$result = $conn->query($sql);
if ($result->num_rows > 0) {
    // output data of each row
    $retorno= array();
    while($row = $result->fetch_assoc()) {

       // Crear un diccionario de Post
        $arreglo = array(
            "username_sender"=>$row['username'],
            "username_receiver"=>$username,
            "status"=>$row['status'],
	    "text"=>$row['text'],
            "owner_username"=>$row['owner_username'],
	    "name"=>$row['name'],
	    "type"=>$row['type'],
  	    "race"=>$row['race'],
	    "birth_date"=>$row['birth_date'],
 	    "gender"=>$row['gender'],
	    "ownerName"=>$row['ownerName'],
	    "lastname"=>$row['lastname'],
	    "address"=>$row['address'],
	    "idLocation"=>$row['idLocation'],
	    "email"=>$row['email'],
	    "ownerBirth"=>$row['ownerBirth'],
	    "ownerGender"=>$row['ownerGender'],
            );
        array_push($retorno, $arreglo);

    }
    $json = array('requests'=>$retorno, "status"=>"1");
    echo json_encode($json);
 }else{
    $json = array("status"=>"0");
    echo json_encode($json);
 }
