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



function skuList(){
	var xhttp1 = new XMLHttpRequest();
	xhttp1.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = xhttp1.responseText;
	        var result=JSON.parse(response);
	        console.log(result);
	      var sku=document.getElementById("sku");
	      sku.innerHTML="";
          for(var key in result.sku){
        	  sku.innerHTML+='<option value='+result.sku[key].sku+'>';
          }
	    }
	};
	xhttp1.open("GET", gUrl.url+"/getSkuList", true);

	xhttp1.send();
	}

window.onload=skuList();

function insertProductionPlan(){
	
	var sku=document.getElementById("sku1").value;
	var line_no=document.getElementById("line_no").value;
	var qty=document.getElementById("qty").value;
	if(sku==""){
		alert("Please Enter Sku");
	}
	else if(line_no==""){
		alert("Please Enter Line No");
	}
	else if(qty==""){
		alert("Please Enter Quantity")
	}
	else{
		console.log(sku);
		 var XHR2 = new XMLHttpRequest();
         var hash={"sku":""+sku+"","line_no":""+line_no+"","qty":""+qty+""}
		
		XHR2.open("POST", gUrl.url+"/insertProductionPlan");
		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


	XHR2.onload = function() {
	          console.log(XHR2.responseText);
	          var response = JSON.parse(XHR2.responseText);
	          if(response['message']=="Successful") {

	           	getProductionPlan();
	           	skuList();
	               alert("Successfully Inserted");
	               document.getElementById("sku1").value="";
	               document.getElementById("line_no").value="";
	           	   document.getElementById("qty").value="";
	          var message = new Paho.MQTT.Message("Kuchh Bhi");
              message.destinationName = "plan";
              client.send(message);
	          }
	          
			  else {

	            alert("unSuccessful");
	        
	          }
	      }


	XHR2.send(JSON.stringify(hash));
}
}
	function getProductionPlan(){
		var xhttp1 = new XMLHttpRequest();
		xhttp1.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		       // Typical action to be performed when the document is ready:
		        var response = xhttp1.responseText;
		        var result=JSON.parse(response);
		        console.log(result);
		  var ppTable=document.getElementById("pp");
		      ppTable.innerHTML="";
		      ppTable.innerHTML+='<tr  style="background:#3c8dbc;color:white;height:40px;text-align:center">'+
              '<th style="text-align:center">SKU</th>'+
              '<th style="text-align:center">LINE NO</th>'+
              '<th style="text-align:center">QTY</th>'+
              '<th style="text-align:center">BATCH NO</th>'+
              '<th style="text-align:center" >TIME DATE</th>'+
              '</tr>'
	         for(var key in result.proplan){
	        	  ppTable.innerHTML+='<tr  style="text-align:center">'+
	              '<td style="text-align:center">'+result.proplan[key].sku+'</td>'+
	              '<td style="text-align:center">'+result.proplan[key].line_no+'</td>'+
	              '<td style="text-align:center">'+result.proplan[key].qty+'</td>'+
	              '<td style="text-align:center">'+result.proplan[key].batch_no+'</td>'+
	              '<td style="text-align:center" >'+result.proplan[key].date+'</td>'+
	              '</tr>'
	          }
		    }
		};
		xhttp1.open("GET", gUrl.url+"/getTodayProductionPlan", true);

		xhttp1.send();
		}
window.onload=getProductionPlan();