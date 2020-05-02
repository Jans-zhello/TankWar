import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * 血块类
 * @zzz Administrator
 *
 */
public class BloodRectangle {
	private int x;// 血块的x坐标
	private int y;// 血块的y坐标
	private int w;// 血块的宽度
	private int h;// 血块的高度
	private int step = 0;
	TankWarClient tClient;// 持有TankWarClient的引用
	private boolean live = true;// 判断血块是否活着

	private int[][] positions = { { 500, 200 }, { 510, 210 },{ 520, 220 },{ 530, 230 },{ 540, 240 },{ 550, 250 },{ 560, 260 } };

	public BloodRectangle() {
		this.x = positions[0][0];
		this.y = positions[0][1];
		this.w = 15;
		this.h = 15;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * 调用画笔画血块
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (!live) {
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.magenta);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}

	public void move() {
		if (step == positions.length) {
			step = 0;
		}
		this.x = positions[step][0];
		this.y = positions[step][1];
		step++;
	}

	/**
	 * 获取血块的矩形区域
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

}
