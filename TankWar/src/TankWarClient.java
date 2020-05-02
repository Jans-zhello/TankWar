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
 * 单机版坦克大战客户端
 * @zzz Administrator
 *
 */
public class TankWarClient extends Frame {
	Image offScreenImage = null;// 创建双缓冲技术屏幕背后的图片
	Wall w1 = new Wall(100, 300, 20, 150, this);// 创建墙1
	Wall w2 = new Wall(300, 100, 150, 20, this);// 创建墙2
	Tank mytank = new Tank(600, 500, true, Tank.Direction.STOP, this);// 创建男主角坦克
	List<Tank> tanks = new ArrayList<Tank>();// 创建敌人的坦克集合
	List<Missile> missiles = new ArrayList<Missile>();// 創建子彈集合
	List<Explode> explodes = new ArrayList<Explode>();// 创建爆炸集合
    BloodRectangle bloodRectangle = new BloodRectangle();
	/**
	 * 設置游戏窗口
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
	 * 設置坦克顏色屬性-paint方法画窗口
	 * 
	 * @param args
	 */
	public void paint(Graphics g) {
		g.drawString("子弹数量:" + missiles.size(), 10, 50);
		g.drawString("爆炸数量:" + explodes.size(), 10, 70);
		g.drawString("敌方坦克数量:" + tanks.size(), 10, 90);
		g.drawString("红方生命值:" + mytank.getLife(), 10, 110);
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
	 * 解决屏幕闪烁问题-双缓冲技术
	 */
	@Override
	public void update(Graphics g) {// g为当前屏幕的画笔
		if (offScreenImage == null) {
			offScreenImage = this.createImage(Utils.screenWidth, Utils.screenHeight);
		}
		Graphics goffScreenImage = offScreenImage.getGraphics();// goffScreenImage为当前屏幕背后图片的画笔
		Color c = goffScreenImage.getColor();
		goffScreenImage.setColor(Color.GREEN);
		goffScreenImage.fillRect(0, 0, Utils.screenWidth, Utils.screenHeight);
		goffScreenImage.setColor(c);
		paint(goffScreenImage);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/**
	 * 开启线程重画paint()方法
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
	 * 添加键盘监听控制坦克移动
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
	 * main方法入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TankWarClient().launchFrame();
	}

}
