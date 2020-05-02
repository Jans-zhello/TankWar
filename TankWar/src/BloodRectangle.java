import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * Ѫ����
 * @zzz Administrator
 *
 */
public class BloodRectangle {
	private int x;// Ѫ���x����
	private int y;// Ѫ���y����
	private int w;// Ѫ��Ŀ��
	private int h;// Ѫ��ĸ߶�
	private int step = 0;
	TankWarClient tClient;// ����TankWarClient������
	private boolean live = true;// �ж�Ѫ���Ƿ����

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
	 * ���û��ʻ�Ѫ��
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
	 * ��ȡѪ��ľ�������
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

}
