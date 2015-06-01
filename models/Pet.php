<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include_once 'User.php';
include_once 'db_connect.php';

class Pet extends User{
  	protected $username;
  	protected $owner_username;
  	protected $name;
  	protected $type;
  	protected $race;
  	protected $birth_date;
  	protected $gender;

    /**
     * name of the table in data base
     * @return name of the table in db related to this model
     */
    public static function tableName(){
        return 'Pet';
    }

    /**
     * get the names of the atributes of this model in an array
     * @return array with the names of the attributes of this model
     */
    public function getAttributes(){
        $attributes = array(
          'username'  => $this->username,
          'owner_username' => $this->owner_username,
          'name' => $this->name,
          'type' => $this->type,
          'race' => $this->race,
          'birth_date' => $this->birth_date,
          'gender' => $this->gender
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
     * Get the value of Owner Username
     *
     * @return mixed
     */
    public function getOwnerUsername()
    {
        return $this->owner_username;
    }

    /**
     * Set the value of Owner Username
     *
     * @param mixed owner_username
     *
     * @return self
     */
    public function setOwnerUsername($owner_username)
    {
        $this->owner_username = $owner_username;

        return $this;
    }

    /**
     * Get the value of Name
     *
     * @return mixed
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * Set the value of Name
     *
     * @param mixed name
     *
     * @return self
     */
    public function setName($name)
    {
        $this->name = $name;

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
     * Get the value of Race
     *
     * @return mixed
     */
    public function getRace()
    {
        return $this->race;
    }

    /**
     * Set the value of Race
     *
     * @param mixed race
     *
     * @return self
     */
    public function setRace($race)
    {
        $this->race = $race;

        return $this;
    }

    /**
     * Get the value of Birth Date
     *
     * @return mixed
     */
    public function getBirthDate()
    {
        return $this->birth_date;
    }

    /**
     * Set the value of Birth Date
     *
     * @param mixed birth_date
     *
     * @return self
     */
    public function setBirthDate($birth_date)
    {
        $this->birth_date = $birth_date;

        return $this;
    }

    /**
     * Get the value of Gender
     *
     * @return mixed
     */
    public function getGender()
    {
        return $this->gender;
    }

    /**
     * Set the value of Gender
     *
     * @param mixed gender
     *
     * @return self
     */
    public function setGender($gender)
    {
        $this->gender = $gender;

        return $this;
    }

}
