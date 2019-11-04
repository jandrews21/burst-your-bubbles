
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Vector;;

public class Player implements Interactable{
    private int health; // the amount of time the player can be in contact with a sphere without dying
    private String healthString; // visual representation of health
    private boolean hasBeenHit = false; // indicator whether player is being hit by a sphere or not
    private double centerX; // x coordinate of center of player
  	private double centerY; // y coordinate of center of player
  	public double velocityX; // how fast the player can move horizontally
    private boolean dead = false; // whether or not the player's health has been depleted
    private double radius; // the distance from the center of player to one of their edges
    public Vector<Bullet> bullets; // the vector of bullets the player can add to
    Main game; // game reference for starting position
    Sphere s; // sphere reference for hit detection method

/////////////////////////////////////////////////////////////////////////////////////////////
//The constructor for player. Starts player off in the middle of the screen with 100 health//
/////////////////////////////////////////////////////////////////////////////////////////////
    public Player(){
      bullets = new Vector<Bullet>();
      health = 100;
      healthString = ""+health+"";
      centerX = game.width/2 + radius;
      centerY = game.height - radius;
      radius = 50;
      velocityX = 0;
    }
/////////////////////////////////////////////////////
//This method draws Player at a moment in time///////
/////////////////////////////////////////////////////
    public void draw(Graphics g){
      g.setColor(Color.BLUE);
      g.fillOval((int)centerX-(int)radius,(int)centerY-(int)radius*2,(int)radius*2,(int)radius*2);
      g.setColor(Color.WHITE);
      g.drawString(healthString,(int)centerX,(int)centerY-(int)radius);
    }
/////////////////////////////////////////////////////////////////////////////
//This method adds the horizontal of the sphere to centerX
//It also continuous to display the Player's current health and makes sure
//Player cannot run off screen
/////////////////////////////////////////////////////////////////////////////
    public void update(){
      if(((centerX-radius) + velocityX < game.width - radius*2) && ((centerX-radius)+ velocityX >0))
        centerX += velocityX;
      healthString = ""+health+"";
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
//This method uses the distance formula to determine if the Player is in contact with an enemy sphere
//Returns true if yes, false if no
/////////////////////////////////////////////////////////////////////////////////////////////////////
   public boolean hitDetection (Sphere s){
     double d = Math.sqrt(Math.pow((centerX-s.centerX),2)+Math.pow(centerY-s.centerY,2));
     double radSum = radius + s.radius;
     if (d<radSum) {
       hasBeenHit = true;
     }
      return hasBeenHit;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This method causes player to add one bullet to the Vector of bullets. Each bullet will be updated by Main.//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void shoot(){
      bullets.add(new Bullet((int)centerX, (int)centerY+(int)radius));
    }
////////////////////////////////////////////////////////////////////////////////////////////
//This method will steadily decrease the player's health if they're in contact with a sphere
////////////////////////////////////////////////////////////////////////////////////////////
    public void decreaseHealth(boolean hit){
      if (hit) {
        --health;
      }
      hasBeenHit = false;
    }
///////////////////////////////////////////////////////////////////////////////
//This method determines whether or not the player has lost all of their health
//Returns false if no, true if yes
//If yes, the player can no longer move
////////////////////////////////////////////////////////////////////////////////
    public boolean isDead(){
      if (health<=0) {
        dead = true;
        velocityX = 0;
      }
      return dead;
    }
///////////////////////////////////////////

}
