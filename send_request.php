<?php
/*
 * Enviar una petición de amistad a una mascota
 * @param receiver, nombre de usuario de la mascota
 * 	a la que se le envía petición
 * @param sender, nombre de usuario de la persona que envía la petición
 * @return Lista de peticiones de amistad
 * @ requirement 13
*/
error_reporting(E_ALL);
ini_set('display_errors', 1);

include_once 'models/Request.php';


$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$model = new Request();
$model->setAttributes($json_array);
$return = $model->save();
echo json_encode($return);

?>
