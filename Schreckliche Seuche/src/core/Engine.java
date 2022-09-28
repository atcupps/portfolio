
package core;

import java.awt.Toolkit;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class Engine extends StateBasedGame 
{
	public final static int RESOLUTION_X = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public final static int RESOLUTION_Y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public final static int FRAMES_PER_SECOND = 60;
	

    public static final int introScreen = 0;

    public static final int settingScreen = 1;
    public static final int simulation = 2;

    public static float popDensity = 3;
    public static float rNaught = 1.0f;

	public Engine(String name) 
	{
		super(name);
		
		
		this.addState(new Intro(introScreen));
		this.addState(new Settings(settingScreen));
//		this.addState(new Simulation(simulation));
		
		
		
	}

	public void initStatesList(GameContainer gc) throws SlickException 
	{
		
		this.getState(introScreen).init(gc, this);
		this.getState(settingScreen).init(gc, this);
//		this.getState(simulation).init(gc,  this);
		
	}

	public static void main(String[] args) 
	{
		try {
			AppGameContainer appgc = new AppGameContainer(new Engine("Schreckliche Seuche"));
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		
			appgc.setDisplayMode(RESOLUTION_X, RESOLUTION_Y, false);
			appgc.setTargetFrameRate(FRAMES_PER_SECOND);
			appgc.start();
			appgc.setVSync(true);

		} catch (SlickException e) 
		{
			e.printStackTrace();
		}
	}
}