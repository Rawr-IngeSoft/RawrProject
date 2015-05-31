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

}

?>
