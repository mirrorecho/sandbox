int HEIGHT = 600;
int WIDTH = 800;
int MOUTH_COUNT = 12;
ArrayList<Mouth> Mouthes = new ArrayList<Mouth>();


float ellipseMax = 1200;

PImage img;


void setup() {
    size(WIDTH,HEIGHT);
    background(255);
    // let's try 3 mouthes:
    //blur = loadShader("blur.glsl");
    for (int i = 0; i<MOUTH_COUNT; i++) {
        Mouthes.add(new Mouth());
    }
    img = get();
}

void draw() {

    noStroke();
    fill(255,4);
    rect(0,0,WIDTH,HEIGHT);
    for (int i = Mouthes.size()-1; i >= 0; i--) {
        Mouthes.get(i).step();
    }
    //loadPixels(); //copy window contents -> pixels[]==g.pixels[]
    //fastSmallShittyBlur(g,img); //g=PImage of main window
    //image(img,0,0); //draw results;
    //set(0, 0, img);
    // A new frame is added to the movie every cycle through draw().
    filter(BLUR, 1);
}

class Mouth {
    float tX;
    float tY;
    float tW; // noise input for width of the ellipse
    float tH; // noise input for heigth of the ellipse
    float tR;
    float tG;
    float tB;
    float tM;
    
    Mouth() {
        tX = random(22.0);
        tY = random(22.0);
        tW = random(22.0);
        tH = random(22.0);
        tR = random(22.0);
        tG = random(22.0);
        tB = random(22.0);
        tM = random(22.0);
    }

    void step() {
        float x = noise(tX);
        float y = noise(tY);
        float w = noise(tW);
        float h = noise(tH);
        float r = noise(tR);
        float g = noise(tG);
        float b = noise(tB);
        float m = noise(tM);
        stroke(r*122+88,g*166+44,b*122, 122);
        noFill();
        ellipse(x*WIDTH, y*HEIGHT, w*m*ellipseMax, h*m*ellipseMax);
        noStroke();
        fill(255,255,255, 8);
        ellipse(x*WIDTH, y*HEIGHT, w*m*ellipseMax*5/6, h*m*ellipseMax*5/6 );
        tX += 0.004;
        tY += 0.004;
        tW += 0.01;
        tH += 0.01;
        tR += 0.001;
        tG += 0.001;
        tB += 0.001;
        tM += 0.001;
    }
}


void fastSmallShittyBlur(PImage a, PImage b){ //a=src, b=dest img
  int pa[]=a.pixels;
  int pb[]=b.pixels;
  int h=a.height;
  int w=a.width;
  final int mask=(0xFF&(0xFF<<2))*0x01010101;
  for(int y=1;y<h-1;y++){ //edge pixels ignored
    int rowStart=y*w  +1;
    int rowEnd  =y*w+w-1;
    for(int i=rowStart;i<rowEnd;i++){
      pb[i]=(
        ( (pa[i-w]&mask) // sum of neighbours only, center pixel ignored
         +(pa[i+w]&mask)
         +(pa[i-1]&mask)
         +(pa[i+1]&mask)
        )>>2)
        |0xFF000000 //alpha -> opaque
        ;
    }
  }
}
