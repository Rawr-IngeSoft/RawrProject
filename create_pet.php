<?php
/*
 * Create a new Owner row
 * atributes read from HTTP Post Request as json
 */

error_reporting(E_ALL);
ini_set('display_errors', 1);

require 'models/Pet.php';


$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$model = new Pet();
$model->setAttributes($json_array);
$model->save();


?>
