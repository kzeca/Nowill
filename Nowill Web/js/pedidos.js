//Model for orders
class Order {
  //Recieve and object
  constructor({ id, client, address, products, payment, status, createdAt }) {
    this.id = id; //String or Number
    this.client = client; //The name of client who ordered (String)
    this.address = address; //His address (String)
    this.products = products; //What he order (Array<Product>)
    this.payment = payment; //Type of payment (String)
    this.status = status; //String -> [accepted, ongoing, denied]
    this.createdAt = createdAt; //String
  }

  calculateTax() {
    const tax = 0.1; //Fixed value for now

    return tax;
  }

  calculateTotalPrice() {
    const tax = this.calculateTax();
    const products = this.products;

    var subTotal = 0.0;

    for (var prod of products) {
      subTotal += prod.price; //Sum of what was ordered
    }

    const taxPrice = parseFloat(subTotal * tax); //Calculate the tax
    const totalPrice = parseFloat(subTotal + taxPrice); //Calculate the total price

    // Return an array
    return [subTotal.toFixed(2), taxPrice.toFixed(2), totalPrice.toFixed(2)];
  }
}

//Model for products
class Product {
  //Recieve and object
  constructor({ id, name, type, price }) {
    this.id = id; //String or Number
    this.name = name; //Product name (String)
    this.type = type; //Producy type (String -> [i. e. entrada, guarnição,...])
    this.price = price; //Product price (Float)
  }
}

//Example of orders list
const orders = [
  new Order({
    id: 9743,
    client: "Moreno Lennertz",
    address: "Rua mama, mama, 1234, amamamamamamamama",
    products: [
      new Product({ id: 1, name: "Mama Juice", type: "Drink", price: 100.1 }),
      new Product({ id: 1, name: "Mama Juice", type: "Drink", price: 100.0 }),
      new Product({ id: 1, name: "Mama Juice", type: "Drink", price: 100.0 }),
    ],
    payment: "money",
    status: "delivered",
    createdAt: "19:20",
  }),
  new Order({
    id: 9746,
    client: "Moreno Lennertz",
    address: "Rua mama, mama, 1234, amamamamamamamama",
    products: [],
    payment: "money",
    status: "ongoing",
    createdAt: "19:20",
  }),
  new Order({
    id: 9744,
    client: "Moreno Lennertz",
    address: "Rua mama, mama, 1234, amamamamamamamama",
    products: [],
    payment: "money",
    status: "",
    createdAt: "19:20",
  }),
  new Order({
    id: 9745,
    client: "Moreno Lennertz",
    address: "Rua mama, mama, 1234, amamamamamamamama",
    products: [],
    payment: "money",
    status: "canceled",
    createdAt: "19:20",
  }),
];

var activeOrder; //The order that is being showing in the browser

//This function get orders in Firebase Realtime Database
function getOrders() {
  // var orders = [];

  //TODO: Get firebase data

  return orders;
}

//This function render all the orders got in Firebase
function renderOrderList() {
  const orders = getOrders();

  for (var order of orders) {
    //Similar to foreach in java
    var statusColor = "";

    //Change the bar color depending of status
    switch (order.status) {
      case "delivered":
        statusColor = "#00EB79";
        break;

      case "ongoing":
        statusColor = "#FAD35D";
        break;

      case "canceled":
        statusColor = "#E00101";
        break;

      default:
        break;
    }

    //Append a new child (order component) to .list
    $(".orders-page .list").append(
      `<div class='item' key='${order.id}'>` +
        ` <h3 id='id'>${order.id}</h3> ` +
        `<div style='display: flex;flex-direction: column;justify-content: start;align-items: center;'>` +
        `<h3 id='client'>${order.client}</h3>` +
        `<h3 id='createdAt'>${order.createdAt}</h3>` +
        `</div>` +
        `<div class='status-bar' style='background: ${statusColor}'></div>` +
        `</div>`
    );
  }
}

/**
 * This function render the active/selected order
 * @param {String} key -> order id
 */
function renderOrder(key) {
  $(".orders").html("<h1>PEDIDOS</h1>"); //Need to be done to <h1> tag not desapear

  const order = orders.find((order, index, obj) => order.id.toString() === key); //Get order by id
  activeOrder = order; //Set the active order

  //Render id of order and name of client
  $(".item-container .info div#line1").html(
    `<p><strong>#${order.id}:</strong> ${order.client}</p>`
  );

  //Render the addresss
  $(".item-container .info div#line2").html(
    `<p><strong>Endereço:</strong> ${order.address}</p>`
  );

  //Render the payment method
  $(".item-container .info div#line3").html(
    `<p><strong>Pagamento:</strong> ${order.payment}</p>`
  );

  const products = order.products;

  //Render all products in .orders div
  for (var prod of products) {
    $(".orders").append(
      `<div id='${prod.id}'>
            <p>1</p>
            <p>${prod.name}</p>
            <p>R$ ${prod.price}</p>
         </div>`
    );
  }

  //Render the price
  const [subtotal, taxprice, total] = order.calculateTotalPrice();
  $(".orders").append(
    `<div class="footer">
        <div class="priceList">
        <div><p>SUBTOTAL</p><p>TAXA DE ENTREGA</p><p><strong>TOTAL</strong></p></div>
        <div><p>R$ ${subtotal}</p><p>${taxprice}</p><p><strong>R$ ${total}</strong></p></div>
        </div>
        </div>`
  );

  //Verification to display the button group for accept or denie an order
  if (order.status !== null && order.status !== "") {
    $(".status-buttons").attr("style", "display: none;");
  } else {
    $(".status-buttons").attr("style", "display: flex;");
  }
}

//Render the order on page start
function renderFirstOrder() {
  const order = getOrders()[0];

  activeOrder = order;

  renderOrder(order.id.toString());
}

//Handle confirm order event click
function handleConfirm() {
  console.log(activeOrder);

  //TODO: Update value in firebase

  //For test purposes
  orders[orders.indexOf(activeOrder)].status = "ongoing";
  console.log(orders);
}

//Handle denie order event click
function handleDenie() {
  console.log(activeOrder);

  //TODO: Update value in firebase

  //For test purposes
  orders[orders.indexOf(activeOrder)].status = "denied";
  console.log(orders);
}

//Run functions
renderOrderList();
renderFirstOrder();

//Garbage code

// var ref = database.ref("user");
// ref.on("value", gotData, errData);

// async function getData(data) {
//   var pedidosListing = document.getElementById("pedidosList");
//   pedidosListing.innerHTML = "";

//   var users = await data.val();

//   var keys = Object.keys(users);
//   //console.log(keys);
//   for (var i = 0; i < keys.length; i++) {
//     var k = keys[i];
//     var name = users[k].name;
//     var pedido = users[k].pedido;
//     //console.log(name, pedido);

//     var li = document.createElement("li");
//     li.appendChild(document.createTextNode(name + " - " + pedido));
//     pedidosListing.appendChild(li);
//   }
// }

// function errData(err) {
//   console.log("ERROR!");
//   console.log(err);
// }
