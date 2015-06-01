<?php

/*
 * crear un usuario mascota
 * @param  input, información del usuario mascota en formato JSON
*/


error_reporting(E_ALL);
ini_set('display_errors', 1);

include_once 'models/Pet.php';


$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$model = new Pet();
$model->setAttributes($json_array);
$return =$model->save();
echo json_encode($return);

?>
