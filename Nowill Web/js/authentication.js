const registerForm = document.querySelector("#register");
const loginForm = document.querySelector('#login');


registerForm.addEventListener('submit', (e) => {

    e.preventDefault();

    const email = registerForm['SignUpEmail'].value;
    const password = registerForm['SignUpPassword'].value;
    const confirm = registerForm['ConfirmPassword'].value;
    const cbAgree = registerForm['cbAgree'];
    if(cbAgree.checked){
        if(password == confirm){
            // REGISTER TO FIREBASE
            auth.createUserWithEmailAndPassword(email, password).then(cred => {
                console.log(cred.user);
                window.location.href = "./register.html";
            });
        }else alert("As senhas não coincidem!");
    }else alert("Você deve aceitar os termos de uso!");
});

loginForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const email = loginForm['SignInEmail'].value;
    const password = loginForm['SignInPassword'].value;
    firebase.auth().signInWithEmailAndPassword(email, password).then(cred => {
        window.location.href = "./pedidos.html";
    }).catch(function(error) {
        var errorCode = error.code;
        var errorMessage = error.message;
      });
});