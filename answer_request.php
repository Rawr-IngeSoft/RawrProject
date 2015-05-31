<?php
include 'db_connect.php';
// header para mostrar que se va a recbir un JSON
header("Content-Type: application/json; charset=UTF-8");
// Create connection to mysql database
$conn = DB::dbConnect();

$request_body = file_get_contents('php://input');

$json_array = json_decode($request_body, true);

$sender= $json_array['sender'];
$receiver=$json_array['receiver'];
$status= $json_array['status'];

$sql="SELECT 1 FROM Request
      WHERE username_sender = '$sender' AND username_receiver = '$receiver'";
$result = $conn->query($sql);
if($result > 0){

  if($status == 'rejected'){
    $sql="UPDATE Request SET status='$status'
          WHERE username_sender='$sender' AND username_receiver='$receiver'";
    if($conn->query($sql) == TRUE)
      $json_return= array('status' => '1');
    else
      $json_return= array('status' => '0');
    echo json_encode($json_return);

  }else if ($status == 'accepted'){
    $sql="UPDATE Request SET status='$status'
          WHERE username_sender='$sender' AND username_receiver='$receiver'";

    if($conn->query($sql) == TRUE){
      $sql="INSERT INTO Friends(username, username_friend)
            VALUES ('$sender', '$receiver')";
      if($conn->query($sql) == TRUE){
         $sql="INSERT INTO Friends(username, username_friend)
            VALUES ('$sender', '$receiver')";
          if($conn->query($sql) == TRUE){
		 $json_return= array('status' => '1');
          }else{
 		//roll back
	      $sql="DELETE FROM Friends
        	    WHERE username='$sender' AND username_friend='$receiver'";
              $json_return= array('status' => '0');

	  }
      }else{
        $json_return= array('status' => '0');
      }
      echo json_encode($json_return);
    }else{

      //roll back
      $sql="UPDATE Request SET status='pending'
            WHERE username_sender='$sender' AND username_receiver='$receiver'";

      $json_return= array('status' => '0');
      echo json_encode($json_return);
    }
  }else {//bad request status wrong
    $json_return= array('status' => '0');
    echo json_encode($json_return);
  }

}else{//request doesn't exist
  $json_return= array('status' => '0');
  echo json_encode($json_return);
}
