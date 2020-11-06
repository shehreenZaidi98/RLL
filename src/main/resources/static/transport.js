
function getAddress(){
	var XHR = new XMLHttpRequest();
	XHR.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = XHR.responseText;
	         var result=JSON.parse(response);
            	        console.log(result);
	        var address=document.getElementById("transport1")
	        for(var key in result.address){
	        address.innerHTML+='<option value='+result.address[key]+'>';
	        }

	    }
	};
	XHR.open("GET", gUrl.url+"/getAddress", true);

	XHR.send();
	}
function insertTransport(){


	var partyName=document.getElementById("partyName").value;
	var address=document.getElementById("address").value;
	var state=document.getElementById("state").value;
	var permit=document.getElementById("permit").value;

 if(partyName==""){
		alert("Please Enter partyName");
	}
else if(address==""){
		alert("Please Enter address");
	}
else if(state==""){
		alert("Please Enter state");
	}
else if(permit==""){
		alert("Please Enter permit");
	}
	else{
		 var XHR2 = new XMLHttpRequest();
         var hash={"driver_name":"",
         "contact_no":"",
         "vehicle_no":"",
         "party_name":""+partyName+"",
         "address":""+address+"",
         "state":""+state+"",
         "pallet_weight":"",
          "total_weight":"",
           "truck_bay_no":"",
         "permit_no":""+permit+""


         }
        console.log(hash);
		XHR2.open("POST", gUrl.url+"/insertTransport");
		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


	XHR2.onload = function() {
	          console.log(XHR2.responseText);
	          var response = JSON.parse(XHR2.responseText);
	          if(response['message']=="Successful") {

	             localStorage.setItem("state",document.getElementById("state").value);
	             if(document.getElementById("myChecked").checked){
	             window.location.href="customOrder";
	             }else{
	             window.location.href="purchase";
	             }
	          }

			  else {

	            alert("unSuccessful");

	          }
	      }


	XHR2.send(JSON.stringify(hash));
}
}



function getPartyName(){
	var XHR = new XMLHttpRequest();
	XHR.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        var response = XHR.responseText;
	         var result=JSON.parse(response);
	        var address=document.getElementById("party1")
	        for(var key in result.party){
	        address.innerHTML+='<option value='+result.party[key]+'>';
	        }

	    }
	};
	XHR.open("GET", gUrl.url+"/getPartyName", true);

	XHR.send();
	}
	
	
	var permitList=[];
	function getPermitList(){
	var XHR = new XMLHttpRequest();
	XHR.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	        var response = XHR.responseText;
	         var result=JSON.parse(response);
	        
	        for(var key in result.permit){
	          permitList.push(result.permit[key]);
             console.log(result.permit[key]);
	        }

	    }
	};
	XHR.open("GET", gUrl.url+"/getPermitNo", true);

	XHR.send();
	}
window.onload=getPermitList();
$(document).ready(function() {
    $('#permit').tokenfield({
      autocomplete: {
        source: permitList,
        delay: 100
      },
      showAutocompleteOnFocus: true
    });
});

function searchInput(){







}

function getState(){
	var XHR = new XMLHttpRequest();
	XHR.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	       // Typical action to be performed when the document is ready:
	        var response = XHR.responseText;
	         var result=JSON.parse(response);
            	        console.log(result);
	        var state=document.getElementById("state")
	        for(var key in result.state){
	        state.innerHTML+='<option value='+result.state[key]+'>'+result.state[key]+'</option>';
	        }

	    }
	};
	XHR.open("GET", gUrl.url+"/getStateList", true);

	XHR.send();
	}


window.onload=getAddress();
window.onload=getPartyName();
window.onload=getState();
