package se.fearlessgames.common.ui.swing.slidertext;

import javax.swing.*;
import java.awt.*;

public class SlidertextTest {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test frame");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setPreferredSize(new Dimension(300, 300));
		frame.getContentPane().add(new JTextSlider(0, 10, 0.1, 1));
		frame.pack();
		frame.setVisible(true);
		System.out.println("Hello world");
	}
}
