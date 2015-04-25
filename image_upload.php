<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);


    $request_body = file_get_contents('php://input');
    $json_array = json_decode($request_body, true);

    $base=$request_body['photo'];
    $extension= $request_body['extension'];
    $username=  $request_body['username'];


    $filename = tempnam('/static', '');
    $base = str_replace(' ', '+', $base);
    $base = str_replace('data:image/'.$extension.';base64,', '', $base);
    $binary=base64_decode($base, TRUE);

    
    $file = fopen('static'.$filename. '.'. $extension, 'wb');
    // Create File
   
    fwrite($file, $binary);
    
    fclose($file);

    $conn = dbConnect();
    $mysql_query = "INSERT INTO Photo(path, route)
                        VALUES('$filename', '$username')";
    if($conn->query($mysql_query) == TRUE){
        $json_return= array('status' => '1');
        echo json_encode($json_return);
    }else{
        $json_return= array('status' => '0');
        echo json_encode($json_return);
    }
    
?>