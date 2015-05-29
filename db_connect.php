<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

/** import database connection variables **/
include 'db_config.php';

class DB{
  /**
   * dbConnect
   * @return connection to data base
   */
  public static function dbConnect() {
      /** Create connection **/
      $conn = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
      $conn->set_charset('utf8');

      if ($conn->connect_error) {
          echo "connection error";
          die("Connection failed: " . $conn->connect_error);
      }

      return $conn;
  }
}

?>
