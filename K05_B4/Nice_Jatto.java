import java.io.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.imageio.ImageIO;

public class Nice_Jatto extends JPanel{
	// メンバ変数宣言
	// 画像を読み込むためのコンポーネントのインスタンス
	BufferedImage img;
	int pos_x = 0, pos_y = 0;

	// Nice_Jattoクラスのコンストラクタ
	public Nice_Jatto () {
		// ウィンドウサイズの指定
		setPreferredSize(new Dimension(1280, 720));

		try {
			// 画像ファイルの取得とBufferedImageインスタンスへの保持
			img = ImageIO.read(new File("test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// GUI上に画像を描画
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.drawImage(img, pos_x, pos_y, null);
	}

	// メインメソッド
	public static void main(String[] args){
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("すごーい！君は画像を表示できるフレンズなんだね！");
			frame.add(new Nice_Jatto());
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		});
	}
}