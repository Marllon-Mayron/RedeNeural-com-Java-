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
	List<String> volantes = Arrays.asList("PARADO" , "DIREITA", "ESQUERDA");
	
	public String acao = "";
	public String acao2 = "";
	public String acao3 = "";
	
	public int experiencia =0;
	
	public SemaforoNeural ambiente;
	
	double desaceleracao = 0.01;
	double aceleracao = 0.0;
	int volante;
	
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
			try {
				acao3 = ambiente.passo(2, 0, estrada.corSemaforo, experiencia, Game.ambiente.horario, detectaSensor(), Game.ambiente.estradas.get(0).xSemaforo, volante);
				if(acao3.equals(volantes.get(0))) {
					volante = 0;
				}else if(acao3.equals(volantes.get(1))) {
					volante = 1;
				}else if(acao3.equals(volantes.get(2))) {
					volante = 2;
				}
				acao2 = ambiente.passo(1, 0, estrada.corSemaforo, experiencia, Game.ambiente.horario, detectaSensor(), Game.ambiente.estradas.get(0).xSemaforo, volante);
				acao = ambiente.passo(0, 0, estrada.corSemaforo, experiencia, Game.ambiente.horario, detectaSensor(), Game.ambiente.estradas.get(0).xSemaforo, volante);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(this.x > Game.WIDTH){
			Game.entities.remove(this);
		}
		//PEDAL
		if(acao.equalsIgnoreCase(decisoes.get(0))) {
			if(aceleracao <= speed) {
				aceleracao += 0.01;
			}
			if(volante == 0) {
				this.x += aceleracao;
			}else if(volante == 1) {
				this.x += aceleracao/2;
				this.y += aceleracao;
			}else if(volante == 2) {
				this.x += aceleracao/2;
				this.y -= aceleracao;
			}
			
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
			}
			if(this.isColliding(this, Game.entities.get(i))) {
				Game.entities.remove(this);
			}
		}
	}
	public int detectaSensor() {
		int detectado = 0;
		boolean frente = false, direita = false, esquerda = false;
		for(int i = 0; i < Game.entities.size(); i++) {
			if(Game.entities.get(i) instanceof Carro) {
				Carro e = (Carro) Game.entities.get(i);
				if(e.equals(this)) {
					continue;
				}
			}
			
			if(this.isCollidingSensorFrontal(this, Game.entities.get(i))) {
				 frente = true;
			}if(this.isCollidingSensorDireito(this, Game.entities.get(i))) {
				direita = true; 
			}if(this.isCollidingSensorEsquerdo(this, Game.entities.get(i))) {
				 esquerda = true;
			}
			
			
		}
		if(frente) {
			detectado = 1;
		}if(direita) {
			detectado = 2;
		}if(esquerda) {
			detectado = 3;
		}if(direita && frente) {
			detectado = 4;
		}if(esquerda && frente) {
			detectado = 5;
		}if(esquerda && direita) {
			detectado = 6;
		}if(esquerda && direita && frente) {
			detectado = 7;
		}
		return detectado;
	}
	public void sensorController() {
		
		if(Game.ambiente.horario == 0) {
			this.sensorFrontalWidth = 26;
			this.sensorFrontalHeight = 16;
			this.sensorDireitoWidth = this.width;
			this.sensorDireitoHeight = 10;
			this.sensorEsquerdoWidth = this.width;
			this.sensorEsquerdoHeight = 10;
		}else if(Game.ambiente.horario == 1) {
			this.sensorFrontalWidth = 22;
			this.sensorFrontalHeight = 12;
			this.sensorDireitoWidth = this.width;
			this.sensorDireitoHeight = 8;
			this.sensorEsquerdoWidth = this.width;
			this.sensorEsquerdoHeight = 8;
		}else if(Game.ambiente.horario == 2) {
			if(ativarFarol) {
				this.sensorFrontalWidth = 22;
				this.sensorFrontalHeight = 12;
				this.sensorDireitoWidth = this.width;
				this.sensorDireitoHeight = 8;
				this.sensorEsquerdoWidth = this.width;
				this.sensorEsquerdoHeight = 8;
			}else {
				this.sensorFrontalWidth = 14;
				this.sensorFrontalHeight = 8;
				this.sensorDireitoWidth = this.width;
				this.sensorDireitoHeight = 6;
				this.sensorEsquerdoWidth = this.width;
				this.sensorEsquerdoHeight = 6;
			}
			
		}
	}
	public void render(Graphics g) {
		if(Game.config.mostrarSensor) {
			g.setColor(Color.orange);
			g.drawRect(this.getX() + this.sensorDireitoWidth, (this.getY() - this.sensorFrontalHeight/2) + this.height/2, this.sensorFrontalWidth, this.sensorFrontalHeight);	
			g.drawRect(this.getX() , (this.getY()) + this.height, this.sensorDireitoWidth, this.sensorDireitoHeight);	
			g.drawRect(this.getX() , (this.getY()) - this.sensorDireitoHeight, this.sensorEsquerdoWidth, this.sensorEsquerdoHeight);	
			
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
