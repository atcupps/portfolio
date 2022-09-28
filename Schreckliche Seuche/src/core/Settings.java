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



public class Settings extends BasicGameState 

{
	int id;
	
	Image background = null;
	
	Image hello = null;
	
	Image sliderBack = null;
	Image sliderClick = null;
	
	Button button;
	
	Slider popDensity;
	Slider rNaught;
	Slider time;
	Slider death;
	Slider sickness;
	Slider size;
	Slider workplace;
	Slider incubation;
	
	
	public static float rNaughtF, popDensityF, timeF, deathF, sicknessF, sizeF, workplaceF, incubationF;
	
	
	public ArrayList<Slider> sliders;
	
	public ArrayList<Button> buttons;
	
	Settings(int id) 
	{
		this.id = id;
		buttons = new ArrayList<Button>();
		sliders = new ArrayList<Slider>();
		
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		// This code happens when you enter a game state for the *first time.*
		gc.setShowFPS(true);
		
		hello = new Image("res/launchButton.png");
		hello.setFilter(Image.FILTER_NEAREST);
		
		background = new Image("res/SettingPage3.png");
		background.setFilter(Image.FILTER_NEAREST);
		
		button = new Button(50f,50f,200f,100f, hello);
		
		
		sliderBack = new Image("res/slider.png");
		sliderBack.setFilter(Image.FILTER_NEAREST);
		
		sliderClick = new Image("res/blue.png");
		sliderClick.setFilter(Image.FILTER_NEAREST);
		
		popDensity = new Slider(75f,475f,700f,15f, sliderClick, sliderBack, 6);
		
		rNaught = new Slider(1150f,725f,700f,15f, sliderClick, sliderBack, 50);
		
		time = new Slider(1150f, 475f, 700f, 15f, sliderClick, sliderBack, 41);
		
		death = new Slider(1150f,900f,700f,15f, sliderClick, sliderBack, 100);
		
		size = new Slider(75f, 325f, 700f, 15f, sliderClick, sliderBack, 20);
		
		sickness = new Slider(1150f, 325f, 700f, 15f, sliderClick, sliderBack, 28);
		
		workplace = new Slider(75f, 675f, 700f, 15f, sliderClick, sliderBack, 30);
		
		incubation = new Slider(75f, 900f, 700f, 15f, sliderClick, sliderBack, 10);
		
		buttons.add(button);
		sliders.add(popDensity);
		sliders.add(rNaught);
		sliders.add(time);
		sliders.add(death);
		sliders.add(sickness);
		sliders.add(size);
		sliders.add(workplace);
		sliders.add(incubation);
		
		popDensityF = 3;
		rNaughtF = 0;
		timeF = 7;
		deathF = 0;
		sizeF = 0;
		incubationF = 0;
		workplaceF = 0;
		
		
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		background.draw(0,0,gc.getWidth(),gc.getHeight());
		
		for(Button b : buttons) {
			b.render(gc, g);
		}
		for(Slider b : sliders) {
			b.render(gc, g);
		}
		
		g.setColor(new Color(200,200,200));
		
		if (button.click()) {
			sbg.addState(new Simulation(2));
			sbg.getState(2).init(gc, sbg);
			sbg.enterState(2);
		}
		g.setColor(new Color(0,0,0));
		g.drawString("" + popDensityF, 175, 500);
		g.drawString("" + rNaughtF, 1175, 750);
		g.drawString("" + timeF, 1175, 500);
		g.drawString("" + deathF, 1175, 925);
		g.drawString("" + sicknessF, 1175, 350);
		g.drawString("" + sizeF, 100, 350);
		g.drawString("" + workplaceF, 100, 700);
		g.drawString("" + incubationF, 100, 925);
	
	}
	


	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{	
		for(Button b : buttons) {
			b.update(gc);
		}
		for(Slider b : sliders) {
			b.update(gc);
		}
		
		popDensityF = popDensity.categorySelected();
		rNaughtF = rNaught.categorySelected()/5;
		timeF = time.categorySelected() + 7;
		deathF = death.categorySelected();
		sicknessF = sickness.categorySelected() + 2;
		sizeF = size.categorySelected() * 50;
		workplaceF = workplace.categorySelected();
		incubationF = incubation.categorySelected();
		
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
	
	public static float getR0() { return rNaughtF; }
	public static float getPopDensity() { return popDensityF; }
	

	// Returns the ID code for this game state
	public int getID() 
	{
		return id;
	}
}