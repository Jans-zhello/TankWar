import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
/**
 * 坦克类
 * @zzz Administrator
 *
 */
public class Tank {

	private int life = 100;// 主人翁坦克的生命值
	private BloodBar bbBar = new BloodBar();// 主人翁坦克血条
	private int x;// 坦克移动坐标x
	private int y;// 坦克移动坐标y

	private int oldX;// 记录上次坦克的x位置
	private int oldY;// 记录上次塔克的y位置

	public static final int XSPEED = 5;// 坦克x移动速度
	public static final int YSPEED = 5;// 坦克y移动速度

	public static final int WIDTH = 30;// 坦克宽度
	public static final int HEIGHT = 30;// 坦克高度

	TankWarClient tc;// 持有TankWarClient的引用
	private boolean good;// 判断是否是敵人坦克
	private boolean live = true;// 判断坦克是否活著
	private int step = Utils.r.nextInt(12) + 3;// 减缓坦克转换方向的速率,以实现坦克可以前进的效果

	/**
	 * 控制坦克八个方向移动
	 */
	private boolean bL = false, bR = false, bU = false, bD = false;

	// 枚举类型
	enum Direction {
		L, LU, LD, R, RU, RD, U, D, STOP
	};

	// 枚举类型转化成数组
	Direction[] dirs = Direction.values();

	// 坦克默认移动方向
	private Direction dir = Direction.STOP;
	// 坦克炮筒的方向
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
	 * 调用画笔画出坦克
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
		// 显示主人翁血条
		if (good) {
			bbBar.draw(g);
		}
		// 判断炮筒方向画出炮筒
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
	 * 根据键盘按下，控制坦克移动的布尔值
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
			tc.missiles.add(fire());// 按下ctrl键添加一发炮弹
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
	 * 根据键盘抬起，控制坦克移动方向的布尔值
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
	 * 根据布尔量确定方向
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
	 * 根据方向确定移动坐标
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
		// 计算炮筒方向
		if (this.dir != Direction.STOP) {
			ptDir = this.dir;
		}
		/**
		 * 控制坦克出界问题
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
				tc.missiles.add(fire());// 给敌人添加一发炮弹
			}
			step--;
		}
	}

	/**
	 * 发射子弹
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
	 * 向固定方向发射子弹
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
	 * 发射大招(向8个方向同時发射子彈)
	 */
	public void superFire() {
		for (int i = 0; i < 8; i++) {
			tc.missiles.add(fire(dirs[i]));
		}
	}

	/**
	 * 获取坦克的矩形框
	 */
	public Rectangle getRec() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	/**
	 * 坦克撞墙
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
	 * 坦克撞坦克
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
	 * 坦克吃血块
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
	 * 添加红方坦克(主人翁)血条-内部类
	 */
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y - 10, WIDTH, 10);// 空心矩形
			int w = WIDTH * life / 100;
			g.fillRect(x, y - 10, w, 10);// 实心矩形
			g.setColor(c);
		}
	}

}
