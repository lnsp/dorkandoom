package de.mooxmirror.dorkandoom.utility;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class PNGAnimator extends Canvas {
	private List<BufferedImage> animationImages;
	private BufferStrategy bufferStrategy;
	private int counter;
	
	private int width, height;
	
	private final String imageSources =
			"res/images/smoke/smoke";
	private final int imageSize = 8;
	public static void main(String[] args) {
		PNGAnimator anim = new PNGAnimator(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		anim.run();
	}
	private void run() {
		init();
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
			draw();
		}
	}
	private void draw() {
		Graphics2D g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
		g2d.clearRect(0, 0, width, height);
		counter += 1;
		if (counter >= animationImages.size()) {
			counter = 0;
		}
		
		g2d.drawImage(animationImages.get(counter), 0, 0, this.width, this.height, null);
		g2d.dispose(); bufferStrategy.show();
	}
	private void init() {
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		animationImages = new ArrayList<BufferedImage>();
		for (int i = 0; i < imageSize; i++) {
			try {
				animationImages.add(ImageIO.read(new File(imageSources + "_" + i + ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public PNGAnimator(int width, int height) {
		super();
		this.width = width; this.height = height;
		JFrame window = new JFrame("PNGAnimator");
		window.setSize(width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,width,height);
		window.add(this);
		window.pack();
		window.setVisible(true);
	}
}
