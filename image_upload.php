<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    include_once 'models/Photo.php';

    $request_body = file_get_contents('php://input');
    $json_array = json_decode($request_body, true);

    $base=$json_array['photo'];
    $extension= $json_array['extension'];
    $username=  $json_array['username'];


    $path = '../photos/'.$username;
    $filename = uniqid();
    if(!file_exists($path)){
      mkdir($path, 0777, true);
      //echo $path . '  created';
    }
    $base = str_replace(' ', '+', $base);
    $base = str_replace('data:image/'.$extension.';base64,', '', $base);
    $binary=base64_decode($base, TRUE);


    // Create File
    $file = fopen($path.'/'.$filename.'.'.$extension, 'wb');
    //echo $path.'/'.$filename. '.'. $extension . ' created';


    fwrite($file, $binary);

    fclose($file);

    $filename= $filename . '.' . $extension;
    $data = array(
        "username" => $username,
        "path" => $filename
    );

    $model = new Photo();
    $model->setAttributes($data);
    $return = $model->save();
    $return['path'] = $model->getPath();
    echo json_encode($return);

?>
