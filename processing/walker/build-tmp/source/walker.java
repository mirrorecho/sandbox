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

public class walker extends PApplet {


Random gen = new Random();

ArrayList<WalkerCluster> walkerClusters = new ArrayList<WalkerCluster>();
//int numWalkers = 99;
 
int specialOpacity = 220;
int minOpacity = 11; 
 
public void setup() {
  size(1280, 720);
  background(255);
  
 
 //for (int x=0; x<numWalkers; x++) {
 //   walkers[x] = new Walker();
 //}
  
}

public void mousePressed() {
  int startR = ceil(random(0, 255));
  int startG = ceil(random(0, 255));
  int startB = ceil(random(0, 255));
  int startOp = minOpacity*2;
  walkerClusters.add(new WalkerCluster(mouseX, mouseY, startR, startG, startB, startOp, 22));
}

public void draw() {
  for (int i = walkerClusters.size()-1; i >= 0; i--) {
    walkerClusters.get(i).stepMany();
  }  
}

public void keyPressed() {
  if (key == ' ') saveFrame("sketches/walker-###.png");
  if (key =='s') walkerClusters.clear();
  if (key =='d') {
    for (int i = walkerClusters.size()-1; i >= 0; i--) {
      
      WalkerCluster myCluster = walkerClusters.get(i);
      walkerClusters.add(
        new WalkerCluster(
          myCluster.walkers[0].x, 
          myCluster.walkers[0].y, 
          myCluster.walkers[0].shadeR, 
          myCluster.walkers[0].shadeG, 
          myCluster.walkers[0].shadeB,
          myCluster.walkers[0].opacity,
          4)
      );
      
    }
  } 
}

class WalkerCluster {
 Walker[] walkers;
  
  WalkerCluster(int startX, int startY, int startR, int startG, int startB, int startOp, int numWalkers) {
    walkers = new Walker[numWalkers];
    walkers[0] = new Walker(startX, startY, startR, startG, startB, startOp, true);
    for (int i=1; i<numWalkers; i++) 
      walkers[i] = new Walker(startX, startY, startR, startG, startB, ceil(startOp *2/3), false);
  }
  
  public void stepMany() {
    boolean moveCluster = false;
    if (random(299) < 1) moveCluster = true;
    for (int i=0; i<walkers.length; i++) {
      
      // move everything to the first one (so that it will be positioned NOT at the current special walker, but some other one)
     
      if (moveCluster && i>0) { //&& random(2) < 1) {
        // move other walkers to the 0th (special!) one:
        walkers[i].x = walkers[0].x; //+ floor(random(-3,4)); ;
        walkers[i].y = walkers[0].y; //+ floor(random(-3,4)); ;
      
        walkers[i].shadeR = walkers[0].shadeR;
        walkers[i].shadeG = walkers[0].shadeG;
        walkers[i].shadeB = walkers[0].shadeB;
        walkers[i].opacity = ceil(walkers[0].opacity *2/3);
        walkers[i].xPref = walkers[0].xPref;
        walkers[i].yPref = walkers[0].yPref;
      }
      walkers[i].step();
      walkers[i].show();
    }
  }
  
  
}


class Walker {
  int shadeR;
  int shadeG;
  int shadeB;
  int opacity = specialOpacity;
  
  int x;
  int y;
  
  int xPref;
  int yPref;
  
  int stepX;
  int stepY;
  
  boolean imSpecial;
  
  Walker(int startX, int startY, int startR, int startG, int startB, int startOp, boolean isSpecial) {
    //x = random(width);
    //y = random(height);
    imSpecial = isSpecial;
   
    xPref = floor(random(-1,2));
    yPref = floor(random(-1,2));
    
    x = startX;
    y = startY;
    shadeR = startR;
    shadeG = startG;
    shadeB = startB;
  }
  
  public void step() {
    
   
    if (imSpecial) shadeG += floor(random(-1,2)); else shadeG += floor(random(-2,3));;
    if (shadeG<0) shadeG = 0;
    if (shadeG>255) shadeG = 255;
    //if (shadeG>shadeR) shadeG = shadeR;
    
    if (imSpecial) shadeR += floor(random(-1,2)); else shadeR += floor(random(-2,3));;
    if (shadeR<0) shadeR = 0;
    if (shadeR>255) shadeR = 255;
    //if (shadeR>shadeG) shadeR = shadeG;
    
    if (imSpecial) shadeB += floor(random(-1,2)); else shadeB += floor(random(-2,3));;
    if (shadeB<0) shadeB = 0;
    if (shadeB>255) shadeB = 255;

    if (imSpecial) opacity+= floor(random(-1,2));
    else opacity-=1;
    
    if (opacity<minOpacity) opacity=minOpacity;
    if (opacity>specialOpacity) opacity=specialOpacity;
    
    if (random(2)<1) xPref = floor(random(-1,2));
   
   
     if (random(2)<1) yPref = floor(random(-1,2));        
   
   
     if (imSpecial || random(4)<1) {
       stepX=round(floor(random(xPref-1, xPref+2)) * (float)gen.nextGaussian() ); 
       stepY=round(floor(random(yPref-1, yPref+2)) * (float)gen.nextGaussian() );
     } else {
       if (random(2)<1) stepX= round( floor(random(-1, 2)) * (float)gen.nextGaussian() ); else stepX=0; 
       if (random(2)<1) stepY= round( floor(random(-1, 2)) * (float)gen.nextGaussian() ); else stepY=0;
     }
     
     // ADDITIONALLY... let's make % chance the walker will move towards the mouse
     // if (random(9)<1) {
     //   if (mouseX < x) stepX -=1; else stepX +=1; 
     //   if (mouseY < y) stepY -=1; else stepY +=1;
     // }
     
     
     
    /*
    if (stepX == 0 || !imSpecial) stepX=floor(random(-2,3)); 
      else if (stepX < 0) stepX = floor(random(-2,2)); 
      else stepX = floor(random(-1,3));

    if (stepY == 0 || !imSpecial) stepY=floor(random(-2,3)); 
      else if (stepY < 0) stepY = floor(random(-2,2)); 
      else stepY = floor(random(-1,3));
    */

    x += stepX;
    y += stepY;
    
   
    // cross over to the other side of the screen if we reach the edge:
    if (x>width) x-=width; else if (x<0) x+=width;
    if (y>height) y-=height; else if (y<0) y+=height;
  }
  
  public void show() {
    stroke(shadeR, shadeG, shadeB, opacity);
    point(x, y);
    stroke(shadeR, shadeG, shadeB, round(opacity/3));
    point(x+1, y);
    point(x-1, y);
    point(x, y+1);
    point(x, y-1);
    stroke(shadeR, shadeG, shadeB, round(opacity/6));
    point(x+1, y+1);
    point(x+1, y-1);
    point(x-1, y+1);
    point(x-1, y-1);
  }
  
} 
 
 
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "walker" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
