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
            if(result.orderDetails[key].status==0){
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
                + '<td style="background:orange;" class="text-center">Pending Order</td>'
                +'<td class="text-center"><button class="btn btn-secondary" name="edit" onclick="editDetail(this)">Edit</button></td>'
				+'<td class="text-center"><button class="btn btn-danger" name="orders" onclick="confirmed(this)">Cancel</button></td></tr>'
        }
            else if(result.orderDetails[key].status==1){
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
                + '<td style="background:lightgreen;" class="text-center">Order Process</td>'
               +'<td class="text-center"><button class="btn btn-secondary" name="edit" onclick="editDetail(this)">Edit</button></td>'
				+'<td class="text-center"><button class="btn btn-danger" onclick="confirmed(this)">Cancel</button></td></tr>'
            }

            else if(result.orderDetails[key].status==2){
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
                 + '<td style="background:lightgreen;" class="text-center">Order Completed</td>'
            }

            else if(result.orderDetails[key].status==3){
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
                 + '<td style="background:#ff6961;" class="text-center">Order Cancelled</td>'
            }
            }
        }
    };
    xhttp1.open("GET", gUrl.url+"/getOrderDetails", true);

    xhttp1.send();
}

window.onload = orderDetails();

function getSpecificOrderId(){
var row=document.getElementsByName("orders");
for(var count=0;count<row.length;count++){
if(row[count].checked){
localStorage.clear();
localStorage.setItem("order_id",document.getElementById("orderTable").rows[count+1].cells[0].innerHTML);
console.log(document.getElementById("orderTable").rows[count+1].cells[6].innerHTML);
if(document.getElementById("orderTable").rows[count+1].cells[6].innerHTML=="Pending Order"||
document.getElementById("orderTable").rows[count+1].cells[6].innerHTML=="Order Process"){
window.location.href="productOrder";
}else
{
window.location.href="completeProduct";
}
}
}

}

function editDetail(element){
/*	console.log(element);*/
	var ed=element.parentNode.parentNode;
	var td=ed.getElementsByTagName("td")[0].innerHTML;
	console.log(td);
	localStorage.setItem("order_id",td);
	location.href="editDetail"
}

function cancelOrder(order_id){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    var response = xhttp.responseText;
    		            //alert(response)
    var result=JSON.parse(response);
    		           // console.log(result);
    if(result.message=="Updated Successfully"){
    alert("Your Ordered has been Canceled");
    var  message = new Paho.MQTT.Message(order_id.toString());
    message.destinationName = "status";
    client.send(message);
    window.location.href="orderDetails"
    }
    }
    };
    xhttp.open("GET", gUrl.url+"/cancelOrder?order_id="+order_id, true);
    xhttp.send();
    }
    function confirmed(element) {
    if (confirm("Are you sure you want to delete this item?")) {
    var ed=element.parentNode.parentNode;
    var td=ed.getElementsByTagName("td")[0].innerHTML;
    cancelOrder(td);
    }
    else {
    location.reload();
    }
    }




