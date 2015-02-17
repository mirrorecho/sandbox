import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class gaussian extends PApplet {



Random gen;

Cloud c = new Cloud(640, 360);

public void setup() {
    size(1280, 720);
    background(22);
    gen = new Random();
    noStroke();
    fill(44,44,99,88);
}



public void draw() {
    c.step();
}


class Cloud {
    int centerX = 0;
    int centerY = 0;

    Cloud(int x, int y) {
        moveCenter(x,y);
    }

    public void step() {
        float numX = (float)gen.nextGaussian();
        float numY = (float)gen.nextGaussian();
        float sd = 88;
        float x = numX * sd + centerX;
        float y = numY * sd + centerY;
        ellipse(x, y, 22, 22);
        if (random(99)<1) moveCenter((int)x, (int)y);
    }

    public void moveCenter(int x, int y) {
        centerX = x;
        centerY = y;
    }    

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "gaussian" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
