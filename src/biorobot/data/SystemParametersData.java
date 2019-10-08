package biorobot.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import biorobot.pack.SystemParameters;

/**
 * System parameters DTO.
 * @author maciej.wojciga
 */

public class SystemParametersData {

	private int gap;
	private int calibratevelocity;
	private int xyvelocity;
	private int zvelocity;
	private int bighole1xcoord;
	private int bighole2xcoord;
	private int bigholeycoord;
	private int xposoffirst;
	private int yposoffirst;
	private int multiplicator;
	private int immersion;
	
	public SystemParametersData() {
		Properties systemProperties = new Properties();
		try {
			FileInputStream propertiesFileIS = new FileInputStream(SystemParameters.SYSPROPERTIESFILE);
			systemProperties.load(propertiesFileIS);
			propertiesFileIS.close();
			
			// Initialize system properties.
			setGap(Integer.parseInt((systemProperties.getProperty("gap").trim())));
			setCalibratevelocity(Integer.parseInt(systemProperties.getProperty("calibratevelocity").trim()));
			setXyvelocity(Integer.parseInt(systemProperties.getProperty("xyvelocity").trim()));
			setZvelocity(Integer.parseInt(systemProperties.getProperty("zvelocity").trim()));
			setBighole1xcoord(Integer.parseInt(systemProperties.getProperty("bighole1xcoord").trim()));
			setBighole2xcoord(Integer.parseInt(systemProperties.getProperty("bighole2xcoord").trim()));
			setBigholeycoord(Integer.parseInt(systemProperties.getProperty("bigholeycoord").trim()));
			setXposoffirst(Integer.parseInt(systemProperties.getProperty("xposoffirst").trim()));
			setYposoffirst(Integer.parseInt(systemProperties.getProperty("yposoffirst").trim()));
			setMultiplicator(Integer.parseInt(systemProperties.getProperty("multiplicator").trim()));
			setImmersion(Integer.parseInt(systemProperties.getProperty("immersion").trim()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getGap() {
		return gap;
	}
	public void setGap(int gap) {
		this.gap = gap;
	}
	public int getCalibratevelocity() {
		return calibratevelocity;
	}
	public void setCalibratevelocity(int calibratevelocity) {
		this.calibratevelocity = calibratevelocity;
	}
	public int getXyvelocity() {
		return xyvelocity;
	}
	public void setXyvelocity(int xyvelocity) {
		this.xyvelocity = xyvelocity;
	}
	public int getZvelocity() {
		return zvelocity;
	}
	public void setZvelocity(int zvelocity) {
		this.zvelocity = zvelocity;
	}
	public int getBighole1xcoord() {
		return bighole1xcoord;
	}
	public void setBighole1xcoord(int bighole1xcoord) {
		this.bighole1xcoord = bighole1xcoord;
	}
	public int getBighole2xcoord() {
		return bighole2xcoord;
	}
	public void setBighole2xcoord(int bighole2xcoord) {
		this.bighole2xcoord = bighole2xcoord;
	}
	public int getBigholeycoord() {
		return bigholeycoord;
	}
	public void setBigholeycoord(int bigholeycoord) {
		this.bigholeycoord = bigholeycoord;
	}
	public int getXposoffirst() {
		return xposoffirst;
	}
	public void setXposoffirst(int xposoffirst) {
		this.xposoffirst = xposoffirst;
	}
	public int getYposoffirst() {
		return yposoffirst;
	}
	public void setYposoffirst(int yposoffirst) {
		this.yposoffirst = yposoffirst;
	}
	public int getMultiplicator() {
		return multiplicator;
	}
	public void setMultiplicator(int multiplicator) {
		this.multiplicator = multiplicator;
	}
	public int getImmersion() {
		return immersion;
	}
	public void setImmersion(int immersion) {
		this.immersion = immersion;
	}
}
