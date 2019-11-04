import java.awt.Graphics;

///////////////////////////////////////////////////
//An interactable object can be drawn and updated//
///////////////////////////////////////////////////
public interface Interactable{
    public void update();
    public void draw(Graphics g);
}