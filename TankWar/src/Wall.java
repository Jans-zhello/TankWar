import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * ǽ��
 * @zzz Administrator
 *
 */
public class Wall {
	private int x;// ǽ��x����
	private int y;// ǽ��y����
	private int w;// ǽ�Ŀ��
	private int h;// ǽ�ĸ߶�
	TankWarClient tClient;// ����TankWarClient������

	public Wall(int x, int y, int w, int h, TankWarClient tClient) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tClient = tClient;
	}
    /**
     * ���û��ʻ�ǽ
     * @param g
     */
	public void draw(Graphics g) {
		g.fillRect(x, y, w, h);
	}
	
	
	/**
	 * ��ȡǽ�ľ�������
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

}
