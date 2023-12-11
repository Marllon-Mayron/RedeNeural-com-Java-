package entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Random;

public class Entity {
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	public double speed;
	protected int sensorWidth;
	protected int sensorHeight;
	protected BufferedImage sprite;
	
	public static Random rand = new Random();
	
	public Entity(double x,double y,int width,int height,double speed,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return this.height;
	}
	
	public int getSensorWidth() {
		return sensorWidth;
	}

	public void setSensorWidth(int sensorWidth) {
		this.sensorWidth = sensorWidth;
	}

	public int getSensorHeight() {
		return sensorHeight;
	}

	public void setSensorHeight(int sensorHeight) {
		this.sensorHeight = sensorHeight;
	}

	public void tick(){}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	

	public boolean isColidding(Entity e1,Entity e2){
		
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY(),e1.getSensorWidth(),e1.getSensorHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getSensorWidth(),e2.getSensorHeight());
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX() ,this.getY() ,null);
	}
	
}
