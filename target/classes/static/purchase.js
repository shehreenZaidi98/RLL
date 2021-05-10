
if(localStorage.getItem("state")==null){
	 
 window.location.href="transport";
 }
 function getSkuList(){
 	var XHR = new XMLHttpRequest();
 	XHR.onreadystatechange = function() {
 	    if (this.readyState == 4 && this.status == 200) {
 	       // Typical action to be performed when the document is ready:
 	        var response = XHR.responseText;
 	         var result=JSON.parse(response);
             	        console.log(result);
 	        var select1=document.getElementById("select1");
 	        select1.innerHTML="";
 	       // select1.innerHTML+='<option value="select">Select</option>';
 	        for(var key in result.production){
 	        select1.innerHTML+='<option value='+result.production[key]+'>'+result.production[key]+'</option>';
 	        }

 	    }
 	};
 	XHR.open("GET", gUrl.url+"/getSkuData?sku="+localStorage.getItem("state"), true);

 	XHR.send();
 	}
 function getOrderId(){
 	var XHR = new XMLHttpRequest();
 	XHR.onreadystatechange = function() {
 	    if (this.readyState == 4 && this.status == 200) {
 	       // Typical action to be performed when the document is ready:
 	        var response = XHR.responseText;
 	         var result=JSON.parse(response);
             	        console.log(result);
             document.getElementById("orderId").innerHTML="Order Id :"+result.permitList[0].order_id
             var permitNo=document.getElementById("permit_no")
             permitNo.innerHTML="";
             permitNo.innerHTML+='<option value="select">Select</option>';
             for(var key in result.permitList[0].permit_list){
                permitNo.innerHTML+='<option value='+result.permitList[0].permit_list[key]+'>'+result.permitList[0].permit_list[key]+'</option>';
             }
 	    }
 	};
 	XHR.open("GET", gUrl.url+"/getTransportData", true);

 	XHR.send();
 	}
 		var tempQty=[];
     	var tQty=[];
     	function getRangeQty(){
     	var XHR = new XMLHttpRequest();
     	XHR.onreadystatechange = function() {
     	    if (this.readyState == 4 && this.status == 200) {
     	       // Typical action to be performed when the document is ready:
     	        var response = XHR.responseText;
     	         var result=JSON.parse(response);
     	         console.log(result);
     	         if(result.quantity==0){
     	         document.getElementById("rangeQty").innerHTML="";
     	         document.getElementById("tempQty").value=0;
     	         document.getElementById("quantity").value=0;
     	         document.getElementById("quantity").readOnly = true;
     	         }
     	         else{

     	         var hash={"sku":document.getElementById("sku").value,
                      	                   "qty":result.quantity};
                      	                   tQty.push(hash)
}

                 var data1 = tQty.filter((thing, index) => {
                         return index === tQty.findIndex(obj => {
                           return JSON.stringify(obj) === JSON.stringify(thing);
                         });
                       });
                       console.log(data1);

                       if(tempQty.length==0){
                                  document.getElementById("rangeQty").innerHTML="Enter Quantity Up To "+(data1[0].qty);
                                  document.getElementById("tempQty").value=(data1[0].qty);
                                  document.getElementById("quantity").readOnly = false;
                       }
                       else{
                       var tempTotQty=0;
                       for(var key in tempQty){
                       if(document.getElementById("sku").value==tempQty[key].sku){
                         tempTotQty+=parseInt(tempQty[key].qty);

                       }
                       }

                       for(var key1 in data1){

                       if(document.getElementById("sku").value==data1[key1].sku){
                        document.getElementById("rangeQty").innerHTML="Enter Quantity Up To "+(data1[key1].qty-tempTotQty);
                        document.getElementById("tempQty").value=(data1[key1].qty-tempTotQty);
                        document.getElementById("quantity").readOnly = false;
                       }
                       }


}
     	    }
     	};
     	XHR.open("GET", gUrl.url+"/getQuantity?sku="+document.getElementById("sku").value, true);

     	XHR.send();
     	}



 	function setQuantityValidation(){
       var qty=document.getElementById("tempQty").value;
       if((parseInt(qty)+1)<=parseInt(document.getElementById("quantity").value)){
       document.getElementById("save").disabled = true;       }
       /*else if(parseInt(document.getElementById("quantity").value)==parseInt(qty)){
       document.getElementById("save").disabled = true;
       }*/
       else{
       document.getElementById("save").disabled = false;
       }


 	}



var tq=0;

