package biorobot.pack;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gui.pack.MainGUI;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import biorobot.data.ExpectedCoordinatesData;
import biorobot.data.InputMessageData;
import biorobot.data.OutputMessageData;
import biorobot.data.SystemParametersData;
import biorobot.threads.ActualCoordinates;
import biorobot.threads.Calibrate;
import biorobot.threads.StartRouteFromFile;
import biorobot.threads.WaitingCounter;

/**
 * Operations class.
 * @author maciej.wojciga
 * @author klaudia.trembecka
 */

public class OperationProcessor implements SerialPortEventListener {
	
	/* THREADS */
	Calibrate calibrateThread;
	ActualCoordinates actualCoordinatesThread;
	StartRouteFromFile startRouteFromFileThread;
	WaitingCounter waitingCounter;

	/* LOG */
	Logger logger = Logger.getLogger(OperationProcessor.class);
	Log log = new Log();

	/* OTHER */
	MainGUI mainGUI;
	private Enumeration availablePorts = null;
	private HashMap portMap = new HashMap();
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort openedSerialPort = null;
	private boolean connectedToPort = false;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	Calculations calculations = new Calculations();
	SystemParametersData systemParametersData = new SystemParametersData();
	
	private boolean globalCalibrated = false;
	
	private String sendedMessage = "initialMessage";
	
	public boolean calibrateDone = false;
	public boolean shouldWait = false;

	private int actX;
	private int actY;
	private int actZ;
	private int expectedX;
	private int expectedY;
	private int expectedZ;

	byte[] buffer = new byte[1024];
	int bytes;
	String end = "E";
	StringBuilder curMsg = new StringBuilder();
	public String inputMessage = "";

	public OperationProcessor(MainGUI mainGUI) {
		this.mainGUI = mainGUI;
	}

	public void searchForPorts() {
		mainGUI.availablePorts.removeAllItems();
		availablePorts = CommPortIdentifier.getPortIdentifiers();
		while (availablePorts.hasMoreElements()) {
			CommPortIdentifier currentPort = (CommPortIdentifier) availablePorts.nextElement();
			log.log(null, "Found port: " + currentPort.getName());
			logger.info("Found port: " + currentPort.getName());
			if (currentPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				mainGUI.availablePorts.addItem(currentPort.getName());
				portMap.put(currentPort.getName(), currentPort);
				log.log(null, currentPort.getName() + " is a serial port. Added.");
				logger.info(currentPort.getName() + " is a serial port. Added.");
			}

		}
	}

	public void connect() {
		log.log("Connecting to " + mainGUI.availablePorts.getSelectedItem() + "...", "Connecting to " + mainGUI.availablePorts.getSelectedItem() + "...");
		logger.info("Connecting to " + mainGUI.availablePorts.getSelectedItem());
		String selectedPort = (String) mainGUI.availablePorts.getSelectedItem();
		selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);
		CommPort commPort = null;
		try {
			commPort = selectedPortIdentifier.open("Biorobot", SystemParameters.TIMEOUT);
			openedSerialPort = (SerialPort) commPort;
			openedSerialPort.setSerialPortParams(SystemParameters.DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			connectedToPort = true;
			mainGUI.btnConnect.setEnabled(false);
			mainGUI.btnDisconnect.setEnabled(true);
			mainGUI.lblConnectionStatus.setText("Connected to " + commPort.getName());
			mainGUI.lblConnectionStatus.setForeground(Color.BLUE);
			mainGUI.enableAllButtons();
			log.log("Successfully connected to " + commPort.getName(), "Successfully connected to " + commPort.getName());
			logger.info("Successfully connected to " + commPort.getName());
		} catch (PortInUseException e) {
			log.log("Could not connect: port is already in use.", "Could not connect: port is already in use.");
			logger.info("Could not connect: port is already in use.");
		} catch (Exception e) {
			log.log("Could not connect: unknown error.", "Could not connect: unknown error.");
			logger.info("Could not connect: " + e.toString());
		}
	}

	public boolean initIOStream() {
		log.log(null, "Opening IOStream.");
		logger.info("Opening IOStream.");
		boolean ioStreamOpened = false;
		try {
			inputStream = openedSerialPort.getInputStream();
			outputStream = openedSerialPort.getOutputStream();
			ioStreamOpened = true;
			log.log(null, "IOStream successfully opened.");
			logger.info("IOStream successfully opened.");
		} catch (IOException e) {
			log.log("Could not open IOStream.", "Could not open IOStream." + e.toString());
			logger.info("Could not open IOStream." + e.toString());
		}
		return ioStreamOpened;
	}

