<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include_once 'db_connect.php';

class Model {

  /**
   * Sets the attribute values of the model.
   * @param array $values attribute values (name => value) to be assigned to the model.
   */
  public function setAttributes($values)
  {
      if (is_array($values)) {
        foreach ($values as $name => $value) {
          $this->$name = $value;
        }
      }
  }

  /**
   * Saves the model in the data base.
   * @return array with status 1 or 0 telling if the query was
   *         successful or not
   */
  public function save()
  {
    //echo "<br> en model <br>";
    $conn = DB::dbConnect();
    $attributes = $this->getAttributes();
    $names = "";
    $values = "";

    $numItems = count($attributes);
    $i = 0;
    if(is_array($attributes)){
      foreach($attributes as $name => $value){
        $value = $value != NULL ? "'$value'" : "NULL";

        $names  .= $name;
        $values .= $value;
        if(++$i !== $numItems) {
          $names  .= ', ';
          $values .= ', ';
        }
      }
    }

    $mysql_query = "INSERT INTO " . $this->tableName() .
                   "(" . $names . ") VALUES( " . $values . ")";
    //echo "<br>" . $mysql_query . "<br>";
    //$conn = DB::dbConnect();

    if($conn->query($mysql_query) == TRUE){
      $json_return= array('status' => '1');
      //echo json_encode($json_return);
      return $json_return;
    }else{
      $json_return= array('status' => '0');
      //echo json_encode($json_return);
      return $json_return;
    }
  }
}
