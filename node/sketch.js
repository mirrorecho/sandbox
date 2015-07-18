var socket;

function setup() {

    socket = io.connect('http://localhost:8080');

    // callback function to send mouse data to server...
    socket.on('mouse-yo',
        function(data) {
            alert('boo');
            // draw a blue circle
            fill(0,0,255);
            noStroke;
            ellipse(data.x, data.y, 80, 80);
        }
    );

  // createCanvas(600, 400);

}

function mouseDragged() {
    var data = {
        x: mouseX,
        y: mouseY
    };
    socket.emit('mouse', data);
}

// function draw() {

//   // background
//   background(42, 169, 217);

//   // ellipse
//   fill(242, 228, 21);
//   ellipse(100,100,100,100);

//   //rectangle
//   fill(162,217, 39);
//   rect(300,100,150,150);

// }