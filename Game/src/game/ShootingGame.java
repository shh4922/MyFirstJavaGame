package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import audio.Audio;

public class ShootingGame extends JFrame {

	public static Game game;
	private Image screenImage;//������۸��� ���� �����̹���
	private Graphics screenGraphic;//������۸��� ���� ����׷���
	
	private JButton exitButton= new JButton(new ImageIcon("src/images/exit.png"));//�޴��ٿ� �� �����ư
	private JLabel menuBar= new JLabel(new ImageIcon("src/images/menuBar.png"));//��� �޴���
	
	private JButton startButton = new JButton(new ImageIcon("src/images/start.png"));//���ӽ��۹�ư
	private JButton helpButton = new JButton(new ImageIcon("src/images/help.png"));//���� ���︻��ư
	
	private Image backgrounimg= new ImageIcon("src/images/main.jpg").getImage();//����̹���

	boolean mainpage=true,gamepage=false;//�ʱ�ȭ��� ����ȭ���� �ĺ��ϱ����� ����
	
	private int mouseX,mouseY;//���콺�� Ŭ���� Ŭ���� x,y��ǥ�� �ޱ�����
	
	private Audio backgroundmusic;
	//������ ���۽� ����Ǵ°͵�
	private void start() {
		backgroundmusic.stop();
		mainpage=false;
		gamepage=true;
		game= new Game();
		game.start();
	}
	
	//ȭ�鿡 ������ UI��
	public void ui() {
		menuBar.setBounds(0,0,768,20);//�޴�����ġ ����
		menuBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX=e.getX();
				mouseY=e.getY();
			}
		});
		menuBar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x=e.getXOnScreen();
				int y=e.getYOnScreen();
				setLocation(x-mouseX,y-mouseY);//�޴��� ���콺�� �巡�׽�, ��ġ����
			}
		});
		add(menuBar);//�����ӿ� �޴��� �߰�.
		
		//�޴������� �ݱ��ư
		exitButton.setBounds(680,20,30,30);
		exitButton.setBorderPainted(false);//������
		exitButton.setContentAreaFilled(false);//������
		exitButton.setFocusPainted(false);//������
		exitButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				//���콺 Ŀ���� ������ ����� �ٲ��
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				//����
				System.exit(0);
			}
		});
		add(exitButton);
		
		startButton.setBounds(400,700, 300, 100);
		startButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				//���콺 Ŀ���� ������ ����� �ٲ��
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				startButton.setVisible(false);
				helpButton.setVisible(false);
				backgrounimg = new ImageIcon("src/images/gamepage.jpg").getImage();
				start();
			}
		});
		helpButton.setBounds(400,850, 300, 100);
		helpButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				//���콺 Ŀ���� ������ ����� �ٲ��
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				startButton.setVisible(false);
				helpButton.setVisible(false);
			}
		});
		
		add(helpButton);
		add(startButton);
	}
	
	//������ΰ͵� �߰��ؾ��Һκ�
	public void init() {
		setUndecorated(true);//�̷��� ���ϸ� �̹����� ��ư ���̾Ⱥ��̰� ��ư �̻���. �̷����ϰ� �޴��ٸ���°� ���ѵ��ϴ�.
		setTitle("1945");
		setSize(720,1082);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0,0,0,0));//������?
		setLayout(null);
		addKeyListener(new KeyListener());		
		backgroundmusic= new Audio("src/audio/menuBGM.wav",true); 
		backgroundmusic.start();
	 }
	
	
	public ShootingGame() {
		init();
		ui();
	}
	
	public void paint(Graphics g) {
		screenImage= createImage(Main.width,Main.heigh);
		screenGraphic=screenImage.getGraphics();
		screenDraw((Graphics2D)screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}
	public void screenDraw(Graphics2D g) {
		if(mainpage) {
			g.drawImage(backgrounimg, 0, 0, null);
		}
		if(gamepage) {
			g.drawImage(backgrounimg, 0, 0, null);
			game.screanDraw(g);
			game.enemyDraw(g);
			game.scoreDraw(g);
		}
		paintComponents(g);
		this.repaint();
	}
	
	class KeyListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if(ShootingGame.game==null) {
				return;
			}
			int keycode=e.getKeyCode();
			switch(keycode) {			
			case KeyEvent.VK_UP:
				game.setUp(true);
				break;
			case KeyEvent.VK_DOWN :
				game.setDown(true);
				break;
			case KeyEvent.VK_LEFT :
				game.setLeft(true);
				break;
			case KeyEvent.VK_RIGHT :
				game.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				game.setAttack(true);
				break;
			case KeyEvent.VK_R:
				if(game.isOver()) {
					game.reset();
				}
				break;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(ShootingGame.game==null) {
				return;
			}
			int keycode=e.getKeyCode();
			switch(keycode) {
			case KeyEvent.VK_UP:
				game.setUp(false);
				break;
				
			case KeyEvent.VK_DOWN :
				game.setDown(false);
				break;
			case KeyEvent.VK_LEFT :
				game.setLeft(false);
				break;
			case KeyEvent.VK_RIGHT :
				game.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				game.setAttack(false);
				break;
			}	
		}
	}
}
