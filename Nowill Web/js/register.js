const registerForm = document.querySelector("#register");

registerForm.addEventListener('submit', (e) => {
    e.preventDefault();
    window.location.href = "./pedidos.html";
});