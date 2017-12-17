
package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import javazoom.jl.player.Player;
import sun.applet.Main;



public class game {

	public static void main(String[] args) {
		Shooting_Frame game = new Shooting_Frame();
	}

}

class Shooting_Frame extends Frame implements Runnable, KeyListener {
	boolean keyUp = false; // 위쪽 화살표가 눌러지지않은채로있다.
	boolean keyDown = false;// 아래쪽 화살표가 눌러지지않은채로있다.
	boolean keyLeft = false;// 왼쪽 화살표가 눌러지지않은채로있다.
	boolean keyRight = false;// 오른쪽 화살표가 눌러지지않은채로있다.
	boolean space = false;
	boolean shift = false;
	boolean p = false;
	boolean et = false;
	boolean r = false;
	
	ArrayList prpark = new ArrayList(); // 교수님을 저장하기위한 배열
	ArrayList paparray = new ArrayList(); // 족보의 오브젝트를 여러개 저장히기 위한 배열
	ArrayList ghostarray = new ArrayList(); // 교수님의 오브젝트를 여러개 저장히기 위한 배열
	
	proffessor pr; // 교수님 클래스의 접근자
	pap pa; // 컨닝페이퍼 클래스의 접근자
	ghostc gh;
	
	Image stdimg = new ImageIcon("학생.png").getImage();
	Image ghostimg = new ImageIcon("고스트.png").getImage();
	Image background = new ImageIcon("강의실.png").getImage();
	Image profimg = new ImageIcon("시공의교수님.jpg").getImage();
	Image papimg = new ImageIcon("컨닝페이퍼.png").getImage();
	Image level1 = new ImageIcon("초급.jpg").getImage();
	Image level2 = new ImageIcon("중급.png").getImage();
	Image level3 = new ImageIcon("고급.jpg").getImage();
	Image buffimg = null; // 더블버퍼링을 사용하기위한 버퍼이미지를 정의한다
	Graphics gc; // 오브젝트들을 그리기 위한 그래픽 툴을 정의한다
	
	int x = 400, y = 300; // 캐릭터의 시작 위치, 그리고 앞으로의 좌표를 받아오기 위한 변수
	int cnt = 0; // 쓰레드의 루프를 세는 변수, 각종 변수를 통제하기 위해 사용된다
	int ghostnum = 0;
	int ghosttime = 0;
	int mode = 0;
	int selectmode = 1;
	int life = 0; // 목숨
	int point = 0;
	int boost = 40;
	int parkappear = 0;
	boolean pause = false;
	boolean ghost = false;
	
	
	Shooting_Frame() {
		setTitle("SiGong Joa");
		setSize(800, 600);
		start(); // 쓰레드의 루프를 시작하기 위한 메써드
		setLocation(250, 80);
		setResizable(false); // 사이즈를 조절할 수 없게 만듬
		setVisible(true); // 프레임을 보이게 만듬
		
		
		
		
		this.addKeyListener(this); // 키리스너를 추가하여 방향키 정보를 받아올 수 있게 한다.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
		
	}
	
	
	
	public void update(Graphics g) {
		paint(g);
	} // 프레임 내의 버퍼이미지를 이용하여 깜빡임 현상을 완전히 없앱니다.

	
	
	public void paint(Graphics g) { // 각종 이미지를 그리기위한메서드를 실행시킨다
		buffimg = createImage(800, 600); // 버퍼이미지를 그린다 (떠블버퍼링을 사용하여 화면의 깜빡임을
											// 없앤다)
		gc = buffimg.getGraphics(); // 버퍼이미지에 대한 그래픽 객채를 얻어온다.
		drawimages(g);
	}
	
	public void drawimages(Graphics g) {
		if (mode == 0) {
			setBackground(Color.BLACK);
			if (selectmode == 1) {
				gc.drawImage(level1, 0,10, this);
			} else if (selectmode == 2) {
				gc.drawImage(level2, 0, 10, this);
			} else if (selectmode == 3) {
				gc.drawImage(level3, 0, 10, this);
			}
			g.drawImage(buffimg, 0, 0, this);
		} // 게임 난이도에 따라서 이미지를 나타냅니다.
		if (mode != 0) {
			backgroundDrawImg(); // 배경의 그림을 그린다
			profDrawImg(); // 교수님의 그림을 그린다
			papDrawImg(); // 컨닝페이퍼의 그림을 그린다
			ghostDrawImg(); // 고스트아이템을 그린다
			stdDrawImg(); // 학생의 그림을 그린다
			g.drawImage(buffimg, 0, 0, this); // 버퍼이미지를 그린다. 0,0으로 좌표를 맞춰서프레임크기에
												// 딱맞춘다
		}
	}
	
