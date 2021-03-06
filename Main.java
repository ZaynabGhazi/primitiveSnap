/* Name:    Zaynab Ghazi 
 * File:    Main.java
 * Desc:
 *
 *
 * This program reads the file specified by the command line
 * applies the filters requested by flags and returns files with filters applied
 */
import java.util.*;
import java.io.*;

public class Main{
    public static void main(String[] args) throws FileNotFoundException, IOException{
	String filename="";
	boolean compression=false;
	boolean edgeDetection=false;
	boolean quadtreeOutline = false;
	String outputName="";
	//detect fileName
	for(int i=0; i<args.length-1;i++){
	    if(args[i].equals( "-i")) filename = args[i+1];
	}
	//detect requested filters:
	for(int i=0; i<args.length-1;i++){
	    if(args[i].equals("-o")) outputName = args[i+1];
	}
	for(int i=0;i<args.length;i++){
	    if(args[i].equals("-c")) compression = true;
	    if(args[i].equals("-e")) edgeDetection = true;
	    if (args[i].equals("-t")) quadtreeOutline = true;
	}
	//call corresponding constructors:
	if ( compression && !edgeDetection){
	    //output 8 files of different resolutions
	    for(int i=0; i<=7;i++){
		QuadTree image = new QuadTree(filename,compression,edgeDetection,quadtreeOutline,outputName+"-"+(i+1)+".ppm");
	    }
	}
	if (edgeDetection){
	    QuadTree image = new QuadTree(filename,compression,edgeDetection,quadtreeOutline,outputName+".ppm");
	}
    }
}


	
