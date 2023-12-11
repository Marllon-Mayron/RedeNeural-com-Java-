package ambiente;

import java.awt.Graphics;

import entidades.Carro;
import entidades.Entity;
import main.Game;

public class Ambiente {
	
	public int horario = 0;
	
	public Ambiente() {
		
	}
	
	public void trocarCor(int cor) {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Carro) {
				try {
					Carro carro = (Carro) e;
					carro.acao = carro.ambiente.passo(0, 0, cor, carro.experiencia, Game.ambiente.horario);
					carro.acao2 = carro.ambiente.passo(1, 0, cor, carro.experiencia, Game.ambiente.horario);
				
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void render(Graphics g) {
		
	}
}