	public void backgroundDrawImg() {
		gc.drawImage(background, 0, 0, this); // 가져온 배경이미지파일을 0,0에 위치시킨다
	}
	
	public void stdDrawImg() {
		
		if (mode == 1) {
			
		} else if (mode == 2) {
			
		}
		if (pause == true) {
			gc.drawString("PAUSE", 340, 250); //일시정지 상태에서 PAUSE를 나타냅니다
		}
		MovestdImage(stdimg, x, y, 30, 30); // 캐릭터의 이미지를 좌표에 따라 그린다(루프가돌아가면서
											// 계속다시그리므로 움직이는것처럼 보임),크기는 30x30
	}
	
	public void profDrawImg() {
		for (int i = 0; i < prpark.size(); ++i) {
			pr = (proffessor) (prpark.get(i));
			gc.drawImage(profimg, pr.position[0], pr.position[1], this);
		}// 추가된 교수님의 수만큼 돌아다니는 시공의 그림을 추가한다.
	}
	
	public void papDrawImg() {
		for (int i = 0; i < paparray.size(); ++i) {
			pa = (pap) (paparray.get(i));
			gc.drawImage(papimg, pa.position[0], pa.position[1], this);
		}// 추가된 컨닝페이퍼의 수만큼 컨닝페이퍼의 이미지를 추가한다
	}
	
	public void ghostDrawImg() {
		for (int i = 0; i < ghostarray.size(); ++i) {
			gh = (ghostc) (ghostarray.get(i));
			gc.drawImage(ghostimg, gh.position[0], gh.position[1], this);
		}// 추가된 고스트아이템의 수만큼 아이템의 이미지를 추가한다
	}
	
	public void MovestdImage(Image stdimg, int x, int y, int width, int height) {
		gc.setClip(x, y, width, height); // 캐릭터의이미지의 좌표와 크기를 받아온다
		gc.drawImage(stdimg, x, y, this); // 캐릭터를 좌표에 따라 장소를 바꾸어 그린다.
	}
	
	public void papmove() {
		for (int i = 0; i < paparray.size(); ++i) {
			pa = (pap) (paparray.get(i)); // 컨닝페이퍼를 추가한다
			int dis1 = (int) Math.pow((x + 10) - (pa.position[0] + 10), 2);
			int dis2 = (int) Math.pow((y + 15) - (pa.position[1] + 15), 2);
			double dist = Math.sqrt(dis1 + dis2); // 캐릭터와 컨닝페이퍼의 거리를 구하는 알고리즘
			if (dist < 25) {
				point++;
				paparray.remove(i); // 거리가 20이하로 줄어들게되면 컨닝페이퍼가 사라지고 점수가 오르게 된다.
			}
		}
		if ((cnt) % 60 == 0) {
			int[] r = GenerateXNY(); // 랜덤으로 좌표를 받아온다
			pa = new pap(r[0], r[1]); // 받아온 좌표에 컨닝페이퍼를 추가시킨다.
			paparray.add(pa); // cnt/40의 시간이 지날때마다 하나의 컨닝페이퍼를 화면에 추가한다
			if (paparray.size() > 5) {
				life--;
				//gameover gg = new gameover((cnt) / 4 * point); // 점수를 넘겨 게임오버 프레임을 엽니다
				dispose(); // 게임은 닫습니다
			} // 아이템을안먹고 피하기만하면 점수얻기가 쉽기때문에 누적 아이템갯수가 5개가 넘어가면 죽습니다
		}
	}
	
