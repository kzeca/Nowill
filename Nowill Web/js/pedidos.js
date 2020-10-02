// $(".selected").click(function (e) {
//   e.preventDefault();

//   console.log($(this).attr());
// });


/* SAVING DATA
var ref = database.ref('user');
var data = {
    name: "José Carlos",
    pedido: "Pizza Mussarela"
}

ref.push(data);
*/

var ref = database.ref('user');
ref.on('value', gotData, errData);

function gotData(data){
    var pedidosListing = document.getElementById("pedidosList");
    pedidosListing.innerHTML = '';


    var users = data.val();
    var keys = Object.keys(users);
    //console.log(keys);
    for(var i = 0; i < keys.length; i++){
        var k = keys[i];
        var name = users[k].name;
        var pedido = users[k].pedido;
        //console.log(name, pedido);

        var li = document.createElement('li');
        li.appendChild(document.createTextNode(name + " - " + pedido));
        pedidosListing.appendChild(li);
    }
}

function errData(err){
    console.log("ERROR!");
    console.log(err);
}