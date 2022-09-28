package core;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Function.Button;
import core.Tile.TileType;



public class Simulation extends BasicGameState 

{
	int id;
	
	public static int time, dataScaler;
	public static float days;
	public static boolean isDay;
	

	public static final float R_0 = Settings.rNaughtF;
	public static final float PERIOD = Settings.sicknessF;
	public static final float I_PER_C = R_0 / PERIOD / 7;
	public static final float INCUBATION = Settings.incubationF;
	public static final float D_PER_I = Settings.deathF / 100;
	public static final float POP_DENSITY = Settings.popDensityF;
	public static final int MAX_WORKERS = (int) Settings.workplaceF;
	public static final int TICK = (int) Settings.timeF * 60 / 24;
	public static final int cityX = 650;
	public static final int cityY = 240;
	public static final int cityW = 1200;
	public static final int cityH = 600;
	public static final int numTiles = 800;
	public static final int tileY = (int)(Math.sqrt(numTiles / 2));
	public static final int tileX = 2 * tileY;
	public static final float tileW = cityW / tileX;
	public static final float tileH = cityH / tileY;
	
	private boolean paused = false;
	private MapType mapType;
	private static Tile[][] tiles;
	private ArrayList<Person> people;
	private ArrayList<Tile> homes;
	private ArrayList<Tile> workplaces;
	private ArrayList<Tile> streets;
	
	private ArrayList<Integer> totalInfected, currentlyInfected, totalDeaths;
	
