import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Font;
import java.awt.image.*;
import java.text.AttributedString;
import javax.imageio.*;
import javax.swing.*;
import java.io.File;


public class Main extends JPanel implements KeyListener {
	class RemindTask extends TimerTask {
		public void run() {
			if(isPlaying && isShooting)
				player.shoot();
			timer.schedule(new RemindTask(),100);
			}
	}

	public static final int width = 1024; //width of screen
	public static final int height = 768; //height of scree
	public static final int FPS = 120;// number of Frames per second the game shall run on
	public Player player; //the representation of the player playing the game
	public Timer timer = new Timer(); // a Timer that time certain events, such as a brief pause after clearing all spheres
	public Vector<Sphere> spheres;//a Vector all spheres shall be contained in to be updated
	public int score; //the overall score of the game
	public int level;// the level the player is on
	private String scoreString;// the visual representation of the score
	public String levelString;//the visual representation of the level
	public String message;// this will display a message tell the player if they won or lost
	int count = 0;
	boolean isPlaying = true; //determines whether the game is being played or if it's on the start or game over screen
	boolean isShooting = true;// determines wheter the player is shooting or not
	public BufferedImage desert; // desert background
	public BufferedImage city;// city background
	public BufferedImage ocean;// ocean background 
	public BufferedImage space;// space background
	public BufferedImage title;// title screen (from Spongebob!)

//////////////////////////////////////////////////////////////////////////////
//The constructor for Main. Sets up everything the player will need to play//
/////////////////////////////////////////////////////////////////////////////
	public Main() { 
		addKeyListener(this); //allows key inputs to be recorded
		spheres = new Vector<Sphere>();  
		player = new Player();
		scoreString = "Score: " + score + "";
		levelString = "";
		message = "Press space to start";
		Thread mainThread = new Thread(new Runner());
		mainThread.start();
		score = 0;
		level = 0;
		loadBackgrounds();
}
//////////////////////////////////////////////////////////////////////////////////
//This method loads all the backgrounds from our image files that we'll provide.//
//////////////////////////////////////////////////////////////////////////////////
	public void loadBackgrounds(){
		try{
		desert = ImageIO.read(new File("NegevNightSky1.jpg"));
		city = ImageIO.read(new File("NY.jpg"));
		ocean = ImageIO.read(new File("ocean.jpg"));
		space = ImageIO.read(new File("space.jpg"));
		title = ImageIO.read(new File("BurstYourBubble.png"));
		}
		catch(Exception e)
		{System.out.println("I will be fixed later");}
	} 
//////////////////////////////////////////////////////////////////////////////////
//This method draws all the components of game (Player,spheres,bullets,messages)//
//This method is also in charge of changing the background of the game as it plays//
//////////////////////////////////////////////////////////////////////////////////
	public void paintComponent(Graphics g) {
		if (isPlaying&&level==0) {
			g.drawImage(title, 0, 0,width,height,this);
		}
		else if (level%2!=0) {
			if (level%3==0) {
				g.drawImage(desert, 0, 0, width, height, this);
			}
			else{
				g.drawImage(city, 0, 0, width, height, this);
			}
		}
		else if (level%4==0) {
			g.drawImage(space, 0, 0, width, height, this);
		}
		else{
			g.drawImage(ocean, 0, 0, width, height, this);
		}
		for (Sphere spr : spheres) //iterates through all the spheres in the vector to draw them
			spr.draw(g);
		for (Bullet bull : player.bullets) // iterates through all the bullets in the vector to draw them
			bull.draw(g);
		g.setColor(Color.BLUE);
		player.draw(g);
		g.setColor(Color.YELLOW);
		g.drawString(scoreString, width - 100, 100);
		g.setColor(Color.WHITE);
		g.drawString(levelString, (width/2)-75, (height/8)+25);
		g.setColor(Color.WHITE);
		g.drawString(message, (width/2)-30, height/8);

		if (player.isDead()) { //if the player loses all their health, the game will draw the Game Over screen
			gameOver(g);
		}
	}


////////////////////////////////////////////////////////
//If the key is released, the player will stop moving.//
////////////////////////////////////////////////////////
	public void keyReleased(KeyEvent e) {
		player.velocityX = 0;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//If the space key is pressed and the game hasn't start yet, this constructor will create new Game components//
//This method basically serves as the start screen.
////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		if (c == ' ' && count ==0)
		{
			spheres = new Vector<Sphere>();
			for (int k = 100; k < 900; k += 300)
				spheres.add(new Sphere(k, 100));
			player = new Player();
			timer.schedule(new RemindTask(),100);
			level = 1;
			score = 0;
			scoreString = "Score: " + score + "";
			count++;
			message = "";
			isPlaying = true;
			repaint();
		}
	}

/////////////////////////////////////////////////////////////////////////////////////
//This method makes sure the keys typed actually are not only read, but registered.//
/////////////////////////////////////////////////////////////////////////////////////
	public void addNotify() {
		super.addNotify();
		requestFocus();
	}
/////////////////////////////////////////////////////////////////////////////////
//This method controls movement. D is left, A is right, and W toggles shooting.//
/////////////////////////////////////////////////////////////////////////////////
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (c == 'd' || c == 'D'){
			if(isPlaying)
				player.velocityX = 10;
		}
		if (c == 'a' || c == 'A'){
			if(isPlaying)
				player.velocityX = -10;
		}
		if ((c == 'W' || c == 'w')){
			isShooting = !isShooting;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This method calls update on the spheres, players, and bullets. Since we've had some interesting errors 
//with ConncurrentModification, we through in some try-catch statements. This method also controls the score display, the 
//collisions of spheres,and the changing of level
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void update() {
		for (Sphere spr : spheres) {
			spr.update();
		}
		for (Bullet bull : player.bullets) {
			try {
				bull.update();
			} catch (Exception e) {}
		}
		player.update();
		try {
			collide();
		} catch (Exception e) {
			System.out.println(e);
		}
		updateScore();
		if (success()) {
			level++;
			levelString = "Prepare for Level " + level + "";
			message = "Bravo!";
			repaint();
		try{
    	Thread.sleep(3000);
		}
		catch(InterruptedException ex){
    	Thread.currentThread().interrupt();
		}
			levelString = "";
			message = "";
			nextLevel();
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This method returns the result of the distance formula between two spheres. Is utilized in collide method//
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public double distFormula(Sphere s1, Sphere s2) {
		return Math.sqrt(Math.pow((s2.centerX - s1.centerX), 2) + Math.pow(s2.centerY - s1.centerY, 2));
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//The collide method checks to see if two spheres are about to collide and switches their velocity values if they are.
//It also determines whether bullets are hitting the spheres and if the player is in contact with a sphere.
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void collide() {
		double d;
		double radSum;
		double dbull;
		double radSumB;
		int count = 0;
		for (Sphere s : spheres) {
			for (int i = 0; i < spheres.size(); ++i) {
				if (count == i) { //skips itself in iterator
					continue;
				} else {
					Sphere sp = spheres.get(i);
					d = distFormula(s, sp);//result of distance formula
					radSum = s.radius + sp.radius; // sum of radii
					if (d < radSum) {
						s.switchSignsX();
						s.switchSignsY();
					}
				}
			}
			++count;
			player.decreaseHealth(player.hitDetection(s));
		}
		for (Bullet b : player.bullets) {
			for (int i = 0; i < spheres.size(); i++) {
				Sphere sp = spheres.get(i);
				dbull = distFormula(b, sp);
				radSumB = b.radius + sp.radius;
				if (dbull < radSumB) {
					sp.decreaseHealth(b.damage);
					b.decreaseHealth(b.damage);
				}
			}
		}
	}
///////////////////////////////////////////////////////////////////////////////
//This method removes dead spheres and bullets that have flown off the screen//
///////////////////////////////////////////////////////////////////////////////
	public void remove() {
		for (int i = 0; i < spheres.size(); ++i) {
			Sphere s = spheres.get(i);
			if (s.health <= 0) {
				spheres.remove(i);
			}
		}
		for (int i = 0; i < player.bullets.size(); i++) {
			Bullet b = player.bullets.get(i);
			if (b.centerY < 0||b.health<0) {
				player.bullets.remove(i);
			}
		}
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This method adds the initial health value of a sphere to the score once it's been killed. It also updates the visual
//represenation of the score
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void updateScore() {
		for (Sphere s : spheres) {
			if (s.health <= 0) {
				score += s.initHealth;
				scoreString = "Score: " + score + "";
			}
		}
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//This method displays the game Over screen and resets the level back to zero. It informs the player to press space to play again.//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void gameOver(Graphics g) {
		String message = "That's a wrap! Press Space to play again.";
		count = 0;
		isPlaying = false;
		g.setColor(Color.RED);
		g.drawString(message, (width / 2) - 50, height / 2);
		level = 0;
	//	score = 0;
	}
/////////////////////////////////////////////////////////////////////////////////////////////
//This method determines if the player has successfully destroyed all the spheres on screen//
/////////////////////////////////////////////////////////////////////////////////////////////
	public boolean success() {
		boolean victory = false;
		if (spheres.size() == 0 && level > 0) {
			victory = true;
		}
		return victory;
	}
////////////////////////////////////////////////////////////////////////////////////////////////
//This method describes what happens when the player progresses to a new level.
//Player's position and health is reset by creating a new instance of player.
//The game adds new spheres up until level 5, where every concurrent level they speed up instead
////////////////////////////////////////////////////////////////////////////////////////////////
private void nextLevel(){
	player = new Player();
	if(level < 5){
		for (int k = 100; k < 900; k += 300/level)
			spheres.add(new Sphere(k, 100));
	}
	else{
		for (int k = 100; k < 900; k += 300/4)
			spheres.add(new Sphere(k, 100));
			for (Sphere spr : spheres){
				spr.velocityX+= level - 4;
				spr.velocityY+= level - 4;
			}
	}
}
///////////////////////////////////////////////////////////////////////////////////////////
//The main method. Creates the frame, and runs update, repaint, and remove simultaneously//
///////////////////////////////////////////////////////////////////////////////////////////
public static void main(String[] args) {
	JFrame frame = new JFrame("Burst Your Bubble");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	Main mainInstance = new Main();
	frame.setContentPane(mainInstance);
	frame.pack();
	frame.setSize(width, height);
	frame.setVisible(true);
}
class Runner implements Runnable {
	public void run() {
		while (true) {
			update();
			repaint();
			remove();
			try {
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {
			}
		}
	}
}
}