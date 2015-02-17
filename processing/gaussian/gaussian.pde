import java.util.*;

Random gen;

Cloud c = new Cloud(640, 360);

void setup() {
    size(1280, 720);
    background(22);
    gen = new Random();
    noStroke();
    fill(44,44,99,88);
}



void draw() {
    c.step();
}


class Cloud {
    int centerX = 0;
    int centerY = 0;

    Cloud(int x, int y) {
        moveCenter(x,y);
    }

    void step() {
        float numX = (float)gen.nextGaussian();
        float numY = (float)gen.nextGaussian();
        float sd = 88;
        float x = numX * sd + centerX;
        float y = numY * sd + centerY;
        ellipse(x, y, 22, 22);
        if (random(99)<1) moveCenter((int)x, (int)y);
    }

    void moveCenter(int x, int y) {
        centerX = x;
        centerY = y;
    }    

}