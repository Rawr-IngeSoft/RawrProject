<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

include_once 'Model.php';

class User extends Model{
  	protected $username;
  	protected $password;
  	protected $idPhoto_profile;

    public function getAttributesUser(){
        $attributes = array(
          'username'  => $this->username,
          'password' => $this->password,
          'idPhoto_profile' => $this->idPhoto_profile
        );
        return $attributes;
    }

    /**
     * overload of parent function save
     * saves a user row first then calls the parent fuction
     * @return array with status 1 or 0 telling if the query was
     *         successful or not
     */
    public function save(){
        $conn = DB::dbConnect();
        $attributes = $this->getAttributesUser();
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

        $mysql_query = "INSERT INTO User" .
                       "(" . $names . ") VALUES( " . $values . ")";

        if($conn->query($mysql_query) == TRUE){
	  $json_returned = parent::save();
          if($json_returned['status'] === 0){
            //roll back si sale mal
            $conn->query("DELETE FROM User WHERE username = '$this->username'");
          }
          return $json_returned;
        }else{
          $json_return= array('status' => '0');
          return $json_return;
        }

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
     * Get the value of Password
     *
     * @return mixed
     */
    public function getPassword()
    {
        return $this->password;
    }

    /**
     * Set the value of Password
     *
     * @param mixed password
     *
     * @return self
     */
    public function setPassword($password)
    {
        $this->password = $password;

        return $this;
    }

    /**
     * Get the value of Id Photo Profile
     *
     * @return mixed
     */
    public function getIdPhotoProfile()
    {
        return $this->idPhoto_profile;
    }

    /**
     * Set the value of Id Photo Profile
     *
     * @param mixed idPhoto_profile
     *
     * @return self
     */
    public function setIdPhotoProfile($idPhoto_profile)
    {
        $this->idPhoto_profile = $idPhoto_profile;

        return $this;
    }

}
