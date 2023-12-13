package entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Estrada extends Entity{
	public boolean isVertical = false;

	public Estrada(double x, double y, int width, int height, double speed, BufferedImage sprite, boolean isVertical) {
		super(x, y, width, height, speed, sprite);
		// TODO Auto-generated constructor stub
	}
	public void render(Graphics g) {
		
	}
}
