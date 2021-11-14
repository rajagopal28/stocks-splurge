$(function() {
     getAllStocksData = function() {
         $.get("./api/stocks", function(data, status){
            console.log("Data: " , data , "\nStatus: " , status);
            // $("#accordion" ).html('');
            console.log(data, status);
            var content = '';
            if(data.length == 0) {
                data = [{
                    name: 'Sample1',
                    id : 1,
                    currentPrice: 12.35,
                    timeCreated: 1636807386,
                    lastUpdated: 1636807386
                }];
            }
            data.forEach(function(item, index){
               var d = new Date(0);
               d.setUTCSeconds(item.timeCreated);
               var createdDate = d.toGMTString();
               d = new Date(0); // reset
               d.setUTCSeconds(item.lastUpdated);
               var lastUpdated = d.toGMTString();
               var header = `<h3>Name: ${item.name}</h3>`;
               var para = `<p>ID: ${item.id} <br/> Price: ${item.currentPrice} <br/> Date Created: ${createdDate} <br/> Last Updated: ${lastUpdated}</p>`;
               var updateBtn = `<button id="delete-${item.id}" onClick="deleteF(${item.id});" class="ui-button ui-widget ui-corner-all">Delete</button>`;
               var deleteBtn = `<button id="update-${item.id}" onClick="updateF(${item.id}, '${item.name}', ${item.currentPrice});" class="ui-button ui-widget ui-corner-all">Update</button>`;
               var div = `<div>${para}${updateBtn}${deleteBtn}</div>`
               content += `${header}${div}\n`;
            });
            $("#accordion-parent").html(`<div id="accordion">${content}</div>`);
            setTimeout(function() {
                $("#accordion").accordion();
            }, 100);
          });
      };

      getAllStocksData();

    $("#add-button").click(function(e) {
        $("#add-stock-dialog").dialog();
    });

    $("#delete-confirm").click(function(e) {
        var id =  $("#delete-stock-id" ).val();
        console.log('invoking api delete for '+id);
        var url = `./api/stocks/${id}`;
        $.ajax({
            url: url,
            type:"DELETE",
            contentType:"application/json; charset=utf-8",
            dataType:"json",
            success: function(data, status){
                console.log("Data: " , data , "Status: " , status);
                if(status !== 'success') {
                    announceDeleteIssue(data, status);
                } else {
                    $("#delete-stock-dialog").dialog('close');
                    getAllStocksData();
                }
            },
            error: announceDeleteIssue
        });
    });

    $("#update-confirm").click(function(e) {
        var id =  $("#update-stock-id" ).val();
        var name = $( "#update-stock-name" ).val();
        var price = $( "#update-stock-price" ).val();
        console.log('invoking api update for '+id);
        var url = `./api/stocks/${id}`;
        if(name.trim().length ===0 || price*1 <=0 ) {
            errorMessageShow("#update-input-warning", '');
        } else {
            $.ajax({
                url: url,
                type:"PUT",
                data: JSON.stringify({
                    "name": name,
                    "currentPrice" : price
                }),
                contentType:"application/json; charset=utf-8",
                dataType:"json",
                success: function(data, status){
                    console.log("Data: " , data , "Status: " , status);
                    if(status !== 'success') {
                        announceUpdateIssue(data, status);
                    } else {
                        $("#update-stock-dialog").dialog('close');
                        getAllStocksData();
                    }
                },
                error: announceUpdateIssue
            });
        }
    });
     $("#add-confirm").click(function(e) {
        var name = $( "#stock-name" ).val();
        var price = $( "#stock-price" ).val();
        console.log(name, price);
        if(name.trim() ==0 || price*1 <=0 ) {
            console.log("info missing");
            // Run the effect
            errorMessageShow("#input-warning", '');
        } else {
            $.ajax({
                url:'./api/stocks',
                type:"POST",
                data: JSON.stringify({
                    "name": name,
                    "currentPrice" : price
                }),
                contentType:"application/json; charset=utf-8",
                dataType:"json",
                success: function(data, status){
                    console.log("Data: " , data , "Status: " , status);
                    if(status !== 'success') {
                        announceAddIssue(data, status);
                    } else {
                        $("#add-stock-dialog").dialog('close');
                        $( "#stock-name" ).val('');
                        $( "#stock-price" ).val('');
                        getAllStocksData();
                    }
             },
             error: announceAddIssue
            });
        }
        console.log(e);
    });
    errorMessageShow = function(element, error) {
       if(error && error.length > 0) {
           $(element).html(`Error:: ${error}`);
       }
        $(element).show( 'shake', {}, 500, function() {
          setTimeout(function() {
            $( `${element}:visible` ).removeAttr("style").fadeOut();
          }, 2000 );
        });
    };
    announceUpdateIssue = function(data, status) {
        console.log(data, status);
        var error = (data && data.responseText && JSON.parse(data.responseText).message ? JSON.parse(data.responseText).message : 'Failed with status '+ status);
        errorMessageShow("#update-response-error", error);
    };
    announceDeleteIssue = function(data, status) {
        console.log(data, status);
        var error = (data && data.responseText && JSON.parse(data.responseText).message ? JSON.parse(data.responseText).message : 'Failed with status '+ status);
        errorMessageShow("#delete-response-error", error);
    };
    announceAddIssue = function(data, status) {
        console.log(data, status);
        var error = (data && data.responseText && JSON.parse(data.responseText).message ? JSON.parse(data.responseText).message : 'Failed with status '+ status);
        errorMessageShow("#add-response-error", error);
    };
    deleteF = function(id) {
        console.log("deleting ", id);
        $("#delete-stock-id" ).val(id);
        //console.log(o);
        $("#delete-stock-dialog").dialog();
    };
    updateF = function(id, name, price) {
        console.log("updating ", id);
        $("#update-stock-id" ).val(id);
        $("#update-stock-name" ).val(name);
        $("#update-stock-price" ).val(price);
        //console.log(o);
        $("#update-stock-dialog").dialog();
      };
  });