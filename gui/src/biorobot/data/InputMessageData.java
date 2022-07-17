package biorobot.data;

/**
 * Input message DTO.
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */
public class InputMessageData {
	// Sl000000x00000y00000z00000t000e0E
	private int[] lIM;
	private int xIM;
	private int yIM;
	private int zIM;
	private int tIM;
	private int eIM;
	
	public int[] getlIM() {
		return lIM;
	}
	public void setlIM(int[] lIM) {
		this.lIM = lIM;
	}
	public int getxIM() {
		return xIM;
	}
	public void setxIM(int xIM) {
		this.xIM = xIM;
	}
	public int getyIM() {
		return yIM;
	}
	public void setyIM(int yIM) {
		this.yIM = yIM;
	}
	public int getzIM() {
		return zIM;
	}
	public void setzIM(int zIM) {
		this.zIM = zIM;
	}
	public int gettIM() {
		return tIM;
	}
	public void settIM(int tIM) {
		this.tIM = tIM;
	}
	public int geteIM() {
		return eIM;
	}
	public void seteIM(int eIM) {
		this.eIM = eIM;
	}
	
}
