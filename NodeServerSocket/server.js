var server = require('http').createServer();
var io = require('socket.io')(server);

io.on('connection', function(socket) {

	console.log('Socket {0} connected'.format(socket.id));

	if (socket.handshake.query.username && socket.handshake.query.userid) {
		console.log("User connecting: {0}, identifier of device: {1}".format(socket.handshake.query.username, socket.handshake.query.userid));
		socket.username = socket.handshake.query.username;
		socket.userid = socket.handshake.query.userid;

		socket.broadcast.emit('isOnline', {
			username: socket.username,
			userid: socket.userid,
			time: new Date().getTime()
		});
	}
	else {
		socket.emit('disconnect', { status: false, message: "Invalid connection parameters" });
	}

	socket.on('sendMessage', function(data) {
		console.log('User {0} sended a message'.format(socket.id));
		if (data && data.message) {
			io.sockets.emit('sendMessage', {
				username: socket.username,
				userid: socket.userid,
				message: data.message,
				time: new Date().getTime()
			});
		}
	});

	socket.on('disconnect', function() {
		console.log('Socket {0} disconnected -> {1} - {2}'.format(socket.id, socket.username, socket.userid));
		socket.broadcast.emit('isOffline', {
			username: socket.username,
			userid: socket.userid,
			time: new Date().getTime()
		});	
	});
});

server.listen(3000, '0.0.0.0', function() {
	console.log('Server listen on port 3000');
});

String.prototype.format = function() {
    var formatted = this;
    for (var i = 0; i < arguments.length; ++i) {
        var regex = new RegExp('\\{'+i+'\\}', 'gi');
        formatted = formatted.replace(regex, arguments[i]);
    }
    return formatted;
};