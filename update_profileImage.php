<?
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();
$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$username = $json_array['username']; // esto debería cambiarse por el id pet
$idPhoto = $json_array['idPhoto'];
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
