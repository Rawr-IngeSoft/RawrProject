<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    echo "stating method";
    $base=file_get_contents('php://input');
    $base = str_replace(' ', '+', $base);
    // Get file name posted from Android App
    $filename = "mi_archivo";

    $extension= "png";
    $username= "disney";

    $filename = tempnam('/static', '');
    
 

    // Decode Image
    $binary=base64_decode($base);

    header('Content-Type: bitmap; charset=utf-8');
    // Images will be saved under 'www/imgupload/uplodedimages' folder
    $file = fopen('static'.$filename. '.'. $extension, 'w');
    // Create File
   
    fwrite($file, $binary);
    
    fclose($file);
    
?>