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
	private Image screenImage;//더블버퍼링을 위한 여백이미지
	private Graphics screenGraphic;//더블버퍼링을 위한 여백그래픽
	
	private JButton exitButton= new JButton(new ImageIcon("src/images/exit.png"));//메뉴바에 들어갈 종료버튼
	private JLabel menuBar= new JLabel(new ImageIcon("src/images/menuBar.png"));//상당 메뉴바
	
	private JButton startButton = new JButton(new ImageIcon("src/images/start.png"));//게임시작버튼
	private JButton helpButton = new JButton(new ImageIcon("src/images/help.png"));//게임 도울말버튼
	
	private Image backgrounimg= new ImageIcon("src/images/main.jpg").getImage();//배경이미지

	boolean mainpage=true,gamepage=false;//초기화면과 게임화면을 식별하기위해 만듬
	
	private int mouseX,mouseY;//마우스로 클릭시 클릭한 x,y좌표값 받기위함
	
	private Audio backgroundmusic;
	//게임이 시작시 실행되는것듯
	private void start() {
		backgroundmusic.stop();
		mainpage=false;
		gamepage=true;
		game= new Game();
		game.start();
	}
	
	//화면에 구성될 UI들
	public void ui() {
		menuBar.setBounds(0,0,768,20);//메뉴바위치 설정
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
				setLocation(x-mouseX,y-mouseY);//메뉴바 마우스로 드래그시, 위치변경
			}
		});
		add(menuBar);//프레임에 메뉴바 추가.
		
		//메뉴바위에 닫기버튼
		exitButton.setBounds(680,20,30,30);
		exitButton.setBorderPainted(false);//뭐였지
		exitButton.setContentAreaFilled(false);//뭐였지
		exitButton.setFocusPainted(false);//뭐였지
		exitButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				//마우스 커서가 닫으면 모양이 바뀌게
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				//종료
				System.exit(0);
			}
		});
		add(exitButton);
		
		startButton.setBounds(400,700, 300, 100);
		startButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				//마우스 커서가 닫으면 모양이 바뀌게
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
				//마우스 커서가 닫으면 모양이 바뀌게
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
	
	//기능적인것들 추가해야할부분
	public void init() {
		setUndecorated(true);//이렇게 안하면 이미지랑 버튼 같이안보이고 무튼 이상함. 이렇게하고 메뉴바만드는게 편한듯하다.
		setTitle("1945");
		setSize(720,1082);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0,0,0,0));//배경흰색?
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
