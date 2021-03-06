/* Name:    Zaynab Ghazi
 * File:    QuadTree.java
 * Desc:
 *
 *
 * This program creates a quadTree object adapted to pixel filtering and image processing.
 *
 */
import java.io.*;
import java.util.*;

public class QuadTree{
    //instance variables to respectively store : tree's root, compressionLevel, width of image, height of image, two pixel arrays one for compression and one for edgeDetection, threshold array corresponding to desired compressionLevels and current threshold, as well as a boolean to detect whether user wants pixel grid:
    private Node root;
    private double compressionLevel;
    private int width,height;
    private Color[][] image;
    private Color[][] array;
    private int[] thresholdArray = {600000,190000,50000,7500,2000,380,33,6};
    private int threshold=0;
    private boolean grid;
    //@return root
    public Node getRoot(){
	return this.root;
    }
    //@return array of pixels 
    public Color[][] getImage(){
	return this.image;
    }
    
    
    //nested-class Node
    private class Node{
	// store measurements of node and its color and children:
	private int  x,y,width,height;
	private Color color;
	private Node[] children;
	
	// (x,y) corresponds to the upper-left bound of the node
	//Constructor1():
	private Node(int x, int y, Color color){
	    this.x=x;
	    this.y=y;
	    this.color=color;
	    this.children= new Node[4];
	}
	//Constructor2():
	private Node(int x, int y, int h, int w){
	    this.x=x;
	    this.y=y;
	    this.height=h;
	    this.width=w;
	    this.children= new Node[4];
	}
	/* returns average of children's colors
	 * @return Color RGB format for node
	 */
	private Color averageColorChildren(){
	    double r=0.0;
	    double g=0.0;
	    double b=0.0;
	    for(int i=0; i<this.children.length;i++){
		if (children[i] != null){
		    r+= children[i].getColor().getRed();
		    g+= children[i].getColor().getGreen();
		    b+= children[i].getColor().getBlue();
		}
	    }
	    r=r/children.length;
	    g=g/children.length;
	    b=b/children.length;
	    return new Color((int)r,(int)g,(int)b);
	}
	//verify whether node is leaf
	public boolean isLeaf(){
	    return this.children[0]==null && this.children[1]==null && this.children[2]==null && this.children[3]==null;
	}
	// @return x-coordinate of node
	public int getX(){
	    return this.x;
	}
	// @return y-coordinate of node
	public int getY(){
	    return this.y;
	}
	// @return color of node
	public Color getColor(){
	    return this.color;
	}
	/* changes node color to newColor
	 * @param Color newColor
	 */
	public void setColor(Color newColor){
	    this.color = newColor;
	}
	// @return childrenNodes of node
	public Node[] getChildren(){
	    return this.children;
	}
	// @return width of node
	public int getWidth(){
	    return this.width;
	}
	// @return height of node
	public int getHeight(){
	    return this.height;
	}
	
        
    }// Node class--end
    
    
    //QuadTree constructor():
    public QuadTree(String filename,boolean compression, boolean edgeDetection, boolean quadtreeOutline, String output) throws FileNotFoundException, IOException{
	//read image
	ImgReader img = new ImgReader(filename);
	//initialize instance variables
	this.compressionLevel = 0;
	this.height= img.getHeight();
	this.width= img.getWidth();
	this.image = new Color[height][width];
	this.grid = quadtreeOutline;
	//apply compression:
	if (compression){
	    int index = Integer.parseInt(output.substring(output.length()-5,output.length()-4));
	    this.threshold = this.thresholdArray[index-1];
	    this.root = compressRegion(img.getArray(), 0,0,height,width);
	    this.reconstructImage();
	    this.compressionLevel = this.compressionLevel/(height*width);
	    if(grid) gridDraw(this.image, new Color(0,0,0));
	    ImgWriter finall = new ImgWriter(output, image);
	    }
	this.array = new Color[height][width];
	//apply edgeDetection
	if (edgeDetection){
	    this.root = compressRegion(img.getArray(), 0,0,height,width);
	    this.reconstructImage();
	    this.array = ImgReader.edgeDetect(image, width, height,0,0);
	    if(grid) gridDraw(this.array, new Color(255,255,255));
	    ImgWriter finall = new ImgWriter(output, array);
	    }
    }
    /* compresses a region
     * @param img pixel-array
     * @param i starting width index
     * @param j starting height index
     * @param h height of region
     * @param w width of region
     * @return  node
     */
    private Node compressRegion(Color[][] img, int i, int j, int h, int w){
	Node node= new Node(j, i, h, w); //node representing region
	Color c =  this.getNodeColor(img,i,j,h,w);
	//if it is a single pixel:
	if(h==1 && w==1){
	    node.setColor(img[i][j]);
	    
	}
	else if (h==1 || w==1){ //series of horizontal/vertical pixels
	    if(h==1){
		if(w <= 4 ){
		    // cannot be divided to 4 pixels
		    for(int k=0; k<w; ++k){ // divide to w pixels
			node.getChildren()[k] = compressRegion(img, i, j+k,h,1);
		    }
		}
		else{
		    int W = w/2;
		    node.getChildren()[0] = compressRegion(img,i,j,1,W);
		    node.getChildren()[1] = compressRegion(img,i,j+W,1,w-W);
		}
	    }
	    else{
		if( h<=4 ){
		    for(int k=0; k<h; ++k){
			node.getChildren()[k] = compressRegion(img,i+k,j,1,w);
		    }
		}
		else{
		    int H=h/2;
		    node.getChildren()[0] = compressRegion(img,i,j,H,1);
		    node.getChildren()[1] = compressRegion(img,i+H,j,h-H,1);
		}
	    }
	    node.setColor(node.averageColorChildren());
	}
	//if colors corresponds to threshold
	else if (c!= null){
	    node.setColor(c);
	   }
	//split into four other nodes
	else{
	    int H= h/2;
	    int W= w/2;
	    node.getChildren()[0] = compressRegion(img,i,j+W,H,w-W);
	    node.getChildren()[1] = compressRegion(img,i,j,H,W);
	    node.getChildren()[2] = compressRegion(img,i+H,j,h-H,W);
	    node.getChildren()[3] = compressRegion(img,i+H,j+W,h-H,w-W);
	    node.setColor(node.averageColorChildren());
	}
	return node;
    }
    /* returns average color if less than threshold by computing MSE
     * @param img pixel array
     * @param i starting width index 
     * @param j starting height index 
     * @param h height of node
     * @param w width of node
     * @return  RGB color of node if MSE less than threshold
     */
    private Color getNodeColor(Color[][] img, int i, int j, int h,int w){
	ArrayList<Color> colors =new ArrayList<>();
	double r=0.0;
	double g=0.0;
	double b=0.0;
	int count=0;
	//compute average color but store individual colors
	for(int k=0; k<h; k++){
	    for(int l=0; l<w;l++){
		count++;
		Color c= img[k+i][l+j];
		colors.add(c);
		r+=c.getRed();
		g+=c.getGreen();
		b+=c.getBlue();
	    }
	}
	double avgR = r/count;
	double avgG = g/count;
	double avgB = b/count;
	double MSE =0.0;
	// compute MEAN SQUARED ERROR
	for(int k=0; k< colors.size();k++){
	    int cRed = colors.get(k).getRed();
	    int cGreen = colors.get(k).getGreen();
	    int cBlue = colors.get(k).getBlue();
	    MSE += Math.pow(cRed-avgR,2)+ Math.pow(cGreen-avgG,2)+ Math.pow(cBlue-avgB,2);
	}
	//return color only if MSE less than threshold
	return (MSE < this.threshold)? new Color((int)avgR,(int)avgG,(int)avgB):null;
    }
    //reconstructs image from pixel array by traversing tree recursively 
    public void reconstructImage(){
	traverse(this.root);
    }
    //helper method to traverse tree recursively
    private void traverse(Node node){
	if (!node.isLeaf()){
	    for (int i=0; i<4;i++){
		if (node.getChildren()[i] != null) traverse (node.getChildren()[i]);
	    }
	}
	else{
	    this.compressionLevel++;
	    for(int i=node.getY(); i< (node.getY()+node.getHeight()); i++){
		   for(int j=node.getX(); j<(node.getX()+node.getWidth()); j++){
		       if(node.getColor()!=null) this.image[i][j] = node.getColor();
		   }
	    }
	}
    }//traverse--end
     /* displays pixel grid by recursion
      * @param arr pixel array
      * @param color of grid
      * @return  void
      */
    public void gridDraw(Color[][] arr,Color color){
	grid(this.root,arr,color);
    }
    /* helper-method displays pixel grid by recursion
     * @param node
     * @param arr pixel array
     * @param color of grid
     * @return  void
     */
    private void grid (Node node,Color[][] arr, Color color){
	if (!node.isLeaf()){
	    for (int i=0; i<4;i++){
		if (node.getChildren()[i] != null) grid(node.getChildren()[i], arr,color);
	    }
	}
	// draw grid for children nodes :
	else{
	    //draw horizontal bound 1
	    for(int k=node.getX();k<node.getX()+node.getWidth();k++){
		int m=node.getY()-1;
		   if ( m == -1) m=0;
		   if(k>=0 && m>=0 && k<this.width && m<this.height)
		   arr[m][k] = color;
	    }
	    //draw vertical bound 1
	    for(int k=node.getY();k<node.getY()+node.getHeight();k++){
		int m=node.getX()-1;
		  if ( m == -1) m=0;
		  
	          if(k>=0 && m>=0 && k<this.height && m<this.width)
		  arr[k][m] = color;
	    }
	    //draw horizontal bound 2
	    for(int k=node.getX();k<node.getX()+node.getWidth();k++){
		int m=node.getY()+node.getHeight()-1;
		if(k>=0 && m>=0 && k<this.width && m<this.height)
		    arr[m][k] = color;
	    }
	    //draw vertical bound 2
	    for(int k=node.getY();k<node.getY()+node.getHeight();k++){
		int m=node.getX()+node.getWidth()-1;
		if(k>=0 && m>=0 && k<this.height && m<this.width)
		    arr[k][m] = color;
	    }
	}
    }
}//QuadTree class--end
    
	
  
    
