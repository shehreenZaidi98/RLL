
'use strict';
var client = new Paho.MQTT.Client(gUrl.mqtt, Number(9001), "clientId");

//set callback handlers
client.onConnectionLost = onConnectionLost;
client.onMessageArrived = onMessageArrived;

//connect the client
client.connect({onSuccess:onConnect});
		        	  function onConnect() {
		        	  	//Once a connection has been made, make a subscription and send a message.
		        	  	console.log("onConnect");
		        	  	client.subscribe("#");
		        	  	}
function onConnectionLost(responseObject) {
	if (responseObject.errorCode !== 0) {
	console.log("onConnectionLost:"+responseObject.errorMessage);
	}
	}

function onMessageArrived(message) {
	console.log(message.payloadString)
	}


function productionList() {
	var xhttp1 = new XMLHttpRequest();
	xhttp1.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			// Typical action to be performed when the document is ready:
			var response = xhttp1.responseText;
			var result = JSON.parse(response);
			console.log(result);

			var tbody = document.getElementById("table");
			tbody.innerHTML="";
			for ( var key in result.productionData) {
				tbody.innerHTML += '<tr>'
						+ '<td>'
						+ result.productionData[key].bay_no
						+ '</td>'
						+ '<td >'
						+ result.productionData[key].batch_no
						+ '</td>'
						+ '<td >'
						+ result.productionData[key].sku
						+ '</td>'
						+ '<td >'
						+ result.productionData[key].qty
						+ '</td>'
						+ '<td >'
						+ result.productionData[key].status
						+ '</td>'
						+ '<td align="center"><input class="btn btn-primary" type="button" data-toggle="modal" data-target="#changeBay" onclick="openForm(this)" value="Change Bay" id="cBay"/></td>'
						+ '<td align="center"><input class="btn btn-primary" type="button" data-toggle="modal" data-target="#changeStatus" onclick="openForm1(this)" value="Change Status" /></td></tr>'

			}
		}
	};
	xhttp1.open("GET", gUrl.url+"/getProductionData1", true);

	xhttp1.send();
}

window.onload = productionList();

var bayList = [];
function openForm(element) {
	bayList = [];
/*	document.getElementById("table").style.display = "block";
*/	var row = element.parentNode.parentNode.rowIndex
	var tboby = document.getElementById("proTable");
	var objCells = tboby.rows.item(row).cells;
	// alert(objCells.item(0).innerHTML);
	document.getElementById("bayNo").value = objCells.item(0).innerHTML;
	document.getElementById("batchNo").innerHTML = objCells.item(1).innerHTML;
	document.getElementById("sku").innerHTML = objCells.item(2).innerHTML;
	document.getElementById("quantity").innerHTML = objCells.item(3).innerHTML;
	document.getElementById("status").innerHTML = objCells.item(4).innerHTML;
	bayList.push(objCells.item(0).innerHTML);
}