	public void ghostmove() {
		if (mode == 1) {
			for (int i = 0; i < ghostarray.size(); ++i) {
				gh = (ghostc) (ghostarray.get(i)); // 폭탄을 추가한다
				int dis1 = (int) Math.pow((x + 10) - (gh.position[0] + 10), 2);
				int dis2 = (int) Math.pow((y + 15) - (gh.position[1] + 15), 2);
				double dist = Math.sqrt(dis1 + dis2); // 캐릭터와 폭탄의 거리를 구하는 알고리즘
				if (dist < 25) {
					ghostnum++;
					ghostarray.remove(i); //고스트아이템에 가까워지면 고스트아이템이 사라지고 소지갯수가 늘어납니다
				}
			}
			if ((cnt) % 800 == 0) {
				int[] r = GenerateXNY(); // 랜덤으로 좌표를 받아온다
				gh = new ghostc(r[0], r[1]); // 받아온 좌표에 컨닝페이퍼를 추가시킨다.
				ghostarray.add(gh); // cnt/40의 시간이 지날때마다 하나의 컨닝페이퍼를 화면에 추가한다
			}
		}
	}

	public void ghostmode(int a) {
		ghosttime = a;
		ghost = true; // 고스트를 호라성화시킵니다,
		ghostnum--; // 고스트모드가 켜지면 소지갯수가 줄어듭니다. 
	}

	public void ghostmode() {
		shift = false;
		if (ghosttime > 0) {
			ghosttime--; //고스트타임이 점점줄어듭니다
			if (ghosttime <= 0) {
				ghost = false; //고스트시간이 다되면 고스트모드를 종료합니다
			}
		}
	}
	