	Simulation(int id) 
	{
		this.id = id;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		// This code happens when you enter a game state for the *first time.*
		gc.setShowFPS(true);
//		R_0 = Settings.rNaughtF;
//		PERIOD = 10;
//		I_PER_C = R_0 / PERIOD / 7;
//		D_PER_I = 0.2f;
//		INCUBATION = 3;
		time = 0;
		days = 1;
		isDay = false;
		
		homes = new ArrayList<Tile>();
		workplaces = new ArrayList<Tile>();
		streets = new ArrayList<Tile>(); 
		
		mapType = MapType.GRID;
		tiles = new Tile[tileX][tileY];
		TileType t = Tile.TileType.STREET;
		for (int i = 0; i < tileX; i++) {
			for (int b = 0; b < tileY; b++) {
				if (i % 2 == 0 || b % 2 == 0) {
					t = Tile.TileType.STREET;
				} else {
					if (Math.random() * 10 > 8 - POP_DENSITY / 2) {
						t = Tile.TileType.WORKPLACE;
					} else {
						t = Tile.TileType.HOME;
					}
				}
				tiles[i][b] = new Tile(cityX + (tileW * i), cityY + (tileH * b), tileW, tileH, t, i, b);
				if (t.equals(Tile.TileType.STREET)) { streets.add(tiles[i][b]); }
				if (t.equals(Tile.TileType.WORKPLACE)) { workplaces.add(tiles[i][b]); }	
				if (t.equals(Tile.TileType.HOME)) { homes.add(tiles[i][b]); }
			}
		}
		
		people = new ArrayList<Person>();
		
		for (int i = 0; i < tileX; i++) {
			for (int b = 0; b < tileY; b++) {
				Tile tile = tiles[i][b];
				if (tile.getType().equals(Tile.TileType.HOME)) {
					while (tile.getResidents().size() < tile.getMaxPeople()) {
						Person p = new Person(tile);
						people.add(p);
						tile.addResident(p);
						tile.addPerson(p);
//						for (int g = 0; i < 5; i++) {
//							Tile workplace = workplaces.get((int)(Math.random() * workplaces.size()));
//							if (workplace.getWorkers().size() < workplace.getMaxPeople()) {
//								p.addWorkplace(workplace);
//								workplace.addWorker(p);
//								break;
//							}
//						}
					}
				}
			}
		}
		
		for (Tile w : workplaces) {
			for (int i = 0; i < people.size(); i++) {
				Person p = people.get((int)(Math.random() * people.size()));
				if (w.getWorkers().size() < w.getMaxPeople()) {
					if (!p.hasWorkplace() && Math.sqrt(Math.pow(p.getHome().getPosX() - w.getPosX(), 2) + Math.pow(p.getHome().getPosY() - w.getPosY(), 2)) < 10) {
						p.addWorkplace(w);
						w.addWorker(p);
					}
				}
				else break;
			}
		}
		
		people.get((int)(Math.random() * people.size())).infect();
	
		totalInfected = new ArrayList<Integer>();
		currentlyInfected = new ArrayList<Integer>();
		totalDeaths = new ArrayList<Integer>();
		
		totalInfected.add(0);
		currentlyInfected.add(0);
		totalDeaths.add(0);
		
		totalInfected.add(1);
		currentlyInfected.add(1);
		totalDeaths.add(0);
		
		dataScaler = 1;
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.setBackground(new Color(45,45,45));		
		
		g.setColor(new Color(255,255,255));
		g.drawString("Day " + days + " at " + (time / (TICK)) + ":00 " + "    R_naught: " + R_0 + "    Incubation time: " + INCUBATION + " days    Infection period: " + PERIOD + "    Death rate: " + D_PER_I + "                                  Press spacebar to pause", 30, 90);
		g.setColor(new Color(255,255,255));
		g.drawString("Susceptible: Not yet infected by disease", 30, 105);
		g.setColor(new Color(238, 255, 130));
		g.drawString("Infectious Asymptomatic: No current symptoms, spreads disease at a lower rate", 30, 120);
		g.setColor(new Color(255, 130, 130));
		g.drawString("Infectious Symptomatic: Clinical symptoms, spreads disease at a higher rate", 30, 135);
		g.setColor(new Color(174, 130, 255));
		g.drawString("Dead: Killed by disease", 30, 150);
		g.setColor(new Color(28, 186, 70));
		g.drawString("Recovered: Survived disease infection, cannot be reinfected", 30, 165);
		
		g.setColor(new Color(66, 67, 82));
		g.drawString("Homes", 1300, 165);
		g.setColor(new Color(82, 66, 76));
		g.drawString("Workplaces/Businesses", 1300, 180);
		
		
		for (int i = 0; i < tileX; i++) {
			for (int b = 0; b < tileY; b++) {
				tiles[i][b].render(g);
			}
		}
		for (Person p : people) {
			p.drawPerson(g);
		}

		g.setColor(new Color(238, 255, 130));
		String totalInf = "Total Infections: " + totalInfected.get(totalInfected.size() - 1);
		g.drawString(totalInf, 60 + ((600 - 60) / 2) - (10 * (totalInf.length() / 2)), 200);
		g.drawLine(60, 200, 60, 480);
		g.drawLine(60, 480, 600, 480);
		
		for (int i = 0; i < totalInfected.size(); i ++) {
			g.fillOval(60 + ((540 * i) / totalInfected.size()) - 3, 480 - (260 * totalInfected.get(i)) / people.size() - 3, 6, 6);
		}
		
		g.setColor(new Color(255, 130, 130));
		String curInf = "Active Infections: " + currentlyInfected.get(currentlyInfected.size() - 1);
		g.drawString(curInf, 60 + ((600 - 60) / 2) - (10 * (curInf.length() / 2)), 490);
		g.drawLine(60, 505, 60, 765);
		g.drawLine(60, 765, 600, 765);
		for (int i = 0; i < currentlyInfected.size(); i ++) {
			g.fillOval(60 + ((540 * i) / currentlyInfected.size()) - 3, 765 - (260 * currentlyInfected.get(i)) / people.size() - 3, 6, 6);
		}
		
		g.setColor(new Color(174, 130, 255));
		String death = "Total Deaths: " + totalDeaths.get(totalDeaths.size() - 1);
		g.drawString(death, 60 + ((600 - 60) / 2) - (10 * (death.length() / 2)), 790);
		g.drawLine(60, 790, 60, 1070);
		g.drawLine(60, 1070, 600, 1070);
		for (int i = 0; i < totalDeaths.size(); i++) {
			g.fillOval(60 + ((540 * i) / totalDeaths.size()) - 3, 1070 - (260 * totalDeaths.get(i)) / people.size() - 3, 6, 6);
		}
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{	
		
//		R_0 = Settings.rNaughtF;
//		PERIOD = 10;
//		I_PER_C = R_0 / PERIOD / 7;
//		D_PER_I = 0.2f;
//		INCUBATION = 3;
		
		
		if (!paused) {
			time++;
			for (Person p : people) {
				p.updatePosition();
			}
			if (time % (TICK / 4) == 0) {
				for (Person p : people) {
					if (!p.getStatus().equals(Person.Status.DEAD)) {
						p.setNewTile();
					}
				}
			}
			if (time % TICK == 0) {
				for (int i = 0; i < tileX; i++) {
					for (int b = 0; b < tileY; b++) {
						tiles[i][b].updateInfectionsInTile();
					}
				}
				for (Person p : people) {
					if (!p.getStatus().equals(Person.Status.DEAD)) {
						p.update();
					}
				}
				int totalInf = 0;
				int curInf = 0;
				int totalDead = 0;
				for (Person p : people) {
					if (!p.getStatus().equals(Person.Status.SUSCEPTIBLE)) totalInf++;
					if (p.getStatus().equals(Person.Status.INFECTIOUS) || p.getStatus().equals(Person.Status.SYMPTOMATIC)) curInf++;
					if (p.getStatus().equals(Person.Status.DEAD)) totalDead++;
				}
				totalInfected.add(totalInf);
				currentlyInfected.add(curInf);
				totalDeaths.add(totalDead);
			}
			
			if (time % (24 * TICK) == 0) {
				days++;
				time = 0;
			}
		}
	}

	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException 
	{
		
	}

	public void leave(GameContainer gc, StateBasedGame sbg) 
	{
		
	} 
	
	public void keyPressed(int key, char c) {
		if (c == ' ') {
			paused = paused ? false : true;
		}
	}
	
	public enum MapType {
		GRID,
		CITY,
		SUBURBS,
		RURAL,
		MIXED
	}
	
	public enum Road {
		MAIN,
		SECONDARY,
		LOCAL
	}

	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	public static int getTime() { return time; }
	public static Tile getTile(int i, int b) { return tiles[i][b]; }
	
	// Returns the ID code for this game state
	public int getID() 
	{
		return id;
	}
}
