package biorobot.pack;

/**
 * Desc: 
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */
public class SystemParameters {
	/* NewRouteGUI.java */
	//public static final int GAP = 800; // gap between the holes.
	public static final String IMMERSION = "01100"; // immersion.
	public static final String ZTRANSPORT = "01370"; // transport height, measured from 0,0,0.
	//public static final int XYVELOCITY = 6; // X and Y motor velocity.
	//public static final int ZVELOCITY = 5; // Z motor velocity.
	//public static final int BIGHOLE1XCOORD = 2650;
	//public static final int BIGHOLE2XCOORD = 10950;
	//public static final int BIGHOLEYCOORD = 2340;
	//public static final int BIGHOLE1XCOORD = 2600;
	//public static final int BIGHOLE2XCOORD = 10900;
	//public static final int BIGHOLEYCOORD = 2370;
	//public static final int XPOSOFFIRST = 3990;
	//public static final int YPOSOFFIRST = 1540;
	//public static final int XPOSOFFIRST = 3940;
	//public static final int YPOSOFFIRST = 1570;
	public static final int XHOLESNO = 8; // Number of holes on X axis
	public static final int YHOLESNO = 3; // Number of holes on Y axis
	/* MainGUI.java */
	public static final String PROPERTYFILE = "./conf/conf.properties";
	public static final String SYSPROPERTIESFILE = "./conf/sys.properties";
	public static final String CHANGELOGFILE = "conf/changelog.txt";
	public static final int STEP = 10;
	public static final int STEPMORE = 1000;
	/* OperationProcessor.java */
	//public final static int MULTIPLICATOR = 10; // must be the same as on uC.
	public final static int TIMEOUT = 2000;
	public final static int DATA_RATE = 9600;
	public final static int NEWLINE_ASCII = 10;
	public final static int DASH_ASCII = 45;
	public final static int SPACE_ASCII = 32;
	/* RoutePanel */
	/* Original values, taken from the schema [mm] */
	public static final double SMALLHOLEDIAMETER = 35;
	public static final double BIGHOLEDIAMETER = 110;
	public static final int GAPINMM = 50;
	
}
