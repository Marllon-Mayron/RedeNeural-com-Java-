package ambiente;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import entidades.Carro;
import entidades.Entity;
import entidades.Estrada;
import main.Game;

public class Ambiente {
	
	public int horario = 0;
	public static List<Estrada> estradas = new ArrayList<Estrada>();
	public Ambiente() {
		Estrada estrada = new Estrada(0, ((Game.HEIGHT * 50) / 100) - ((Game.HEIGHT * 42) /100) /2 , Game.WIDTH, Game.HEIGHT * 42 /100, null, false);
		estradas.add(estrada);
	}
	
	public void trocarCor(int cor) {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Carro) {
				try {
					Carro carro = (Carro) e;
					//carro.acao = carro.ambiente.passo(0, 0, cor, carro.experiencia, Game.ambiente.horario, 0, Game.ambiente.estradas.get(0).xSemaforo);
					//carro.acao2 = carro.ambiente.passo(1, 0, cor, carro.experiencia, Game.ambiente.horario, 0, Game.ambiente.estradas.get(0).xSemaforo);
				
					
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
