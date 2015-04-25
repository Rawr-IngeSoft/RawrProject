<?php
    echo "stating method";
    $base=file_get_contents('php://input');
     echo "got base";
    // Get file name posted from Android App
    $filename = $_REQUEST['filename'];

    $extension= $_REQUEST['extension'];
    $username= $_REQUEST['username'];

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