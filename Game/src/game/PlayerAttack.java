package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class PlayerAttack {
	Image img = new ImageIcon("src/images/player_attack.png").getImage();
	

	int x,y;
	int width=img.getWidth(null);
	int heigh= img.getHeight(null);
	int attack=5;
	
	public PlayerAttack(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void fire() {
		this.y-=15;
	}
}
