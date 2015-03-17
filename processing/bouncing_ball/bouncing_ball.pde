PVector location = new PVector(100,100);
PVector velocity = new PVector(1.2,2.2);

void setup() {
    size(640, 480);
    background(44);
}

void draw() {
    //background(44, 44);
    location.add(velocity);
    
    // bouncing:
    if ((location.x>width) || (location.x<0)) velocity.x = velocity.x * -1;
    if ((location.y>height) || (location.y<0)) velocity.y = velocity.y * -1;
    stroke(0);
    fill(166);
    ellipse(location.x,location.y,22,22);

}