var gBayList=[];
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
 	        var sku=document.getElementById("select1");
 	        sku.innerHTML="";
 	        sku.innerHTML+='<option value="select">Select</option>';
 	        for(var key in result.production){
 	        sku.innerHTML+='<option value='+result.production[key]+'>'+result.production[key]+'</option>';
 	        }

 	    }
 	};
 	XHR.open("GET", gUrl.url+"/getSkuData?sku="+localStorage.getItem("state"), true);

 	XHR.send();
 	}
 function getBatchNo(){
 	var XHR = new XMLHttpRequest();
 	XHR.onreadystatechange = function() {
 	    if (this.readyState == 4 && this.status == 200) {
 	       // Typical action to be performed when the document is ready:
 	        var response = XHR.responseText;
 	         var result=JSON.parse(response);
             	        console.log(result);
 	        var batch_no=document.getElementById("batch_no");
 	        batch_no.innerHTML="";
 	        batch_no.innerHTML='<option value="select">Select</option>';
 	        for(var key in result.batch){
 	        batch_no.innerHTML+='<option value='+result.batch[key]+'>'+result.batch[key]+'</option>';
 	        }

 	    }
 	};
 	XHR.open("GET", gUrl.url+"/getBatch?sku="+document.getElementById("sku").value, true);

 	XHR.send();
 	}
 function getBay(){
 	var XHR = new XMLHttpRequest();
 	XHR.onreadystatechange = function() {
 	    if (this.readyState == 4 && this.status == 200) {
 	       // Typical action to be performed when the document is ready:
 	        var response = XHR.responseText;
 	         var result=JSON.parse(response);
             	        console.log(result);
 	        var bay_no=document.getElementById("bay_no");
 	        bay_no.innerHTML="";
 	        bay_no.innerHTML='<option value="select">Select</option>';
 	        for(var key in result.bay){
 	        var count=0;
 	        for(var key1 in gBayList){
 	        if(result.bay[key]==gBayList[key1].bay_no &&
 	        document.getElementById("batch_no").value==gBayList[key1].batch_no
 	        &&document.getElementById("sku").value==gBayList[key1].sku){
 	        count++;
 	        }
 	        }
 	        if(count==0){
 	        bay_no.innerHTML+='<option value='+result.bay[key]+'>'+result.bay[key]+'</option>';
 	        }}

 	    }
 	};
 	XHR.open("GET", gUrl.url+"/getBay?sku="+document.getElementById("sku").value+"&batch_no="+document.getElementById("batch_no").value, true);

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
 function getRangeQty(){
 	var XHR = new XMLHttpRequest();
 	XHR.onreadystatechange = function() {
 	    if (this.readyState == 4 && this.status == 200) {
 	       // Typical action to be performed when the document is ready:
 	        var response = XHR.responseText;
 	         var result=JSON.parse(response);
 	         if(result.quantity==0){
 	         document.getElementById("rangeQty").innerHTML="";
 	         document.getElementById("tempQty").value=0;
 	         document.getElementById("quantity").value=0;
 	         document.getElementById("quantity").readOnly = true;
 	         }
 	         else{

             document.getElementById("rangeQty").innerHTML="Enter Quantity Up To "+result.quantity;
             document.getElementById("tempQty").value=result.quantity;
             document.getElementById("quantity").readOnly = false;

 	         }
 	    }
 	};
 	XHR.open("GET", gUrl.url+"/getBayQuantity?sku="+document.getElementById("sku").value+"&batch_no="+document.getElementById("batch_no").value+"&bay_no="+document.getElementById("bay_no").value, true);

 	XHR.send();
 	}
 function setQuantityValidation(){
       var qty=document.getElementById("tempQty").value;
       if((parseInt(qty)+1)<=parseInt(document.getElementById("quantity").value)){
       document.getElementById("save").disabled = true;
              }
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
  getRangeQty();
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
   var bay_no=document.getElementById("bay_no").value;
   gBayList.push({
   "bay_no":bay_no,
   "batch_no":document.getElementById("batch_no").value,
   "sku":document.getElementById("sku").value
   });

    console.log("hash");
    console.log(gBayList);
    count = count + 1;

    var permit_no=document.getElementById("permit_no").value;
    output = '<tr id="row_'+count+'">';
      output += '<td>'+permit_no+' <input type="hidden" name="hidden_permit_no[]" id="sku'+count+'" value="'+permit_no+'" /></td>';
   output += '<td>'+sku+' <input type="hidden" name="hidden_sku[]" id="sku'+count+'" value="'+sku+'" /></td>';
    output += '<td>'+bay_no+' <input type="hidden" name="bay_no[]" id="bay_no'+count+'" value="'+bay_no+'" /></td>';
    output += '<td>'+document.getElementById("batch_no").value+' <input type="hidden" name="batch_no[]" id="batch_no'+count+'" value="'+document.getElementById("batch_no").value+'" /></td>';
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
  output += '<td>'+bay_no+' <input type="hidden" name="bay_no[]" id="bay_no'+count+'" value="'+bay_no+'" /></td>';
      output += '<td>'+document.getElementById("batch_no").value+' <input type="hidden" name="batch_no[]" id="batch_no'+count+'" value="'+document.getElementById("batch_no").value+'" /></td>';
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
  var x = document.getElementById("bay_no");
        x.remove(x.options.length - 1);
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
 	var bay_no=document.getElementsByName("bay_no[]");
 	var batch_no=document.getElementsByName("batch_no[]");
 	var tempOrderId=document.getElementById("orderId").innerHTML;
 	var tmpOrder=tempOrderId.split(":");
        if(permit_no.length>0){
        for(var i=0;i< permit_no.length; i++){
         		 var XHR2 = new XMLHttpRequest();
                  var hash={"permit_no":""+permit_no[i].value+"",
                  "sku":""+sku[i].value+"",
                  "qty":""+quantity[i].value+"",
                  "bay_no":""+bay_no[i].value+"",
                  "batch_no":""+batch_no[i].value +"",
                  "order_id":""+tmpOrder[1].trim()+""

                  }


                 console.log(hash);
         		XHR2.open("POST", gUrl.url+"/insertBatchPurchaseData");
         		XHR2.setRequestHeader("Content-Type", "application/json;charset=UTF-8");


         	XHR2.onload = function() {
         	          console.log(XHR2.responseText);
         	          var response = JSON.parse(XHR2.responseText);
         	          if(response['message']=="Successful") {
         	          }

         			  else {

         	            alert("unSuccessful");

         	          }
         	      }


         	XHR2.send(JSON.stringify(hash));
        }
        updateTotalWeightAndQty(tmpOrder[1].trim(),document.getElementById("totalquantity").value);
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
  var bay=objCells.item(2).innerHTML;
  var batch=objCells.item(3).innerHTML;
  var qty=objCells.item(4).innerHTML;
  var sku=objCells.item(1).innerHTML;
  var bayArray=bay.split(" ");
  var qtyArray=qty.split(" ");
  var skuArray=sku.split(" ");
  var batchArray=batch.split(" ");
  for(var key in gBayList){
    if(bayArray[0]==gBayList[key].bay_no && batchArray[0]==gBayList[key].batch_no
    && skuArray[0]==gBayList[key].sku){
    gBayList.splice(key, 1);
    console.log(gBayList);
  }}
  tq-=parseInt(qtyArray[0]);
  getpalletQty(skuArray[0],-parseInt(qtyArray[0]));
  document.getElementById("totalquantity").value=tq;
  alert(bayArray[0]);
  var x = document.getElementById("bay_no");
  var option = document.createElement("option");
  option.text = bayArray[0].trim();
  x.add(option);
 }
 window.onload=getOrderId();
 window.onload=getSkuList();