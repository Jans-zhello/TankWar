import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
/**
 * ̹����
 * @zzz Administrator
 *
 */
public class Tank {

	private int life = 100;// ������̹�˵�����ֵ
	private BloodBar bbBar = new BloodBar();// ������̹��Ѫ��
	private int x;// ̹���ƶ�����x
	private int y;// ̹���ƶ�����y

	private int oldX;// ��¼�ϴ�̹�˵�xλ��
	private int oldY;// ��¼�ϴ����˵�yλ��

	public static final int XSPEED = 5;// ̹��x�ƶ��ٶ�
	public static final int YSPEED = 5;// ̹��y�ƶ��ٶ�

	public static final int WIDTH = 30;// ̹�˿��
	public static final int HEIGHT = 30;// ̹�˸߶�

	TankWarClient tc;// ����TankWarClient������
	private boolean good;// �ж��Ƿ��ǔ���̹��
	private boolean live = true;// �ж�̹���Ƿ����
	private int step = Utils.r.nextInt(12) + 3;// ����̹��ת�����������,��ʵ��̹�˿���ǰ����Ч��

	/**
	 * ����̹�˰˸������ƶ�
	 */
	private boolean bL = false, bR = false, bU = false, bD = false;

	// ö������
	enum Direction {
		L, LU, LD, R, RU, RD, U, D, STOP
	};

	// ö������ת��������
	Direction[] dirs = Direction.values();

	// ̹��Ĭ���ƶ�����
	private Direction dir = Direction.STOP;
	// ̹����Ͳ�ķ���
	private Direction ptDir = Direction.D;

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, Tank.Direction dir, TankWarClient tc) {
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}

	/**
	 * ���û��ʻ���̹��
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!live) {
			if (!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLUE);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		// ��ʾ������Ѫ��
		if (good) {
			bbBar.draw(g);
		}
		// �ж���Ͳ���򻭳���Ͳ
		switch (ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y
					+ Tank.HEIGHT / 2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
			break;
		case LD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y
					+ Tank.HEIGHT);
			break;
		case R:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y + Tank.HEIGHT / 2);
			break;
		case RU:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH,
					y + Tank.HEIGHT);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH
					/ 2, y);
			break;
		default:
			g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH
					/ 2, y + Tank.HEIGHT);
			break;
		}
		move();
	}

	/**
	 * ���ݼ��̰��£�����̹���ƶ��Ĳ���ֵ
	 * 
	 * @param e
	 */
	public void keyPress(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_F2:
			if (!this.isLive()) {
				this.live = true;
				this.life = 100;
			}
			break;
		case KeyEvent.VK_CONTROL:
			tc.missiles.add(fire());// ����ctrl�����һ���ڵ�
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		default:
			break;
		}
		locationDirection();
	}

	/**
	 * ���ݼ���̧�𣬿���̹���ƶ�����Ĳ���ֵ
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		default:
			break;
		}
		locationDirection();
	}

	/**
	 * ���ݲ�����ȷ������
	 * 
	 * @param e
	 */
	public void locationDirection() {
		if (bL && !bD && !bU && !bR) {
			dir = Direction.L;
		} else if (bL && bD && !bU && !bR) {
			dir = Direction.LD;
		} else if (bL && !bD && bU && !bR) {
			dir = Direction.LU;
		} else if (!bL && !bD && !bU && bR) {
			dir = Direction.R;
		} else if (!bL && !bD && bU && bR) {
			dir = Direction.RU;
		} else if (!bL && bD && !bU && bR) {
			dir = Direction.RD;
		} else if (!bL && !bD && bU && !bR) {
			dir = Direction.U;
		} else if (!bL && bD && !bU && !bR) {
			dir = Direction.D;
		} else {
			dir = Direction.STOP;

		}

	}

	/**
	 * ���ݷ���ȷ���ƶ�����
	 */
	public void move() {
		this.oldX = x;
		this.oldY = y;
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED / Math.sqrt(2);
			y -= YSPEED / Math.sqrt(2);
			break;
		case LD:
			x -= XSPEED / Math.sqrt(2);
			y += YSPEED / Math.sqrt(2);
			break;
		case R:
			x += XSPEED;
			break;
		case RU:
			x += XSPEED / Math.sqrt(2);
			y -= YSPEED / Math.sqrt(2);
			break;
		case RD:
			x += XSPEED / Math.sqrt(2);
			y += YSPEED / Math.sqrt(2);
			break;
		case U:
			y -= YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		default:
			break;
		}
		// ������Ͳ����
		if (this.dir != Direction.STOP) {
			ptDir = this.dir;
		}
		/**
		 * ����̹�˳�������
		 */
		if (x < 0) {
			x = 0;
		}
		if (y < 30) {
			y = 30;
		}
		if (x + Tank.WIDTH > Utils.screenWidth) {
			x = Utils.screenWidth - Tank.WIDTH;
		}
		if (y + Tank.HEIGHT > Utils.screenHeight) {
			y = Utils.screenHeight - Tank.HEIGHT;
		}
		if (!good) {
			if (step == 0) {
				step = Utils.r.nextInt(12) + 3;
				int rnumber = Utils.r.nextInt(dirs.length);
				dir = dirs[rnumber];
			}
			if (Utils.r.nextInt(40) > 38) {
				tc.missiles.add(fire());// ���������һ���ڵ�
			}
			step--;
		}
	}

	/**
	 * �����ӵ�
	 */
	public Missile fire() {
		if (!this.isLive()) {
			return null;
		}
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile ms = new Missile(x, y, ptDir, this.good, this.tc);
		return ms;

	}

	/**
	 * ��̶��������ӵ�
	 * 
	 * @param dir
	 * @return
	 */
	public Missile fire(Direction dir) {
		if (!this.isLive()) {
			return null;
		}
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile ms = new Missile(x, y, dir, this.good, this.tc);
		return ms;

	}

	/**
	 * �������(��8������ͬ�r�����ӏ�)
	 */
	public void superFire() {
		for (int i = 0; i < 8; i++) {
			tc.missiles.add(fire(dirs[i]));
		}
	}

	/**
	 * ��ȡ̹�˵ľ��ο�
	 */
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * ̹��ײǽ
	 */
	public boolean collidesWithWall(Wall wall) {
		if (this.isLive() && this.getRec().intersects(wall.getRect())) {
			this.x = oldX;
			this.y = oldY;
			return true;
		}
		return false;
	}

	/**
	 * ̹��ײ̹��
	 */
	public boolean collidesWithTank(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.isLive() && t.isLive()
						&& this.getRec().intersects(t.getRec())) {
					this.x = oldX;
					this.y = oldY;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ̹�˳�Ѫ��
	 */
	public boolean eatBloodRec(BloodRectangle bRectangle) {
		if (this.isLive() && bRectangle.isLive()
				&& this.getRec().intersects(bRectangle.getRect())) {
			bRectangle.setLive(false);
			this.life = 100;
			return true;
		}

		return false;
	}

	/**
	 * ��Ӻ췽̹��(������)Ѫ��-�ڲ���
	 */
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, WIDTH, 10);// ���ľ���
			int w = WIDTH * life / 100;
			g.fillRect(x, y - 10, w, 10);// ʵ�ľ���
			g.setColor(c);
		}
	}

}
