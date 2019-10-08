package biorobot.test;

import biorobot.pack.Calculations;
import biorobot.pack.SystemParameters;


public class Test {
	public static void main(String[] args) {
		int zdir = 2;
		String zdir2 = String.format("%05d", zdir);
		System.out.println(zdir2);
	}
	
	private static double[] calculateCoordinates(int holeNo) {
		// Get step coordinates for a hole and map them to [mm]/2.
		double coordinates[] = new double[2];
		// SystemParameters.GAP = 50/2 [mm] (taken from the schema).
		int gap = 768 ;
		double xCoord = (Calculations.mapHoleNoToCoordinates(holeNo)[0]*SystemParameters.GAPINMM/2)/gap;
		double yCoord = (Calculations.mapHoleNoToCoordinates(holeNo)[1]*SystemParameters.GAPINMM/2)/gap;
		// Now we have the coordinates of the center of hole. We want to have the coordinates of the top left corner.
		xCoord = (holeNo == 0 || holeNo == 25 ) ? xCoord - SystemParameters.BIGHOLEDIAMETER/4: xCoord - SystemParameters.SMALLHOLEDIAMETER/4;
		yCoord = (holeNo == 0 || holeNo == 25 ) ? yCoord - SystemParameters.BIGHOLEDIAMETER/4: yCoord - SystemParameters.SMALLHOLEDIAMETER/4;
		coordinates[0] = xCoord;
		coordinates[1] = yCoord;
		return coordinates;
	}
}