	public void initListener() {
		try {
			log.log(null, "Initializing listener.");
			logger.info("Initializing listener.");
			openedSerialPort.addEventListener(this);
			openedSerialPort.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			log.log("Could not add event listener.", "Could not add event listener. " + e.toString());
			logger.info("Could not add event listener. " + e.toString());
		}
	}

	public void disconnect() {
		if (connectedToPort == true) {
			openedSerialPort.removeEventListener();
			openedSerialPort.close();
			log.log("Disconnected from " + openedSerialPort.getName(), "Disconnected from " + openedSerialPort.getName());
			logger.info("Disconnected from " + openedSerialPort.getName());
			try {
				inputStream.close();
				outputStream.close();
				connectedToPort = false;
				mainGUI.lblConnectionStatus.setText("Disconnected!");
				mainGUI.lblConnectionStatus.setForeground(Color.RED);
				mainGUI.btnConnect.setEnabled(true);
				mainGUI.btnDisconnect.setEnabled(false);
				mainGUI.disableAllButtons();
				log.log(null, "IOStream closed.");
				logger.info("IOStream closed.");
			} catch (IOException e) {
				log.log("Could not close IOStream.", "Could not close IOStream." + e.toString());
				logger.info("Could not close IOStream." + e.toString());
			}
		} else {
			log.log("You are not connected to any port!", "Tried to disconnect, but no port was opened.");
			logger.info("Tried to disconnect, but no port was opened.");
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				inputMessage = "";
				bytes = inputStream.read(buffer);
				curMsg.append(new String(buffer, 0, bytes, Charset.forName("UTF-8")));
				int endIdx = curMsg.indexOf(end);
				if (endIdx != -1) {
					inputMessage = curMsg.substring(0, endIdx + end.length()).trim();
					curMsg.delete(0, endIdx + end.length());
					log.log(null, "Received: " + inputMessage);
					logger.info("Received: " + inputMessage);
					System.out.println("[R]: " + inputMessage);
					setActualCoordinates(inputMessage);
					// TODO Shorten this code, use methods.
					// If we've received a time in the message, start the timer and update main GUI.
					if (shouldWait) {
						checkForTime(sendedMessage);
						shouldWait = false;
					}
					boolean calibrateMS = false;
					boolean calibrating = false;
					boolean actualMS = false;
					boolean errorMS = false;
					boolean lockMS = false;
					boolean compareMS = false;
					calibrateMS = checkIfCalibrate(inputMessage);
					calibrating = checkIfCalibrating(inputMessage);
					actualMS = checkIfActual(inputMessage);
					errorMS = checkIfError(inputMessage);
					lockMS = checkIfLock(inputMessage);
					compareMS = compareMessages(inputMessage, sendedMessage);
					logger.info("Calibrate: " + calibrateMS + "; Actual: " + actualMS + "; Error: " + errorMS + "; Lock: " + lockMS + "; Comparison: " + compareMS + "; Calibrating: " + calibrating);
					if (calibrateMS) {
						calibrateThread.resume();
					}
					if (actualMS) {
						actualCoordinatesThread.resume();
					}
					if (errorMS) {
						log.log("Received a message with an error from " + openedSerialPort.getName(), "Received a message with an error from " + openedSerialPort.getName());
						logger.info("Received a message with an error from " + openedSerialPort.getName());
					}
					if (lockMS && !calibrating) {
						log.log("The robot has reached the limit switch!", "The robot has reached the limit switch!");
						logger.info("The robot has reached the limit switch!");
						resetThreads();
					}
					if (compareMS && !errorMS && !actualMS) {
						log.log("Reached [" + actX + ", " + actY + ", " + actZ + "].", "Reached [" + actX + ", " + actY + ", " + actZ + "].");
						logger.info("Reached [" + actX + ", " + actY + ", " + actZ + "].");
						startRouteFromFileThread.resume();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.log("An exception occured. See more in log file.", "An exception occured. See more in log file.");
				logger.error(e.toString());
			}
		}

	}

	/**
	 * Writes the data to the uC.
	 * @param velocity
	 * @param xdir
	 * @param ydir
	 * @param zdir
	 * @param x
	 * @param y
	 * @param z
	 * @param t
	 * @param error
	 * @param hierarchy
	 */
	public void writeData(int velocity, int xdir, int ydir, int zdir, int x, int y, int z, int t, int error, int hierarchy) {
		try {
			String xStr = String.format("%05d", x);
			String yStr = String.format("%05d", y);
			String zStr = String.format("%05d", z);
			String tStr = String.format("%03d", t);
			// Sv9d000x10000y20000z20000t602e0h0E
			// Count the expected values.
			int[] coordinates = {x, y, z };
			int[] directories = { xdir, ydir, zdir };
			int[] actualCoordinates = { actX, actY, actZ };
			ExpectedCoordinatesData expectedCoordinatesData = new ExpectedCoordinatesData();
			calculations.countExpectedCoordinates(coordinates, directories, actualCoordinates, expectedCoordinatesData);
			expectedX = expectedCoordinatesData.getExpectedX();
			expectedY = expectedCoordinatesData.getExpectedY();
			expectedZ = expectedCoordinatesData.getExpectedZ();
			// If actZ is greater then SystemParameters.ZTRANSPORT, then set h to 1, to move Z first (because its submerged).
			// Also, if we're submerged and we want to move X or Y, it won't allow us to do so. 
			if (actZ > Integer.parseInt(SystemParameters.ZTRANSPORT) * systemParametersData.getMultiplicator() + 10) {
				hierarchy = 1;
				if (z * systemParametersData.getMultiplicator() < actZ - Integer.parseInt(SystemParameters.ZTRANSPORT) * systemParametersData.getMultiplicator() + 10 && (x != 0 || y != 0)) {
					int newZ = (actZ - Integer.parseInt(SystemParameters.ZTRANSPORT) * systemParametersData.getMultiplicator() + 10) / 10;
					zStr = String.format("%05d", newZ);
					zdir = 0;
					expectedZ = Integer.parseInt(SystemParameters.ZTRANSPORT);
				}
			}
			// Color the expected hole on the GUI
			higlightExpectedHole(expectedX-10, expectedY-10, Color.YELLOW);
			shouldWait = (t != 0) ? true : false;
			// Send the message.
			outputStream.flush();
			String toSend = "S".concat("v" + velocity + "d" + xdir + ydir + zdir + "x" + xStr + "y" + yStr + "z" + zStr + "t" + tStr + "e" + error + "h" + hierarchy + "E");
			sendedMessage = toSend;
			System.out.println("[S]: " + toSend);
			outputStream.write(toSend.getBytes());
			outputStream.flush();
			// Log errors.
			if (error == 0) {
				log.log("Moving by [" + x + ", " + y + ", " + z + "] than waiting " + t + " seconds.", "Sent: " + toSend);
				logger.info("Sent: " + toSend);
			} else if (error == 2) {
				logger.info("Sent: " + toSend);
			} else if (error == 3) {
				logger.info("Sent: " + toSend);
			} else {
				log.log("Robot stopped by the user!", "Robot stopped by the user!");
				logger.info("Robot stopped by the user!");
			}
		} catch (Exception e) {
			log.log("Could not write data: " + e.toString(), "Could not write data: " + e.toString());
			logger.info("Could not write data: " + e.toString());
		}
	}

	/**
	 * Gets the loaded route file and sends to uC message after message.
	 * Firstly it checks if actual coordinates are [10,10,10].
	 * @param loadedFile
	 */
	public synchronized void useLoadedFile(File loadedFile) {
		boolean actCoordinatesDone = getActCoordinates();
		if (actCoordinatesDone) {
			// Check if calibration is done, by checking actualCoordinates, if they are equal 10,10,10. 
			// If not, ask user to calibrate.
			if (actX == 10 && actY == 10 && actZ == 10) {
				log.log("Starting route...", null);
				startRouteFromFileThread = new StartRouteFromFile(mainGUI, this, loadedFile);
			} else {
				log.log("It seems that the robot is not calibrated. Please calibrate.", "It seems that the robot is not calibrated.");
				logger.error("It seems that the robot is not calibrated.\nPlease calibrate.");
				JOptionPane.showMessageDialog(MainGUI.mainGUIfrm, "It seems that the robot is not calibrated.\nPlease calibrate.", "Not calibrated.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Sends info to uC that we want to calibrate.
	 * Firstly, it checks actual coordinates.
	 */
	public void calibrate() {
		calibrateDone = false;
		boolean actCoordinatesDone = getActCoordinates();
		if (actCoordinatesDone) {
			log.log("Calibrating...", null);
			logger.info("Starting calibration.");
			calibrateThread = new Calibrate(mainGUI, this);
		}
	}
	
	/**
	 * Ends the calibration thread.
	 */
	public void calibrated() {
		try {
			// Wait for the calibrateThread to end
			calibrateThread.calibrateThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (calibrateThread.calibrateThread.getState().equals(Thread.State.TERMINATED)) {
			calibrateDone = true;
		}
	}
	
	/**
	 * Gets actual coordinates.
	 * @return
	 */
	public boolean getActCoordinates() {
		boolean actualCoordinatesDone = false;
		log.log("Aquiring actual coordinates...", null);
		actualCoordinatesThread = new ActualCoordinates(mainGUI, this);
		try {
			// Wait for the actualCoordinatesThread to end
			actualCoordinatesThread.actualCoordinatesThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.log("An exception occured. See more in log file.", "An exception occured. See more in log file.");
			logger.error(e.toString());
		}
		if (actualCoordinatesThread.actualCoordinatesThread.getState().equals(Thread.State.TERMINATED)) {
			actualCoordinatesDone = true;
		}
		return actualCoordinatesDone;
	}

	/**
	 * Sends a message to uC with error flag set to "1"
	 */
	public void stopRobotMovement() {
		/* Send the stop message to uC */
		writeData(0, 0, 0, 0, 0, 0, 0, 0, 1, 0);
	}

	/**
	 * Resets all threads.
	 */
	public void resetThreads() {
		if (actualCoordinatesThread != null) {
			if (!actualCoordinatesThread.actualCoordinatesThread.getState().equals(Thread.State.TERMINATED)) {
				actualCoordinatesThread.suspend();
			}
		}
		if (calibrateThread != null) {
			if (!calibrateThread.calibrateThread.getState().equals(Thread.State.TERMINATED)) {
				calibrateThread.suspend();
			}
		}
		if (startRouteFromFileThread != null) {
			if (!startRouteFromFileThread.startRouteFromFileThread.getState().equals(Thread.State.TERMINATED)) {
				startRouteFromFileThread.suspend();
				startRouteFromFileThread.reset();
				mainGUI.txtpnRouteOverview.setText("");
			}
		}
		mainGUI.routePanel.setExpectedHole(27);
		mainGUI.routePanel.setActualHole(27);
		mainGUI.routePanel.repaint();
	}

	/**
	 * Checks if there was an lock information in input message.
	 * @param inputMessage
	 * @return
	 */
	public boolean checkIfLock(String inputMessage) {
		boolean lockInMessage = false;
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		int lockTable[] = inputMessageData.getlIM();
		for (int i = 0; i < inputMessageData.getlIM().length; i++) {
			if (lockTable[i] == 1) {
				lockInMessage = true;
				break;
			}
		}
		return lockInMessage;
	}

	/**
	 * Checks if robot has already calibrated.
	 * @param inputMessage
	 * @return
	 */
	public boolean checkIfCalibrate(String inputMessage) {
		boolean calibrateInMessage = false;
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		if (inputMessageData.geteIM() == 3 && inputMessageData.getxIM() == 10 && inputMessageData.getyIM() == 10 && inputMessageData.getzIM() == 10) {
			calibrateInMessage = true;
		}
		return calibrateInMessage;
	}

	/**
	 * Checks if robot is calibrating.
	 * @param inputMessage
	 * @return
	 */
	public boolean checkIfCalibrating(String inputMessage) {
		boolean calibratingInMessage = false;
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		if (inputMessageData.geteIM() == 3 && (inputMessageData.getxIM() != 10 || inputMessageData.getyIM() != 10 || inputMessageData.getzIM() != 10)) {
			calibratingInMessage = true;
		}
		return calibratingInMessage;
	}

	/**
	 * Checks if there is an actual coordinates token (e == 2) in input message.
	 * @param inputMessage
	 * @return
	 */
	public boolean checkIfActual(String inputMessage) {
		boolean actualInMessage = false;
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		if (inputMessageData.geteIM() == 2) {
			actualInMessage = true;
		}
		return actualInMessage;
	}

	/**
	 * Checks if there has been an error in input message.
	 * @param inputMessage
	 * @return
	 */
	public boolean checkIfError(String inputMessage) {
		boolean errorInMessage = false;
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		if (inputMessageData.geteIM() == 1) {
			errorInMessage = true;
		}
		return errorInMessage;
	}

	/**
	 * Processes input and output messages and compares them.
	 * @param inputMessage
	 * @param outputMessage
	 * @return
	 */
	public boolean compareMessages(String inputMessage, String outputMessage) {
		boolean theSame = false;
		// Process input message.
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		// Process output message.
		OutputMessageData outputMessageData = new OutputMessageData();
		outputMessageData = calculations.processOutputMessage(outputMessage, outputMessageData);
		// check if x,y,z,t are the same
		if ((inputMessageData.getxIM() == expectedX || outputMessageData.getxOM() == 0) && (inputMessageData.getyIM() == expectedY || outputMessageData.getyOM() == 0) && (inputMessageData.getzIM() == expectedZ || outputMessageData.getzOM() == 0) && inputMessageData.gettIM() == outputMessageData.gettOM()) {
			theSame = true;
		}
		logger.info("[X:Y:Z][EX:RE:SE:AC]" + "[" + expectedX + ":" + inputMessageData.getxIM() + ":" + outputMessageData.getxOM() + ":" + actX + "]" + "[" + expectedY + ":" + inputMessageData.getyIM() + ":" + outputMessageData.getyOM() + ":" + actY + "]" + "[" + expectedZ + ":" + inputMessageData.getzIM() + ":" + outputMessageData.getzOM() + ":" + actZ + "]" + "[T:" + outputMessageData.gettOM() + ":" + inputMessageData.gettIM() + "]");
		return theSame;
	}

	/**
	 * Sets actual coordinates by checking the input message.
	 * @param inputMessage
	 */
	public void setActualCoordinates(String inputMessage) {
		InputMessageData inputMessageData = new InputMessageData();
		inputMessageData = calculations.processInputMessage(inputMessage, inputMessageData);
		actX = inputMessageData.getxIM();
		actY = inputMessageData.getyIM();
		actZ = inputMessageData.getzIM();
		higlightActualHole(actX-10, actY-10, Color.BLUE);
		mainGUI.actualPosition.setText("[" + actX + ", " + actY + ", " + actZ + "]");
	}
	
	/**
	 * Updates the waiting label on the main GUI.
	 * @param inputMessage
	 */
	public void checkForTime(String outputMessage) {
		OutputMessageData outputMessageData = new OutputMessageData();
		outputMessageData = calculations.processOutputMessage(outputMessage, outputMessageData);
		int time = 0;
		time = outputMessageData.gettOM();
		if (time != 0) {
			waitingCounter = new WaitingCounter(time, mainGUI);
		}
	}

	/**
	 * Highlights actual hole.
	 * @param x
	 * @param y
	 * @param color
	 */
	public void higlightActualHole(int x, int y, Color color) {
		int holeNo = calculations.mapCoordinatesToButtonNo(x, y);
		mainGUI.routePanel.setActualColor(color);
		mainGUI.routePanel.setActualHole(holeNo);
		mainGUI.routePanel.repaint();
	}
	
	/**
	 * Highlights expected hole.
	 * @param x
	 * @param y
	 * @param color
	 */
	public void higlightExpectedHole(int x, int y, Color color) {
		int holeNo = calculations.mapCoordinatesToButtonNo(x, y);
		mainGUI.routePanel.setExpectedColor(color);
		mainGUI.routePanel.setExpectedHole(holeNo);
		mainGUI.routePanel.repaint();
	}

	public void goToSpecifiedHole(int holeNo) {
		// check if globalCalibrate is set to true
		if (globalCalibrated) {
			// check actual coordinates
			getActCoordinates();
			// check coordinates of specified hole
			int[] holeCoordinates = Calculations.mapHoleNoToCoordinates(holeNo);
			// calculate a command to send
			int[] actualCoordinates = { actX, actY, actZ };
			String messageToSend = calculations.calculateOutputMessage(holeNo, actualCoordinates);
			// send
			//writeData(velocity, xdir, ydir, zdir, x, y, z, t, error, hierarchy);
		} else {
			log.log("It seems that the robot is not calibrated. Please calibrate.", "It seems that the robot is not calibrated.");
			logger.error("It seems that the robot is not calibrated.\nPlease calibrate.");
			JOptionPane.showMessageDialog(MainGUI.mainGUIfrm, "It seems that the robot is not calibrated.\nPlease calibrate.", "Not calibrated.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/* GETTERS & SETTERS */

	public boolean isConnectedToPort() {
		return connectedToPort;
	}

	public void setConnectedToPort(boolean connectedToPort) {
		this.connectedToPort = connectedToPort;
	}

	public String getSendedMessage() {
		return sendedMessage;
	}

	public void setSendedMessage(String sendedMessage) {
		this.sendedMessage = sendedMessage;
	}

	public String getInputMessage() {
		return inputMessage;
	}

	public void setInputMessage(String inputMessage) {
		this.inputMessage = inputMessage;
	}

	public int getActX() {
		return actX;
	}

	public void setActX(int actX) {
		this.actX = actX;
	}

	public int getActY() {
		return actY;
	}

	public void setActY(int actY) {
		this.actY = actY;
	}

	public int getActZ() {
		return actZ;
	}

	public void setActZ(int actZ) {
		this.actZ = actZ;
	}

	
	public boolean isGlobalCalibrated() {
		return globalCalibrated;
	}
	

	public void setGlobalCalibrated(boolean globalCalibrated) {
		this.globalCalibrated = globalCalibrated;
	}
}