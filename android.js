var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var port = process.env.PORT || 3000;
var clients =[];
var activeSockets={};
var messagesQueue={};


var mySQL= require('mysql');
var db_config = {
    host: 'localhost',
    user: 'root',
    password: 'tgisispuj',
    database: 'rawrdbPrueba'
};
var connection = mySQL.createConnection(db_config);
var pool  = mySQL.createPool(db_config);

server.listen(port, function () {
  console.log('Server listening at port %d', port);
});
var usernames = {};
var numUsers = 0;


/**
 * 
 * Función encargada de escuchar las peticiones de los clientes
 * @ requirement 18 
 */
io.on('connection', function (socket) {
  console.log("conexión del socket "+ socket);
 
  console.log("Se ha conectado un usuario");


/**
 * Función que envía el mensaje al un receptor
 * Almacena el mensaje en la base de datos
 * @ requirement 18 
 * @ requirement 19
 * @param msg, mensaje en formato JSON que contiene
 * el nombre de usuario del emisor, del receptor y el texto a enviar
 */
  socket.on('chat_message', function(msg){
    var message=msg;
    var receiver= message.receiver;
    console.log("envían un mensaje de chat para "+ receiver);
        if(typeof(clients[receiver]) != "undefined"){
          console.log("el receptor está conectado y se le envía el mensaje");



          pool.query('INSERT INTO Message SET ?',
          {status:'read',text: message.message, username_receiver:message.receiver, username_sender:message.sender}
          ,function(err, rows, fields) {
            if (!err){
            console.log('Mensaje enviado y almacenado');
            io.to(clients[receiver]).emit('chat_message', msg);
          }else{
            io.to(clients[receiver]).emit('chat_message', msg);
            console.log(err);
            console.log('Error al enviar el mensaje');
          }
      });




      }else{
        
       
        pool.query('INSERT INTO Message SET ?',
        {status:'unread',text: message.message, username_receiver:message.receiver, username_sender:message.sender}
        ,function(err, rows, fields) {
          if (!err){
            console.log('Mensaje enviado y almacenado');
            io.to(clients[receiver]).emit('chat_message', msg);
          }else{
            io.to(clients[receiver]).emit('chat_message', msg);
            console.log(err);
            console.log('Error al enviar el mensaje');
          }
      });
  


    }
  });
/**
 * Función que envía una notificacion al receptor
 * @param msg, notificación en formato JSON que contiene
 * @ requirement 07
 * @ requirement 13
 * @ requirement 34
 * el nombre de usuario del emisor, del receptor y el texto a enviar
 */

  socket.on('notification', function(msg){
    
    console.log("envían un mensaje notification");
    var message=JSON.parse(msg);
    var receiver= message.receiver;
    io.to(clients[receiver]).emit('notification', msg);
  });
/**
 * Función que desconecta a un cliente del chat
 * Borra el socket asociado al nombre de usuario
 * @ requirement 18  
 */
  socket.once('disconnect', function(){
   
    var username = activeSockets[socket.id];
    console.log("El usuario "+ username+ " se ha desconectado");
    
    delete clients[username];
    delete activeSockets[socket.id];
    socket.broadcast.emit('disconnect',{user:username});
  });
/**
 * Función busca un usuario cuyo nombre contenga una subcadena
 * envía los usuarios cuyos nombres empiecen por la subcadena
 * @param msg, información básica del usuario
 * contiene parte del nombre del usuario
 * @ requirement 18 
 */
  socket.on('start_session', function(msg){
    var m = msg;
    console.log("envían un mensaje start_session para "+ m.username);
    clients[m.username]=socket.id;
    console.log("ahora en clients está "+ clients[m.username]);
    activeSockets[socket.id]=m.username;
    var return_list = [];

    for(key in activeSockets){
      return_list.push(activeSockets[key]);
      console.log("está conectado " +activeSockets[key]);
    }

    io.emit('response_start_session', JSON.stringify({users:return_list}));
  });
/**
 * Función que identifica a un usuario y le asigna un socker
 * @param msg, información básica del usuario
 * contiene el nombre del usuario que se va a autenticar
* @ requrement 33
 */
  socket.on('hint', function(msg){
    var m = msg.hint;
    m= m+ "%";
    console.log("Got a hint "+m);

        pool.query('SELECT p.username, ph.path FROM (Pet p INNER JOIN User o ON p.username = o.username) LEFT JOIN Photo ph ON ph.idPhoto =o.idPhoto_profile  WHERE p.username  LIKE ?', [m], function(err, results) {
            console.log(results);
            io.to(socket.id).emit('hint', {result:results});
        });

  });
  
});