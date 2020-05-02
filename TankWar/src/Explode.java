import java.awt.Color;
import java.awt.Graphics;
/**
 * 爆炸类
 * @zzz Administrator
 *
 */
public class Explode {
	private int x;// x位置坐标
	private int y;// y位置坐标
	private boolean live = true;// 判断是否活着
	private int[] diameter = { 4, 14, 26, 32, 43, 57, 69, 78, 102, 84, 65, 46,
			32, 22, 16, 8 };// 直径从小到大再到小
	private int step = 0;// 直径数组下标
	TankWarClient tClient;// 持有TankWarClient的引用

	public Explode(int x, int y, TankWarClient tClient) {
		this.x = x;
		this.y = y;
		this.tClient = tClient;
	}

	/**
	 * 调用画笔画出爆炸
	 */
	public void draw(Graphics g) {
		if (!live) {
			tClient.explodes.remove(this);
			return;
		}
		if (step == diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
	}

}
