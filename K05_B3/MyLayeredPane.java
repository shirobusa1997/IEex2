import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class MyLayeredPane extends JLayeredPane implements MyInterface{
	public int m_width, m_height;
	public MainMenu menuPanel;
	public MyDron gamePanel;
	public MyInterface m_IF;

	public MyLayeredPane(int width, int height) {
		super();

		m_width = width;	m_height = height;

		gamePanel = new MyDron();
		gamePanel.setBounds(0, 0, width, height);

		this.add(gamePanel);
		this.setLayer(gamePanel, -1);

		menuPanel = new MainMenu(this);
		menuPanel.setBounds(0, 0, width, height);

		this.add(menuPanel);
		this.setLayer(menuPanel, 0);
	}

	public void pushSTART(){
		for(int dy = 0; dy >= -420; dy = dy - 10){
			menuPanel.setBounds(0, dy, m_width, m_height);
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gamePanel.startGame();
	}

	public void startGame(){}
}