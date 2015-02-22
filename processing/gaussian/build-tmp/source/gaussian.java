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

int sketchX = 800;
int sketchY = 600;

ArrayList<Cloud> clouds = new ArrayList<Cloud>();
Cloud c = new Cloud(sketchX/2, sketchY/2);

public void setup() {
    print(-121 % 120);
    size(sketchX, sketchY);
    background(212);
    gen = new Random();
    noStroke();
}

public void mousePressed() {
  clouds.add(new Cloud(mouseX, mouseY));
}


public void draw() {
  for (int i = clouds.size()-1; i >= 0; i--) {
    clouds.get(i).step();
  }  
}

public void keyPressed() {
  if (key == ' ') {
        saveFrame("sketches/gauss-dot-###.png"); 
        print("Saved!");
    }
    if (key == 'w') {
      for (int i = clouds.size()-1; i >= 0; i--) {
        clouds.get(i).colorR=244;
        clouds.get(i).colorG=244;
        clouds.get(i).colorB=244;
      }  
    }
}


class Cloud {
    int centerX = 0;
    int centerY = 0;
    int colorR = round(random(99));
    int colorG = round(random(99));
    int colorB = round(random(99));
    int radiusMax = round(random(299) + 2);
    int radiusX = round(random(radiusMax) + 1);
    int radiusY = round(random(radiusMax) + 1);
    float sd = 44;

    Cloud(int x, int y) {
        moveCenter(x,y);
    }

    public int colorRange(float colorV) {
        if (colorV > 255) colorV = 255;
        if (colorV < 0) colorV = 0;
        return round(colorV);
    }

    public void step() {
        float numX = (float)gen.nextGaussian();
        float numY = (float)gen.nextGaussian();
        float x = ((numX * sd) + centerX) % sketchX;
        if (x < 0) x += sketchX;
        float y = ((numY * sd) + centerY) % sketchY;
        if (y < 0) y += sketchY;
        fill(colorR,colorG,colorB,ceil(random(3)));
        ellipse(x, y, radiusX, radiusY);
        if (random(44)<1) {
            colorR = colorRange(colorR + ( random(41) - 20));
            colorG = colorRange(colorG + ( random(41) - 20));
            colorB = colorRange(colorB + ( random(41) - 20));
            
            radiusX = round(random(22) + 1);
            radiusY = round(random(22) + 1);
            // print(round(radiusY));
            // print("|");
            sd = random(99) + 1;
            moveCenter((int)x, (int)y);
        }
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
