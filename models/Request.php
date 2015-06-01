<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include_once 'Model.php';

class Request extends Model{
  	protected $username_sender;
  	protected $username_receiver;
    protected $date;
  	protected $text;
  	protected $status;

    /**
     * name of the table in data base
     * @return name of the table in db related to this model
     */
    public static function tableName(){
        return 'Request';
    }

    /**
     * get the names of the atributes of this model in an array
     * @return array with the names of the attributes of this model
     */
    public function getAttributes(){
        $attributes = array(
          'username_sender'  => $this->username_sender,
          'username_receiver' => $this->username_receiver,
          'date' => $this->date,
          'text' => $this->text,
          'status' => $this->status
        );
        return $attributes;
    }

    /**
     * Get the value of Username Sender
     *
     * @return mixed
     */
    public function getUsernameSender()
    {
        return $this->username_sender;
    }

    /**
     * Set the value of Username Sender
     *
     * @param mixed username_sender
     *
     * @return self
     */
    public function setUsernameSender($username_sender)
    {
        $this->username_sender = $username_sender;

        return $this;
    }

    /**
     * Get the value of Username Receiver
     *
     * @return mixed
     */
    public function getUsernameReceiver()
    {
        return $this->username_receiver;
    }

    /**
     * Set the value of Username Receiver
     *
     * @param mixed username_receiver
     *
     * @return self
     */
    public function setUsernameReceiver($username_receiver)
    {
        $this->username_receiver = $username_receiver;

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

}
?>
