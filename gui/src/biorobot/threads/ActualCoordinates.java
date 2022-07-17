package biorobot.threads;

import gui.pack.MainGUI;

import org.apache.log4j.Logger;

import biorobot.pack.Log;
import biorobot.pack.OperationProcessor;

/**
 * Desc: 
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */
public class ActualCoordinates implements Runnable {

	MainGUI mainGUI;
	OperationProcessor operationProcessor;
	public Thread actualCoordinatesThread;
	
	/* LOG */
	Logger logger = Logger.getLogger(ActualCoordinates.class);
	Log log = new Log();

	private boolean suspendFlag = true;

	public ActualCoordinates(MainGUI mainGUI, OperationProcessor operationProcessor) {
		this.mainGUI = mainGUI;
		this.operationProcessor = operationProcessor;
		actualCoordinatesThread = new Thread(this, "ActualCoordinatesThread");
		actualCoordinatesThread.start();
	}

	@Override
	public void run() {
		try {
			/* The uC will read the actual coordinates */
			int vFile = 0;
			int xDirFile = 0;
			int yDirFile = 0;
			int zDirFile = 0;
			int xFile = 0;
			int yFile = 0;
			int zFile = 0;
			int tFile = 0;
			int hFile = 0;
			int eFile = 2;
			operationProcessor.writeData(vFile, xDirFile, yDirFile, zDirFile, xFile, yFile, zFile, tFile, eFile, hFile);
			synchronized (this) {
				while (suspendFlag) {
					// Wait for OP to read a proper message
					wait();
				}
			}
			/* If OP read a message that the robot has actualCoordinated, than... */
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
			log.log(null, "Actual coordinates message: " + actFullMessage);
			logger.info("Actual coordinates message: " + actFullMessage);
			mainGUI.actualPosition.setText("[" + x + ", " + y + ", " + z + "]");
			log.log("Actual coordinates: [" + x + ", " + y + ", " + z + "]", "Actual coordinates: [" + x + ", " + y + ", " + z + "]");
			logger.info("Actual coordinates: [" + x + ", " + y + ", " + z + "]");
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
