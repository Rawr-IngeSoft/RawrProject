<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
//require 'Owner.php';
require '../db_connect.php';

class Pet {
	protected $username;
	protected $owner;
	protected $name;
	protected $type;
	protected $race;
	protected $birth_date;
	protected $gender;

	public function __contruct(){

	}
	public static function withAll($pUsername, $pOwner, $pName, $pType, $pRace, $pBirth_date, $pGender){
		$instace= new self();
		/*To Do: falta crear el resto de los métodos */
		$this->username =$pUsername;
		$this->owner = $pOwner;
		$this->name = $pName;
		$this->type = $pType;
		$this->race = $pRace;
		$this->birth_date= $pBirth_date;
		$this->gender= $pGender;
		return $instance;
	}
	public static function withPK($pUsername){
		$instance= new self();
		$instance->setUsername($pUsername);
		return $instance;
	}
	public function setUsername($pUsername){
		$this->username= $pUsername;
	}
	public function getUsername(){
		return $this->username;
	}

	public function setOwner($pOwner){
		$this->owner= $pOwner;
	}
	public function getOwner(){
		return $this->owner;
	}
	public function loadPet(){
		$conn = dbConnect();
		$mysql_query = "SELECT * FROM Pet WHERE username ='".$this->getUsername()."'";
		$result = $conn->query($mysql_query);
		if ($result->num_rows == 1) {
    		$this->setUsername($row['username']);
    		$this->setOwner($row['username']);
    		$this->setUsername($row['username']);
    		$this->setUsername($row['username']);
    		/*
    		$row = $result->fetch_assoc())
            $row['idComment']
            $row['username']
            $row['text']
            $row['date']
            */

    		return true;
 		}else{

    		return false;
 		}


	}

}


$pet =  Pet::withPK('Pongo');
