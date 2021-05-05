
function bayList(){
	var xhttp1 = new XMLHttpRequest();
	xhttp1.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        var response = xhttp1.responseText;
	        var result=JSON.parse(response);
	        console.log(result);
	      var bay=document.getElementById("bay");
	      bay.innerHTML="";
	      bay.innerHTML='<option value="select">Select</option>';
          for(var key in result.bay){
        	  bay.innerHTML+='<option value='+result.bay[key].bay+'>'+result.bay[key].bay+'</option>';

          }
	    }
	};
	xhttp1.open("GET", gUrl.url+"/getBayList", true);

	xhttp1.send();
	}

window.onload=bayList();

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
	      sku.innerHTML='<option value="select">Select</option>';
          for(var key in result.sku){
        	  sku.innerHTML+='<option value='+result.sku[key].sku+'>'+result.sku[key].sku+'</option>';
          }
	    }
	};
	xhttp1.open("GET", "/api/getSkuList", true);

	xhttp1.send();
	}
window.onload=skuList();
function getProduction(){
	var xhttp1 = new XMLHttpRequest();
	xhttp1.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = xhttp1.responseText;
	        var result=JSON.parse(response);
	        console.log(result);
	      var table=document.getElementById("ip");
	      table.innerHTML="";
	      table.innerHTML=' <tr  style="background:#3c8dbc;color:white;height:40px;text-align:center">'+
                                                         '<th style="text-align:center">BAY</th>'+
                                                         '<th style="text-align:center">SKU</th>'+
                                                         '<th style="text-align:center">BATCH NO</th>'+
                                                         '<th style="text-align:center">QTY</th>'+
                                                         '</tr>';
          for(var key in result.production){
        	  table.innerHTML+='<tr  style="text-align:center">'+
                                      '<td style="text-align:center">'+result.production[key].bay_no+'</td>'+
                                      '<td style="text-align:center">'+result.production[key].sku+'</td>'+
                                      '<td style="text-align:center">'+result.production[key].batch_no+'</td>'+
                                      '<td style="text-align:center">'+result.production[key].qty+'</td>'+
                                      '</tr>';

          }
	    }
	};
	xhttp1.open("GET", gUrl.url+"/allProductionData", true);

	xhttp1.send();
	}

window.onload=bayList();
window.onload=getProduction();

function insertManualProduction(){

	var sku=document.getElementById("sku").value;
	var batch_no=document.getElementById("batch_no").value;
	var bay=document.getElementById("bay").value;
	var qty=document.getElementById("qty").value;
	var date=document.getElementById("date").value;
	if(sku==""||sku=="select"){
		alert("Please Select Sku");
	}
	else if(batch_no==""){
		alert("Please Enter Batch No");
	}
	else if(bay=="" || bay=="select"){
		alert("Please Select Bay")
	}
    else if(qty==""){
		alert("Please Enter Quantity")
	}
	else if(date==""){
		alert("Select Any Date")
	}
	else{
		 var XHR2 = new XMLHttpRequest();
         var hash={"sku":""+sku+"","batch_no":""+batch_no+"","bay_no":""+bay+"","qty":""+qty+""}

		XHR2.open("POST", "/api/insertManualProduction?month="+date);
		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


	XHR2.onload = function() {
	          console.log(XHR2.responseText);
	          var response = JSON.parse(XHR2.responseText);
	          if(response['message']=="Successful") {
			       alert("Successfully Inserted");
	           	   document.getElementById("bay").value="";
	           	   document.getElementById("qty").value="";
                   getProduction()
	          }
			  else {
	            alert("unSuccessful");
	          }
	      }
	XHR2.send(JSON.stringify(hash));
}
}


