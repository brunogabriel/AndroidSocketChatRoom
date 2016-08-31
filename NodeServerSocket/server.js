/**
	Only to simplify log 
**/

String.prototype.format = function() {
    var formatted = this;
    for (var i = 0; i < arguments.length; ++i) {
        var regex = new RegExp('\\{'+i+'\\}', 'gi');
        formatted = formatted.replace(regex, arguments[i]);
    }
    return formatted;
};

var server = require('http').createServer();
var io = require('socket.io')(server);

io.on('connection', function(socket) {

	console.log('Socket {0} connected'.format(socket.id));

	socket.on('sendMessage', function(data) {

	});

	socket.on('disconnect', function() {
		console.log('Socket ' + socket.id + ' disconnected');
	});
});

server.listen(3000, '0.0.0.0', function() {
	console.log('Server listen on port 3000');
});