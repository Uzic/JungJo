
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
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;


public class game {

	public static void main(String[] args) {
		Shooting_Frame game = new Shooting_Frame();
	}

}

class Shooting_Frame extends Frame implements Runnable, KeyListener {
	boolean keyUp = false; // ���� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyDown = false;// �Ʒ��� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyLeft = false;// ���� ȭ��ǥ�� ������������ä���ִ�.
	boolean keyRight = false;// ������ ȭ��ǥ�� ������������ä���ִ�.
	boolean space = false;
	boolean shift = false;
	boolean p = false;
	boolean et = false;
	boolean r = false;
	
	
		Image background = new ImageIcon("���ǽ�.png").getImage();
	Image buffimg = null; // ������۸��� ����ϱ����� �����̹����� �����Ѵ�
	Graphics gc; // ������Ʈ���� �׸��� ���� �׷��� ���� �����Ѵ�
	
	int x = 400, y = 300; // ĳ������ ���� ��ġ, �׸��� �������� ��ǥ�� �޾ƿ��� ���� ����
	int cnt = 0; // �������� ������ ���� ����, ���� ������ �����ϱ� ���� ���ȴ�
	int mode = 0;
	int selectmode = 1;
	int life = 0; // ���
	int parkappear = 0;
	boolean pause = false;
	boolean ghost = false;
	
	Shooting_Frame() {
		setTitle("SiGong Joa");
		setSize(800, 600);
		start(); // �������� ������ �����ϱ� ���� �޽��
		setLocation(250, 80);
		setResizable(false); // ����� ������ �� ���� ����
		setVisible(true); // �������� ���̰� ����
		this.addKeyListener(this); // Ű�����ʸ� �߰��Ͽ� ����Ű ������ �޾ƿ� �� �ְ� �Ѵ�.
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
	} // ������ ���� �����̹����� �̿��Ͽ� ������ ������ ������ ���۴ϴ�.

	
	
	public void paint(Graphics g) { // ���� �̹����� �׸������Ѹ޼��带 �����Ų��
		buffimg = createImage(800, 600); // �����̹����� �׸��� (������۸��� ����Ͽ� ȭ���� ��������
											// ���ش�)
		gc = buffimg.getGraphics(); // �����̹����� ���� �׷��� ��ä�� ���´�.
		drawimages(g);
	}
	
	public void drawimages(Graphics g) {
		if (mode == 0) {
			setBackground(Color.BLACK);
			g.drawImage(buffimg, 0, 0, this);
		} // ���� ���̵��� ���� �̹����� ��Ÿ���ϴ�.
		if (mode != 0) {
			backgroundDrawImg(); // ����� �׸��� �׸���
			g.drawImage(buffimg, 0, 0, this); // �����̹����� �׸���. 0,0���� ��ǥ�� ���缭������ũ�⿡
												// �������
		}
	}
	
	public void backgroundDrawImg() {
		gc.drawImage(background, 0, 0, this); // ������ ����̹��������� 0,0�� ��ġ��Ų��
	}

public void start() {
		Thread th = new Thread(this); // ������ �� ����
		th.start(); // �������� ������ ���۽�Ų��
	}



@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}



@Override
public void keyPressed(KeyEvent e) {
	// TODO Auto-generated method stub
	
}



@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}



@Override
public void run() {
	// TODO Auto-generated method stub
	
	}
}


