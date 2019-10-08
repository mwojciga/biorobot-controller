package biorobot.threads;

import gui.pack.MainGUI;

import org.apache.log4j.Logger;

import biorobot.data.SystemParametersData;
import biorobot.pack.Log;
import biorobot.pack.OperationProcessor;

/**
 * Desc: 
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */
public class Calibrate implements Runnable {

	MainGUI mainGUI;
	OperationProcessor operationProcessor;
	public Thread calibrateThread;
	SystemParametersData systemParametersData = new SystemParametersData();

	/* LOG */
	Logger logger = Logger.getLogger(Calibrate.class);
	Log log = new Log();
	
	private boolean suspendFlag = true;

	public Calibrate(MainGUI mainGUI, OperationProcessor operationProcessor) {
		this.mainGUI = mainGUI;
		this.operationProcessor = operationProcessor;
		calibrateThread = new Thread(this, "CalibrateThread");
		calibrateThread.start();
	}

	@Override
	public void run() {
		try {
			/* The uC will try to calibrate the robot */
			int vFile = systemParametersData.getCalibratevelocity();
			int xDirFile = 0;
			int yDirFile = 0;
			int zDirFile = 0;
			int xFile = 99999;
			int yFile = 99999;
			int zFile = 99999;
			int tFile = 0;
			int eFile = 3;
			int hFile = 1;
			operationProcessor.writeData(vFile, xDirFile, yDirFile, zDirFile, xFile, yFile, zFile, tFile, eFile, hFile);
			synchronized (this) {
				while (suspendFlag) {
					// Wait for OP to read a proper message
					wait();
				}
			}
			// If OP read a message that the robot has calibrated, than...
			String actFullMessage = operationProcessor.getInputMessage();
			int xStartFM = actFullMessage.indexOf("x") + 1;
			int xEndFM = actFullMessage.indexOf("y");
			int yStartFM = actFullMessage.indexOf("y") + 1;
			int yEndFM = actFullMessage.indexOf("z");
			int zStartFM = actFullMessage.indexOf("z") + 1;
			int zEndFM = actFullMessage.indexOf("t");
			int x = Integer.parseInt(actFullMessage.substring(xStartFM, xEndFM));
			int y = Integer.parseInt(actFullMessage.substring(yStartFM, yEndFM));
			int z = Integer.parseInt(actFullMessage.substring(zStartFM, zEndFM));
			operationProcessor.setActX(x);
			operationProcessor.setActY(y);
			operationProcessor.setActZ(z);
			log.log(null, "Actual message: " + actFullMessage);
			logger.info("Actual message: " + actFullMessage);
			mainGUI.actualPosition.setText("[" + x + ", " + y + ", " + z + "]");
			log.log("Calibrated.", "Calibrated.");
			logger.info("Calibrated.");
			operationProcessor.setGlobalCalibrated(true);
			operationProcessor.calibrated();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.log("An exception occured. See more in log file.", "An exception occured. See more in log file.");
			logger.error(e.toString());
		}
	}

	public synchronized void suspend() {
		suspendFlag = true;
	}

	public synchronized void resume() {
		suspendFlag = false;
		notify();
	}

}
