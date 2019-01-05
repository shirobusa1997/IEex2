import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;

import javafx.scene.media.AudioClip;
import javafx.scene.transform.Rotate;

import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import java.util.Date;
import java.util.Random;

public class MyDron extends JPanel implements Runnable, KeyListener, MyInterface {

	private static final long serialVersionUID = 1L;

	public static final double GREEN_FREQUENCY = 0.008;
	public static final double GLAY_FREQUENCY = 0.004;
	private Color state[][];
	private int xSize, ySize;
	private int block;
	private int xL, yL, xR, yR;
	private int dxL, dyL, dxR, dyR;
	private int rotL, rotR;
	private int item_L, item_R;		// アイテム保持状態(0:なし、1:障害物(相手or自分)、2:障害物(ランダム)、3:リセットボタン)
	private boolean liveL, liveR;
	private Thread thread;
	private String message, ItemName_L, ItemName_R, tmp;
	private Font font;
	private int width, height;
	private int countR, countL;
	public int hpR,hpL;
	public int directleft;
	public long st,ed,diff,remain, speedremain, startremain = 30;
	public Date start,end;
	BufferedImage imgleft,imgright;
	AffineTransform af_L, af_R;
	
    AudioClip se1 = new AudioClip(new File("./BGM/SE1.mp3").toURI().toString());
	AudioClip se2 = new AudioClip(new File("./BGM/SE2.mp3").toURI().toString());
	Random rnd_inst = new Random();
	Random rnd2_inst = new Random();//念のため二つ乱数を生成
	
