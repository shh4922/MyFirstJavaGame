package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class EnemyAttack {
	Image image= new ImageIcon("src/images/enemy_attack.png").getImage();
	int x,y;
	int height = image.getHeight(null);
	int width = image.getWidth(null);
	int attack=10;
	
	public EnemyAttack(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	
	//����ü �����Լ�

	public void fire() {
		this.y+=12;
	}
	
}
