<?php
/*
 * crear un post
 * @param  input, información del post en formato JSON
*/
error_reporting(E_ALL);
ini_set('display_errors', 1);

include_once 'models/Post.php';


$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$model = new Post();
$model->setAttributes($json_array);
$return = $model->save();
echo json_encode($return);

?>
