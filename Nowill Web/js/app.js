var firebaseConfig = {
    apiKey: "AIzaSyCZpGw_AoMJ8w9xWEMOig9XzkrgiQVVK7w",
    authDomain: "nowill-db412.firebaseapp.com",
    databaseURL: "https://nowill-db412.firebaseio.com",
    projectId: "nowill-db412",
    storageBucket: "nowill-db412.appspot.com",
    messagingSenderId: "1015544913638",
    appId: "1:1015544913638:web:80b92d4b910197672f2527",
    measurementId: "G-T23QJMQC7Z"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();
  const database = firebase.database();
  const auth = firebase.auth();