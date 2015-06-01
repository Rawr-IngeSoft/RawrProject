<?
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$username = $_GET['username']; // esto debería cambiarse por el id pet
$idPhoto = $_GET['idPhoto'];
$sql =
    "UPDATE User SET idPhoto_profile = '$idPhoto'
    WHERE username = '$username'";

if ($conn->query($sql)) {
    $json = array('pets'=>$retorno, "status"=>"1");
    echo json_encode($json);
 }else{
    $json = array("status"=>"0");
    echo json_encode($json);
 }
