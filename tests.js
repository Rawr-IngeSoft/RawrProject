var should = require('should');
var io = require('socket.io-client');
var socketURL = 'http://0.0.0.0:3000/';
var options ={
  transports: ['websocket'],
  'force new connection': true
};

var chatUser1 = {'username':'TorresM'};
var chatUser2 = {'username':'JaimeP'};
var chatUser3 = {'username':'AlfredoS'};

describe("doAsync",function(){
	this.timeout(3000);

	/*
	* 
	* Prueba unitaria 1, 
	* avisar a todos los clientes sobre una conexión nueva
	* 
	*/
	
	it('Should notify new connection', function(done){
	  var client1 = io.connect(socketURL, options);
	  var client2 = io.connect(socketURL, options);
	  
		client1.on('response_start_session', function(data1){
	  	 		var connectedUser = JSON.parse(data1);
	  			connectedUser.users[0].should.equal(chatUser2['username']);
				connectedUser.users[0].should.equal("JaimeP");
	 	});
	 	client2.emit('start_session', chatUser2);
	 	client2.emit('hint', {hint:'p'});
	 	done();

	});
	/*
	* 
	* Prueba unitaria 2, 
	* Debe Enviar y Recibir Mensajes
	* 
	*/
	it('Should send and receive messages', function(done){
	  var client1 = io.connect(socketURL, options);
	  var client2 = io.connect(socketURL, options);
	  
		client1.on('chat_message', function(data1){
	  	 		var connectedUser = JSON.parse(data1);
	  			connectedUser['sender'].should.equal('JaimeP');		
	 	});
	 	var message ={sender:'JaimeP', receiver: 'TorresM'};
	 	client2.emit('start_session',message );
	 	done();

	});

	/*
	* 
	* Prueba unitaria 3, 
	* Debe Enviar y Recibir Notificaciones al usuario
	* 
	*/

	it('Should send and receive notifications', function(done){
	  var client1 = io.connect(socketURL, options);
	  var client2 = io.connect(socketURL, options);
	  
		client1.on('notification', function(data1){
	  	 		var connectedUser = JSON.parse(data1);
	  			connectedUser[0].username.should.equal('pongo');		
	 	});
	 	var message ={sender:'JaimeP', receiver: 'TorresM'};
	 	client2.emit('notification',message );
	 	done();

	});
	/*
	*
	* 
	* Prueba unitaria 3, 
	* Debe dar pistas sobre el username enviado
	* 
	*/

	it('Should send and receive notifications', function(done){
	  var client1 = io.connect(socketURL, options);
	  var client2 = io.connect(socketURL, options);
	  
		client1.on('hint', function(data1){
	  	 		var connectedUser = JSON.parse(data1);
	  			console.log(connectedUser);
	  			//connectedUser['sender'].should.equal('JaimeP');		
	 	});
	 	// falta mockear 
	 	var message ={username:'pon'};
	 	client2.emit('hint',message );
	 	done();

	});



});