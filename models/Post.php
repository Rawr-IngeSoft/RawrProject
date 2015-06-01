<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include_once 'User.php';
include_once 'db_connect.php';

class Post extends Model{
  	protected $username;
  	protected $text;
  	protected $date;
  	protected $idPhoto;
  	protected $type;
  	protected $status;
  	protected $price;

    /**
     * name of the table in data base
     * @return name of the table in db related to this model
     */
    public static function tableName(){
        return 'Post';
    }

    /**
     * get the names of the atributes of this model in an array
     * @return array with the names of the attributes of this model
     */
    public function getAttributes(){
        $attributes = array(
          'username'  => $this->username,
          'text' => $this->text,
          'idPhoto' => $this->idPhoto,
          'type' => $this->type,
          'status' => $this->status,
          'price' => $this->price
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
     * Get the value of Text
     *
     * @return mixed
     */
    public function getText()
    {
        return $this->text;
    }

    /**
     * Set the value of Text
     *
     * @param mixed text
     *
     * @return self
     */
    public function setText($text)
    {
        $this->text = $text;

        return $this;
    }

    /**
     * Get the value of Date
     *
     * @return mixed
     */
    public function getDate()
    {
        return $this->date;
    }

    /**
     * Set the value of Date
     *
     * @param mixed date
     *
     * @return self
     */
    public function setDate($date)
    {
        $this->date = $date;

        return $this;
    }

    /**
     * Get the value of Id Photo
     *
     * @return mixed
     */
    public function getIdPhoto()
    {
        return $this->idPhoto;
    }

    /**
     * Set the value of Id Photo
     *
     * @param mixed idPhoto
     *
     * @return self
     */
    public function setIdPhoto($idPhoto)
    {
        $this->idPhoto = $idPhoto;

        return $this;
    }

    /**
     * Get the value of Type
     *
     * @return mixed
     */
    public function getType()
    {
        return $this->type;
    }

    /**
     * Set the value of Type
     *
     * @param mixed type
     *
     * @return self
     */
    public function setType($type)
    {
        $this->type = $type;

        return $this;
    }

    /**
     * Get the value of Status
     *
     * @return mixed
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * Set the value of Status
     *
     * @param mixed status
     *
     * @return self
     */
    public function setStatus($status)
    {
        $this->status = $status;

        return $this;
    }

    /**
     * Get the value of Price
     *
     * @return mixed
     */
    public function getPrice()
    {
        return $this->price;
    }

    /**
     * Set the value of Price
     *
     * @param mixed price
     *
     * @return self
     */
    public function setPrice($price)
    {
        $this->price = $price;

        return $this;
    }

}

?>
