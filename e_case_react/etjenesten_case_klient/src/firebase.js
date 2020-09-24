import firebase from 'firebase';

const firebaseConfig = {
    apiKey: "AIzaSyD6lrhVA9CgAwFzWxC1xX9_0ZLCEDKJIDE",
    authDomain: "ecase-48776.firebaseapp.com",
    databaseURL: "https://ecase-48776.firebaseio.com",
    projectId: "ecase-48776",
    storageBucket: "ecase-48776.appspot.com",
    messagingSenderId: "162210099533",
    appId: "1:162210099533:web:ba2d6703fbc45175ce4704"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);

  export default firebase;