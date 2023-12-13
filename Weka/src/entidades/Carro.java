package entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import algoritimo.SemaforoNeural;
import main.Game;



public class Carro extends Entity{

	public double speed;
	
	List<String> decisoes = Arrays.asList("ACELERAR" , "FREAR", "PARAR");
	List<String> farol = Arrays.asList("LIGADO" , "DESLIGADO");
	
	public String acao = "";
	public String acao2 = "";
	
	public int experiencia =0;
	
	public SemaforoNeural ambiente;
	
	double desaceleracao = 0.01;
	double aceleracao = 0.0;
	
	public boolean ativarFarol;
	public Estrada estrada;
	
	public double frame, secReacao = 5;
	
	public Carro(double x, double y, int width, int height, double speed, BufferedImage sprite, SemaforoNeural ambiente, int experiencia, Estrada estrada) {
		super(x, y, width, height, sprite);
		this.speed = speed;
		this.ambiente = ambiente;
		this.height = height;
		this.width = width;
		this.speed = speed;
		this.experiencia = experiencia;
		this.estrada = estrada;
	}
	
	public void tick() {
		frame++;
		if(frame >= secReacao) {
			frame = 0;
			Game.ambiente.trocarCor(estrada.corSemaforo);
		}
		
		if(this.x > Game.WIDTH){
			Game.entities.remove(this);
		}
		//PEDAL
		if(acao.equalsIgnoreCase(decisoes.get(0))) {
			if(aceleracao <= speed) {
				aceleracao += 0.01;
			}
			this.x += aceleracao;
		}else if(acao.equalsIgnoreCase(decisoes.get(1))) {
			if(aceleracao > 0.1) {
				aceleracao -= desaceleracao;
				if(aceleracao <= 0.1) {
					aceleracao = 0.0;
				}
			}
			this.x += aceleracao;
		}else if(acao.equalsIgnoreCase(decisoes.get(2))) {
			aceleracao = 0.0;
		}
		//FAROL
		if(acao2.equalsIgnoreCase(farol.get(0))) {
			ativarFarol = true;
		}if(acao2.equalsIgnoreCase(farol.get(1))) {
			ativarFarol = false;
		}
		
		//MUDA O SENSOR DO CARRO
		sensorController();
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Carro) {
				Carro e = (Carro) Game.entities.get(i);
				if(e.equals(this)) {
					continue;
				}
				if(this.isCollidingSensor(this, e)) {
					try {
						acao = ambiente.passo(0, 0, estrada.corSemaforo, experiencia, Game.ambiente.horario, 1, Game.ambiente.estradas.get(0).xSemaforo);
						acao2 = ambiente.passo(1, 0, estrada.corSemaforo, experiencia, Game.ambiente.horario, 1, Game.ambiente.estradas.get(0).xSemaforo);
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				
			}
		}
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Carro) {
				Carro e = (Carro) Game.entities.get(i);
				if(e.equals(this)) {
					continue;
				}
				if(this.isColliding(this, e)) {
					Game.entities.remove(this);
				}
			}
		}
	}
	public void sensorController() {
		
		if(Game.ambiente.horario == 0) {
			this.sensorWidth = 30;
			this.sensorHeight = 22;
			
		}else if(Game.ambiente.horario == 1) {
			this.sensorWidth = 26;
			this.sensorHeight = 18;
		}else if(Game.ambiente.horario == 2) {
			if(ativarFarol) {
				this.sensorWidth = 26;
				this.sensorHeight = 18;
			}else {
				this.sensorWidth = 18;
				this.sensorHeight = 14;
			}
			
		}
	}
	public void render(Graphics g) {
		if(Game.config.mostrarSensor) {
			g.setColor(Color.orange);
			g.drawRect(this.getX() , (this.getY() - this.sensorHeight/2) + this.height/2, this.sensorWidth, this.sensorHeight);	
		}
		if(ativarFarol) {
			g.setColor(Color.gray);
		}else {
			g.setColor(Color.darkGray);
		}
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		if(experiencia >= 0 && experiencia < 1) {
			g.setColor(Color.red);
		}else if(experiencia >= 1 && experiencia < 2) {
			g.setColor(Color.yellow);
		}else if(experiencia >= 2){
			g.setColor(Color.green);
		}
		g.fillRect(this.getX() + 6, this.getY(), 2, 4);
	}
	
}
