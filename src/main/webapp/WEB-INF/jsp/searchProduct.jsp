
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Warehouse Management System</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.6 -->
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
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
        <link rel="stylesheet" href="plugins/jvectormap/jquery-jvectormap-1.2.2.css">
        <!-- Date Picker -->
        <link rel="stylesheet" href="plugins/datepicker/datepicker3.css">
        <!-- Daterange picker -->
        <link rel="stylesheet" href="plugins/daterangepicker/daterangepicker.css">
        <!-- bootstrap wysihtml5 - text editor -->
        <link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">

        <link rel="stylesheet" href="bootstrap.min.css">

    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">

            <header class="main-header">

                <a href="index" class="logo">
                    <!-- mini logo for sidebar mini 50x50 pixels -->
                    <span class="logo-mini"><b>W</b>MS</span>
                    <!-- logo for regular state and mobile devices -->
                    <span class="logo-lg"><b>Warehouse</b> Management System</span>
                </a>
                <!-- Header Navbar: style can be found in header.less -->
                <nav class="navbar navbar-static-top">
                    <!-- Sidebar toggle button-->
                    <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                        <span class="sr-only">Toggle navigation</span>
                    </a>

                    <div class="navbar-custom-menu">
                        <ul class="nav navbar-nav">
                            <!-- Messages: style can be found in dropdown.less-->
                            <li class="dropdown messages-menu">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                </a>
                            </li>
                            <!-- Notifications: style can be found in dropdown.less -->
                            <li class="dropdown notifications-menu">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                </a>

                            </li>
                            <!-- Tasks: style can be found in dropdown.less -->

                            <!-- User Account: style can be found in dropdown.less -->
                            <li class="dropdown user user-menu">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <img src="Image/logo.png" class="user-image" alt="User Image">
                                    <span class="hidden-xs"> </span>
                                </a>
                                <ul class="dropdown-menu">
                                    <!-- User image -->
                                    <li class="user-header">
                                        <img src="Image/logo2.png" class="img-circle" alt="User Image">

                                        <p>

                                        <i style="font-size: 18px"><%=session.getAttribute("userid")%></i><br>

                                            <small></small>
                                        </p>
                                    </li>

                                    <!-- Menu Footer-->
                                    <li class="user-footer">
                                        <div class="pull-left">
                                            <a href="#" class="btn btn-default btn-flat">Profile</a>
                                        </div>
                                        <div class="pull-right">
                                            <a href="login" class="btn btn-default btn-flat">Sign out</a>
                                        </div>
                                    </li>
                                </ul>
                            </li>
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
                                                    <li class="active"><a data-toggle="modal" data-target="#modalid"><i class="fa fa-circle-o"></i>Manual Insert Product</a></li>
                                                    <li class="active"><a href="updateProduction"><i class="fa fa-circle-o"></i>Update Product</a></li>
                                                     <li><a href="verifyProduct"><i class="fa fa-circle-o"></i>Verify Production</a></li>
                                                    <li><a href="searchProduct"><i class="fa fa-circle-o"></i>Search Product</a></li>
                                                    <li><a href="excelImport" "><i class="fa fa-circle-o"></i>Add SKU</a></li>
                                                        <li><a href="changeBayCapacity"><i class="fa fa-circle-o"></i>Update Bay</a></li>
                                                       <li><a href="changeSkuCapacity"><i class="fa fa-circle-o"></i>Update SKU</a></li>
                                                     <li><a href="/api/generateExcel"><i class="fa fa-circle-o"></i>GenerateReport</a></li>
                                                      <li><a href="downloadProductionExcel"><i class="fa fa-circle-o"></i>Download Production</a></li>
                                                     <li><a href="productionPlanImport"><i class="fa fa-circle-o"></i>Import Production Plan</a></li>

                                                </ul>
                                            </li>
                                            <li class="treeview">
                                                <a href="#">
                                                    <i class="fa fa-arrow-circle-right"></i>
                                                    <span>OUT</span>
                                                    <i class="fa fa-angle-left pull-right"></i>
                                                    </span>
                                                </a>
                                                <ul class="treeview-menu">
                                                  <li><a href="transport"><i class="fa fa-circle-o"></i>Make A Plan</a></li>
                                                  <li><a href="addDriverDetails"><i class="fa fa-circle-o"></i> Place Order To Bay</a></li>
                                                  <li><a href="orderDetails"><i class="fa fa-circle-o"></i> Order List</a></li>
                                                  <li><a href="dispatchExcelImport"><i class="fa fa-circle-o"></i> Import Dispatch Plan</a></li>
                                                  <li><a href="/api/generateTExcel"><i class="fa fa-circle-o"></i> Generate Report</a></li>
                                                  <li><a href="downloadTransportExcel"><i class="fa fa-circle-o"></i>Download Dispatch Plan</a></li>
                                                  <li><a href="manualOrder"><i class="fa fa-circle-o"></i>Mannual Order</a></li>                                                           <li><a href="complete"><i class="fa fa-circle-o"></i> Complete Order</a></li>
                                                </ul>
                                            </li>
                                           <!--  <li class="treeview">
                    						<li><a href="changepassword"><i class="fa fa-key"></i>Change Password</a></li>
                    						</li> -->



                                    </li>
                                </ul>

        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
           <ol class="breadcrumb">
  <h4 style="float: left;margin-right:20px;" ><a href="in"><i class="fa fa-arrow-circle-left"></i>&nbsp;IN</a></h4> <h4 style="float: right "><a href="index"><i class="fa fa-home"></i>&nbsp;Home</a></h4>

            </ol>
        </section>

         <div class="container">
            <h2 style="text-align:center"> SEARCH PRODUCT</h2>
             <section class="content">
                    <div class="panel panel-primary" style="width:100%;overflow:scroll">
