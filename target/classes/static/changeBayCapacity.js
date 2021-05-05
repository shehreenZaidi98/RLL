function bayList(){
	var xhttp1 = new XMLHttpRequest();
	xhttp1.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = xhttp1.responseText;
	        var result=JSON.parse(response);
	        console.log(result);
	      var bay=document.getElementById("bay");
	      var ppTable=document.getElementById("pp");
	      ppTable.innerHTML="";
	      ppTable.innerHTML+='<tr  style="background:#3c8dbc;color:white;height:40px;text-align:center">'+
          '<th style="text-align:center">BAY</th>'+
          '<th style="text-align:center">CAPACITY</th>'+
          '</tr>'
	      bay.innerHTML="";
          for(var key in result.bay){
        	  bay.innerHTML+='<option value='+result.bay[key].bay+'>';
        	  ppTable.innerHTML+='<tr  style="text-align:center">'+
              '<td style="text-align:center">'+result.bay[key].bay+'</td>'+
              '<td style="text-align:center">'+parseInt(result.bay[key].capacity)+'</td></tr>'
        	  
          }
	    }
	};
	xhttp1.open("GET", gUrl.url+"/getBayList", true);

	xhttp1.send();
	}

window.onload=bayList();

function insertBay(){
	
	var bay=document.getElementById("bay1").value;
	var capacity=document.getElementById("capacity").value;
	if(bay==""){
		alert("Please Enter Bay");
	}
	else if(capacity==""){
		alert("Please Enter Capacity");
	}
	
	else{
		 var XHR2 = new XMLHttpRequest();
         var hash={"bay":""+bay+"","capacity":""+capacity+""}
		
		XHR2.open("POST", gUrl.url+"/insertBay");
		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


	XHR2.onload = function() {
	          console.log(XHR2.responseText);
	          var response = JSON.parse(XHR2.responseText);
	          if(response['message']=="Successful") {
	        	  

	               alert("Successfully Inserted");
	               document.getElementById("bay1").value="";
	               document.getElementById("capacity").value="";
	           	   bayList();
	          }
	          
			  else {

	            alert("unSuccessful");
	        
	          }
	      }


	XHR2.send(JSON.stringify(hash));
}
}
	