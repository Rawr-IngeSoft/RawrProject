<?php


function dbConnect() {
    // import database connection variables
    include 'db_config.php';

    // Create connection
    $conn = new mysqli('localhost', 'root', 'tgisispuj', 'rawrdb');
    $conn->set_charset('utf8');
    if ($conn->connect_error) {
        echo "connection error";
        die("Connection failed: " . $conn->connect_error);
    }

    return $conn;
}
?>
