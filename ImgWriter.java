/* Name:    Zaynab Ghazi
 * File:    ImgWriter.java
 * Desc:
 *
 * This program writes a ppm file from just an array of pixels.
 * 
 *
 */
import java.util.*;
import java.io.*;
public class ImgWriter{
    //comstructor():
    public ImgWriter(String filename, Color[][] img) throws IOException{
	PrintWriter out = new PrintWriter(filename);
	out.print("P3 ");
	out.println(img[0].length+" "+img.length+" 255");
	for(int i=0; i<img.length; i++){
	    for(int j=0;j<img[0].length;j++){
		out.print(img[i][j].getRed()+" "+img[i][j].getGreen()+" "+img[i][j].getBlue()+" ");
	    }
	    out.println();
	}
	out.close();
    }
}
	    
    
