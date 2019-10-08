package biorobot.pack;

import java.util.ArrayList;

import biorobot.data.ExpectedCoordinatesData;
import biorobot.data.InputMessageData;
import biorobot.data.OutputMessageData;
import biorobot.data.SystemParametersData;

/**
 * Calculates things.
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */

public class Calculations {
	
	static SystemParametersData systemParametersData = new SystemParametersData();
	
	/**
	 * Maps coordinates to hole number.
	 * @param x
	 * @param y
	 * @return
	 */
	public int mapCoordinatesToButtonNo(int x, int y) {
		int holeNo = 27;
		// Check which hole corresponds to given x and y
		if (x == systemParametersData.getBighole1xcoord() * systemParametersData.getMultiplicator() && y == systemParametersData.getBigholeycoord() * systemParametersData.getMultiplicator()) {
			holeNo = 0;
		} else if (x == systemParametersData.getBighole2xcoord() * systemParametersData.getMultiplicator() && y == systemParametersData.getBigholeycoord() * systemParametersData.getMultiplicator()) {
			holeNo = 25;
		} else { 
			// Check if these are a proper hole coordinates
			if ((x - systemParametersData.getXposoffirst() * systemParametersData.getMultiplicator()) % systemParametersData.getGap() * systemParametersData.getMultiplicator() == 0 && (y - systemParametersData.getYposoffirst() * systemParametersData.getMultiplicator()) % systemParametersData.getGap() * systemParametersData.getMultiplicator() == 0 ) {
				// Create an array of possible holes.
				int k = 1;
				int[][] holes = new int[3][8];
				for (int col = 0; col < 8; col++) {
					for (int row = 0; row < 3; row++) {
						holes[row][col] = k;
						k++;
					}
				}
				// Check which row and column is it.
				int xArray = (x - systemParametersData.getXposoffirst() * systemParametersData.getMultiplicator()) / (systemParametersData.getGap() * systemParametersData.getMultiplicator());
				int yArray = (y - systemParametersData.getYposoffirst() * systemParametersData.getMultiplicator()) / (systemParametersData.getGap() * systemParametersData.getMultiplicator());
				// Take the button number.
				holeNo = holes[yArray][xArray];
			} else {
				// TODO If not, draw a cross at actual position and a line to that cross.
			}
		}
		return holeNo;
	}
	
	/**
	 * Maps hole number to coordinates.
	 * Note that you must add after-calibration coordinates to returned values.
	 * @param holeNo
	 * @return
	 */
	public static int[] mapHoleNoToCoordinates(int holeNo) {
		int coordinates[] = new int[2];
		// Check if this is the first or last hole.
		if (holeNo == 0) {
			coordinates[0] = systemParametersData.getBighole1xcoord();
			coordinates[1] = systemParametersData.getBigholeycoord();
		} else if (holeNo == 25) {
			coordinates[0] = systemParametersData.getBighole2xcoord();
			coordinates[1] = systemParametersData.getBigholeycoord();
		} else {
			// Make a map of holes.
			int k = 1;
			int[][] holes = new int[3][8];
			for (int col = 0; col < 8; col++) {
				for (int row = 0; row < 3; row++) {
					holes[row][col] = k;
					k++;
				}
			}
			// Calculate in which row and column is our hole placed.
			int row;
			int column;
			row = (holeNo % SystemParameters.YHOLESNO);
			if (row == 0) {
				row += 3;
				column = (holeNo / SystemParameters.YHOLESNO);
			} else {
				column = (holeNo / SystemParameters.YHOLESNO) + 1;
			}
			// Calculate coordinates.
			int xCoordinate = systemParametersData.getXposoffirst() + (systemParametersData.getGap() * (column - 1));
			int yCoordinate = systemParametersData.getYposoffirst() + (systemParametersData.getGap() * (row - 1));
			coordinates[0] = xCoordinate;
			coordinates[1] = yCoordinate;
		}
		return coordinates;
	}

	/**
	 * Takes input message and outputs useful data.
	 * @param inputMessage
	 * @return
	 */
	public InputMessageData processInputMessage(String inputMessage, InputMessageData inputMessageData) {
		// Sl000000x00000y00000z00000t000e0E - inputMessage
		int lStartIM = inputMessage.indexOf("l") + 1;
		int lEndIM = inputMessage.indexOf("x");
		int xStartIM = inputMessage.indexOf("x") + 1;
		int xEndIM = inputMessage.indexOf("y");
		int yStartIM = inputMessage.indexOf("y") + 1;
		int yEndIM = inputMessage.indexOf("z");
		int zStartIM = inputMessage.indexOf("z") + 1;
		int zEndIM = inputMessage.indexOf("t");
		int tStartIM = inputMessage.indexOf("t") + 1;
		int tEndIM = inputMessage.indexOf("e");
		int eStartIM = inputMessage.indexOf("e") + 1;
		int eEndIM = inputMessage.indexOf("E");
		// Fill DTO with new values.
		String lIM = inputMessage.substring(lStartIM, lEndIM);
		int lockTable[] = new int[6];
		for (int i = 0; i < lIM.length(); i++) {
			lockTable[i] = lIM.charAt(i);
		}
		inputMessageData.setlIM(lockTable);
		inputMessageData.setxIM(Integer.parseInt(inputMessage.substring(xStartIM, xEndIM)));
		inputMessageData.setyIM(Integer.parseInt(inputMessage.substring(yStartIM, yEndIM)));
		inputMessageData.setzIM(Integer.parseInt(inputMessage.substring(zStartIM, zEndIM)));
		inputMessageData.settIM(Integer.parseInt(inputMessage.substring(tStartIM, tEndIM)));
		inputMessageData.seteIM(Integer.parseInt(inputMessage.substring(eStartIM, eEndIM)));
		return inputMessageData;
	}