	private void initialize(boolean isInGame) {
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
					if( (i >= 10 && j >= 10) && (i <= xSize-10 && j <= ySize-10) ) { //スタート地点から障害物の発生を防ぐ　
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
		rotL = 180; rotR = 0;

		if (!isInGame)
		{
			hpR = 3; hpL = 3;		// 障害物衝突時のHP管理的なもの 
			liveL = liveR = true;	// 生存判定フラグ
			message = "Game started!";
		} else { message = ">>盤面リセット<<"; }
				ItemName_L = "なし"; ItemName_R = "なし";	// アイテム保有状態(初期化)
	}


	public MyDron() {
		setPreferredSize(new Dimension(320, 390));
		
		xSize = ySize = 80;
		block = 4;
		state = new Color[xSize][ySize];
		message = "NO MESSAGE";
		font = new Font("Monospaced", Font.PLAIN, 12);
		Dimension size = getPreferredSize();
		width = size.width; height = size.height;
		try {
			imgleft = ImageIO.read(new File("./images/carred.gif"));
			imgright = ImageIO.read(new File("./images/carblue.gif"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		af_L = new AffineTransform(); af_R = new AffineTransform();
		
	}

	public void pushSTART(){}

	public void startGame(){
		AudioClip c = new AudioClip(new File("bgm/BGM1.mp3").toURI().toString());
		
		c.setCycleCount(AudioClip.INDEFINITE);
		c.play();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFocusable(true);
		addKeyListener(this);
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

	public void itemResolver(boolean GET_or_USE, boolean is1P) {
		double dRandNum = rnd_inst.nextDouble();
		int xMin, xMax, yMin, yMax;

		if (GET_or_USE) {
			// 入手処理
			if (dRandNum < 0.50)
			{
				if(is1P){ item_L = 1; ItemName_L = "障害物設置"; }
				else 	{ item_R = 1; ItemName_R = "障害物設置"; }
			} else if (dRandNum < 0.8)
			{
				if(is1P){ item_L = 2; ItemName_L = "障害物設置(ランダム)"; }
				else 	{ item_R = 2; ItemName_R = "障害物設置(ランダム)"; }
			} else
			{
				if(is1P){ item_L = 3; ItemName_L = "盤面リセットボタン"; }
				else 	{ item_R = 3; ItemName_R = "盤面リセットボタン"; }
			}
		} else {
			int item = (is1P)? item_L : item_R;

			switch(item){
				case 1:		
					// 反転します。
					boolean flag = (rnd_inst.nextDouble() > 0.8)? !is1P : is1P;
					if (flag)
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
								state[dx][dy] == Color.WHITE &&
								state[dx+1][dy] == Color.WHITE &&
								state[dx][dy+1] == Color.WHITE &&
								state[dx+1][dy+1] == Color.WHITE)
							{
								state[dx][dy] = Color.GRAY;
								state[dx+1][dy] = Color.GRAY;
								state[dx][dy+1] = Color.GRAY;
								state[dx+1][dy+1] = Color.GRAY;
							}
						}
					}
					se1.play();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case 2:		
					// 障害物配置(ランダム)
					xMin = yMin = 2;
					xMax = xSize - 3;	yMax = ySize - 3;
					for(int dy = yMin; dy < yMax; dy++){
						for(int dx = xMin; dx < xMax; dx++){
							if (rnd_inst.nextDouble() < 0.005 && 
								state[dx][dy] == Color.WHITE &&
								state[dx+1][dy] == Color.WHITE &&
								state[dx][dy+1] == Color.WHITE && 
								state[dx+1][dy+1] == Color.WHITE)
							{
								state[dx][dy] = Color.GRAY;
								state[dx+1][dy] = Color.GRAY;
								state[dx][dy+1] = Color.GRAY;
								state[dx+1][dy+1] = Color.GRAY;
							}
						}
					}
					se1.play();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case 3:
					// 盤面リセット
					initialize(true);
					se2.play();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				default:	break;
			}

			// アイテム消費
			if(is1P){ item_L = 0; ItemName_L = "なし"; }
			else 	{ item_R = 0; ItemName_R = "なし"; }
		}

		return;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gL = (Graphics2D) g;
		Graphics2D gR = (Graphics2D) g;
		
		g.clearRect(0, 0, width, height); 
		String left, left_i, right, right_i;
		int i, j;
		for (i = 0; i < xSize; i++) {
			for (j = 0; j < ySize; j++) {
				g.setColor(state[i][j]);
				g.fillRect(i * block, j * block, block, block);
			}
		}
		
		tmp = "残り時間:"+remain + "秒 " + message;
		left    = "Left :  A(L), S(D), W(U), D(R)  残基: "+hpL+"Life";
		left_i  = "        アイテム「" + ItemName_L + "」 Eで発動！";
		right   = "Right:  J(L), K(D), I(U), L(R)  残基: "+hpR+"Life";
		right_i = "        アイテム「" + ItemName_R + "」 Uで発動！";
		g.setFont(font);
		g.setColor(Color.GREEN.darker());
		g.drawString (tmp, 2 * block, block * (ySize + 3));
		g.setColor(Color.RED.darker());
		g.drawString(left, 2 * block, block * (ySize + 6));
		g.drawString(left_i, 2 * block, block * (ySize + 9));
		g.setColor(Color.BLUE.darker());
		g.drawString(right, 2 * block, block * (ySize + 12));
		g.drawString(right_i, 2 * block, block * (ySize + 15));
		// あ右入力したときに回転させたい
		
		gL.drawImage(imgleft, (xL-1) * block, yL*block, block * 3, block * 3, null);
		gR.drawImage(imgright, (xR-1) * block, yR * block, block * 3, block * 3, null);
		if ( directleft == 1) {
			af_L.rotate(90 * Math.PI / 180.0, block * xL, block * yL);
			gL.setTransform(af_L);
			gL.drawImage(imgleft, xL * block, yL * block, block * 3, block * 3, null);
		}

		
	}


	public void run() {
		Thread thisThread = Thread.currentThread();
		while (thisThread == thread) {
			start = new Date();
			st = start.getTime();
			initialize(false);
			requestFocus();
			while (liveL && liveR) {
				end = new Date();
				ed = end.getTime();
				diff = (ed -st)/1000;
				speedremain = 30 - diff;
				remain = startremain - diff;
				if ( remain < 0 ) { 
					if ( hpL < hpR) {
						liveL = false;
					} else if ( hpL > hpR) {
						liveR = false;
					} else {
						liveL = false; liveR = false;
					}
					break; 
				} 
				xL += dxL; yL += dyL;
				xR += dxR; yR += dyR;
				if (state[xL][yL] != Color.WHITE) {
					if ( xL >= xSize-1 ) {
						if (state[1][yL] != Color.RED || state[1][yL] != Color.BLUE) { xL = 1; }
					} else if ( xL < 1 ) {
						if ( state[xSize-2][yL] != Color.RED || state[xSize-2][yL] != Color.BLUE) { xL = xSize-2; }
					} else if ( yL >= ySize -1) {
						if ( state[xL][1] != Color.RED || state[xL][1] != Color.BLUE) { yL = 1; }
					} else if ( yL < 1) {
						if ( state[xL][ySize-2] != Color.RED || state[xL][ySize-2] != Color.BLUE) {yL = ySize-2; }
					} else if (state[xL][yL] == Color.GREEN){
						itemResolver(true, true);
					}else if (state[xL][yL]==Color.GRAY) {
						--hpL;
						if( hpL == 0 ) { liveL = false; }
					} else {
						liveL = false;
					}
					state[xL][yL] = Color.RED;
				}  else {
					state[xL][yL] = Color.RED;
				}	
				
				if (state[xR][yR] != Color.WHITE) {
					if ( xR >= xSize-1 ) {
						if ( state[1][yR] != Color.RED || state[1][yR] != Color.BLUE) { xR = 1; } 
					} else if ( xR < 1) {
						if (state[xSize-2][yR] != Color.RED || state[xSize-2][yR] != Color.BLUE) { xR = xSize-2; } 
					} else if ( yR >= ySize-1) {
						if ( state[xR][1] != Color.RED || state[xR][1] != Color.BLUE) { yR = 1; } 
					} else if ( yR < 1) {
						if ( state[xR][ySize-2] != Color.RED || state[xR][ySize-2] != Color.BLUE) { yR = ySize-2; } 
					} else if (state[xR][yR] == Color.GREEN){
						itemResolver(true, false);
					}else if(state[xR][yR]==Color.GRAY) {
						--hpR;
						if( hpR == 0 ) { liveR = false;}
					} else {
						liveR = false;
					}
					state[xR][yR] = Color.BLUE;
					if(xR == xL && yR == yL) {
						liveL = false;
						state[xL][yL] = Color.MAGENTA.darker();
					}
				} else {
					state[xR][yR] = Color.BLUE;
				}

				if (!liveL) {
					if (!liveR) {
						message = "引き分け!";
					} else {
						countR++;
						message = "青色の勝ち!";
					}
					hpR = 3; hpL = 3;
					startremain = 30;
				} else if (!liveR) {
					countL++;
					message = "赤色の勝ち!";
					hpR = 3; hpL = 3;
					startremain = 30;
				}
				repaint();
				try{
					if ( remain < 20) {
						Thread.sleep(150);
					} else if(remain < 25) {
						Thread.sleep(200);
					} else {
						Thread.sleep(250);
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
		case 'A': if ( dxL != 1)  { dxL = -1;}  dyL =  0; rotL = 90;  break;
		case 'S': if ( dyL != -1) { dyL =  1;}  dxL =  0; rotL = 180; break;
		case 'W': if ( dyL != 1)  { dyL = -1;}  dxL =  0; rotL = 0;   break;
		case 'D': if ( dxL != -1) { dxL =  1;}  dyL =  0; rotL = 270; directleft = 1; break;
		case 'J': if ( dxR != 1)  { dxR = -1;}  dyR =  0; rotL = 90;  break;
		case 'K': if ( dyR != -1) { dyR =  1;}  dxR =  0; rotL = 180; break;
		case 'I': if ( dyR != 1)  { dyR = -1;}  dxR =  0; rotL = 0;   break;
		case 'L': if ( dxR != -1) { dxR =  1;}  dyR =  0; rotL = 270; break;
		case 'E':  itemResolver(false, true);  break;
		case 'U':  itemResolver(false, false); break;

		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	/*
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("MyDrooooon ...");
			frame.add(new MyDron());
			frame.pack();
			frame.setVisible(true);
			
			AudioClip c = new AudioClip(new File("bgm/BGM1.mp3").toURI().toString());
			
			c.setCycleCount(AudioClip.INDEFINITE);
			c.play();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
	}*/
}