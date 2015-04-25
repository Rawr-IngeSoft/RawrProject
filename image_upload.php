<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    include 'db_connect.php';

    $request_body = file_get_contents('php://input');
    $json_array = json_decode($request_body, true);

    $base=$json_array['photo'];
    $extension= $json_array['extension'];
    $username=  $json_array['username'];


    $filename = tempnam('/static', '');
    $base = str_replace(' ', '+', $base);
    $base = str_replace('data:image/'.$extension.';base64,', '', $base);
    $binary=base64_decode($base, TRUE);

    
    $file = fopen('static'.$filename. '.'. $extension, 'wb');
    // Create File
   
    fwrite($file, $binary);
    
    fclose($file);

    $conn = dbConnect();
    $filename= $filename . '.' . $extension;
    $mysql_query = "INSERT INTO Photo(path, username)
                        VALUES('$filename', '$username')";
   
    if($conn->query($mysql_query) == TRUE){
        $json_return= array('status' => '1');
        echo json_encode($json_return);
    }else{
        $json_return= array('status' => '0');
        echo json_encode($json_return);
    }
    
?>