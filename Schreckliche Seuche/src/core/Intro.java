package core;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Function.Button;
import Function.Slider;


public class Intro extends BasicGameState 



{
	int id;
	
	Image hello = null;

	Image background = null;

	
	Button button;

	
	public ArrayList<Button> buttons;
	
	
	Intro(int id) 
	{
		this.id = id;
		buttons = new ArrayList<Button>();
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		// This code happens when you enter a game state for the *first time.*
		gc.setShowFPS(true);
		
		background = new Image("res/IntroScreenNEW.png");
		background.setFilter(Image.FILTER_NEAREST);
		
		hello = new Image("res/New Piskel.png");
		hello.setFilter(Image.FILTER_NEAREST);
		
		
		
	
		button = new Button(400f,200f,400f,200f, hello);
		
		buttons.add(button);
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		background.draw(0,0,gc.getWidth(),gc.getHeight());
		
		for(Button b : buttons) {
			b.render(gc, g);
		}
		
		
		
		g.setColor(new Color(200,200,200));
		
		if (button.click()) {
			g.drawRect(300, 300, 400, 400);
			sbg.enterState(1);
		}
		
	}
	


	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{	
		for(Button b : buttons) {
			b.update(gc);
		}
		
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		
	}

	public void leave(GameContainer gc, StateBasedGame sbg) 
	{
		
	}
	
	public void mousePressed(int buttonClick, int mx, int my)
	{
		for(Button b : buttons) {
			b.mousePressed(buttonClick, mx, my);
		}
	}

	// Returns the ID code for this game state
	public int getID() 
	{
		return id;
	}
}