import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * 墙类
 * @zzz Administrator
 *
 */
public class Wall {
	private int x;// 墙的x坐标
	private int y;// 墙的y坐标
	private int w;// 墙的宽度
	private int h;// 墙的高度
	TankWarClient tClient;// 持有TankWarClient的引用

	public Wall(int x, int y, int w, int h, TankWarClient tClient) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tClient = tClient;
	}
    /**
     * 调用画笔画墙
     * @param g
     */
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);
	}
	
	
	/**
	 * 获取墙的矩形区域
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

}
