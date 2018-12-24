import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import java.util.Date;
import java.util.Random;

public class MyDron extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	public static final double GREEN_FREQUENCY = 0.0015;

=======
	public static final double GREEN_FREQUENCY = 0.002;
	public static final double GLAY_FREQUENCY = 0.004;
>>>>>>> 4470543559c3bed18c2f223bf7992a61ed733d85
	private Color state[][];
	private int xSize, ySize;
	private int block;
	private int xL, yL, xR, yR;
	private int dxL, dyL, dxR, dyR;
	private boolean liveL, liveR;
	private Thread thread;
	private String message,tmp;
	private Font font;
	private int width, height;
	private int countR, countL;
	public int hpR,hpL;
	public long st,ed,diff,remain;
	public Date start,end;

	Random rnd_inst = new Random();
	Random rnd2_inst = new Random();//念のため二つ乱数を生成
	private void initialize() {
		int i,j;
		
		for(j = 0; j < ySize; j++) {
			state[0][j] = state[xSize - 1][j] = Color.BLACK;
		}
		for (i = 1; i < xSize - 1; i++) {
			state[i][0] = state[i][ySize - 1] = Color.BLACK;
			for (j = 1; j < ySize - 1; j++) {
				if (rnd_inst.nextDouble() < GREEN_FREQUENCY)
				{
					state[i][j] = Color.GREEN;
				}else if(rnd_inst.nextDouble()<GLAY_FREQUENCY) {
					if( (i >= 10 && j >= 10) && (i <= xSize-10 && j <= ySize-10) ) {//スタート地点から障害物の発生を防ぐ　
						state[i][j] = Color.GRAY;
					}
				}else{
					state[i][j] = Color.WHITE;
				}
			}
		}
		xL = yL = 2;
		xR = xSize - 3; yR = ySize - 3;
		dxL = dxR = 0;
		dyL = 1; dyR = -1;

		hpR = 3; hpL = 3;//障害物衝突時のHP管理的なもの
		liveL = liveR = true;
	}


	public MyDron() {
		setPreferredSize(new Dimension(320, 360));
		
		xSize = ySize = 80;
		block = 4;
		state = new Color[xSize][ySize];
		message = "Game started!";
		font = new Font("Monospaced", Font.PLAIN, 12);
		setFocusable(true);
		addKeyListener(this);
		Dimension size = getPreferredSize();
		width = size.width; height = size.height;

		startThread();
	}


	public void startThread() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}


	public void stopThread() {
		if (thread != null) {
			thread = null;
		}
	}

	public void itemResolver(boolean is1P) {
		double dRandNum = rnd_inst.nextDouble();
		int xMin, xMax, yMin, yMax;

		if (dRandNum < 0.50)
		{
			// 障害物設置
			// 確率で逆効果に反転します。
			is1P = (rnd_inst.nextDouble() > 0.9)? false : true;
			if (is1P)
			{
				xMin = (xR - 10 < 1)? 1 : xR - 10;
				xMax = (xR + 10 > xSize - 1)? xSize - 1 : xR + 10;
				yMin = (yR - 10 < 1)? 1 : yR - 10;
				yMax = (yR + 10 > ySize - 1)? ySize - 1 : yR + 10;
			} else
			{
				xMin = (xL - 10 < 1)? 1 : xL - 10;
				xMax = (xL + 10 > xSize - 1)? xSize - 1 : xL + 10;
				yMin = (yL - 10 < 1)? 1 : yL - 10;
				yMax = (yL + 10 > ySize - 1)? ySize - 1 : yL + 10;
			}

			for(int dy = yMin; dy < yMax; dy++){
				for(int dx = xMin; dx < xMax; dx++){
					if (rnd_inst.nextDouble() < 0.01 && 
						(state[dx][dy] != Color.RED || state[dx][dy] != Color.BLUE || state[dx][dy] != Color.BLACK || state[dx][dy] != Color.GREEN)&&
						(state[dx+1][dy] != Color.RED || state[dx+1][dy] != Color.BLUE || state[dx+1][dy] != Color.BLACK || state[dx+1][dy] != Color.GREEN)&&
						(state[dx][dy+1] != Color.RED || state[dx][dy+1] != Color.BLUE || state[dx][dy+1] != Color.BLACK || state[dx][dy+1] != Color.GREEN)&&
						(state[dx+1][dy+1] != Color.RED || state[dx+1][dy+1] != Color.BLUE || state[dx+1][dy+1] != Color.BLACK || state[dx+1][dy+1] != Color.GREEN))
					{
						state[dx][dy] = Color.GRAY;
						state[dx+1][dy] = Color.GRAY;
						state[dx][dy+1] = Color.GRAY;
						state[dx+1][dy+1] = Color.GRAY;
					}
				}
			}

		} else if (dRandNum < 0.8)
		{
			// 障害物配置(ランダム)
			xMin = yMin = 2;
			xMax = xSize - 3;	yMax = ySize - 3;
			for(int dy = yMin; dy < yMax; dy++){
				for(int dx = xMin; dx < xMax; dx++){
					if (rnd_inst.nextDouble() < 0.005 && 
						(state[dx][dy] != Color.RED || state[dx][dy] != Color.BLUE || state[dx][dy] != Color.BLACK || state[dx][dy] != Color.GREEN)&&
						(state[dx+1][dy] != Color.RED || state[dx+1][dy] != Color.BLUE || state[dx+1][dy] != Color.BLACK || state[dx+1][dy] != Color.GREEN)&&
						(state[dx][dy+1] != Color.RED || state[dx][dy+1] != Color.BLUE || state[dx][dy+1] != Color.BLACK || state[dx][dy+1] != Color.GREEN)&&
						(state[dx+1][dy+1] != Color.RED || state[dx+1][dy+1] != Color.BLUE || state[dx+1][dy+1] != Color.BLACK || state[dx+1][dy+1] != Color.GREEN))
					{
						state[dx][dy] = Color.GRAY;
						state[dx+1][dy] = Color.GRAY;
						state[dx][dy+1] = Color.GRAY;
						state[dx+1][dy+1] = Color.GRAY;
					}
				}
			}

		} else
		{
			// 盤面リセット
			initialize();
		}

		return;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		g.clearRect(0, 0, width, height); 
		String left,right;
		int i, j;
		for (i = 0; i < xSize; i++) {
			for (j = 0; j < ySize; j++) {
				g.setColor(state[i][j]);
				g.fillRect(i * block, j * block, block, block);
			}
		}
		if ( remain < 0) {
			g.setFont(font);
			g.setColor(Color.RED);
			g.drawString("Game Over!",  2 * block,  block *(ySize+3));
		} else {
		tmp = "残り時間:"+remain + "秒 " + message;
		left = "Left:  A(L), S(D), W(U), D(R)  残基: "+hpL+"Life";
		right = "Right: J(L), K(D), I(U), L(R)  残基: "+hpR+"Life";
		g.setFont(font);
		g.setColor(Color.GREEN.darker());
		g.drawString (tmp, 2 * block, block * (ySize+3));
		g.setColor(Color.RED.darker());
		g.drawString(left, 2 * block, block * (ySize + 6));
		g.setColor(Color.BLUE.darker());
		g.drawString(right, 2 * block, block * (ySize + 9));
		}
	
	}


	public void run() {
		Thread thisThread = Thread.currentThread();
		while (thisThread == thread) {
			start = new Date();
			st = start.getTime();
			initialize();
			requestFocus();
			while (liveL && liveR) {
				end = new Date();
				ed = end.getTime();
				diff = (ed -st)/1000;
				remain = 30 - diff;
				if ( remain < 0 ) { break; } 
				xL += dxL; yL += dyL;
				xR += dxR; yR += dyR;
				if (state[xL][yL] != Color.WHITE) {
					if ( xL >= xSize-2 ) {
						if (state[1][yL] == Color.WHITE) { xL = 1; }
					} else if ( xL < 1 ) {
						if ( state[xSize-2][yL] == Color.WHITE) { xL = xSize-2; }
					} else if ( yL >= ySize -2) {
						if ( state[xL][1] == Color.WHITE) { yL = 1; }
					} else if ( yL < 1) {
						if ( state[xL][ySize-2] == Color.WHITE) {yL = ySize-2; }
					} else if (state[xL][yL] == Color.GREEN){
						itemResolver(true);
						state[xL][yL] = Color.RED;
					}else if (state[xL][yL]==Color.GRAY) {
						if( hpL == 0 ) { liveL = false; }
						hpL--;
						state[xL][yL] = Color.RED;//色の塗り替え
					} else {
						liveL = false;
					}
					state[xL][yL] = Color.RED;
				}  else {
					state[xL][yL] = Color.RED;
				}	
				
				if (state[xR][yR] != Color.WHITE) {
					if ( xR >= xSize-2 ) {
						if ( state[1][yR] == Color.WHITE) { xR = 1; } 
					} else if ( xR < 1) {
						if (state[xSize-2][yR] == Color.WHITE) { xR = xSize-2; } 
					} else if ( yR >= ySize-2) {
						if ( state[xR][1] == Color.WHITE) { yR = 1; } 
					} else if ( yR < 1) {
						if ( state[xR][ySize-2] == Color.WHITE) { yR = ySize-2; } 
					} else if (state[xR][yR] == Color.GREEN){
						itemResolver(false);
						state[xR][yR] = Color.BLUE;
					}else if(state[xR][yR]==Color.GRAY) {
						--hpR;

						if( hpR == 0 ) { liveR = false;}
						state[xR][yR] = Color.BLUE;//色の塗り替え
						hpR--;
					} else {
						liveR = false;
					}
					if(xR == xL && yR == yL) {
						liveL = false;
						state[xL][yL] = Color.MAGENTA.darker();
					}
				} else {
					state[xR][yR] = Color.BLUE;
				}

				if (!liveL) {
					if (!liveR) {
						message = "Draw!";
					} else {
						countR++;
						message = "R won!";
					}
				} else if (!liveR) {
					countL++;
					message = "L won!";
				}
				repaint();
				try{
					if ( remain < 20) {
						Thread.sleep(200);
					} else if(remain < 25) {
						Thread.sleep(250);
					} else {
						Thread.sleep(350);
					}
				} catch(InterruptedException e) {}
			}
			try{
				Thread.sleep(1750);
			} catch(InterruptedException e) {}
		}
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case 'A':  dxL = -1; dyL =  0; break;
		case 'S':  dxL =  0; dyL =  1; break;
		case 'W':  dxL =  0; dyL = -1; break;
		case 'D':  dxL =  1; dyL =  0; break;
		case 'J':  dxR = -1; dyR =  0; break;
		case 'K':  dxR =  0; dyR =  1; break;
		case 'I':  dxR =  0; dyR = -1; break;
		case 'L':  dxR =  1; dyR =  0; break;
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("MyDrooooon ...");
			frame.add(new MyDron());
			frame.pack();
			frame.setVisible(true);

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
	}
}