	/**
	 * Takes output message and outputs useful data.
	 * @param outputMessage
	 * @param outputMessageData
	 * @return
	 */
	public OutputMessageData processOutputMessage(String outputMessage, OutputMessageData outputMessageData) {
		// Sv9d000x00100y00200z00200t602e0h0E - outputMessage
		int vStartOM = outputMessage.indexOf("v") + 1;
		int vEndOM = outputMessage.indexOf("d");
		int dStartOM = outputMessage.indexOf("d") + 1;
		int dEndOM = outputMessage.indexOf("x");
		int xStartOM = outputMessage.indexOf("x") + 1;
		int xEndOM = outputMessage.indexOf("y");
		int yStartOM = outputMessage.indexOf("y") + 1;
		int yEndOM = outputMessage.indexOf("z");
		int zStartOM = outputMessage.indexOf("z") + 1;
		int zEndOM = outputMessage.indexOf("t");
		int tStartOM = outputMessage.indexOf("t") + 1;
		int tEndOM = outputMessage.indexOf("e");
		int eStartOM = outputMessage.indexOf("e") + 1;
		int eEndOM = outputMessage.indexOf("h");
		int hStartOM = outputMessage.indexOf("h") + 1;
		int hEndOM = outputMessage.indexOf("E");
		// Fill DTO with new values.
		outputMessageData.setvOM(Integer.parseInt(outputMessage.substring(vStartOM, vEndOM)));
		String dOM = outputMessage.substring(dStartOM, dEndOM);
		int dirTable[] = new int[3];
		for (int i = 0; i < dOM.length(); i++) {
			dirTable[i] = Character.getNumericValue(dOM.charAt(i));
		}
		outputMessageData.setdOM(dirTable);
		outputMessageData.setxOM(Integer.parseInt(outputMessage.substring(xStartOM, xEndOM)));
		outputMessageData.setyOM(Integer.parseInt(outputMessage.substring(yStartOM, yEndOM)));
		outputMessageData.setzOM(Integer.parseInt(outputMessage.substring(zStartOM, zEndOM)));
		outputMessageData.settOM(Integer.parseInt(outputMessage.substring(tStartOM, tEndOM)));
		outputMessageData.seteOM(Integer.parseInt(outputMessage.substring(eStartOM, eEndOM)));
		outputMessageData.sethOM(Integer.parseInt(outputMessage.substring(hStartOM, hEndOM)));
		return outputMessageData;
	}

	/**
	 * Counts the expected coordinates values.
	 * @param coordinates
	 * @param directories
	 * @param actualCoordinates
	 * @param expectedCoordinatesData
	 * @return
	 */
	public ExpectedCoordinatesData countExpectedCoordinates(int[] coordinates, int[] directions, int[] actualCoordinates, ExpectedCoordinatesData expectedCoordinatesData) {
		if (directions[0] == 1) {
			expectedCoordinatesData.setExpectedX(actualCoordinates[0] + coordinates[0] * systemParametersData.getMultiplicator());
		}
		if (directions[0] == 0) {
			expectedCoordinatesData.setExpectedX(actualCoordinates[0] - coordinates[0] * systemParametersData.getMultiplicator());
		}
		if (directions[1] == 1) {
			expectedCoordinatesData.setExpectedY(actualCoordinates[1] + coordinates[1] * systemParametersData.getMultiplicator());
		}
		if (directions[1] == 0) {
			expectedCoordinatesData.setExpectedY(actualCoordinates[1] - coordinates[1] * systemParametersData.getMultiplicator());
		}
		if (directions[2] == 1) {
			expectedCoordinatesData.setExpectedZ(actualCoordinates[2] + coordinates[2] * systemParametersData.getMultiplicator());
		}
		if (directions[2] == 0) {
			expectedCoordinatesData.setExpectedZ(actualCoordinates[2] - coordinates[2] * systemParametersData.getMultiplicator());
		}
		return expectedCoordinatesData;
	}

