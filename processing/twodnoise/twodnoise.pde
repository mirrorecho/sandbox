float xOffR = random(999);
float yOffR = random(999);
float timeOffR = random(999);
float xOffG = random(999);
float yOffG = random(999);
float timeOffG = random(999);
float xOffB = random(999);
float yOffB = random(999);
float timeOffB = random(999);

float increment = 0.01;

void setup() {
    size(600, 600);
    background(222);
    noiseDetail(2, 0.75);



    //for (int x=0; x<numWalkers; x++) {
    //   walkers[x] = new Walker();
    //}
  
}

void draw() {
    loadPixels();

    for (int x=0; x<width; x++) {

        for (int y=0; y<height; y++) {

            float brightR = noise(x/300.0 + xOffR, y/300. + yOffR, timeOffR) * 255;
            float brightG = noise(x/300.0 + xOffG, y/300. + yOffG, timeOffG) * 255;
            float brightB = noise(x/300.0 + xOffB, y/300. + yOffB, timeOffB) * 255;
            
            pixels[x + y*width] = color(brightR, brightG, brightB);
    
        }
    }
    updatePixels();
    timeOffR += increment;
    timeOffG += increment;
    timeOffB += increment;

}