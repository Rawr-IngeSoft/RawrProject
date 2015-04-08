<?php


function dbConnect() {
    // import database connection variables
    include 'db_config.php';

    // Create connection
    //$conn = new mysqli('localhost', 'root', 'atlantis', 'rawrdb');
    $conn = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset('utf8');
    if ($conn->connect_error) {
        echo "connection error";
        die("Connection failed: " . $conn->connect_error);
    }

    return $conn;
}
?>
