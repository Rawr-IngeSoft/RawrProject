<?php
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
    echo "stating method";
    $base=file_get_contents('php://input');
    echo "got base";
    // Get file name posted from Android App
    $filename = $_POST['Filename'];

    $extension= $_POST['Extension'];
    $username= $_POST['Username'];

    $filename = tempnam('/static', '');
    
    echo "got filename";

    // Decode Image
    $binary=base64_decode($base);
     echo "de encoded";
    header('Content-Type: bitmap; charset=utf-8');
    // Images will be saved under 'www/imgupload/uplodedimages' folder
    $file = fopen('static/'.$filename . '.' .$extension, 'w');
    // Create File
    echo "opened file";
    fwrite($file, $binary);
     echo "written file";
    fclose($file);
     echo "closed file";
?>