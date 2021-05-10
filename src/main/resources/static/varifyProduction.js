
'use strict';
var client = new Paho.MQTT.Client(gUrl.mqtt, Number(9001), "clientId");

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
			for ( var key in result.pData) {
				tbody.innerHTML += '<tr>'
						+ '<td>'
						+ result.pData[key].bay
						+ '</td>'
						+ '<td >'
						+ result.pData[key].batch_no
						+ '</td>'
						+ '<td >'
						+ result.pData[key].sku
						+ '</td>'
						+ '<td >'
						+ result.pData[key].line_no
						+ '</td>'
						+ '<td >'
						+ result.pData[key].qty
						+ '</td>'
						+ '<td align="center"><input class="btn btn-primary" type="button" data-toggle="modal" data-target="#verify" onclick="openForm(this)" value="Verify" id="vProduction"/></td>'
						+'</tr>'

			}
		}
	};
	xhttp1.open("GET", gUrl.url+"/getAllData", true);

	xhttp1.send();
}

window.onload = productionList();


var qty=[];
function openForm(element) {
qty=[];
	var row = element.parentNode.parentNode.rowIndex
	var tboby = document.getElementById("proTable");
	var objCells = tboby.rows.item(row).cells;
	document.getElementById("bayNo").innerHTML = objCells.item(0).innerHTML;
	document.getElementById("batchNo").innerHTML = objCells.item(1).innerHTML;
	document.getElementById("sku").innerHTML = objCells.item(2).innerHTML;
	document.getElementById("line_no").innerHTML = objCells.item(3).innerHTML;
	document.getElementById("quantity").innerHTML = objCells.item(4).innerHTML;
	qty.push(objCells.item(4).innerHTML);
}
 

   function verify(){
  	 var bay=document.getElementById("bayNo").innerHTML;
  	  var batch_no=document.getElementById("batchNo").innerHTML;
  	  var sku=document.getElementById("sku").innerHTML;
  	  var line_no=document.getElementById("line_no").innerHTML;
  	  var qty1=document.getElementById("quantity").innerHTML;
  	  var XHR2 = new XMLHttpRequest();
  	  var qt=-parseInt(qty[0])+parseInt(qty1);
  	  var hash={"bay_no":""+bay.trim()+"","batch_no":""+batch_no+"" ,"sku":""+sku+"" ,"qty":""+qt+"","status":"PASS" }
  	 console.log(hash)
  	 XHR2.open("POST",gUrl.url+"/verify?line_no="+line_no);
  	 XHR2.setRequestHeader("Content-Type","application/json;charset=UTF-8");

  	  XHR2.onload = function() {
  		  console.log(XHR2.responseText);
  	      var response =JSON.parse(XHR2.responseText);
  	      if(response['message']=="Successful") {
              alert("Successful");
		  window.location.reload(true);

  	 } else {

  	 alert("Unsuccessful"); } }

  	 XHR2.send(JSON.stringify(hash));}
  
  

	

	