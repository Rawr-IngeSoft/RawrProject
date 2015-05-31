<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include_once 'User.php';
include_once 'db_connect.php';

class Photo extends Model{
  	protected $username;
  	protected $path;


    public static function tableName(){
        return 'Photo';
    }

    public function getAttributes(){
        $attributes = array(
          'username'  => $this->username,
          'path' => $this->path
        );
        return $attributes;
    }



    /**
     * Get the value of Username
     *
     * @return mixed
     */
    public function getUsername()
    {
        return $this->username;
    }

    /**
     * Set the value of Username
     *
     * @param mixed username
     *
     * @return self
     */
    public function setUsername($username)
    {
        $this->username = $username;

        return $this;
    }

    /**
     * Get the value of Path
     *
     * @return mixed
     */
    public function getPath()
    {
        return $this->path;
    }

    /**
     * Set the value of Path
     *
     * @param mixed path
     *
     * @return self
     */
    public function setPath($path)
    {
        $this->path = $path;

        return $this;
    }

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
      $json_return= array('status' => '1', 'id' => $conn->insert_id);
      //echo json_encode($json_return);
      return $json_return;
    }else{
      $json_return= array('status' => '0');
      //echo json_encode($json_return);
      return $json_return;
    }
  }

}

?>
