package core;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Person {

	private float x, y, targetX, targetY, xDif, yDif;
	
	private Status status;
	private Tile oldTile, curTile, home, workplace;
	private int ticksInfected;

	private boolean isWorking;
	private int timeGoWork, timeGoHome;
	
	public Person(Tile home) {
		status = Status.SUSCEPTIBLE;
		this.home = home;
		
		oldTile = Simulation.getTile(home.getPosX() - 1, home.getPosY());
		curTile = home;
		
		timeGoWork = 8 + (int)(Math.random() * 4) - 2;
		timeGoHome = timeGoWork + 10;
		
		x = oldTile.getCenterX();
		y = oldTile.getCenterY();
		float maxPeople = curTile.getMaxPeople();
		targetX = curTile.getX() + (curTile.getW() * (curTile.getCurPeople().size() % (maxPeople + 1))) / (maxPeople + 1);
		targetY = curTile.getY() + (curTile.getH() * (curTile.getCurPeople().size() % (maxPeople + 1))) / (maxPeople + 1);
		xDif = targetX - x;
		yDif = targetY - y;
		
		
		
		ticksInfected = 0;
	}
	
	public void update() {
		if (status.equals(Status.INFECTIOUS) || status.equals(Status.SYMPTOMATIC)) {
			ticksInfected++;
		}
		if (status.equals(Status.SYMPTOMATIC) && Math.random() < 1 - Math.pow(1 - Simulation.D_PER_I, 1 / (24 * (Simulation.PERIOD - Simulation.INCUBATION)))) {
			status = Status.DEAD;
			curTile.removePerson(this);
		}
		if (!status.equals(Status.DEAD) && ticksInfected > Simulation.INCUBATION * 24) {
			status = Status.SYMPTOMATIC;
		}
		if (!status.equals(Status.DEAD) && ticksInfected > Simulation.PERIOD * 24) {
			status = Status.RECOVERED;
		}
		if (workplace == null) {
			workplace = home;
		}
	}
	
	public void updatePosition() {
		if (!status.equals(Status.DEAD)) {
			if (curTile != oldTile) {
				if (!curTile.getTileType().equals(Tile.TileType.STREET)) {
					x += xDif / (float)(Simulation.TICK / 4);
					y += yDif / (float)(Simulation.TICK / 4);
				} else {
					if (!oldTile.getTileType().equals(Tile.TileType.STREET)) {
						x += xDif / (float)(Simulation.TICK / 4);
						y += yDif / (float)(Simulation.TICK / 4);
					} else {
						if (curTile.getCenterX() > x) {
							x += Simulation.tileW / (float)(Simulation.TICK / 4);
						} else
						if (curTile.getCenterX() < x) {
							x -= Simulation.tileW / (float)(Simulation.TICK / 4);
						} else
						if (curTile.getCenterY() > y) {
							y += Simulation.tileH / (float)(Simulation.TICK / 4);
						} else
						if (curTile.getCenterY() < y) {
							y -= Simulation.tileH / (float)(Simulation.TICK / 4);
						}
						if (Math.abs(curTile.getCenterX() - x) < 1f) {
							x = curTile.getCenterX();
						}
						if (Math.abs(curTile.getCenterY() - y) < 1f) {
							y = curTile.getCenterY();
						}
					}
				}
			}
		} else {
			if (y < 1000) y++;
			if (y > 100) y = 1000;
		}
	}
	
	public void setNewTile() {
		if (Simulation.getTime() / Simulation.TICK < timeGoWork || Simulation.getTime() / Simulation.TICK > timeGoHome) {
			if (curTile.equals(home)) {
				oldTile = curTile;
			}
			else if ((Math.abs(home.getPosX() - curTile.getPosX()) < 1 && home.getPosY() == curTile.getPosY()) || (Math.abs(home.getPosY() - curTile.getPosY()) < 1 && home.getPosX() == curTile.getPosX())) {
				oldTile = curTile;
				curTile = home;
			} else {
				if (Math.abs(curTile.getPosX() - home.getPosX()) > Math.abs(curTile.getPosY() - home.getPosY())) {
					if (curTile.getPosX() < home.getPosX()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX() + 1, curTile.getPosY());
					}
					else if (curTile.getPosX() > home.getPosX()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX() - 1, curTile.getPosY());
					}
				} else {
					if (curTile.getPosY() < home.getPosY()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX(), curTile.getPosY() + 1);
					}
					else if (curTile.getPosY() > home.getPosY()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX(), curTile.getPosY() - 1);
					}
				}
			}
		} else {
			if (curTile.equals(workplace)) {
				oldTile = curTile;
			}
			else if ((Math.abs(workplace.getPosX() - curTile.getPosX()) < 1 && workplace.getPosY() == curTile.getPosY()) || (Math.abs(workplace.getPosY() - curTile.getPosY()) < 1 && workplace.getPosX() == curTile.getPosX())) {
				oldTile = curTile;
				curTile = workplace;
			} 
			else {
				if (Math.abs(curTile.getPosX() - workplace.getPosX()) > Math.abs(curTile.getPosY() - workplace.getPosY())) {
					if (curTile.getPosX() < workplace.getPosX()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX() + 1, curTile.getPosY());
					}
					else if (curTile.getPosX() > workplace.getPosX()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX() - 1, curTile.getPosY());
					}
				} else {
					if (curTile.getPosY() < workplace.getPosY()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX(), curTile.getPosY() + 1);
					}
					else if (curTile.getPosY() > workplace.getPosY()) {
						oldTile = curTile;
						curTile = Simulation.getTile(curTile.getPosX(), curTile.getPosY() - 1);
					}
				}
			}
		}
		if (!curTile.equals(oldTile)) {
			if (!curTile.getType().equals(Tile.TileType.STREET)) {
				if (!(curTile.equals(home) || curTile.equals(workplace))) {
					if (Math.random() * 10 > 5) {
						if (oldTile.getPosX() - 1 >= 0 && Simulation.getTile(oldTile.getPosX() - 1, oldTile.getPosY()).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX() - 1, oldTile.getPosY());
						}
						else if (oldTile.getPosY() - 1 >= 0 && Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() - 1).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() - 1);
						}
						else if (oldTile.getPosX() + 1 < Simulation.tileX && Simulation.getTile(oldTile.getPosX() + 1, oldTile.getPosY()).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX() + 1, oldTile.getPosY());
						}
						else if (oldTile.getPosY() + 1 < Simulation.tileY && Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() + 1).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() + 1);
						}
					} else {
						if (oldTile.getPosX() + 1 < Simulation.tileX && Simulation.getTile(oldTile.getPosX() + 1, oldTile.getPosY()).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX() + 1, oldTile.getPosY());
						}
						else if (oldTile.getPosY() + 1 < Simulation.tileY && Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() + 1).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() + 1);
						}
						else if (oldTile.getPosX() - 1 >= 0 && Simulation.getTile(oldTile.getPosX() - 1, oldTile.getPosY()).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX() - 1, oldTile.getPosY());
						}
						else if (oldTile.getPosY() - 1 >= 0 && Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() - 1).getTileType().equals(Tile.TileType.STREET)) {
							curTile = Simulation.getTile(oldTile.getPosX(), oldTile.getPosY() - 1);
						}
					}
				}
			}
		}
		
		if (curTile.equals(workplace) && !oldTile.equals(workplace)) {
			timeGoHome = timeGoWork + 8;
		}
		
		oldTile.removePerson(this);
		curTile.addPerson(this);
		
		if (!curTile.getTileType().equals(Tile.TileType.STREET)) {
			float maxPeople = curTile.getMaxPeople();
			targetX = curTile.getX() + (curTile.getW() * (curTile.getCurPeople().size() % (maxPeople + 1))) / (maxPeople + 1);
			targetY = curTile.getY() + (curTile.getH() * (curTile.getCurPeople().size() % (maxPeople + 1))) / (maxPeople + 1);
			xDif = targetX - x;
			yDif = targetY - y;
		} else {
			if (!oldTile.getTileType().equals(Tile.TileType.STREET)) {
				targetX = curTile.getCenterX();
				targetY = curTile.getCenterY();
				xDif = targetX - x;
				yDif = targetY - y;
			}
		}
	}
	
	public void drawPerson(Graphics g) {
		switch(status) {
		case SUSCEPTIBLE:
			g.setColor(new Color(255,255,255));
			break;
		case INFECTIOUS:
			g.setColor(new Color(238, 255, 130));
			break;
		case SYMPTOMATIC:
			g.setColor(new Color(255, 130, 130));
			break;
		case DEAD:
			g.setColor(new Color(174, 130, 255));
			break;
		case RECOVERED:
			g.setColor(new Color(28, 186, 70));
		}
		float circleSize = Simulation.tileX * (18 - 2 * Simulation.POP_DENSITY) / 100;
		g.fillOval(x - circleSize / 2, y - circleSize / 2, circleSize, circleSize);
	}
	
	public void render(Graphics g) {

	}
	
	public void addWorkplace(Tile workplace) {
		this.workplace = workplace;
	}
	
	public void infect() {
		status = Status.INFECTIOUS;
	}
	
	public enum Status {
		SUSCEPTIBLE,
		INFECTIOUS,
		SYMPTOMATIC,
		DEAD,
		RECOVERED
	}
	
	public Tile getHome() { return home; }
	public Tile getWorkplace() { return workplace; }
	public Tile getTile() { return curTile; }
	public Status getStatus() { return status; }
	public boolean hasWorkplace() { return ((workplace == null) ? false : true); }
}
