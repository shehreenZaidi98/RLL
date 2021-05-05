function getSku(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		       // Typical action to be performed when the document is ready:
		        var response = xhttp.responseText;
		   var sku=document.getElementById("sku");
		    var result=JSON.parse(response);
		       for(var key in result.SkuData){
		    	   sku.innerHTML+= '<option value='+result.SkuData[key].sku+'>'+ result.SkuData[key].sku+ '</option>';

		       }
		    }
		};
		xhttp.open("GET", gUrl.url+"/getSkuListData", true);

		xhttp.send();
		}
 function getBay(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		       // Typical action to be performed when the document is ready:
		        var response = xhttp.responseText;
		   var bay=document.getElementById("bay");
		    var result=JSON.parse(response);
		       for(var key in result.bay){
		    	   bay.innerHTML+= '<option value='+result.bay[key].bay+'>'+ result.bay[key].bay+ '</option>';

		       }
		    }
		};
		xhttp.open("GET", gUrl.url+"/getBayList", true);

		xhttp.send();
		}
function getBatchNo(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		       // Typical action to be performed when the document is ready:
		        var response = xhttp.responseText;
		   var batch_no=document.getElementById("batch");
		    var result=JSON.parse(response);
		    console.log(result)
		       for(var key in result.BatchNo){
		    	   console.log(result.BatchNo[key])
		    	   batch_no.innerHTML+= '<option value='+result.BatchNo[key]+'>'+ result.BatchNo[key]+ '</option>';

		       }
		    }
		};
		xhttp.open("GET", gUrl.url+"/getBatchNo", true);

		xhttp.send();
		}


function getSearchWithSku(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		   if (this.readyState == 4 && this.status == 200) {
		   var response = xhttp.responseText;
           var result=JSON.parse(response);
		   var tbody=document.getElementById("searchOrder");
		   tbody.innerHTML="";
		    for ( var key in result.SearchData) {
                         tbody.innerHTML += '<tr>'
                            + '<td>'
                            + result.SearchData[key].sku
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].batch_no
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].qty
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].bay_no
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].status
                            + '</td></tr>'
}
            }

		};
				   var sku=document.getElementById("sku").value;
		xhttp.open("GET", gUrl.url+"/getSearchProduct?sku="+sku, true);

		xhttp.send();
		}

function getSearchWithBatch(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		   if (this.readyState == 4 && this.status == 200) {
		   var response = xhttp.responseText;
           var result=JSON.parse(response);
		   var tbody=document.getElementById("searchOrder");
		   tbody.innerHTML="";
		    for ( var key in result.SearchData) {
                         tbody.innerHTML += '<tr>'
                            + '<td>'
                            + result.SearchData[key].sku
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].batch_no
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].qty
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].bay_no
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].status
                            + '</td></tr>'
                            
}
            }

		};
				   var batch_no=document.getElementById("batch").value;
		xhttp.open("GET", gUrl.url+"/getSearchProduct?batch_no="+batch_no, true);

		xhttp.send();
		}

function getSearchWithBay(){
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
		   if (this.readyState == 4 && this.status == 200) {
		   var response = xhttp.responseText;
           var result=JSON.parse(response);
		   var tbody=document.getElementById("searchOrder");
		   tbody.innerHTML="";
		    for ( var key in result.SearchData) {
                         tbody.innerHTML += '<tr>'
                            + '<td>'
                            + result.SearchData[key].sku
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].batch_no
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].qty
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].bay_no
                            + '</td>'
                            + '<td >'
                            + result.SearchData[key].status
                            + '</td></tr>'
}
            }

		};
				   var bay_no=document.getElementById("bay").value;
		xhttp.open("GET", gUrl.url+"/getSearchProduct?bay_no="+bay_no, true);

		xhttp.send();
		}

	window.onload=getBatchNo();
	window.onload=getBay();
	window.onload=getSku();