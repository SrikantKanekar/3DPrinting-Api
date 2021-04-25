<#macro main css js title="3d printer website">
<!doctype html>
<html lang="en">

	<head>
		<title>${title}</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

		<!--===============================================================================================-->
		<link rel="icon" type="image/png" href="/static/images/icons/favicon.ico" />
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/bootstrap/css/bootstrap.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/fonts/Linearicons-Free-v1.0.0/icon-font.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/animate/animate.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/css-hamburgers/hamburgers.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/animsition/css/animsition.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/select2/select2.min.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/vendor/daterangepicker/daterangepicker.css">
		<!--===============================================================================================-->
		<link rel="stylesheet" type="text/css" href="/static/css/auth/util.css">
		<link rel="stylesheet" type="text/css" href=${css}>
		<!--===============================================================================================-->  
	</head>
	<body>
		
		<#nested/>
		
		<!--===============================================================================================-->
		<script src="/static/vendor/jquery/jquery-3.2.1.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/animsition/js/animsition.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/bootstrap/js/popper.js"></script>
		<script src="/static/vendor/bootstrap/js/bootstrap.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/select2/select2.min.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/daterangepicker/moment.min.js"></script>
		<script src="/static/vendor/daterangepicker/daterangepicker.js"></script>
		<!--===============================================================================================-->
		<script src="/static/vendor/countdowntime/countdowntime.js"></script>
		<!--===============================================================================================-->
		<script src=${js}></script>
	</body>
</html>
</#macro>