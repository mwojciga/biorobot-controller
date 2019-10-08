package biorobot.data;

/**
 * Output message DTO.
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */
public class OutputMessageData {
	//Sv9d000x00100y00200z00200t602e0h0E
	private int vOM;
	private int[] dOM;
	private int xOM;
	private int yOM;
	private int zOM;
	private int tOM;
	private int eOM;
	private int hOM;
	
	public int getvOM() {
		return vOM;
	}
	public void setvOM(int vOM) {
		this.vOM = vOM;
	}
	public int[] getdOM() {
		return dOM;
	}
	public void setdOM(int[] dOM) {
		this.dOM = dOM;
	}
	public int getxOM() {
		return xOM;
	}
	public void setxOM(int xOM) {
		this.xOM = xOM;
	}
	public int getyOM() {
		return yOM;
	}
	public void setyOM(int yOM) {
		this.yOM = yOM;
	}
	public int getzOM() {
		return zOM;
	}
	public void setzOM(int zOM) {
		this.zOM = zOM;
	}
	public int gettOM() {
		return tOM;
	}
	public void settOM(int tOM) {
		this.tOM = tOM;
	}
	public int geteOM() {
		return eOM;
	}
	public void seteOM(int eOM) {
		this.eOM = eOM;
	}
	public int gethOM() {
		return hOM;
	}
	public void sethOM(int hOM) {
		this.hOM = hOM;
	}
	
}
