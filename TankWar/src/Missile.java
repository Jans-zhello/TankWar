import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
/**
 * 子弹类
 * @zzz Administrator
 *
 */
public class Missile {
	public static final int XSPEED = 10;// 子弹x移动速度
	public static final int YSPEED = 10;// 子弹y移动速度
	private int x;// 子弹的x坐标
	private int y;// 子弹的y坐标
	private Tank.Direction dir;// 子弹的方向
	private TankWarClient tc;// 持有TankWarClient的引用
	public static final int WIDTH = 10;// 子弹宽度
	public static final int HEIGHT = 10;// 子弹高度

	private boolean live = true;// 控制子弹是否活着
	private boolean good;// 区分子弹，让子弹不打自己人

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
	 * 调用画笔画出子弹
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
	 * 根据方向确定移动坐标
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
	 * 获取子弹的矩形框
	 */
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * 子弹击中坦克
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
	 * 子弹击中一个集合的坦克
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
     * 检测子弹撞墙
     */
	public boolean hitWall(Wall wall){
		if (this.isLive() && this.getRec().intersects(wall.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
}
