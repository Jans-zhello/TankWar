import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
/**
 * �ӵ���
 * @zzz Administrator
 *
 */
public class Missile {
	public static final int XSPEED = 10;// �ӵ�x�ƶ��ٶ�
	public static final int YSPEED = 10;// �ӵ�y�ƶ��ٶ�
	private int x;// �ӵ���x����
	private int y;// �ӵ���y����
	private Tank.Direction dir;// �ӵ��ķ���
	private TankWarClient tc;// ����TankWarClient������
	public static final int WIDTH = 10;// �ӵ����
	public static final int HEIGHT = 10;// �ӵ��߶�

	private boolean live = true;// �����ӵ��Ƿ����
	private boolean good;// �����ӵ������ӵ������Լ���

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public Missile(int x, int y, Tank.Direction dir, boolean good,
			TankWarClient tc) {
		this(x, y, dir);
		this.tc = tc;
		this.good = good;
	}

	/**
	 * ���û��ʻ����ӵ�
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if (good) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
	}

	/**
	 * ���ݷ���ȷ���ƶ�����
	 */
	public void move() {
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
		if (x < 0 || y < 0 || x > Utils.screenWidth
				|| y > Utils.screenHeight) {
			live = false;
		}
	}

	/**
	 * ��ȡ�ӵ��ľ��ο�
	 */
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * �ӵ�����̹��
	 */

	public boolean hitTank(Tank tank) {
		if (this.isLive() && this.getRec().intersects(tank.getRec())
				&& tank.isLive() && this.good != tank.isGood()) {
			if (tank.isGood()) {
                tank.setLife(tank.getLife()-20);
                if (tank.getLife()<=0) {
					tank.setLive(false);
					Explode explode = new Explode(x - 30, y - 30, tc);
					tc.explodes.add(explode);
				}
                
			}else {
				tank.setLive(false);
				Explode explode = new Explode(x - 30, y - 30, tc);
				tc.explodes.add(explode);
			}
			live = false;
			
			return true;
			
		}
		return false;
	}

	/**
	 * �ӵ�����һ�����ϵ�̹��
	 * 
	 * @param tanks
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
    /**
     * ����ӵ�ײǽ
     */
	public boolean hitWall(Wall wall){
		if (this.isLive() && this.getRec().intersects(wall.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
}
