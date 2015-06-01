<?php

/*
 * crear un usuario dueño
 * @param  input, información del usuario dueño en formato JSON
 * @ requirement 01
*/


error_reporting(E_ALL);
ini_set('display_errors', 1);

include_once 'models/Owner.php';


$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$model = new Owner();
$model->setAttributes($json_array);
$return = $model->save();
echo json_encode($return);

?>