	/**
	 * Generates a single message from the given parameters.
	 * @param xdir
	 * @param ydir
	 * @param zdir
	 * @param x
	 * @param y
	 * @param z
	 * @param t
	 * @param error
	 * @param hierarchy
	 * @return
	 */
	public String generateSingleMessage(int velocity, int xdir, int ydir, int zdir, int x, int y, int z, int t, int error, int hierarchy) {
		String singleMessage = "";
		// Concatenate message.
		singleMessage = "S" +
						"v" + velocity + 
						"d" + xdir + ydir + zdir + 
						"x" + String.format("%05d", x) + 
						"y" + String.format("%05d", y) + 
						"z" + String.format("%05d", z) +
						"t" + String.format("%03d", t) +
						"e" + error +
						"h" + hierarchy +
						"E";
		return singleMessage;
	}
	
	/**
	 * Generates a list of messages from route list, containing only numbers of holes and submerging time. 
	 * @param routeList
	 * @return
	 */
	public ArrayList<String> generateFullMessageFromRoute(ArrayList<String> routeList) {
		//FIXME Time mustn't equal 999 in the middle of the route! What will happen then?
		int actualX = 0;
		int actualY = 0;
		int actualZ = 0;
		int xPos;
		int yPos;
		int xDir;
		int yDir;
		int xToMove;
		int yToMove;
		ArrayList<String> commandList = new ArrayList<String>();
		String element = "";
		String singleCommand = "";
		String holeAndTime[] = new String [2];
		int holeNo;
		int submergeTime;
		// Do this for every element on the list.
		for (int i = 0; i < routeList.size(); i++) {
			// Get first element, which should be like "27,60;".
			// Get rid of the semicolon.
			element = routeList.get(i).replaceFirst(";", "");
			// Now we've got something like "27,60". Get holeNo and submergeTime.
			holeAndTime = element.split(",");
			holeNo = Integer.parseInt(holeAndTime[0]);
			submergeTime = Integer.parseInt(holeAndTime[1]);
			// Generate message to send.
			// Map hole no to coordinates.
			int coordinates[] = mapHoleNoToCoordinates(holeNo);
			xPos = coordinates[0];
			yPos = coordinates[1];
			// Count directions
			xDir = (actualX < xPos) ? 1 : 0;
			yDir = (actualY < yPos) ? 1 : 0;
			// Calculate the steps that robot must make to move to specified hole.
			xToMove = Math.abs(actualX - xPos);
			yToMove = Math.abs(actualY - yPos);
			// Calculate submerging time.
			//submergeTime = (submergeTime == 999) ? 0 : submergeTime;
			// Check other situations.
			if (actualZ == 0) {
				// If actual Z position equals zero, we're at the top. We should move the arm down firstly.
				singleCommand = generateSingleMessage(systemParametersData.getZvelocity(), 0, 0, 1, 0, 0, Integer.parseInt(SystemParameters.ZTRANSPORT), 0, 0, 1);
				commandList.add(singleCommand);
				// Set actual Z position.
				actualZ = Integer.parseInt(SystemParameters.ZTRANSPORT);
			}
			if (!(xToMove == 0 && yToMove == 0)) {
				// If either xToMove or yToMove will be different then 0, generate message.
				singleCommand = generateSingleMessage(systemParametersData.getXyvelocity(), xDir, yDir, 0, xToMove, yToMove, 0, 0, 0, 0);
				commandList.add(singleCommand);
				// After every X or Y move, generate submerging message.
				singleCommand = generateSingleMessage(systemParametersData.getZvelocity(), 0, 0, 1, 0, 0, Integer.parseInt(SystemParameters.IMMERSION), submergeTime, 0, 1);
				commandList.add(singleCommand);
				// If time doesn't equal 999, we don't want to keep the robot submerged.
				if (submergeTime != 999) {
					singleCommand = generateSingleMessage(systemParametersData.getZvelocity(), 0, 0, 0, 0, 0, Integer.parseInt(SystemParameters.IMMERSION), 0, 0, 1);
					commandList.add(singleCommand);
				}
				// Update actual coordinates
				actualX = xPos;
				actualY = yPos;
			}
		}
		return commandList;
	}
	
	/**
	 * Calculates the route details that are displayed on mainGUI.
	 * @param nodesList
	 * @return
	 */
	public int[] calculateRouteDetails(ArrayList<String> nodesList) {
		int[] holesAndTotalTime = new int[2];
		int holes = 0;
		int totalTime = 0;
		for (int i = 0; i < nodesList.size(); i++) {
			String[] holeAndTime = nodesList.get(i).replace(";", "").split(",");
			holes++;
			totalTime += Integer.parseInt(holeAndTime[1]);
		}
		holesAndTotalTime[0] = holes;
		holesAndTotalTime[1] = totalTime;
		return holesAndTotalTime;
	}

	public String calculateOutputMessage(int holeNo, int[] actualCoordinates) {
		//TODO Take directions into account.
		int xToMove = 0;
		int yToMove = 0;
		int zToMove = 0;
		// get the coordinates of a hole
		int[] holeCoordinates = mapHoleNoToCoordinates(holeNo);
		// subtract actual coordinates from the hole coordinates
		xToMove = holeCoordinates[0] - actualCoordinates[0];
		yToMove = holeCoordinates[1] - actualCoordinates[1];
		//zToMove = 
		// generate message
		return "test";
	}
}
