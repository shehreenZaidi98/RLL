function orderProduct() {
    var xhttp1 = new XMLHttpRequest();
    xhttp1.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            var response = xhttp1.responseText;
            var result = JSON.parse(response);
            console.log(result);

            var tbody = document.getElementById("orderProducts");
            console.log(tbody)
            tbody.innerHTML="";
            for ( var key in result.OrderIdProduct) {

                tbody.innerHTML += '<tr>'
                + '<td>'
                + result.OrderIdProduct[key].sku
                + '</td>'
                + '<td >'
                + result.OrderIdProduct[key].batch_no
                + '</td>'
                + '<td >'
                + result.OrderIdProduct[key].qty
                + '</td>'
                + '<td >'
                + result.OrderIdProduct[key].bay
                + '</td></tr>'
        }
            }

    };
    xhttp1.open("GET", gUrl.url+"/getOrderProduct?order_id="+localStorage.getItem("order_id"), true);

    xhttp1.send();
}

window.onload = orderProduct();