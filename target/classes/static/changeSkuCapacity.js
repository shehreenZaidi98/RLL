'use strict';


/*var client = new Paho.MQTT.Client(gUrl.mqtt, Number(9001), "clientId");

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
	}*/



function skuList(){
	var xhttp1 = new XMLHttpRequest();
	xhttp1.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = xhttp1.responseText;
	        var result=JSON.parse(response);
	        console.log(result);
	      var sku=document.getElementById("sku");
	      var ppTable=document.getElementById("pp");
	      ppTable.innerHTML="";
	      ppTable.innerHTML+='<tr  style="background:#3c8dbc;color:white;height:40px;text-align:center">'+
          '<th style="text-align:center">SKU</th>'+
          '<th style="text-align:center">CAPACITY</th>'+
          '<th style="text-align:center">WEIGHT</th>'+
          '</tr>'
	      sku.innerHTML="";
          for(var key in result.sku){
        	  sku.innerHTML+='<option value='+result.sku[key].sku+'>';
        	  ppTable.innerHTML+='<tr  style="text-align:center">'+
              '<td style="text-align:center">'+result.sku[key].sku+'</td>'+
              '<td style="text-align:center">'+parseInt(result.sku[key].cases_of_pallets)+'</td>'+
              '<td style="text-align:center">'+parseInt(result.sku[key].pallet_weight)+'</td></tr>'
        	  
          }
	    }
	};
	xhttp1.open("GET", gUrl.url+"/getSkuList", true);

	xhttp1.send();
	}

window.onload=skuList();

function insertSku(){
	
	var sku=document.getElementById("sku1").value;
	var capacity=document.getElementById("capacity").value;
	var weight=document.getElementById("weight").value;
	if(sku==""){
		alert("Please Enter Sku");
	}
	else if(capacity==""){
		alert("Please Enter Capacity");
	}
	else if(weight==""){
		alert("Please Enter weight");
	}

	else{
		console.log(sku);
		 var XHR2 = new XMLHttpRequest();
         var hash={"sku":""+sku+"","cases_of_pallets":""+capacity+"","pallet_weight":""+weight+""}
		
		XHR2.open("POST", gUrl.url+"/updateSku");
		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


	XHR2.onload = function() {
	          console.log(XHR2.responseText);
	          var response = JSON.parse(XHR2.responseText);
	          if(response['message']=="Successful") {
	        	  

	               alert("Successfully Inserted");
	               document.getElementById("sku1").value="";
	               document.getElementById("capacity").value="";
	               document.getElementById("weight").value="";
	           	   skuList();
	          }
	          
			  else {

	            alert("unSuccessful");
	        
	          }
	      }


	XHR2.send(JSON.stringify(hash));
}
}
	