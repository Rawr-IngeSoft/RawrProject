<?php
include '../db_connect.php';

if($_SERVER['REQUEST_METHOD']=="GET"){
	echo "hicieron un request GET";
}elseif($_SERVER['REQUEST_METHOD']=="POST"){
	echo "hicieron un request POST";
}elseif ($_SERVER['REQUEST_METHOD']=="PUT") {
	echo "hicieron un request PUT";
}elseif($_SERVER['REQUEST_METHOD']=="DELETE"){
	echo "hicieron un request DELETE";
}