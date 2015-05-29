<?php
/*
 * Create a new Owner row
 * atributes read from HTTP Post Request as json
 */

error_reporting(E_ALL);
ini_set('display_errors', 1);

require 'models/Owner.php';


$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

echo $request_body;
$model = new Owner();
$model->setAttributes($json_array);
$model->save();


?>
