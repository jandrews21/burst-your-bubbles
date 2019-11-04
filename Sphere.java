import java.util.Random;
import javax.swing.plaf.basic.BasicScrollPaneUI.ViewportChangeHandler;
import java.awt.Color;
import java.awt.Graphics;

public class Sphere implements Interactable{
	public double velocityX; //the speed at which the sphere is traveling horizontally
	public double velocityY;// the speed at which the sphere is traveling vertically
	public double centerX;// the x coordinate of the center of the sphere
	public double centerY; // the y coordinate of the center of the sphere
	public double radius; // the radius of each sphere
	public final int initHealth; // the spheres initial health, this value is added to the score once the sphere is destroyed
	public int health; // the  sphere's health, slowly depletes as it's being shot
	public String healthDisplay; // the visual representation of the health
	public Color color; // the color of the sphere
////////////////////////////////////////////////////////////////////////////////////////////
//The constructor of Sphere. The velocities, radii, health, and colors are all randomized.//
////////////////////////////////////////////////////////////////////////////////////////////
	public Sphere(int k, int j){

		velocityX = rand.nextInt(4)-2;
		velocityY = rand.nextInt(3)+2;
		centerX = k;
		centerY = j;
		radius = rand.nextInt(10)+29;
		initHealth = rand.nextInt(95)+5;
		health = initHealth;
		healthDisplay = " "+ health +" ";
		color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
	}
///////////////////////////////////////////////////////////////////////////////////////////////////
//This method's purpose is to decrease the sphere's health by the amount of damage the bullets do//
///////////////////////////////////////////////////////////////////////////////////////////////////
	public void decreaseHealth(int damage){
		health = health - damage;
	}
////////////////////////////////////////////////////////////
//This method switches the sign of the horizontal velocity//
////////////////////////////////////////////////////////////
	public void switchSignsX(){
		velocityX = -velocityX;
	}
///////////////////////////////////////////////////////////
//This method switches the sign of the vertical velocity//
/////////////////////////////////////////////////////////
	public void switchSignsY(){
		velocityY = -velocityY;
	}
/////////////////////////////////////////////////////////////////////////////
//This method adds the velocities of the sphere to their respective centers
//It also continuous to display the sphere's current health and makes sure
//the sphere bounces off the sides of the wall in the game
/////////////////////////////////////////////////////////////////////////////
	public void update(){
		centerX +=velocityX;
		centerY +=velocityY;
		healthDisplay = ""+ health +"";
		bounce(game);
	}
/////////////////////////////////////////////////////
//This method draws each sphere at a moment in time//
/////////////////////////////////////////////////////
	public void draw (Graphics g){
		if(health>0){
			Color c = g.getColor();
			g.setColor(color);
			g.fillOval((int)centerX-(int)radius,(int)centerY-(int)radius,(int)radius*2,(int)radius*2);
			g.setColor(c);
			g.drawString(healthDisplay,(int)centerX,(int)centerY);
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////
//This method tells a sphere what to do if it bounces off each of the four walls of Main.
//It switches velocityX for collisions of the left and right wall.
//It swtiches velocityY for collisons of the top  and bottom wall
/////////////////////////////////////////////////////////////////////////////////////////
	private void bounce(Main game){
		if (centerX-radius<0){
			switchSignsX();
			centerX = radius;
		}
		if (centerX+radius>game.width) {
			switchSignsX();
			centerX = game.width - radius;
		}
		if (centerY-radius<0) {
			switchSignsY();
			centerY = radius;
		}

		if (centerY+radius>game.height) {
			switchSignsY();
			centerY = game.height - radius;
		}
	}
//////////////////////////////////////////////////


	Main game;

	Random rand = new Random();
}

class Bullet extends Sphere{ // Bullets are what the player produces, but they're also types of spheres
	int damage; // this is the set damage that bullets do to spheres, it's set blow
	public Bullet(int k, int j){
		super(k,j);
		centerX = k;
		centerY = j;
		velocityX = 0;
		velocityY = -10;
		radius = 5;
		health = 1;
		healthDisplay = "";
		color = Color.WHITE;
		damage = 1;
	}
	public void update(){
		centerX +=velocityX;
		centerY +=velocityY;
	}
}