$(document).ready(function(){
 var count = 0;

 $('#product_dialog').dialog({
  autoOpen:false,
  width:400
 });

 $('#add').click(function(){
  $('#product_dialog').dialog('option', 'title', 'Add Data');
  $('#serial_no').val('');
  $('#quantity').val('');
  $('#sku').val('');
  $('#error_product_name').text('');
  $('#error_product_category').text('');
  $('#error_serial_no').text('');
  $('#error_quantity').text('');
  $('#error_sku').text('');
  $('#serial_no').css('border-color', '');
  $('#product_name').css('border-color', '');
  $('#product_category').css('border-color', '');
  $('#sku').css('border-color', '');
  $('#quantity').css('border-color', '');
  $('#save').text('Save');
  $('#product_dialog').dialog('open');
 });

 $('#save').click(function(){
  var error_serial_no = '';
  var error_product_name = '';
  var error_product_category = '';
  var error_product_quanity = '';
  var error_sku = '';
  var serial_no = '';
  var product_name = '';
  var product_category = '';
  var product_quanity = '';
  if($('#serial_no').val() == '')
  {
   error_serial_no = 'Serial No is required';
   $('#error_serial_no').text(error_serial_no);
   $('#serial_no').css('border-color', '#cc0000');
   serial_no = '';
  }
  else
  {
   error_serial_no = '';
   $('#error_serial_no').text(error_serial_no);
   $('#serial_no').css('border-color', '');
   serial_no = $('#serial_no').val();
  }
  if($('#product_name').val() == '')
  {
   error_product_name = 'Product Name is required';
   $('#error_product_name').text(error_product_name);
   $('#product_name').css('border-color', '#cc0000');
   product_name = '';
  }
  else
  {
   error_product_name = '';
   $('#error_product_name').text(error_product_name);
   $('#product_name').css('border-color', '');
   product_name = $('#product_name').val();
  }
  if($('#product_category').val() == '')
  {
   error_product_category = 'Product Category is required';
   $('#error_product_category').text(error_product_category);
   $('#product_category').css('border-color', '#cc0000');
   product_category = '';
  }
  else
  {
   error_product_category = '';
   $('#error_product_category').text(error_product_category);
   $('#product_category').css('border-color', '');
   product_category = $('#product_category').val();
  }
  if($('#quantity').val() == '')
  {
   error_quantity = 'Quantity is required';
   $('#error_quantity').text(error_quantity);
   $('#quantity').css('border-color', '#cc0000');
   quantity = '';
  }
  else
  {
   error_quantity = '';
   $('#error_quantity').text(error_quantity);
   $('#quantity').css('border-color', '');
   quantity = $('#quantity').val();
  }

  if($('#sku').val() == '')
  {
   error_sku = 'SKU is required';
   $('#error_sku').text(error_sku);
   $('#sku').css('border-color', '#cc0000');
   sku = '';
  }
  else
  {
   error_sku = '';
   $('#error_sku').text(error_sku);
   $('#sku').css('border-color', '');
   sku = $('#sku').val();
  }
  if(error_serial_no != '' || error_product_name != '' || error_product_category !='' || error_quantity !='')
  {
    return false;
  }
  else
    {
   if($('#save').text() == 'Save')
   {
   document.getElementById("save").disabled = true;
   tq+=parseInt(document.getElementById("quantity").value);
   var q=parseInt(document.getElementById("quantity").value);
   document.getElementById("totalquantity").value=tq;
   getpalletQty(document.getElementById("sku").value,q);
    count = count + 1;
var hash={"sku":document.getElementById("sku").value,
    "qty":document.getElementById("quantity").value};
    tempQty.push(hash);

    var permit_no=document.getElementById("permit_no").value;
    output = '<tr id="row_'+count+'">';
      output += '<td>'+permit_no+' <input type="hidden" name="hidden_permit_no[]" id="sku'+count+'" value="'+permit_no+'" /></td>';
   output += '<td>'+sku+' <input type="hidden" name="hidden_sku[]" id="sku'+count+'" value="'+sku+'" /></td>';
    output += '<td>'+quantity+' <input type="hidden" name="hidden_quantity[]" id="quantity'+count+'" value="'+quantity+'" /></td>';
    output += '<td><button type="button" name="remove_details" class="btn btn-danger btn-xs remove_details" id="'+count+'" onclick="addData(this)">Remove</button></td>';
        output += '</tr>';
    $('#product_data').append(output);
   }
   else
   {
    var row_id = $('#hidden_row_id').val();
       output += '<td>'+permit_no+' <input type="hidden" name="hidden_permit_no[]" id="sku'+row_id+'" value="'+permit_no+'" /></td>';
       output += '<td>'+sku+' <input type="hidden" name="hidden_sku[]" id="sku'+row_id+'" value="'+sku+'" /></td>';
    output += '<td>'+quantity+' <input type="hidden" name="hidden_quantity[]" id="quantity'+count+'" value="'+quantity+'" /></td>';
    output += '<td><button type="button" name="remove_details" class="btn btn-danger btn-xs remove_details" id="'+row_id+'" onclick="addData(this)">Remove</button></td>';
    $('#row_'+row_id+'').html(output);
   }

   $('#product_dialog').dialog('close');
  }
 });
 $(document).on('click', '.remove_details', function(){
  var row_id = $(this).attr("id");
  if(confirm("Are you sure you want to remove this row data?"))
  {
   $('#row_'+row_id+'').remove();

  }
  else
  {
   return false;
  }
 });

 $('#action_alert').dialog({
  autoOpen:false
 });

 $('#product_form').on('submit', function(event){
  event.preventDefault();
  var count_data = 0;
  $('.serial_no').each(function(){
   count_data = count_data + 1;
  });
  if(count_data > 0)
  {
   var form_data = $(this).serialize();
   $.ajax({
    url:"purchase1",
    method:"POST",
    data:form_data,
    success:function(data)
    {

    }
   })
  }
  else
  {
   $('#action_alert').html('<p>Please Add atleast one data</p>');
   $('#action_alert').dialog('open');
  }
 });

});

