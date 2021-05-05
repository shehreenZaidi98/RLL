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
                +(parseInt(key)+1)
                + '</td>'
                + '<td >'
                + result.OrderIdProduct[key].bay
                + '</td>'
                + '<td>'
                 + result.OrderIdProduct[key].batch_no
                + '</td>'
                + '<td >'
               + result.OrderIdProduct[key].sku
                + '</td>'
                + '<td  contenteditable="true">'
                + result.OrderIdProduct[key].qty
                + '</td>'

             +'<td align="center"><button class="btn btn -success" onclick="confirmed(this)">Done</button></td></tr>'


        }
            }

    };
    xhttp1.open("GET", gUrl.url+"/getOrderProduct?order_id="+localStorage.getItem("order_id"), true);

    xhttp1.send();
}

/*fetch data from the table*/


window.onload = orderProduct();

 function confirmed(element) {
    if (confirm("Are you sure you want to delete this item?")) {
    editQuantity(element);
    }
    else {
    location.reload();
    }
    }

  function editQuantity(element){
  var ed=element.parentNode.parentNode;
   var bay=ed.getElementsByTagName("td")[1].innerHTML;
   var batch_no=ed.getElementsByTagName("td")[2].innerHTML;
   var sku=ed.getElementsByTagName("td")[3].innerHTML;
   var qty=ed.getElementsByTagName("td")[4].innerHTML;
   var order_id=localStorage.getItem("order_id");

 if(bay==""){
      alert("Please Enter bay");
   }
else if(batch_no==""){
      alert("Please Enter batch_no");
   }
else if(sku==""){
      alert("Please Enter sku");
   }
else if(qty==""){
      alert("Please Enter qty");
   }
   else{
       var XHR2 = new XMLHttpRequest();
          var hash={"bay":""+bay+"",
         "batch_no":""+batch_no+"",
         "sku":""+sku+"",
         "qty":""+qty+"",
         "order_id":""+order_id+""
                 }
        console.log(hash);
      XHR2.open("POST", gUrl.url+"/c");
      XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


   XHR2.onload = function() {
             console.log(XHR2.responseText);
             var response = XHR2.responseText;
             if(response=="Updated") {
                alert("Data Updated Successfully");
                var  message = new Paho.MQTT.Message(order_id.toString());
                    message.destinationName = "orderEdit";
                    client.send(message);
                location.href="orderDetails";
             }
           else {
            location.reload();
             }
         }
   XHR2.send(JSON.stringify(hash));
}
}
