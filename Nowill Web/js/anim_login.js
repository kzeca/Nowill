var lg = document.getElementById("login");
var rgstr = document.getElementById("register");
var btn = document.getElementById("btn");
console.log(lg);

function registerAnim(){
    lg.style.left = "-400px";
    rgstr.style.left = "50px"
    btn.style.left = "110px"
}

function loginAnim(){
    lg.style.left = "50px";
    rgstr.style.left = "450px"
    btn.style.left = "0"
}