function insertPurchaseOrder(){

 	var permit_no=document.getElementsByName("hidden_permit_no[]");
 	var sku=document.getElementsByName("hidden_sku[]");
 	var quantity=document.getElementsByName("hidden_quantity[]");
 	var tempOrderId=document.getElementById("orderId").innerHTML;
 	var tmpOrder=tempOrderId.split(":");
        if(permit_no.length>0){
           var allItem=[];
         		  for(var i=0;i< permit_no.length; i++){
                  var hash={"permit_no":""+permit_no[i].value+"",
                  "sku":""+sku[i].value+"",
                  "qty":""+quantity[i].value+"",
                  "order_id":""+tmpOrder[1].trim()+""}
                  allItem.push(hash);
                }
                var uniqueItem=[];

                for(i=0;i<allItem.length;i++){
                if(uniqueItem.length==0){
                 var hash={"permit_no":allItem[i].permit_no,
                           "sku":allItem[i].sku,
                           "qty":allItem[i].qty,
                           "order_id":allItem[0].order_id
                           }
                           uniqueItem.push(hash)
                }
                else{
                var count=0,id=0;

                for(var j=0;j<uniqueItem.length;j++){
                if(allItem[i].sku==uniqueItem[j].sku){
                count=1;
                id=j;
                }

                }
                var totQty=0;
                if(count>0){
                totQty=parseInt(uniqueItem[id].qty);
                totQty+=parseInt(allItem[i].qty)
                uniqueItem[id].qty=totQty;
                }
                else{
                 var hash={"permit_no":allItem[i].permit_no,
                                           "sku":allItem[i].sku,
                                           "qty":allItem[i].qty,
                                           "order_id":allItem[0].order_id
                                           }
                                           uniqueItem.push(hash)
                }

                }
                }
                 console.log(uniqueItem);
                 var suc=0;
                 for(var key=0;key<uniqueItem.length;key++){
                 var XHR2 = new XMLHttpRequest();

         		XHR2.open("POST", gUrl.url+"/insertPurchaseData");
         		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


         	XHR2.onload = function() {
         	          console.log(XHR2.responseText);
         	          var response = JSON.parse(XHR2.responseText);
         	          if(response['message']=="Successful") {
         	          suc++;
         	          }

         			  else {

         	            alert("unSuccessful");

         	          }
         	      }

         	XHR2.send(JSON.stringify(uniqueItem[key]));
        }
        updateTotalWeightAndQty(tmpOrder[1].trim(),document.getElementById("totalquantity").value);
        console.log(tmpOrder[1].trim()+" "+document.getElementById("totalquantity").value)
        window.location.href="transport";

        }
        else{
        alert("Please Add At Least One Row")
        }

 }
  var tw=0;
 function getpalletQty(sku,qty){
 	var XHR = new XMLHttpRequest();
 	XHR.onreadystatechange = function() {
 	    if (this.readyState == 4 && this.status == 200) {
 	       // Typical action to be performed when the document is ready:
 	        var response = XHR.responseText;
 	         var result=JSON.parse(response);
 	         console.log(result.PalletWeight[0]);
             tw+=parseInt(result.PalletWeight[0])*qty;
             console.log(tw);
 	        document.getElementById("totalweight").value=tw;
 	        console.log(tw);
 	        }

 	    
 	};
 	XHR.open("GET", gUrl.url+"/getPalletWeight?sku="+sku, true);

 	XHR.send();
 	}
 	
 	
 	function updateTotalWeightAndQty(order_id,total_qty){

         		 var XHR2 = new XMLHttpRequest();
         		 var sku=document.getElementsByName("hidden_sku[]");
         		 var qty=document.getElementsByName("hidden_quantity[]");
         		 var allSku="";
         		 for(var key=0 ;key<sku.length;key++){
         		 allSku+=sku[key].value+"("+qty[key].value+") ";
         		 }
         		 console.log(allSku);
                  var hash={"order_id":""+order_id+"",
                  "total_qty":""+total_qty+"",
                  "sku":""+allSku+""
                  }


                 console.log(hash);
         		XHR2.open("POST", gUrl.url+"/updateTransport");
         		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


         	XHR2.onload = function() {
         	          console.log(XHR2.responseText);
         	          var response = JSON.parse(XHR2.responseText);
         	         
         	      }


         	XHR2.send(JSON.stringify(hash));
 }

 function addData(element){
 var row = element.parentNode.parentNode.rowIndex
  var tbl = document.getElementById("product_data");
  var objCells = tbl.rows.item(row).cells;
    var qty=objCells.item(2).innerHTML;
      var qtyArray=qty.split(" ");
  tq-=parseInt(qtyArray[0]);
  document.getElementById("totalquantity").value=tq;
 }

window.onload=getOrderId();
window.onload=getSkuList();