	public void start() {
		Thread th = new Thread(this); // 쓰레드 를 정의
		th.start(); // 쓰레드의 루프를 시작시킨다
	}

	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyLeft = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyRight = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyUp = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyDown = true;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (mode != 3) {
				space = true;
		} //고급모드에서는 부스터가 사용되지않으므로
		}
		else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (ghostnum > 0) {
				ghostmode(200);
			}// 고스트게이지를 200부터시작시킵니다.
				shift = true;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			p = true;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			r = true;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			et = true;
		}
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			keyLeft = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			keyRight = false;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			keyUp = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			keyDown = false;
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			space = false;
		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shift = false;
		} else if (e.getKeyCode() == KeyEvent.VK_P) {
			p = false;
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			r = false;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			et = false;
		}
	}

	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			while (life == 0) { // life가 0이하로 떨어지면 루프가 끝난다
				try {
					modeandpause();
					arrowkey(); // 받은 키에 따른 캐릭터의 이동을 통제
					repaint(); // 리페인트함수(그림을 그때그때 새로기리기위함)
					Thread.sleep(20); // 20밀리세컨드당 한번의 루프가 돌아간다
					if (pause == false) {
						if (mode != 0) {
							if (r == true) {
								reset(); //r을 누르면 리셋시킨다.
							}
							profmove(); // 교수님을 추가/움직이게 함
							papmove(); // 컨닝페이퍼의 추가메써드
							ghostmove();
							boostcontrol();
							cnt++; // 루프가 돌아간 횟수
						}
					}
				} catch (Exception e) {
				}
			}
	}
	
	public void boostcontrol() {
		if (space == false) { // 부스터 게이지를 통제하기 위한 if문
			if (boost < 40) {
				if (cnt % 20 == 0) {
					boost++; //부스터게이지가 space를 떼고있을때는 천천히 충전됩니다
				}
			}
		} else {
			if (boost > 0) {
				boost--; //space를 누르면 부스터게이지가 줄어듭니다
			} else {
				space = false;
			}
		}
	}
	
	
	
	public void arrowkey() { // 캐릭터의 이동속도와 방향키에 따른 이동방향을 결정하고, 캐릭터를 화면
		// 밖으로못빠져나가가게합니다. 그리고 부스터 또한 통제합니다
		if (mode == 0) {
			if (keyLeft == true) {
				selectmode--;
				if (selectmode == 0) {
					selectmode = 3;
				}
				keyLeft = false;
			}
			if (keyRight == true) {
				selectmode++;
				if (selectmode == 4) {
					selectmode = 1;
				}
				keyRight = false;
			}
		} else {
			if (keyUp == true && pause == false) {
				if (y > 0) {
					if (space == false) {
						y -= 8;
					} else {
						y -= 15;
					}
				}
			}
			if (keyDown == true && pause == false) {
				if (y < 570) {
					if (space == false) {
						y += 8;
					} else {
						y += 15;
					}
				}
			}
			if (keyLeft == true && pause == false) {
				if (x > 0) {
					if (space == false) {
						x -= 8;
					} else {
						x -= 15;
					}
				}
			}
			if (keyRight == true && pause == false) {
				if (x < 780) {
					if (space == false) {
						x += 8;
					} else {
						x += 15;
					}
				}
			}
			if (et == true) {
				mode = selectmode;
			}
		}
	}
	
	
	
	public void profmove() {
		ghostmode();
		for (int i = 0; i < prpark.size(); ++i) {
			pr = (proffessor) (prpark.get(i)); // 시공을 추가시킨다.
			pr.move(); // 교수님읭 움직임을 통제하는 메서드를 불러온다
			int dis1 = (int) Math.pow((x + 10) - (pr.position[0] + 10), 2);
			int dis2 = (int) Math.pow((y + 15) - (pr.position[1] + 15), 2);
			double dist = Math.sqrt(dis1 + dis2); // 교수님과 학생의 거리를 재는 알고리즘
			Random rand = new Random();
			if (dist < 14) {
				if (ghost == false) {
					life--;
					//gameover gg = new gameover((point * 10) * (cnt / 200)); // 거리가줄어들면게임오버
					dispose(); // 게임프레임은 닫습니다
				}
			}
		}
		if ((cnt) % parkappear == 0) {
			int[] r = GenerateXNY(); // 좌표를 랜덤으로 받아옵니다
			pr = new proffessor(r[0], r[1]); // 받아온 좌표에 cnt/100의 시간이 지날때마다
												// 교수님을추가시킵니다
			prpark.add(pr);
		}
	}
	
	int[] GenerateXNY() { // 좌표를 랜덤으로 불러오는 메써드
		Random rand = new Random();
		int x_rand = (rand.nextInt() % 550) + 100;
		x_rand = Math.abs(x_rand);
		int y_rand = (rand.nextInt() % 400) + 150;
		y_rand = Math.abs(y_rand);// x,y자표를 랜덤으로 받습니다
		int x_rand2 = (int) Math.pow(x - x_rand, 2);
		int y_rand2 = (int) Math.pow(y - y_rand, 2);
		double dist = Math.sqrt(x_rand2 + y_rand2); // 캐릭터와 받아온 좌표의 거리를 구합니다
		int[] res = new int[2];
		if (dist > 200) {
			res[0] = x_rand;
			res[1] = y_rand;
			return res; // 받아온 좌표가 캐릭터와 200거리 밖에 있다면, 좌표를 리턴시킵니다
		} else {
			return GenerateXNY(); // 좌표가 캐릭터와 200거리안에있다면, 다시 좌표를 정해 리턴시킵니다.
		}
	}
	

	public void modeandpause() {
		if (mode == 0) {
			if (et == true) {
				mode = selectmode; //엔터를 누르면 해당모드를 적용시킨 후 게임이 시작됩니다
				Music introMusic = new Music("introMusic.mp3", true);
				introMusic.start();
			}
			if (mode == 1) { // 초급에서 사용되는 아이템들입니다.
				ghostnum = 2;
				parkappear = 80; // 초급에서는 교수님의 생성 주기가깁니다
			} else if (mode == 2) {
				ghostnum = 0;
				parkappear = 65; 
			} else if (mode == 3) {
				ghostnum = 0;
				parkappear = 40; //고급에서는 교수님의 생성 주기가 짧습니다
			}
		}
		if (p == true) { // p를 누르면 게임이 멈춥니다
			pause = !pause;
			p = false;
		}
	}
	
	public void reset() { // 게임을 모두 초기상태로 돌립니다.
		prpark.removeAll(prpark);
		paparray.removeAll(paparray);
		ghostarray.removeAll(ghostarray);
		point = 0;
		mode = 0;
		cnt = 0;
		x = 400;
		y = 300;
	}
	
	
	class proffessor { // 교수님의클래스
		int position[] = new int[2]; // 교수님의 위치를 랜덤으로 받아오기 위한 변수
		private int velosity; // 교수님의 이동속도를 조절하기 위한 변수
		private int dst[] = new int[2]; // 교수님의 목적지를 정하기 위한 변수

		proffessor(int x, int y) {
			position[0] = x;
			position[1] = y;
			dst = GenerateXNY(); // 목적지를 랜덤으로 받아온다
			Random rand = new Random();
			velosity = (rand.nextInt() % 3) + 2; // 2와 4사이의 속도를 받아온다
		}

		int[] resetDst() { // 좌표를 다시 정해서 리턴시키는 메서드
			int res[] = new int[2];
			res = GenerateXNY();
			return res;
		}

		int[] GenerateXNY() { // 좌표를 랜덤으로 리턴시키는 메서드
			Random rand = new Random();
			int x_rand = (rand.nextInt() % 650) + 100;
			x_rand = Math.abs(x_rand);
			int y_rand = (rand.nextInt() % 450) + 100;
			y_rand = Math.abs(y_rand);
			int[] res = new int[2];
			res[0] = x_rand;
			res[1] = y_rand;
			return res;
		}

		public void move() { // 교수님의 움직임을 통제하기 위한 메서드
			if (position[0] >= dst[0] && position[0] >= 0) {
				position[0] = position[0] - velosity;
			}
			if (position[0] <= dst[0] && position[0] <= 800) {
				position[0] = position[0] + velosity;
			}

			if (position[1] >= dst[1] && position[1] >= 0) {
				position[1] = position[1] - velosity;
			}
			if (position[1] <= dst[1] && position[1] <= 600) {
				position[1] = position[1] + velosity;
			} // 교수님이 화면 밖으로 못벗어나는 범위 내에서 돌아다니게 만든다

			if (position[0] > 800) {
				dst = resetDst();
			}
			if (position[1] > 600) {
				dst = resetDst();
			}

			if (position[0] < 0) {
				dst = resetDst();
			}
			if (position[1] < 0) {
				dst = resetDst();
			} // 교수님이 만에하나 화면밖으로 넘어갔을 경우에 화면반대편으로 나오게하고, 목적지를 재설정한다
			if (Math.abs(position[0] - dst[0]) <= velosity * 2) {
				dst = resetDst();
			}
			if (Math.abs(position[1] - dst[1]) <= velosity * 2) {
				dst = resetDst();
			}
			// 교수님이 목적지 근처에 도착하였을 경우 목적지를 재설정한다
			if (position[0] > 800 || position[0] < velosity) {
				dst = resetDst();
			}
			if (position[1] > 600 || position[1] < velosity) {
				dst = resetDst();
			}// 교수님이 화면밖으로 넘어갈 경우 목적지를 재설정한다
		}
	}
	
	class pap { // 컨닝페이퍼의 클래스
		int position[] = new int[2]; // 컨닝페이퍼의 출현 위치를 정하기 위한 변수

		pap(int x, int y) {
			position[0] = x;
			position[1] = y;
		}
	}
	
	class ghostc { // 고스트아이템의 클래스
		int position[] = new int[2]; // 고스트 아이템의 출현 위치를 정하기 위한 변수

		ghostc(int x, int y) {
			position[0] = x;
			position[1] = y;
		}

		int[] GenerateXNY() { // 좌표를 랜덤으로 받아오는 메서드
			Random rand = new Random();
			int x_rand = (rand.nextInt() % 650) + 100;
			x_rand = Math.abs(x_rand);
			int y_rand = (rand.nextInt() % 450) + 100;
			y_rand = Math.abs(y_rand);
			int[] res = new int[2];
			res[0] = x_rand;
			res[1] = y_rand;
			return res;
		}
	}
	
	public class Music extends Thread {
		
		private Player player;
		private boolean isLoop;
		private File file;
		private FileInputStream fis;
		private BufferedInputStream bis;
		
		public Music(String name, boolean isLoop) {
			try {
				this.isLoop = isLoop;
				file = new File(Shooting_Frame.class.getResource("../music/" + name). toURI());
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		public int getTime() {
			if (player == null)
				return 0;
			return player.getPosition();
		}
		
		public void close() { // 음악을 종료하는 함수
			isLoop = false;
			player.close();
			this.interrupt();
		}
		@Override
		public void run() {
			try {
				do {
					player.play();
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					player = new Player(bis);
				} while(isLoop);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

}