package entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import algoritimo.SemaforoNeural;



public class Carro extends Entity{

	List<String> decisoes = Arrays.asList("ACELERAR" , "FREAR", "PARAR");
	List<String> farol = Arrays.asList("LIGADO" , "DESLIGADO");
	public String acao = "";
	public String acao2 = "";
	public int experiencia =0;
	public SemaforoNeural ambiente;
	double desaceleracao = 0.01;
	double aceleracao = 0.0;
	public boolean ativarFarol;
	public Carro(double x, double y, int width, int height, double speed, BufferedImage sprite, SemaforoNeural ambiente, int experiencia) {
		super(x, y, width, height, speed, sprite);
		this.ambiente = ambiente;
		this.height = height;
		this.width = width;
		this.speed = speed;
		this.experiencia = experiencia;
	}
	
	public void tick() {
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
		
	}
	
	public void render(Graphics g) {
		if(ativarFarol) {
			g.setColor(Color.gray);
		}else {
			g.setColor(Color.darkGray);
		}
		
		g.fillRect(this.getX(), this.getY(), width, height);
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