var statusList = [];
function openForm1(element) {
	statusList = [];
/*	document.getElementById("table").style.display = "block";
*/	var row = element.parentNode.parentNode.rowIndex
	var tboby = document.getElementById("proTable");
	var objCells = tboby.rows.item(row).cells;
	// alert(objCells.item(0).innerHTML);
	document.getElementById("bayNo1").innerHTML = objCells.item(0).innerHTML;
	document.getElementById("batchNo1").innerHTML = objCells.item(1).innerHTML;
	document.getElementById("sku1").innerHTML = objCells.item(2).innerHTML;
	document.getElementById("quantity1").innerHTML = objCells.item(3).innerHTML;
	document.getElementById("status1").value = objCells.item(4).innerHTML;
	statusList.push(objCells.item(4).innerHTML);
}
 
 function shiftProduction(){
	 var bay=document.getElementById("bayNo").value; 
	  var batch_no=document.getElementById("batchNo").innerHTML; 
	  var sku=document.getElementById("sku").innerHTML; 
	  var qty=document.getElementById("quantity").innerHTML; 
	  var status=document.getElementById("status").innerHTML; 
		if(confirm("Press ok to confirm")){	  
	  var XHR2 = new XMLHttpRequest(); 
	  var hash={"bay_no":""+bay.trim()+"","batch_no":""+batch_no+"" ,"sku":""+sku+"" ,"qty":""+qty+"","status":""+status.trim()+"" } 
	 console.log(hash)
	 XHR2.open("POST",gUrl.url+"/changeBayAndStatus");
	 XHR2.setRequestHeader("Content-Type","application/json;charset=UTF-8");
	 
	  XHR2.onload = function() {
		  console.log(XHR2.responseText); 
	      var response =JSON.parse(XHR2.responseText); 
	      if(response['message']=="Successful") { 
		 var XHR3 = new XMLHttpRequest(); 
		 var hash1={"bay_no":""+bayList[0].trim()+"","batch_no":""+batch_no+"" ,"sku":""+sku+"" ,"qty":""+-qty+"","status":""+status+"" } 
		 console.log(hash1) 
		 XHR3.open("POST", gUrl.url+"/changeBayAndStatus"); XHR3.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	 
	  XHR3.onload = function() { 
		  console.log(XHR3.responseText); 
	  var response =JSON.parse(XHR3.responseText); 
	 if(response['message']=="Successful") {
		 changeLocation(bay.trim(),batch_no,sku,qty,status,bayList[0].trim());
	 alert("Successfully Inserted"); 
	 
	 productionList(); } 
	 else {
	 
	alert(response); } }
	 
	 XHR3.send(JSON.stringify(hash1)); }

	  else {
	 
	 alert(response); } }
	 
	 XHR2.send(JSON.stringify(hash));}
}
 
  function statusProduction(){
  var bay=document.getElementById("bayNo1").innerHTML;
  var batch_no=document.getElementById("batchNo1").innerHTML; 
   var sku=document.getElementById("sku1").innerHTML; 
   var qty=document.getElementById("quantity1").innerHTML; 
   var status=document.getElementById("status1").value;
   if(bay==""){ 
	   alert("PleaseEnter bay"); } 
   else if(qty==""){
	   alert("Please Enter Quantity"); } 
   else{
   }
  if(confirm("Press ok to confirm")){
  var XHR2 = new XMLHttpRequest(); 
  var hash={"bay_no":""+bay+"","batch_no":""+batch_no+"" ,"sku":""+sku+"" ,"qty":""+qty+"","status":""+status.trim()+"" } 
 XHR2.open("POST",gUrl.url+"/changeBayAndStatus"); XHR2.setRequestHeader("Content-Type","application/json;charset=UTF-8");
 
  XHR2.onload = function() { console.log(XHR2.responseText); 
 var response =JSON.parse(XHR2.responseText); if(response['message']=="Successful") { 
	 var XHR3 = new XMLHttpRequest(); 
	 var hash1={"bay_no":""+bay+"","batch_no":""+batch_no+"" ,"sku":""+sku+"" ,"qty":""+-qty+"","status":""+statusList[0].trim()+"" } 
	 XHR3.open("POST", gUrl.url+"/changeBayAndStatus");
	  XHR3.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
 
  XHR3.onload = function() { 
	  console.log(XHR3.responseText); 
    var response =JSON.parse(XHR3.responseText); 
    if(response['message']=="Successful") {
    	changeStatus1(bay,batch_no,sku,qty,status);
 alert("Successfully Inserted");
 var hash={"message":"change location"}
 			 var  message = new Paho.MQTT.Message(JSON.stringify(hash));
 	        	  	message.destinationName = "location";
 	        	  	client.send(message);
		  window.location.reload(true);
 } 
 else {
 
alert(response); } }
 
 XHR3.send(JSON.stringify(hash1)); }

  else {
 
 alert(response); } }
 
 XHR2.send(JSON.stringify(hash)); 
	}
}
 
  
  
  
  function getBay(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		       // Typical action to be performed when the document is ready:
		        var response = xhttp.responseText;
		   var bay=document.getElementById("bayNo");
		    var result=JSON.parse(response);
		       for(var key in result.bay){
		    	   bay.innerHTML+= '<option value='+result.bay[key].bay+'>'+ result.bay[key].bay+ '</option>';    

		       }
		    }
		};
		xhttp.open("GET", gUrl.url+"/getBayList", true);

		xhttp.send();
		}


	window.onload=getBay();
	
	
	 function changeLocation(bay,batch,sku,qty,status,oldBay){
		
		  
		  var XHR2 = new XMLHttpRequest(); 
		  var hash={"bay_no":""+bay.trim()+"","batch_no":""+batch+"" ,"sku":""+sku+"" ,"qty":""+qty+"","status":""+status+"","old_bay":""+oldBay+"" } 
		 XHR2.open("POST",gUrl.url+"/insertChangeLocation");
		  
		 XHR2.setRequestHeader("Content-Type","application/json;charset=UTF-8");
		 
		  XHR2.onload = function() {
			  console.log(XHR2.responseText); 
		 var response =JSON.parse(XHR2.responseText);
		 if(response['message']=="Successful") { 
		      var hash={"message":"change location"}
			 var  message = new Paho.MQTT.Message(JSON.stringify(hash));
	        	  	message.destinationName = "location";
	        	  	client.send(message);
		  window.location.reload(true);

		 } else {
		 
		 alert(response); } 
		  }
		 XHR2.send(JSON.stringify(hash));
		 }
	 
	 
	 
	 
	 function changeStatus1(bay,batch,sku,qty,status){
		  var XHR2 = new XMLHttpRequest(); 
		  var hash={"bay_no":""+bay.trim()+"","batch_no":""+batch+"" ,"sku":""+sku+"" ,"qty":""+qty+"","status":""+status+""} 
		 XHR2.open("POST",gUrl.url+"/insertGoodStatus");
		  
		 XHR2.setRequestHeader("Content-Type","application/json;charset=UTF-8");
		 
		  XHR2.onload = function() {
			  console.log(XHR2.responseText); 
		 var response =JSON.parse(XHR2.responseText);
		 if(response['message']=="Successful") { 
			//mqtt laga lena\

		 } else {
		 
		 alert(response); } 
		  }
		 XHR2.send(JSON.stringify(hash));
		 }
	
	