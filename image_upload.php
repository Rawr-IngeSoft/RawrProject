<?php
    // Get image string posted from Android App
    $base=$_REQUEST['image'];
    // Get file name posted from Android App
    $filename = $_REQUEST['filename'];
    $extension= $_REQUEST['extension'];
    
    $filename = tempnam('/static', '');
    // Decode Image
    $binary=base64_decode($base);
    header('Content-Type: bitmap; charset=utf-8');
    // Images will be saved under 'www/imgupload/uplodedimages' folder
    $file = fopen('uploadedimages/'.$filename . '.' .$extension, 'wb');
    // Create File
    fwrite($file, $binary);
    fclose($file);
    
?>