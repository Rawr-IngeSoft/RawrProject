<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    $base=file_get_contents('php://input');
    $base = str_replace(' ', '+', $base);
    echo $base;
    // Get file name posted from Android App
    $filename = "mi_archivo";
    $extension= "png";
    $username= "disney";
    $filename = tempnam('/static', '');
    // Decode Image
    $binary=base64_decode($base);
    // Images will be saved under 'www/imgupload/uplodedimages' folder
    $file = fopen('static'.$filename. '.'. $extension, 'w');
    // Create File
   
    fwrite($file, $binary);
    
    fclose($file);
    
?>