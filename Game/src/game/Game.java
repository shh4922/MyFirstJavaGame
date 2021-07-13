package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import audio.Audio;

public class Game extends Thread {
	private int delay = 20;// ?
	private long pretime;// ?
	private int cnt;// ?
	private int score;

	private Image player = new ImageIcon("src/images/player.png").getImage();// 이미지 불러옴

	private Image attackimg = new ImageIcon("src/images/player_attack.png").getImage();

	private int dmg = 10;// 공격시 데미지
	private int playerX;
	private int playerY;
	private int spead = 10;// 방향키에따른 이동속도
	private int HP ;// player체력

	private ArrayList<Enemy> enemylist = new ArrayList<Enemy>();
	private ArrayList<EnemyAttack> enemyattacklist = new ArrayList<EnemyAttack>();
	private Enemy enemy;
	private EnemyAttack enemyattack;

	ArrayList<PlayerAttack> attacklist = new ArrayList<PlayerAttack>();// 공격이미지를 받을 배열
	private PlayerAttack playerattack;

	private boolean up = false, down = false, right = false, left = false, attack = false;
	private boolean isOver;
	// 사운드
	private Audio backgroundmusic;
	private Audio hitsound;

	public void screanDraw(Graphics2D g) {
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.green);
		g.fillRect(playerX, playerY + player.getHeight(null), HP, 20);
		for (int i = 0; i < attacklist.size(); i++) {
			playerattack = attacklist.get(i);
			g.drawImage(playerattack.img, playerattack.x, playerattack.y, null);
		}
	}

	public void enemyDraw(Graphics2D g) {
		for (int i = 0; i < enemylist.size(); i++) {
			enemy = enemylist.get(i);
			g.drawImage(enemy.image, enemy.x, enemy.y, null);
		}

		for (int i = 0; i < enemyattacklist.size(); i++) {
			enemyattack = enemyattacklist.get(i);
			g.drawImage(enemyattack.image, enemyattack.x, enemyattack.y, null);
		}
	}
	
	public void scoreDraw(Graphics2D g) {
		g.setColor(Color.black);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("SCORE:"+score, 550, 50);
		if(isOver) {
			g.setColor(Color.DARK_GRAY);
			g.setFont(new Font("Arial",Font.BOLD,80));
			g.drawString("Reset press 'R' ", 100, 500);
		}
	}

	@Override
	public void run() {

		backgroundmusic = new Audio("src/audio/gameBGM.wav", true);
		hitsound = new Audio("src/audio/hitsound.wav", true);
		backgroundmusic.start();

		reset();
		try {
			while (true) {
				while (!isOver) {
					pretime = System.currentTimeMillis();// 현재시간가져옴
					if (System.currentTimeMillis() - pretime < delay) {// 실행시작시간-처음의 현제시간이<20보다 작으면 실행 즉 0.002에 한번씩
																		// 동작가능?
						try {
							Thread.sleep(delay - System.currentTimeMillis() + pretime);// 다시0.002딜레이이후
							keycontrol();
							playerAttackProcess();
							enemyAppearProcess();
							enemyMoveProcess();
							enemyAttackProcess();
							cnt++;// 왜하는지 몰랐는데 스레드의 동작속도 제어를 위해서 계속 증가시켜주는것같다. 아마도
							System.out.println(cnt);
						} catch (InterruptedException e) {
							System.out.println(e);
						}
					}
				}
				try {
					Thread.sleep(100);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void reset()
	{
		playerX = Main.width / 2;
		playerY = Main.heigh - 200;
		HP=100;
		score=0;
		cnt = 0;
		backgroundmusic.start();
		attacklist.clear();
		enemylist.clear();
		enemyattacklist.clear();
		isOver=false;
	}
	public void keycontrol() {
		if (up && playerY > 20)
			playerY -= spead;
		if (down && playerY < 850)
			playerY += spead;
		if (right && playerX < 620)
			playerX += spead;
		if (left && playerX > 0)
			playerX -= spead;
		if (attack && cnt % 10 == 0) {
			playerattack = new PlayerAttack(playerX + player.getWidth(null) / 2, playerY);
			attacklist.add(playerattack);
			System.out.println(player.getWidth(null));
		}
	}

	public void playerAttackProcess() {
		for (int i = 0; i < attacklist.size(); i++) {
			playerattack = attacklist.get(i);
			playerattack.fire();

			for (int j = 0; j < enemylist.size(); j++) {
				enemy = enemylist.get(j);
				if (playerattack.x > enemy.x && playerattack.x < enemy.x + enemy.width && playerattack.y > enemy.y
						&& playerattack.y < enemy.y + enemy.height) {
					hitsound.start();
					enemy.hp -= playerattack.attack;
					attacklist.remove(playerattack);
				}
				if (enemy.hp <= 0) {
					enemylist.remove(enemy);
					score+=100;
				}
			}
		}
	}

	// 적이 출현하는 메소드
	private void enemyAppearProcess() {
		if (cnt % 30 == 0) {
			enemy = new Enemy((int) (Math.random() * 600), 0);
			enemylist.add(enemy);
		}
	}

	private void enemyAttackProcess() {
		if (cnt % 40 == 0) {
			// 적이 태어나는 위치+적기체 크기의/2= 그 중앙이 적공격의 x좌표중앙
			// 적기체의 y좌표+ 적기체의 y크기 = 적 공격이 스폰되는 y좌표
			// 적공격이 스폰되는 위치.
			enemyattack = new EnemyAttack(enemy.x + enemy.image.getWidth(null) / 2,
					enemy.y + enemy.image.getHeight(null));
			enemyattacklist.add(enemyattack);
		}

		for (int i = 0; i < enemyattacklist.size(); i++) {
			enemyattack = enemyattacklist.get(i);
			enemyattack.fire();
			// 적기체 공격 어택판정
			if(enemyattack.x+enemyattack.width/2>=playerX && enemyattack.x+enemyattack.width/2<=playerX+player.getWidth(null) && enemyattack.y+enemyattack.height>=playerY && enemyattack.y<playerY+player.getHeight(null)) {
				hitsound.start();
				HP -= enemyattack.attack;
				enemyattacklist.remove(enemyattack);
			}
			if(HP<=0) {
				isOver=true;
			}
		}
	}

	// 적이 아래로 내려오는 메소드
	public void enemyMoveProcess() {
		for (int i = 0; i < enemylist.size(); i++) {
			enemy = enemylist.get(i);
			enemy.move();
		}
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public boolean isOver() {
		return isOver;
	}
}
