const functions = require('firebase-functions');
const admin = require('firebase-admin');
const dateTime = require('node-datetime');

admin.initializeApp(functions.config().firebase);

exports.addMessage = functions.https.onRequest((req, res) => {
  const original = req.query.text;

  admin.database().ref('/messages').push({original: original}).then(snapshot => {
    res.redirect(303, snapshot.ref);
  });
});

exports.makeUppercase = functions.database.ref('/messages/{pushId}/original').onWrite(
  event => {
    const original = event.data.val();
    console.log('Uppercasing', event.params.pushId, original);
    const uppercase = original.toUpperCase();

    return event.data.ref.parent.child('uppercase').set(uppercase);
  }
);

exports.helloWorld = functions.https.onRequest((request, response) => {
  // Obtener el valor actual
  admin.database().ref('/tracking_data/callsToService').once('value').then(function(snapshot) {
    console.log('Valor obtenido: ' + snapshot.val());
    newVal = parseInt(snapshot.val()) + 1;
    console.log('Nuevo valor: ' + newVal);
    admin.database().ref('/tracking_data/callsToService').set(newVal);
  });

  var dt = dateTime.create();

  var formatted = dt.format('d/m/Y H:M:S');

  response.send("Hola! Soy una funcion :). Me llamaste justo a " + formatted);
});
