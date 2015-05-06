var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var port = process.env.PORT || 3000;
var clients =[];
var activeSockets=[];
var messagesQueue=[];

server.listen(port, function () {
  console.log('Server listening at port %d', port);
});

// Routing

// Chatroom

// usernames which are currently connected to the chat
var usernames = {};
var numUsers = 0;

io.on('connection', function (socket) {
  var addedUser = false;
  console.log("se conecta");

  // when the client emits 'new message', this listens and executes
  socket.on('new message', function (data) {
    // we tell the client to execute 'new message'
	console.log("manda un new message");
    socket.broadcast.emit('new message', {
      username: socket.username,
      message: data
    });



  });

  socket.on('add user', function (username) {
     console.log("add user");

    socket.username = username;

    usernames[username] = username;
    ++numUsers;
    addedUser = true;
    socket.emit('login', {
      numUsers: numUsers
    });
    socket.broadcast.emit('user joined', {
      username: socket.username,
      numUsers: numUsers
    });
  });

  console.log("Se ha conectado un usuario");
    socket.on('chat_message', function(msg){
      console.log("envían un mensaje de chat");
      var message=JSON.parse(msg);
      var receiver= message.receiver;
      
      if(typeof(clients[receiver]) != "undefined"){
      io.to(clients[receiver]).emit('chat_message', msg);
      //add mysql insert message status received
      }else{
      //add mysql insert message status not read
      }

  });
   
   socket.on('new message', function (data) {
    // we tell the client to execute 'new message'
   console.log("llega un mensaje new message ");
   socket.broadcast.emit('new message', {
          username: socket.username,
          message: data
      });
    });

  socket.on('notification', function(msg){
     console.log("envían un mensaje notification");
      var message=JSON.parse(msg);
      var receiver= message.receiver;
      io.to(clients[receiver]).emit('notification', msg);
  });

  socket.on('disconnect', function(){
    var username = activeSockets[socket.id];
    console.log("El usuario "+ username+ " se ha desconectado");
    delete clients[username];
    delete activeSockets[socket.id];
  });

  socket.on('start_session', function(msg){
    
    var m = msg;
    console.log("envían un mensaje start_session para "+ m.username);

    clients[m.username]=socket.id;
    activeSockets[socket.id]=m.username;

    io.to(clients[m.username]).emit('response_start_session', m.username+ " está conectado");


  });
  
});