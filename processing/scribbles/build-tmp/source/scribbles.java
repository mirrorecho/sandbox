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

public class scribbles extends PApplet {


Random gen = new Random();

public void setup() {
  size(1280, 720);
  background(60,40,44);
  
 
 //for (int x=0; x<numWalkers; x++) {
 //   walkers[x] = new Walker();
 //}
  
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "scribbles" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
