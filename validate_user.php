<?php
ini_set('display_errors', true);
 error_reporting(E_ALL);

include 'db_connect.php';

$conn = dbConnect();

$username=NULL;
$password=NULL;
if(isset($_POST["username"])) $username = $_POST["username"];
if(isset($_POST["password"])) $password = $_POST["password"];


$request_body = @file_get_contents('php://input'); // coger el contenido del body del request
$json_array = json_decode($request_body, true);//volver el string en un arreglo

$mysql_query = "SELECT * 
				FROM User
            	WHERE username='$username' AND password='$password'";

echo json_encode($json_array);
$returnn = $conn->query($mysql_query);

$row_cnt = $returnn->num_rows;

if($row_cnt == 1){
     // echo "Successful";
}else{
     // echo "Not Successfull";
}

$conn->close();
?>
