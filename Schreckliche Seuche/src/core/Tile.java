package core;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import core.Tile.TileType;

public class Tile {

	private TileType t;
	private float x, y, w, h;
	private int posX, posY;
	private float maxPeople;
	
	private ArrayList<Person> peopleInTile;
	private ArrayList<Person> workers;
	private ArrayList<Person> residents;
	
	public Tile(float x,float y,float w,float h, TileType t, int posX, int posY) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.t = t;
		this.posX = posX;
		this.posY = posY;
		
		peopleInTile = new ArrayList<Person>();
		
		switch (this.t) {
		case HOME: 
			residents = new ArrayList<Person>();
			maxPeople = Simulation.POP_DENSITY;
			break;
		case WORKPLACE: 
			workers = new ArrayList<Person>();
			maxPeople = Simulation.MAX_WORKERS;
			break;
		case STREET:
			maxPeople = Integer.MAX_VALUE;
		}
	}
	
	public void updateInfectionsInTile() {
		for (Person p : peopleInTile) {
			if (p.getStatus().equals(Person.Status.INFECTIOUS)) {
				for (Person j : peopleInTile) {
					if (j.getStatus().equals(Person.Status.SUSCEPTIBLE)) {
						if (Math.random() * 1 < Simulation.I_PER_C / 2) {
							j.infect();
						}
					}
				}
			} else
			if (p.getStatus().equals(Person.Status.SYMPTOMATIC)) {
				for (Person j : peopleInTile) {
					if (j.getStatus().equals(Person.Status.SUSCEPTIBLE)) {
						if (Math.random() * 1 < Simulation.I_PER_C * 2) {
							j.infect();
						}
					}
				}
			}
		}
		
//		for (Person p : peopleInTile) {
//			if (p.getStatus().equals(Person.Status.INFECTIOUS)) {
//				for (Person j : peopleInTile) {
//					if (!j.equals(p)) {
//						if (t.equals(TileType.STREET)) {
//							if (Math.random() * 100 < Simulation.I_PER_C / 4) {
//								j.infect();
//								System.out.println(this + " had an infection!");
//							} else {
//								System.out.println(this + " had a non-infection.");
//							}
//						} else {
//							if (Math.random() * 100 < Simulation.I_PER_C / 2) {
//								j.infect();System.out.println(this + " had an infection!");
//							} else {
//								System.out.println(this + " had a non-infection.");
//							}
//						}
//					}
//				}
//			}
//			else if (p.getStatus().equals(Person.Status.SYMPTOMATIC)) {
//				for (Person j : peopleInTile) {
//					if (!j.equals(p)) {
//						if (t.equals(TileType.STREET)) {
//							if (!j.equals(p)) {
//								if (Math.random() * 100 < Simulation.I_PER_C / 2) {
//									j.infect();
//								}
//							}
//						} else {
//							if (!j.equals(p)) {
//								if (Math.random() * 100 < Simulation.I_PER_C * 2) {
//									j.infect();
//								}
//							}
//						}
//					}
//				}
//			}
//		}
	}
	
	public void render(Graphics g) {
		if (t.equals(TileType.HOME)) {
			g.setColor(new Color(66, 67, 82));
			g.fillRect(x - w / 4, y - w / 4, w + w / 2, h + h / 2);
		}
		else if (t.equals(TileType.WORKPLACE)) {
			g.setColor(new Color(82, 66, 76));
			g.fillRect(x - w / 4, y - w / 4, w + w / 2, h + h / 2);
		}
//		else {
//			g.setColor(new Color(0,0,0));
//			g.fillRect(x - 25, y - 25, 150, 150);
//		}
				
	}
	
	public void addResident(Person p) {
		residents.add(p);
	}
	
	public void addWorker(Person p) {
		workers.add(p);
	}
	
	public void addPerson(Person p) {
		peopleInTile.add(p);
	}
	
	public void removePerson(Person p) {
		peopleInTile.remove(p);
	}
	
	public TileType getType() { return t; }
	public float getMaxPeople() { return maxPeople; }
	public TileType getTileType() { return t; }
	public ArrayList<Person> getResidents() { return residents; }
	public ArrayList<Person> getWorkers() { return workers; }
	public ArrayList<Person> getCurPeople() { return peopleInTile; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getW() { return w; }
	public float getH() { return h; }
	public int getPosX() { return posX; }
	public int getPosY() { return posY; }
	public float getCenterX() { return x + (w / 2); }
	public float getCenterY() { return y + (h / 2); }
	
	public enum TileType {
		STREET,
		HOME,
		WORKPLACE
	}
}
