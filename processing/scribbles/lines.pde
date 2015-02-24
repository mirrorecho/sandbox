int HEIGHT = 600;
int WIDTH = 800;

void setup() {
    size(WIDTH,HEIGHT);
    background(44);
    noFill();
}

void draw() {

}

void mousePressed() {
    //arc(mouseX, mouseY, random(HEIGHT / 2 + 1), random(HEIGHT / 2 + 1), PI);
    background(44);
    float startPi = PI * random(2);
    float endPi = startPi + PI * random(2);
    arc(mouseX, mouseY, random(HEIGHT / 2 + 20), random(HEIGHT / 2 + 20), startPi, endPi);
}