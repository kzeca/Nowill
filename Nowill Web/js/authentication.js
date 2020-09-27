const registerForm = document.querySelector("#register");

registerForm.addEventListener('submit', (e) => {

    e.preventDefault();

    const email = registerForm['SignUpEmail'].value;
    const password = registerForm['SignUpPassword'].value;
    console.log(email, password);

    auth.createUserWithEmailAndPassword(email, password).then(cred => {
        console.log(cred.user);
    });
});