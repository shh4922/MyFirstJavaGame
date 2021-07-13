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

	private Image player = new ImageIcon("src/images/player.png").getImage();// �̹��� �ҷ���

	private Image attackimg = new ImageIcon("src/images/player_attack.png").getImage();

	private int dmg = 10;// ���ݽ� ������
	private int playerX;
	private int playerY;
	private int spead = 10;// ����Ű������ �̵��ӵ�
	private int HP ;// playerü��

	private ArrayList<Enemy> enemylist = new ArrayList<Enemy>();
	private ArrayList<EnemyAttack> enemyattacklist = new ArrayList<EnemyAttack>();
	private Enemy enemy;
	private EnemyAttack enemyattack;

	ArrayList<PlayerAttack> attacklist = new ArrayList<PlayerAttack>();// �����̹����� ���� �迭
	private PlayerAttack playerattack;

	private boolean up = false, down = false, right = false, left = false, attack = false;
	private boolean isOver;
	// ����
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
					pretime = System.currentTimeMillis();// ����ð�������
					if (System.currentTimeMillis() - pretime < delay) {// ������۽ð�-ó���� �����ð���<20���� ������ ���� �� 0.002�� �ѹ���
																		// ���۰���?
						try {
							Thread.sleep(delay - System.currentTimeMillis() + pretime);// �ٽ�0.002����������
							keycontrol();
							playerAttackProcess();
							enemyAppearProcess();
							enemyMoveProcess();
							enemyAttackProcess();
							cnt++;// ���ϴ��� �����µ� �������� ���ۼӵ� ��� ���ؼ� ��� ���������ִ°Ͱ���. �Ƹ���
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

	// ���� �����ϴ� �޼ҵ�
	private void enemyAppearProcess() {
		if (cnt % 30 == 0) {
			enemy = new Enemy((int) (Math.random() * 600), 0);
			enemylist.add(enemy);
		}
	}

	private void enemyAttackProcess() {
		if (cnt % 40 == 0) {
			// ���� �¾�� ��ġ+����ü ũ����/2= �� �߾��� �������� x��ǥ�߾�
			// ����ü�� y��ǥ+ ����ü�� yũ�� = �� ������ �����Ǵ� y��ǥ
			// �������� �����Ǵ� ��ġ.
			enemyattack = new EnemyAttack(enemy.x + enemy.image.getWidth(null) / 2,
					enemy.y + enemy.image.getHeight(null));
			enemyattacklist.add(enemyattack);
		}

		for (int i = 0; i < enemyattacklist.size(); i++) {
			enemyattack = enemyattacklist.get(i);
			enemyattack.fire();
			// ����ü ���� ��������
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

	// ���� �Ʒ��� �������� �޼ҵ�
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
