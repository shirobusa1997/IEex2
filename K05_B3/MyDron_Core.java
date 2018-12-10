import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class MyDron_Core extends JFrame{
	public MyDron_Core() {
		setBounds(0, 0, 320, 410);

		MyLayeredPane MyPane = new MyLayeredPane(this.getWidth(), this.getHeight());
		Container MyContainer = getContentPane();
		MyContainer.add(MyPane);

		setVisible(true);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				dispose();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new MyDron_Core();
		});
	}
}