<table>
                    <tr>
                    <td>
                    <div class="form-group" style="margin-left:18px">
                    <label for="sku">Sku:</label>
                        <select class="form-control" id="sku"style="width:160px;height:40px" onchange="getSearchWithSku()">
                         <option value="Select">Select</option>

                        </select></div></td>

                    <td>
                     <div class="form-group" style="margin-left:50px">
                    <label for="batch">Batch No:</label>
                    <select class="form-control" id="batch"style="width:150px;height:40px" onchange="getSearchWithBatch()">
                    <option value="Select">Select</option>
                    </select>
                    </div>
                    </td>

                     <td>
                      <div class="form-group" style="margin-left:50px">
                     <label for="cars">Bay No:</label>
                     <select class="form-control" id="bay" style="width:150px;height:40px" onchange="getSearchWithBay()">
                     <option value="Select">Select</option>
                   
                     </select></div></td>
                     </tr>
                     </table>
                        <div class="container">


                            <table id="orderTable" class="table table-bordered table-striped" style="width:100%;">
                        <thead>
                        <tr>
                          <th>Sku.</th>
                          <th>Batch No.</th>
                          <th>Quantity.</th>
                          <th>Bay.</th>
                          <th>Status.</th>

                        </tr>
                        </thead>
                       <tbody id="searchOrder">

                        </tbody>
                      </table>


                        </div>
                    </div>
                </section>





                </div>


  </div>

    <!-- /.content-wrapper -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
        </div>
       <!--  <strong>Copyright &copy; 2018-2019 <a href="#"></a>.</strong> -->
    </footer>

    <!-- Control Sidebar -->
    <aside class="control-sidebar control-sidebar-dark">
        <!-- Create the tabs -->
        <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
            <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
            <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
        </ul>

    </aside>
    <!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->
	<!--popup modal  -->
	<div class="modal fade" id="modalid" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header" style="background:#3c8dbc">
        <h3 class="modal-title" style="color:white">Enter Password To Insert Production</h3>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
           <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      		<div class="row"> 
      			<div class="col-md-12">
      				<h2 id="comp_name"></h2>
      			</div>
      		</div> 
      		<br>
      			<div class="row"> 
      			<div class="col-md-12">
      				<input type="password" id="password" class="form-control" placeholder="Enter Password">
      			</div>
      		</div> 
       </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" onclick="popup()">Confirm</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<!-- jQuery 2.2.3 -->
<script src="plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- jQuery UI 1.11.4 -->
<script src="jquery-ui.min.js"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
    $.widget.bridge('uibutton', $.ui.button);
</script>
<!-- Bootstrap 3.3.6 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- Morris.js charts -->
<script src="raphael-min.js"></script>
<script src="plugins/morris/morris.min.js"></script>
<!-- Sparkline -->
<script src="plugins/sparkline/jquery.sparkline.min.js"></script>
<!-- jvectormap -->
<script src="plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- jQuery Knob Chart -->
<script src="plugins/knob/jquery.knob.js"></script>
<!-- daterangepicker -->
<script src="moment.min.js"></script>
<script src="plugins/daterangepicker/daterangepicker.js"></script>
<!-- datepicker -->
<script src="plugins/datepicker/bootstrap-datepicker.js"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<!-- Slimscroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="dist/js/pages/dashboard.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
 <script src="url.js"></script>
<script src="searchProduct.js"></script>
<script src="paho.js"></script>
<script src="completionAlert.js"></script>
<script>
document.getElementById("comp_name").innerHTML=localStorage.getItem("user_id");

function popup(){
	var XHR = new XMLHttpRequest();
	XHR.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = XHR.responseText;
	        var result=JSON.parse(response);
	        console.log(result);
			if(result.message=="Successful"){
			
			window.location.href="insertProduction";
			}else{
			alert("Please Enter Correct Password");
			}
	    }
	};
	XHR.open("GET", gUrl.url+"/getPassword?user_name="+document.getElementById("comp_name").innerHTML+"&password="+document.getElementById("password").value, true);
	XHR.send();
	}
</script>
</body>
</html>
