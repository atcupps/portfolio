package Function;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import core.Game;



public class Slider

{
	
	float x;
	float y;
	float altX, altY;
	float height;
	float width;
	Image button = null;
	boolean clicked, pressed;
	int time;
	Image back = null;
	int categories;
	float split;
	float jumpSpot;
	int chosen;
	
	public Slider(float x, float y, float width, float height, Image button, Image back, int categories)
	{
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.button = button;
		clicked = false;
		pressed = false;
		time = 0;
		this.back = back;
		this.categories = categories;
		jumpSpot = 0;
		chosen = 1;
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		// This code happens when you enter a game state for the *first time.*
		gc.setShowFPS(true);
		
		button.setFilter(Image.FILTER_NEAREST);
		back.setFilter(Image.FILTER_NEAREST);
		
		altX = 0;
		altY = 0;
		split = 0;
		chosen = 1;
	
	}

	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		g.setBackground(new Color(0,50,0));
		back.draw(x, y, width, height);
		button.draw(x + altX, y - height, width/categories, height*3);
		
		
//		g.drawString(Mouse.getY() + "", 50, 50);
//		g.drawString(""+y, 100, 50);

//		for (int i = 0; i < categories; i++) {
//			g.drawRect(x + split * i, y, split/2, 50);
//		}
		
//		g.drawString(" " + chosen, x, y + height + 50);
	}
	


	public void update(GameContainer gc) throws SlickException
	{	
	
		split = width/categories;
		
		if (!pressed) {
			
			for (int i = 0; i < categories; i++) {
				if (x + altX > x + split*i && x + altX < x + split*(i+1)) {
					jumpSpot = split*i + (width/categories)/2;
					chosen = i+1;
				}
			}
			
			altX = jumpSpot - (width/categories)/2;
			
		}
		
		if(gc.getInput().isMouseButtonDown(0))   
		{

			
			if (Mouse.getX() > x + altX - (width/categories)/2 && Mouse.getX() < (x + altX) + width/categories && (gc.getHeight()-Mouse.getY()) > y - (height*1.5) && (gc.getHeight()-Mouse.getY()) < y + (height*1.5)) {
					pressed = true; 
			}
		    
			
		}
		
		if (!gc.getInput().isMouseButtonDown(0)) {
			pressed = false;
		}
		

		
//		if (Math.abs((x - altX) - (x - (button.getWidth() * 4))) < 20) {
//			altX = (width*2) - (width/4) ;
//		}
//		if (Math.abs((x - altX) - ((x + button.getWidth() / 2) - (button.getWidth() * 5) + width * 4)) < 20) {
//			altX = -(width*2) - (width/8) ;
//		}
		
		
		
		if (pressed && Mouse.getX() >= x - (width/categories)  && Mouse.getX() <= x + width) {
			altX = Math.abs(x-Mouse.getX()) - (width/categories)/2;

		}
		
		
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		
	}

	public void leave(GameContainer gc, StateBasedGame sbg) 
	{
		
	}
	
	public float categorySelected() {
		return chosen;
	}
	
	public boolean click() {
		return clicked;
		
	}
	
	
//	public boolean selected() {
//		if(Mouse.isButtonDown(0)) {
//			
//			if (Mouse.getX() > x && Mouse.getX() < (x + width) && Mouse.getY() > y && Mouse.getY() < (y + height)) {
//				System.out.println("segeg");
//				return true;	
//			}
//		}
//		return false;
//	}
//	
//	public void mousePressed(int button)
//	{
//		if (button == Input.MOUSE_LEFT_BUTTON) {
//			
//			System.out.println(Mouse.getX());
//			System.out.println(Mouse.getY());
//			System.out.println(x-altX);
//			System.out.println(y);
//			if (Mouse.getX() > x - altX && Mouse.getX() < x - altX + (width/4) && Mouse.getY() > y && Mouse.getY() < y + height) {
//				System.out.println("segeg");
//				pressed = true;	
//			}
//			
//		}
//	}
	

}