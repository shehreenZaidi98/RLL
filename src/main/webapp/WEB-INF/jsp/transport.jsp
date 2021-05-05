
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Warehouse Management System</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<!-- Font Awesome -->
 <!-- Font Awesome -->
        <link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css">
        <!-- Ionicons -->
        <link rel="stylesheet" href="ionicons.min.css">
        <!-- Theme style -->
<link rel="stylesheet" href="dist/css/AdminLTE.min.css">

<link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">
<!-- iCheck -->
<link rel="stylesheet" href="plugins/iCheck/flat/blue.css">
<!-- Morris chart -->
<link rel="stylesheet" href="plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet"
	href="plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Date Picker -->
<link rel="stylesheet" href="plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="plugins/daterangepicker/daterangepicker.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<link rel="stylesheet" href="dist/boot.css">
<link rel="stylesheet" href="dist/boot1.css">
<link rel="stylesheet" href="dist/boot3.css">
<link rel="stylesheet" href="style.css">
<link rel="stylesheet"href="jquery-ui.css">
<style>
.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

#popup {
    width:220px;
    height:100px;
    padding:20px;
    background-color:#3c8dbc;
    position:absolute;
    top:200px;
    left:600px;
    display:none;
}
</style>


</head>
<body class="hold-transition skin-blue sidebar-mini">
	<div class="wrapper">

		<header class="main-header">

			<a href="index" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
				<span class="logo-mini"><b>W</b>MS</span> <!-- logo for regular state and mobile devices -->
				<span class="logo-lg"><b>Warehouse</b> Management System</span>
			</a>
			<!-- Header Navbar: style can be found in header.less -->
			<nav class="navbar navbar-static-top">
				<!-- Sidebar toggle button-->
				<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
					role="button"> <span class="sr-only">Toggle navigation</span>
				</a>

				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<!-- Messages: style can be found in dropdown.less-->
						<li class="dropdown messages-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> </a></li>
						<!-- Notifications: style can be found in dropdown.less -->
						<li class="dropdown notifications-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> </a></li>
						<!-- Tasks: style can be found in dropdown.less -->

						<!-- User Account: style can be found in dropdown.less -->
						<li class="dropdown user user-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> <img
								src="Image/logo.png" class="user-image" alt="User Image">
								<span class="hidden-xs"> </span>
						</a>
							<ul class="dropdown-menu">
								<!-- User image -->
								<li class="user-header"><img src="Image/logo2.png"
									class="img-circle" alt="User Image">

									<p>

										<i style="font-size: 18px"><%=session.getAttribute("userid")%></i><br>

										<small></small>
									</p></li>

								<!-- Menu Footer-->
								<li class="user-footer">
									<div class="pull-left">
										<a href="#" class="btn btn-default btn-flat">Profile</a>
									</div>
									<div class="pull-right">
										<a href="login" class="btn btn-default btn-flat">Sign out</a>
									</div>
								</li>
							</ul></li>
						<!-- Control Sidebar Toggle Button -->

					</ul>
				</div>
			</nav>
		</header>
		<!-- Left side column. contains the logo and sidebar -->
		<aside class="main-sidebar">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar">
				<!-- Sidebar user panel -->
				<div class="user-panel">
					<div class="pull-left image">
						<img src="Image/logo.png" class="img-circle" alt="User Image">
					</div>
					<div class="pull-left info">
						<p></p>

					</div>
				</div>

				<!-- /.search form -->
				<!-- sidebar menu: : style can be found in sidebar.less -->
				   <ul class="sidebar-menu">
                                        <li class="header">MAIN NAVIGATION</li>
                                        <li class="treeview">
                                            <a href="#">
                                                <i class="fa fa-arrow-circle-left"></i> <span>IN</span>
                                                <span class="pull-right-container">
                                                    <i class="fa fa-angle-left pull-right"></i>
                                                </span>
                                            </a>
                                            <ul class="treeview-menu">
                                             <li><a href="productionPlan"><i class="fa fa-circle-o"></i>Production Plan</a></li>
                                             <li class="active"><a href="#" title="This Option is disable in Out"><i class="fa fa-circle-o"></i>Manual Insert Product</a></li>
                                             <li class="active"><a href="updateProduction"><i class="fa fa-circle-o"></i>Update Product</a></li>
                                             <li><a href="verifyProduct"><i class="fa fa-circle-o"></i>Verify Production</a></li>
                                             <li><a href="searchProduct"><i class="fa fa-circle-o"></i>Search Product</a></li>
                                             <li><a href="excelImport" "><i class="fa fa-circle-o"></i>Add SKU</a></li>
                                              <li><a href="changeBayCapacity"><i class="fa fa-circle-o"></i>Update Bay</a></li>
                                              <li><a href="changeSkuCapacity"><i class="fa fa-circle-o"></i>Update SKU</a></li>
                                              <li><a href="/api/generateExcel"><i class="fa fa-circle-o"></i>GenerateReport</a></li>
                                              <li><a href="downloadProductionExcel"><i class="fa fa-circle-o"></i>Download Production</a></li>
                                              <li><a href="productionPlanImport"><i class="fa fa-circle-o"></i>Import Production Plan</a></li>
                                            </ul</li>
                                        <li class="treeview">
                                            <a href="#">
                                                <i class="fa fa-arrow-circle-right"></i>
                                                <span>OUT</span>
                                                <i class="fa fa-angle-left pull-right"></i>
                                                </span>
                                            </a>
                                            <ul class="treeview-menu">
                                                <li><a href="transport"><i class="fa fa-circle-o"></i>Make An Order</a></li>
                                                <li><a href="orderDetails"><i class="fa fa-circle-o"></i> Order Details</a></li>
                                            </ul>
                                        </li>




                                </li>
                            </ul>



			</section>
			<!-- /.sidebar -->
		</aside>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<h1>
					Dashboard <small>Control panel</small>
				</h1>
				<ol class="breadcrumb">
					<h4 style="float: left; margin-right: 20px;">
						<a href="out"><i class="fa fa-arrow-circle-right"
							style="margin-left: 20px;"></i>&nbsp;OUT</a>
					</h4>
					<h4 style="float: right">
						<a href="index"><i class="fa fa-home"></i>&nbsp;Home</a>
					</h4>

				</ol>
			</section>

			<!-- Main content -->
			<section class="content">
				<div class="panel panel-primary" style="width:100%;overflow:auto;">
				<div align="left">
			<a   href="purchase">Previous Order</a><div>
			<div align="right" style="margin-top:-20px">
		    <a   href="addDriverDetails">Place Order To Bay</a></div >
            <div align="center" style="margin-top:-10px">
            <h2>Make Custom Bay Order<h2>
            <label class="switch">
              <input type="checkbox" id="myChecked" onclick="makePassword()">
              <span class="slider round"></span>
            </label>
		    </div>
					<div class="container" style="margin-left: 12%;">
						<table>
							<tr>
								<td>
									<div class="form-group">
										<h5 style="font-weight: bold;">
											Party Name:&nbsp;<label for="code"
												style="font-weight: bold; color: red;">*</label>
											<h5>
												<input list="party1" class="form-control" id="partyName"
													name="partyName" placeholder="Enter Party Name" required>
												<datalist id="party1"></datalist>
									</div>
								</td>
								<td>

									<div class="form-group" style="margin-left: 100px;">
										<h5 style="font-weight: bold;">
											Address:&nbsp; <label for="code"
												style="font-weight: bold; color: red;">*</label>
										</h5>
										<input list="transport1" class="form-control" id="address"
											name="address" placeholder="Enter Address" required>
										<datalist id="transport1"></datalist>

									</div>
								</td>

								<td>

									<div class="form-group" style="margin-left: 100px;">

										<h5 style="font-weight: bold;">
											State:&nbsp;<label for="code"
												style="font-weight: bold; color: red;">*</label>
										</h5>

										<select class="form-control" id="state" name="state">
											<option value="Select">Select</option>

										</select>
									</div>
								</td>
							</tr>
						</table>
						<div class="form-group" style="width: 69%">
							<h5 style="font-weight: bold;">
								Permit No:&nbsp;<label for="code"
									style="font-weight: bold; color: red;">*</label>
							</h5>
							<input type="text" class="form-control" id="permit"
								placeholder="Enter Permit No" />
						</div>

						<br>
						<div style="margin-left: 350px">
							<button class="btn btn-danger" onclick="insertTransport()">Submit</button>
						</div>


						<br> <br> <br>

					</div>
				</div>
			</section>
			<!-- /.content -->
		</div>
		<!-- /.content-wrapper -->
		<footer class="main-footer">
			<div class="pull-right hidden-xs"></div>
			<!--   <strong>Copyright &copy; 2018-2019 <a href="#"></a>.</strong> -->
		</footer>

		<!-- Control Sidebar -->
		<aside class="control-sidebar control-sidebar-dark">
			<!-- Create the tabs -->
			<ul class="nav nav-tabs nav-justified control-sidebar-tabs">
				<li><a href="#control-sidebar-home-tab" data-toggle="tab"><i
						class="fa fa-home"></i></a></li>
				<li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i
						class="fa fa-gears"></i></a></li>
			</ul>

		</aside>
		<!-- /.control-sidebar -->
		<!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
		<div class="control-sidebar-bg"></div>
	</div>
	<div id="popup">
        <div>Enter Password:</div>
        <input id="password" type="password"/>
        <button onclick="done()" style="background:lightgreen">Done</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button onclick="cancel()" style="background:orange">Cancel</button>

    </div>
	<script src="jquery-1.12.4.js"></script>
     <script src="jquery-ui.js"></script>
	<script src="dist/bootstrap-tokenfield.js"></script>
	<script src="paho.js"></script>
 <script src="url.js"></script>
	<script src="transport.js"></script>
	<script src="paho.js"></script>
	<script src="completionAlert.js"></script>


</body>
</html>