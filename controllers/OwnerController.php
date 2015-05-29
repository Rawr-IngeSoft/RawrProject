<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require '../models/Owner.php';



/*$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);
*/

$model = new Owner();

$data = array(
    'username'  => 'nuevonuevonuevo',
    'name' => 'algo',
    'password' => 'pass'
  );

$model->setAttributes($data);
$model->save();
