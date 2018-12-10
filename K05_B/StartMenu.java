import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class StartMenu extends JPanel implements ActionListener{
	public MyLayeredPane instance;

	public StartMenu(MyLayeredPane tmp){
		super();

		instance = tmp;

		setLayout(null);

		setOpaque(true);

		JButton startButton = new JButton("GAME START!");
		startButton.setLocation(110, 200);
		startButton.setSize(100, 50);
		startButton.addActionListener(this);
		this.add(startButton);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.setFont(new Font(Font.SERIF, Font.BOLD, 50));
		g.drawString("MyDron...", 50, 100);
	}

	public void actionPerformed(ActionEvent event){
		instance.pushSTART();
	}
}