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

	protected int sensorFrontalWidth;
	protected int sensorFrontalHeight;
	protected int sensorDireitoWidth;
	protected int sensorDireitoHeight;
	protected int sensorEsquerdoWidth;
	protected int sensorEsquerdoHeight;
	
	protected BufferedImage sprite;
	
	public static Random rand = new Random();
	
	public Entity(double x,double y,int width,int height,BufferedImage sprite){
		this.x = x;
		this.y = y;
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
	
	public int getSensorFrontalWidth() {
		return sensorFrontalWidth;
	}

	public void setSensorFrontalWidth(int sensorWidth) {
		this.sensorFrontalWidth = sensorWidth;
	}

	public int getSensorFrontalHeight() {
		return sensorFrontalHeight;
	}

	public void setSensorFrontalHeight(int sensorHeight) {
		this.sensorFrontalHeight = sensorHeight;
	}
	
	public int getSensorDireitoWidth() {
		return sensorDireitoWidth;
	}

	public void setSensorDireitoWidth(int sensorDireitoWidth) {
		this.sensorDireitoWidth = sensorDireitoWidth;
	}

	public int getSensorDireitoHeight() {
		return sensorDireitoHeight;
	}

	public void setSensorDireitoHeight(int sensorDireitoHeight) {
		this.sensorDireitoHeight = sensorDireitoHeight;
	}

	public int getSensorEsquerdoWidth() {
		return sensorEsquerdoWidth;
	}

	public void setSensorEsquerdoWidth(int sensorEsquerdoWidth) {
		this.sensorEsquerdoWidth = sensorEsquerdoWidth;
	}

	public int getSensorEsquerdoHeight() {
		return sensorEsquerdoHeight;
	}

	public void setSensorEsquerdoHeight(int sensorEsquerdoHeight) {
		this.sensorEsquerdoHeight = sensorEsquerdoHeight;
	}

	public void tick(){}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	

	public boolean isCollidingSensorFrontal(Entity e1,Entity e2){
		
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.getSensorDireitoWidth(),e1.getY(),e1.getSensorFrontalWidth(),e1.getSensorFrontalHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}public boolean isCollidingSensorDireito(Entity e1,Entity e2){
		
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY() + e1.getHeight(), e1.getSensorDireitoWidth(),e1.getSensorDireitoHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}public boolean isCollidingSensorEsquerdo(Entity e1,Entity e2){
		
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY() - e1.sensorDireitoHeight, e1.getSensorEsquerdoWidth(),e1.getSensorEsquerdoHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}
	public boolean isColliding(Entity e1,Entity e2){
		
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY(),e1.getWidth(),e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite,this.getX() ,this.getY() ,null);
	}
	
}
