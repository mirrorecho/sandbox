x = 0;
y = 0;
// height=480;
// width=640;
xspeed = 4;
yspeed = 4;



function setup() {
	background(111);
	createCanvas(640, 480);
}

function draw() {
	l1 = new p5.Vector(0,0);
	l2 = new p5.Vector(100,100);
	velocity = new p5.Vector(1,1);

	background(111);
	l1 = l1.add(velocity);

	if ((l1.x > width) || (l1.x < 0)) {
		velocity.x *= -1;
	};

	if ((l1.y > height) || (l1.y < 0)) {
  		velocity.y *= -1;
  	};

	stroke(0);
	fill(199);
	ellipse(l1.x,l1.y,12,12);

}