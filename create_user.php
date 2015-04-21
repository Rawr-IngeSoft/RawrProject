<?php

/*
 * Create a new user row
 * @param conn      conection to db
 * @param username  user usernaem
 * @param password  user password or null
 */
function createUser($conn, $username, $password){

  $password = $password != NULL ? "'$password'" : "NULL";
  /* MySql query insert new row in UserOwner */
  $mysql_query = "INSERT INTO User(username, password)
                        VALUES('$username', $password)";

  return $conn->query($mysql_query);
}

?>
