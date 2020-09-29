const registerForm = document.querySelector("#register");

registerForm.addEventListener('submit', (e) => {

    e.preventDefault();

    const email = registerForm['SignUpEmail'].value;
    const password = registerForm['SignUpPassword'].value;
    const confirm = registerForm['ConfirmPassword'].value;
    const cbAgree = registerForm['cbAgree'];
    if(cbAgree.checked){
        if(password == confirm){
            auth.createUserWithEmailAndPassword(email, password).then(cred => {
                console.log(cred.user);
            });
        }else alert("As senhas não coincidem!");
    }else alert("Você deve aceitar os termos de uso!");
});