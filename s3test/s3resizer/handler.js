'use strict';

// Permite la ejecución de código Javascript asincrono
var async = require('async');
// SDK de Amazon Web Services
var AWS = require('aws-sdk');
// GraphicsMagick, para la manipulacion de imagenes usando ImageMagick
var gm = require('gm').subClass({ imageMagick: true });
// Libreria de utilidades de NodeJS
var util = require('util');

var s3 = new AWS.S3();

module.exports.resize = (event, context, callback) => {

  console.log("Iniciando Lambda de redimensionamiento de imagenes");

  var bucket = event.Records[0].s3.bucket.name;
  var srcKey = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, " "));
  var destKey = "new_" + srcKey;
  var destDir = "resized/";

  var typeMatch = srcKey.match(/\.([^.]*)$/);
    if (!typeMatch) {
        callback("Formato de archivo desconocido.");
        return;
    }
    var imageType = typeMatch[1];
    if (imageType != "jpg" && imageType != "png") {
        callback('Tipo de imagen no soportado: ${imageType}');
        return;
    }

    console.log("Se subio la imagen " + srcKey + " al bucket " + bucket);
    async.waterfall([

      function download(next) {
        console.log("Descargado archivo...");
        s3.getObject({
          Bucket: bucket,
          Key: srcKey
        }, next);
      },
      function transform(response, next) {
        console.log("Iniciando transformacion...");
        console.log("A procesar: " + response.Body);

        gm(response.Body).size(function(err, size) {

          console.log("Size es: " + size);

          if(size == 'undefined') {
            next("Errorsito :()");
          }

          // Se determina el factor de escalamiento para que la imagen se mantenga proporcional
          var scalingFactor = Math.min(150/size.width, 150/size.height);

          var width = size.width / scalingFactor;
          var height = size.height / scalingFactor;

          this.resize(width, height).toBuffer(imageType, function(err, buffer) {
            if(err) {
              next(err);
            } else {
              next(null, response.ContentType, buffer);
            }
          });

        });
      },
      function upload(contentType, data, next) {
        console.log("Subiendo archivo resultante...");
        s3.putObject({
          Bucket: bucket,
          Key: destDir + destKey,
          ContentType: contentType
        }, next);
      }], function (err) {
      if(err) {
        console.error("Fallo el proceso. Error: " + err);
      } else {
        console.log("Redimensionamiento finalizado");
      }

    }
  );
  callback(null, "message");
}
