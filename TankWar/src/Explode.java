import java.awt.Color;
import java.awt.Graphics;
/**
 * ��ը��
 * @zzz Administrator
 *
 */
public class Explode {
	private int x;// xλ������
	private int y;// yλ������
	private boolean live = true;// �ж��Ƿ����
	private int[] diameter = { 4, 14, 26, 32, 43, 57, 69, 78, 102, 84, 65, 46,
			32, 22, 16, 8 };// ֱ����С�����ٵ�С
	private int step = 0;// ֱ�������±�
	TankWarClient tClient;// ����TankWarClient������

	public Explode(int x, int y, TankWarClient tClient) {
		this.x = x;
		this.y = y;
		this.tClient = tClient;
	}

	/**
	 * ���û��ʻ�����ը
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
