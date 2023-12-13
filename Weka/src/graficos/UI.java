package graficos;


import java.awt.Color;
import java.awt.Graphics;

import main.Game;

public class UI {
	public int corEscolhida = -35, horarioEscolhido = 0;
	public void render(Graphics g) {
		/**
		g.setColor(Color.green);
		g.fillRect(Game.WIDTH*35/100, 0, Game.WIDTH*10/100, Game.HEIGHT*10/100);
		g.setColor(Color.yellow);
		g.fillRect(Game.WIDTH*45/100, 0, Game.WIDTH*10/100, Game.HEIGHT*10/100);
		g.setColor(Color.red);
		g.fillRect(Game.WIDTH*55/100, 0, Game.WIDTH*10/100, Game.HEIGHT*10/100);
		g.setColor(Color.black);
		g.drawRect(Game.WIDTH*corEscolhida/100, 0, Game.WIDTH*10/100, Game.HEIGHT*10/100);
		**/
		g.setColor(Color.yellow);
		g.fillRect(Game.WIDTH*0/100, 0, Game.WIDTH*5/100, Game.HEIGHT*5/100);
		g.setColor(Color.orange);
		g.fillRect(Game.WIDTH*5/100, 0, Game.WIDTH*5/100, Game.HEIGHT*5/100);
		g.setColor(Color.blue);
		g.fillRect(Game.WIDTH*10/100, 0, Game.WIDTH*5/100, Game.HEIGHT*5/100);
		g.setColor(Color.black);
		g.drawRect(Game.WIDTH*horarioEscolhido/100, 0, Game.WIDTH*5/100, Game.HEIGHT*5/100);

	}
	
}
