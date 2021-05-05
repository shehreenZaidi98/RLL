
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
function orderProduct() {
    var xhttp1 = new XMLHttpRequest();
    xhttp1.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            var response = xhttp1.responseText;
            var result = JSON.parse(response);
            var tbody = document.getElementById("order");
            tbody.innerHTML="";
            for ( var key in result.OrderIdProduct) {
                tbody.innerHTML += '<tr>'
                + '<td >'
                + result.OrderIdProduct[key].bay
                + '</td>'
                + '<td>'
                + result.OrderIdProduct[key].sku
                + '</td>'
                + '<td >'
                + result.OrderIdProduct[key].batch_no
                + '</td>'
                + '<td >'
                + result.OrderIdProduct[key].qty
                + '</td></tr>'
        }
            }

    };
    xhttp1.open("GET", gUrl.url+"/getOrderProduct?order_id="+localStorage.getItem("order_id"), true);

    xhttp1.send();
}

window.onload = orderProduct();

function cancelOrder(){
		   var xhttp = new XMLHttpRequest();
		   xhttp.onreadystatechange = function() {
		       if (this.readyState == 4 && this.status == 200) {
		           var response = xhttp.responseText;
		            //alert(response)
		            var result=JSON.parse(response);
		           // console.log(result);
                    if(result.message=="Updated Successfully"){
                    

                        alert("Your Ordered has been Canceled");
			    var  message = new Paho.MQTT.Message(localStorage.getItem("order_id").toString());
	        	  	message.destinationName = "status";
	        	  	client.send(message);
                        window.location.href="orderDetails"
                   		            }
		            }
		      };
		       xhttp.open("GET", gUrl.url+"/cancelOrder?order_id="+localStorage.getItem("order_id"), true);
		       xhttp.send();
	   }
	   function confirmed() {

         if (confirm("Are you sure you want to delete this item?")) {
          cancelOrder();
         } else {
           location.reload();
         }

       }


