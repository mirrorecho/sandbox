var http = require('http');
var path = require('path');
var fs = require('fs');
// var ext = require('ext');

var handleRequest = function(request, response) {

    var pathname = request.url;

    if (pathname == '/') pathname = '/index.html';

    // get file extension
    var extension = path.extname(pathname);

    // map extension to file type
    var typeExt = {
        '.html' : 'text/html',
        '.js' : 'text/javascript',
        '.css' : 'text/css'
    }

    // content tyep... if type not defined, default to plan text:
    var contentType = typeExt[extension] || 'text/plain';

    // read and write back the file with the appropriate content type

    fs.readFile(__dirname + pathname,
        function (err, data) {
            // if there's an error, say so...
            if (err) {
                response.writeHead(500);
                return response.end('Error loading ' + pathname);
            }
            // otherwise, send the file contents...
            response.writeHead(200, { 'Content-Type':contentType} );
            response.end(data);
        }
        )
}

var server = http.createServer(handleRequest);
server.listen(8080);

var io = require('socket.io').listen(server);

io.sockets.on('connection',
    function (socket) {
        console.log("we have new new thing! " + socket.id);
        socket.on('disconnect', function() {
            console.log("Disconnected!");
        });
        socket.on('mouse', function(data) {
            // data coming in....including objects!!!
            console.log("Received: 'mouse' " + data.x + " " + data.y);
            // send it to all other clients.... ? WTF?
            socket.broadcast.emit('mouse-yo', data);
        });
        // socket.on('mouse-yo', function(data) {
        //     onsole.log("MOUSE AGAIN " + data.x + " " + data.y);
        // });
    }
);

// io.sockets.on('mouse-yo', function(data) {
//     onsole.log("MOUSE AGAIN " + data.x + " " + data.y);
// });

console.log('Server started on port 8080');