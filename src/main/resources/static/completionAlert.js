var client = new Paho.MQTT.Client(gUrl.mqtt, Number(9001), "CompletedAlert");

//set callback handlers
client.onConnectionLost = onConnectionLost;
client.onMessageArrived = onMessageArrived;

//connect the client
client.connect({onSuccess:onConnect});
                   function onConnect() {
                    //Once a connection has been made, make a subscription and send a message.
                    console.log("onConnect");
                    client.subscribe("completeOrder");
                    }
function onConnectionLost(responseObject) {
   if (responseObject.errorCode !== 0) {
   console.log("onConnectionLost:"+responseObject.errorMessage);
   }
   }

function onMessageArrived(message) {
   showAlert();
   }
function showNotification() {
   const notification = new Notification("Order Completion Alert",{
      body: "Please Confirmed This Order?"
   })
   notification.onclick = (e) => {
      window.location.href = "http://localhost:9080/complete";
   };
}
   function showAlert(){
   if (Notification.permission === "granted") {
     showNotification();
   } else if (Notification.permission !== "denied") {
      Notification.requestPermission().then(permission => {
         console.log(permission);
      });
   }
}
if (Notification.permission !== "denied") {
      Notification.requestPermission().then(permission => {
         console.log(permission);
      });
   }

   console.log(Notification.permission);