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
console.log(localStorage.getItem("order_id"));


function orderDetails() {
    var xhttp1 = new XMLHttpRequest();
    xhttp1.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            var response = xhttp1.responseText;
            var result = JSON.parse(response);
            console.log(result);

            var tbody = document.getElementById("order");
            tbody.innerHTML="";
            for ( var key in result.orderDetails) {
            if(result.orderDetails[key].status==5){
                tbody.innerHTML += '<tr>'
                + '<td class="text-center">'
                + result.orderDetails[key].order_id
                + '</td>'
                + '<td class="text-center">'
                + result.orderDetails[key].vehicle_no
                + '</td>'
                + '<td class="text-center">'
                + result.orderDetails[key].party_name
                + '</td>'
                + '<td class="text-center">'
                + result.orderDetails[key].state
                + '</td>'
                + '<td class="text-center"><div class="scrollable">'
                + result.orderDetails[key].sku
               +'</div></td>'

                + '<td class="text-center">'
                + result.orderDetails[key].quantity
                + '</td>'
                +'<td class="text-center"><button class="btn btn-secondary" name="edit" onclick="confirmed(this)">Complete</button></td>'
        }

            }
        }
    };
    xhttp1.open("GET", gUrl.url+"/getOrderDetails", true);

    xhttp1.send();
}
window.onload = orderDetails();

function completeOrder(order_id){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    var response = xhttp.responseText;
    		            //alert(response)
    var result=JSON.parse(response);
    		           // console.log(result);
    if(result.message=="Updated Successfully"){
    alert("Your Ordered has been Completed");
    var  message = new Paho.MQTT.Message(order_id.toString());
    message.destinationName = "complete";
    client.send(message);
    location.reload();
    }
    }
    };
    xhttp.open("GET", gUrl.url+"/orderComplete?order_id="+order_id, true);
    xhttp.send();
    }
    function confirmed(element) {
    if (confirm("Are you sure you want to Complete this Order?")) {
    var ed=element.parentNode.parentNode;
    var td=ed.getElementsByTagName("td")[0].innerHTML;
    completeOrder(td);
    }
    else {
    location.reload();
    }
    }



