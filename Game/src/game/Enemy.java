package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Enemy {
	Image image= new ImageIcon("src/images/enemy.png").getImage();
	
	int x,y;//���� ��ǥ
	
	int width=image.getWidth(null);//�̹��� ���� ũ��
	int height=image.getHeight(null);//�̹��� ���� ũ��
	
	int hp=10;
	
	public Enemy(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	public void move() {
		this.y+=7;//�ӵ�
	}
}
