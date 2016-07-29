<?php
	if (isset($_POST["Token"])) {

		   $_uv_Token=$_POST["Token"];
		   $conn = mysqli_connect("mysql3.gear.host","tutorme","pizza_pie","tutorme") or die("Error connecting");
		   $q="INSERT INTO Token (Token) VALUES ( '$_uv_Token') "
              ." ON DUPLICATE KEY UPDATE Token = '$_uv_Token';";

      mysqli_query($conn,$q) or die(mysqli_error($conn));
      mysqli_close($conn);
	}
 ?>