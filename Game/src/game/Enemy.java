package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Enemy {
	Image image= new ImageIcon("src/images/enemy.png").getImage();
	
	int x,y;//적의 좌표
	
	int width=image.getWidth(null);//이미지 가로 크기
	int height=image.getHeight(null);//이미지 세로 크기
	
	int hp=10;
	
	public Enemy(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	public void move() {
		this.y+=7;//속도
	}
}
