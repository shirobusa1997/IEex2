import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Bong extends JPanel implements Runnable, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Thread thread = null;
	private double x, dx, y, dy;
	private int xSize, ySize;
	private double paddleXL, paddleYL, paddleXR, paddleYR;
	private double paddleSize;
	private String message;
	private Font font;

	private int width, height;

	public Bong() {
		setPreferredSize(new Dimension(481, 400));
		Dimension size = getPreferredSize();
		width = size.width; height = size.height;
		xSize = width; ySize = height - 80;
		paddleSize = 20;
		message = "Game started!";
		font = new Font("Monospaced", Font.PLAIN, 12);
		setFocusable(true);
		addKeyListener(this);

		startThread();
	}

	private void initialize() {
		x = 100; y = 100;
		dx = 3.2; dy = 2.0;
		paddleYL = paddleYR = ySize / 2;
		paddleXL = 30; paddleXR = xSize - 30;
	}

	@Override
	public void paintComponent(Graphics g) {
		// 全体を背景色で塗りつぶす。
		g.clearRect(0, 0, width, height); 

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, xSize - 1, ySize - 1);
		g.setColor(Color.MAGENTA.darker());
		g.fillOval((int)(x - 3), (int)(y - 3), 6, 6);

		g.setColor(Color.RED);
		g.fillRect((int)(paddleXL - 2), (int)(paddleYL - paddleSize / 2), 4, (int)paddleSize);
		g.setColor(Color.BLUE);
		g.fillRect((int)(paddleXR - 2), (int)(paddleYR - paddleSize / 2), 4, (int)paddleSize);

		g.setFont(font);
		g.setColor(Color.GREEN.darker());
		g.drawString(message, 5, ySize + 12);
		g.setColor(Color.RED.darker());
		g.drawString("Left:  Z(D), W(U)", 5, ySize + 24);
		g.setColor(Color.BLUE.darker());
		g.drawString("Right: M(D), I(U)", 5, ySize + 36);	
	}

	public void run() {
		Thread thisThread = Thread.currentThread();
		while (thread == thisThread) {
			initialize();
			requestFocus();
			while (true) {
				x += dx;  y += dy;
				if (dx < 0 && (x - paddleXL) * (x - dx - paddleXL) <= 0) {
					double rY = y + dy * (paddleXL - x) / dx;
					if ((rY - paddleYL + paddleSize / 2) * (rY - paddleYL - paddleSize / 2) <= 0) {
						x = 2 * paddleXL - x;
						dx *= -1;
						message = "";
					}
				}
				if (x < 0) {
					x = -x;
					dx *= -1;
					message = "R won!";
				}
				if (dx > 0 && (x - paddleXR) * (x - dx - paddleXR) <= 0) {
					double rY = y + dy * (paddleXR - x) / dx;
					if ((rY - paddleYR + paddleSize / 2) * (rY - paddleYR - paddleSize / 2) <= 0) {
						x = 2 * paddleXR - x;
						dx *= -1;
						message = "";
					}
				}
				if (x > xSize) {
					x = 2 * xSize - x;
					dx *= -1;
					message = "L won!";
				}
				if (y < 0) {
					y = -y;
					dy *= -1;
				}
				if (y > ySize) {
					y = 2 * ySize - y;
					dy *= -1;
				}
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
		}	
	}

	public void startThread() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stopThread() {
		thread = null;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case 'W':  paddleYL -= 10; break;
		case 'Z':  paddleYL += 10; break;
		case 'I':  paddleYR -= 10; break;
		case 'M':  paddleYR += 10; break;
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			/* タイトルバーに表示する文字列を指定できる */
			JFrame frame = new JFrame("Bong!");       
			/* Bong はクラスの名前にあわせる */
			/* アプレット部分のサイズを指定する */
			frame.add(new Bong());
			frame.pack();
			frame.setVisible(true);
			/* ×ボタンを押したときの動作を指定する */
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
	}
}
