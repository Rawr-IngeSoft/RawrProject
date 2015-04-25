<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    $base=file_get_contents('php://input');
    $base = str_replace(' ', '+', $base);
    echo $base;
    $extension= "png";
    $filename = tempnam('/static', '');
    
    $binary=base64_decode($base, TRUE);
    echo $binary;
    
    $file = fopen('static'.$filename. '.'. $extension, 'wb');
    // Create File
   
    fwrite($file, $binary);
    
    fclose($file);
    
?>