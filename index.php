<?php
	if (isset($_POST["Token"])) {
	$token=$_POST["Token"];
		   $connection = mysqli_connect("mysql3.gear.host:3306","tutorme","pizza_pie","tutorme") or die("Database connectivity error");
		   $query="INSERT INTO tokens (Token) VALUES ( '$token') "
              ." ON DUPLICATE KEY UPDATE Token = '$token';";
      mysqli_query($connection,$query) or die(mysqli_error($connection));
      mysqli_close($connection);
	}
 ?>