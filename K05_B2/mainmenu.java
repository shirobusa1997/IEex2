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
import java.awt.event.*;

import javax.swing.*;

import java.util.Date;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class mainmenu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	public static final double GREEN_FREQUENCY = 0.008;
	public static final double GLAY_FREQUENCY = 0.004;
	private Color state[][];
	private int xSize, ySize;
	private int block;
	private int xL, yL, xR, yR;
	private int dxL, dyL, dxR, dyR;
	private int item_L, item_R;		// �A�C�e���ێ����(0:�Ȃ��A1:��Q��(����or����)�A2:��Q��(�����_��)�A3:���Z�b�g�{�^��)
	private boolean liveL, liveR;
	private Thread thread;
	private String  ItemName_L, ItemName_R, tmp;
	private Font font;
	private int width, height;
	private int countR, countL;
	public int hpR,hpL;
	public int directleft;
	public long st,ed,diff,remain, speedremain, startremain = 30;
	public Date start,end;
	BufferedImage imgback;
	String message = MyDron.getMessage();
	JFrame frame = new JFrame();
	AudioClip c = new AudioClip(new File("bgm/BGM3.mp3").toURI().toString());
	public mainmenu() {
		c.setCycleCount(AudioClip.INDEFINITE);
		c.play();
		setPreferredSize(new Dimension(320, 390));
		xSize = ySize = 80;
		block = 4;
		state = new Color[xSize][ySize];
		font = new Font("Monospaced", Font.PLAIN, 20);
		try {
			imgback = ImageIO.read(new File("./images/over.gif"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		setFocusable(true);
		JButton back = new JButton("Game Restart!");
		back.addActionListener(this);
		Dimension size = getPreferredSize();
		width = size.width; height = size.height;
		back.setBounds(10,10,10,10);
		add(back);
		repaint();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//MyDron.main().frame.setVisible(true);
		frame.add(new MyDron());
		frame.pack();
		frame.setVisible(true);
		c.stop();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.clearRect(0, 0, width, height); 
		g.drawImage(imgback,0,0,320, 390,null);
		tmp = message;
		g.setFont(font);
		g.drawString (tmp, 100,160);
		
	}

	
}