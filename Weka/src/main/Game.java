package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import algoritimo.SemaforoNeural;
import ambiente.Ambiente;
import entidades.Carro;
import entidades.Entity;
import graficos.UI;



public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	public int x, y;
	private BufferedImage image;
	
	public static double score = 0;
	public static Random rand = new Random();
	public static UI ui = new UI();
	public static List<Entity> entities;
	public static Ambiente ambiente = new Ambiente();
	public static Config config = new Config();
	
	public Game() throws Exception{
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		
		//ADICIONAR OS CARROS
		for(int j = 0; j < 3; j++) {
			for(int i = 1; i < 4; i++) {
				SemaforoNeural ambiente = new SemaforoNeural();
				Carro carro = new Carro(j * WIDTH*18 /100, (HEIGHT*15)*(i+1)/100, 10, 4, 1, null, ambiente, 2/**rand.nextInt(3)**/, null);
				entities.add(carro); 
			}
		}
		
		
	}
	
	public void initFrame(){
		frame = new JFrame("IA TRAINING");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception{
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for(int i =0; i < ambiente.estradas.size(); i++) {
			ambiente.estradas.get(i).tick();
		}
		
	}

	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		
		if(ambiente.horario == 0) {
			g.setColor(new Color(122,130,255));
		}else if(ambiente.horario == 1){
			g.setColor(new Color(122,80,255));
		}else if(ambiente.horario == 2){
			g.setColor(new Color(100,0,255));
		}
		
		g.fillRect(0, 0,WIDTH,HEIGHT);
		for(int i =0; i < ambiente.estradas.size(); i++) {
			ambiente.estradas.get(i).render(g);
		}
		//Graphics2D g2 = (Graphics2D) g;
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				//System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		
		stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
	}

	@Override
	public void keyReleased(KeyEvent e) {

		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(this.y > 0 && this.y <= Game.HEIGHT*10/100) {
			if(this.x >= Game.WIDTH*35/100 && this.x <= Game.WIDTH*35/100 + Game.WIDTH*10/100) {
				ui.corEscolhida = 35;
				//ambiente.trocarCor(0);
			}else if(this.x >= Game.WIDTH*45/100 && this.x <= Game.WIDTH*45/100 + Game.WIDTH*10/100) {
				ui.corEscolhida = 45;
				//ambiente.trocarCor(1);
			}else if(this.x >= Game.WIDTH*55/100 && this.x <= Game.WIDTH*55/100 + Game.WIDTH*10/100) {
				ui.corEscolhida = 55;
				//ambiente.trocarCor(2);
			}
		}
		
		if(this.y > 0 && this.y <= Game.HEIGHT*5/100) {
			if(this.x >= 0 && this.x <= Game.WIDTH*5/100) {
				ui.horarioEscolhido = 0;
				ambiente.horario = 0;
			}else if(this.x >= Game.WIDTH*5/100 && this.x <= Game.WIDTH*5/100 + Game.WIDTH*5/100) {
				ui.horarioEscolhido = 5;
				ambiente.horario = 1;
			}else if(this.x >= Game.WIDTH*10/100 && this.x <= Game.WIDTH*10/100 + Game.WIDTH*5/100) {
				ui.horarioEscolhido = 10;
				ambiente.horario = 2;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.x = (e.getX()/3);
		this.y = (e.getY()/3);
	}

	
}
