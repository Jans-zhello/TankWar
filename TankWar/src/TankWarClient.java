import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
/**
 * ������̹�˴�ս�ͻ���
 * @zzz Administrator
 *
 */
public class TankWarClient extends Frame {
	Image offScreenImage = null;// ����˫���弼����Ļ�����ͼƬ
	Wall w1 = new Wall(100, 300, 20, 150, this);// ����ǽ1
	Wall w2 = new Wall(300, 100, 150, 20, this);// ����ǽ2
	Tank mytank = new Tank(600, 500, true, Tank.Direction.STOP, this);// ����������̹��
	List<Tank> tanks = new ArrayList<Tank>();// �������˵�̹�˼���
	List<Missile> missiles = new ArrayList<Missile>();// �����ӏ�����
	List<Explode> explodes = new ArrayList<Explode>();// ������ը����
    BloodRectangle bloodRectangle = new BloodRectangle();
	/**
	 * �O����Ϸ����
	 */
	public void launchFrame() {
		for (int i = 0; i < 10; i++) {
			tanks.add(new Tank(80 + 40 * (i + 1), 60, false, Tank.Direction.D,
					this));
		}
		this.setLocation(100, 100);
		this.setSize(Utils.screenWidth, Utils.screenHeight);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		new Thread(new PaintThread()).start();
	}

	/**
	 * �O��̹���ɫ����-paint����������
	 * 
	 * @param args
	 */
	public void paint(Graphics g) {
		g.drawString("�ӵ�����:" + missiles.size(), 10, 50);
		g.drawString("��ը����:" + explodes.size(), 10, 70);
		g.drawString("�з�̹������:" + tanks.size(), 10, 90);
		g.drawString("�췽����ֵ:" + mytank.getLife(), 10, 110);
		if (tanks.size() <= 0) {
			for (int i = 0; i < 10; i++) {
				tanks.add(new Tank(80 + 40 * (i + 1), 60, false, Tank.Direction.D,
						this));
			}
		}
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			if (m != null) {
				m.hitTanks(tanks);
				m.hitTank(mytank);
				m.hitWall(w1);
				m.hitWall(w2);
				m.draw(g);
			}
		}
		for (int i = 0; i < explodes.size(); i++) {
			Explode explode = explodes.get(i);
			if (explode != null) {
				explode.draw(g);
			}
		}
		for (int i = 0; i < tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if (tank != null) {
				tank.draw(g);
				tank.collidesWithWall(w1);
				tank.collidesWithWall(w2);
				tank.collidesWithTank(tanks);
			}
		}
		mytank.draw(g);
		mytank.eatBloodRec(bloodRectangle);
		bloodRectangle.draw(g);
		w1.draw(g);
		w2.draw(g);
	}

	/**
	 * �����Ļ��˸����-˫���弼��
	 */
	@Override
	public void update(Graphics g) {// gΪ��ǰ��Ļ�Ļ���
		if (offScreenImage == null) {
			offScreenImage = this.createImage(Utils.screenWidth, Utils.screenHeight);
		}
		Graphics goffScreenImage = offScreenImage.getGraphics();// goffScreenImageΪ��ǰ��Ļ����ͼƬ�Ļ���
		Color c = goffScreenImage.getColor();
		goffScreenImage.setColor(Color.GREEN);
		goffScreenImage.fillRect(0, 0, Utils.screenWidth, Utils.screenHeight);
		goffScreenImage.setColor(c);
		paint(goffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/**
	 * �����߳��ػ�paint()����
	 */

	private class PaintThread implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					repaint();
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��Ӽ��̼�������̹���ƶ�
	 */
	private class KeyMonitor extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			mytank.keyPress(e);
		}

		public void keyReleased(KeyEvent e) {
			mytank.keyReleased(e);
		}
	}

	/**
	 * main�������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TankWarClient().launchFrame();
	}

}
