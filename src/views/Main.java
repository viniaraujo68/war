package views;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
	public static void main(String[] args) {

		Menu menu = new Menu();

		PrimFrame frame = new PrimFrame();
		frame.setTitle("War");
		frame.setResizable(false);
		frame.setLayout(null);
		frame.add(menu);
		
//		frame.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int x = e.getX() - 8;
//                int y = e.getY() - 32;
//                System.out.println("Clicou na posição (x, y): (" + x + ", " + y + ")");
//            }
//        });
		
		frame.setVisible(true);
	
	}
	
	
}