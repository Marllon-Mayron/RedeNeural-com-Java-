package entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Estrada extends Entity{
	
	public boolean isVertical = false;
	public int xSemaforo =  Game.WIDTH*80/100;
	public int ySemaforo = Game.HEIGHT*15/100;
	public static int corSemaforo = 2;
	public int corEscolhida = -35;
	public int frame, sec, maxSec, tempoSemaforo = 2;

	public Estrada(double x, double y, int width, int height, BufferedImage sprite, boolean isVertical) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
	public void tick() {
		frame++;
		if(frame == 60) {
			sec++;
			frame = 0;
			if(sec == tempoSemaforo) {
				sec = 0;
				if(corSemaforo == 2) {
					corSemaforo = 0;
				}else {
					corSemaforo++;
				}
			}
		}
	}
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect((int)x, (int)y, width, height);
		
		g.setColor(Color.green);
		g.fillRect(xSemaforo, ySemaforo + Game.HEIGHT*3/100, Game.WIDTH*2/100, Game.HEIGHT*3/100);
		g.setColor(Color.yellow);
		g.fillRect(xSemaforo, ySemaforo + Game.HEIGHT*6/100, Game.WIDTH*2/100, Game.HEIGHT*3/100);
		g.setColor(Color.red);
		g.fillRect(xSemaforo, ySemaforo + Game.HEIGHT*9/100,Game.WIDTH*2/100 , Game.HEIGHT*3/100);
		g.setColor(Color.black);
		g.drawRect(xSemaforo - 1, (ySemaforo -1) + Game.HEIGHT*(3 * (corSemaforo + 1)) /100,(int) (Game.WIDTH*2.3/100),  (int)(Game.HEIGHT*3.3/100));
		g.fillRect(xSemaforo + 1, ySemaforo + 18, Game.WIDTH*1/100 , Game.HEIGHT*5/100);
		
	}
}
