/* Name:    Zaynab Ghazi
 * File:    Color.java
 * Desc:
 *
 * This program creates a new object Color(a,b,c)to store RGB color of pixels.
 *
 */
public class Color{
    //initialize instance variables to store color components of general RGB color:
    private int red;
    private  int green;
    private int blue;
    //constructor():
    public Color(int red, int green, int blue){
	this.red = red;
	this.green=green;
	this.blue=blue;
    }
    //@return red component of RGB color
    public int getRed(){ return this.red;}
    //@return green component of RGB color
    public int getGreen(){ return this.green;}
    //@return blue component of RGB color
    public int getBlue(){ return this.blue;}
    //@return String representation of RGB color object
    public String toString(){
	return "["+this.red+", "+this.green+", "+this.blue+"]";
    }
}
    
