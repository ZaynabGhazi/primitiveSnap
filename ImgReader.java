/* Name:    Zaynab Ghazi 
 * File:    ImgReader.java
 * Desc:
 *
 *
 * This program reads a ppm file and turns into an exploitable array of pixels,
 * it also contains a static method to apply an edgeDetection filter.
 */
import java.util.*;
import java.io.*;
public class ImgReader{
    //initialize instance variables to respectively store image's width,height and pixels:
    private int width;
    private int height;
    private Color[][] image;
    //constructor():
    public ImgReader(String filename) throws FileNotFoundException{
	Scanner input = new Scanner(new File(filename));
	String magicNumber="";
	//skip magic number
	//magicNumber= input.nextLine();
	while(!input.hasNextInt()){
	    input.next();
	}

        width= input.nextInt();
	
	height= input.nextInt();
		
	//skip over max color value 255
	input.nextInt();
	image = new Color[height][width];
	while(input.hasNext()){
	    //create RGB Color() object and store it in pixel array
	    for(int i=0; i< this.height;i++){
		for(int j=0; j< this.width;j++){
		    int red = input.nextInt();
		    int green = input.nextInt();
		    int blue = input.nextInt();
		    image[i][j] = new Color(red,green,blue);
		}
	    }
	    
	}
	
	
    }
    //@return pixel array
    public Color[][] getArray(){
	return this.image;
    }
    //@return width of image
    public int getWidth(){
	return this.width;
    }
    //@return height of image
    public int getHeight(){
	return this.height;
	
    }
    /* applies edgeDetection filter to an array of pixels
     * @param image : array of pixels
     * @param width: picture width
     * @param height: picture height
     * @param hindex : starting height index
     * @param winex: starting width index
     * @return new filtered pixel array
     */
    public static Color[][] edgeDetect(Color[][] image, int width, int height, int hindex, int windex){
	int left,right, bottom, top;
	int red, green, blue;
	Color newColor;
	Color[][] edgeDetectImg = new Color[height][width];
	//Apply grayScale filter to assist edgedetection
	for(int i=windex; i<width;i++){
	    for(int j=hindex;j<height;j++){
		int c =(int)(image[j][i].getRed()*0.3)+(int)(image[j][i].getGreen()*0.59)+(int)(image[j][i].getBlue()*0.99);
		image[j][i]= new Color (c,c,c);
	    }
	}//grayScale--end
	//apply edgeDetection on new array
	for(int i=windex; i<width;i++){
	    for(int j=hindex;j<height;j++){
	        left = i-1;
		right = i+1;
		top = j-1;
		bottom = j+1;
		if(left < 0){
		    left = 0;
		}
		if(right >= width){
		    right = width-1;
		}
		if(top < 0 ){
		    top =0;
		}
		if(bottom >= height){
		    bottom = height-1;
		}
		//compute red compenent
		red = -1*(image[top][left].getRed())- (image[top][i].getRed())- (image[top][right].getRed())- (image[j][left].getRed())+ 8*(image[j][i].getRed())- (image[j][right].getRed())- (image[bottom][left].getRed())- (image[bottom][i].getRed())- (image[bottom][right].getRed());
		if(red<0){
		    red = 0;
		}
		//compute blue component
		blue = -1*(image[top][left].getBlue())- (image[top][i].getBlue())- (image[top][right].getBlue())- (image[j][left].getBlue())+ 8*(image[j][i].getBlue())- (image[j][right].getBlue())- (image[bottom][left].getBlue())- (image[bottom][i].getBlue())- (image[bottom][right].getBlue());
		if(blue<0){
		    blue =0;
		}
		//compute green component
		green = -1*(image[top][left].getGreen())- (image[top][i].getGreen())- (image[top][right].getGreen())- (image[j][left].getGreen())+ 8*(image[j][i].getGreen())- (image[j][right].getGreen())- (image[bottom][left].getGreen())- (image[bottom][i].getGreen())- (image[bottom][right].getGreen());
		if(green<0){
		    green =0;
		}
		//verify reasonable component values
		if (green>255) green=255;
		if(red>255) red=255;
		if(blue>255) blue=255;
		//compare distance from black and white and stretch to closest color
		int greenDiff = 255-green; int redDiff= 255-red; int blueDiff = 255-blue;
		if (greenDiff < green) green=255;
		if (redDiff < red) red=255;
		if (blueDiff < green) blue=255;
		newColor = new Color(red, green, blue);
		edgeDetectImg[j][i] = newColor;
	    }
	}
	return edgeDetectImg;
	
    }//edgeDetect
    
    //UNIT_TEST
    public static void main(String[] args) throws FileNotFoundException,IOException{
	ImgReader img = new ImgReader("kira-bunny.ppm");
	Color[][] output = edgeDetect(img.getArray(), img.getWidth(), img.getHeight(),0,0);
	ImgWriter test= new ImgWriter("test18.ppm",output);
    } 
}



