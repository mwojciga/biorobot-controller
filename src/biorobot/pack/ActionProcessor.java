package biorobot.pack;

import gui.pack.MainGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

public class ActionProcessor {
	MainGUI mainGUIfrm = null;
	private ArrayList<String> nodesList = new ArrayList<String>();
	Calculations calculations = new Calculations();

	/* LOG */
	Logger logger = Logger.getLogger(ActionProcessor.class);
	Log log = new Log();
	
	public ActionProcessor(MainGUI mainGUIfrm){
		this.mainGUIfrm = mainGUIfrm;
	}

	public File loadFile() {
		final JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(mainGUIfrm.mainPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File choosenFile = fileChooser.getSelectedFile();
				log.log("Choosen route file: " + choosenFile.getCanonicalPath(), null);
				logger.info("Choosen route file: " + choosenFile.getCanonicalPath());
				mainGUIfrm.txtChoosenRoutePath.setText(choosenFile.getPath());
				try {
					BufferedReader reader = new BufferedReader(new FileReader(choosenFile));
					String line = "";
					while ((line = reader.readLine()) != null){
						nodesList.add(line);
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String actualRouteTmp = "";
				for (int i = 0; i < nodesList.size(); i++) {
					// TODO Make it more human readable!
					actualRouteTmp = actualRouteTmp + i + ": [" + nodesList.get(i) + "]\n";
				}
				mainGUIfrm.txtpnRouteOverview.setText(actualRouteTmp);
				int[] nodesAndHoles = new int[2];
				nodesAndHoles = calculations.calculateRouteDetails(nodesList);
				mainGUIfrm.lblTotalNumberOfHolesNo.setText(String.valueOf(nodesAndHoles[0]));
				// calculate how many hours, minutes, seconds is in nodesAndHoles[1] [sec]
				int hours = nodesAndHoles[1]/3600;
				int minutes = (nodesAndHoles[1] - (hours * 3600)) / 60;
				int seconds = nodesAndHoles[1] - (hours * 3600) - (minutes * 60);
				String hoursStr = String.format("%02d", hours);
				String minutesStr = String.format("%02d", minutes);
				String secondsStr = String.format("%02d", seconds);
				mainGUIfrm.lblTotalWaitingTimeNo.setText(hoursStr + "h " + minutesStr + "min " + secondsStr + "sec");
				mainGUIfrm.lblTotalNumberOfHolesNo.setEnabled(true);
				mainGUIfrm.lblTotalWaitingTimeNo.setEnabled(true);
				actualRouteTmp = "";
				return choosenFile;
			} catch (IOException e) {
				log.log("An exception occured. See more in log file.", "An exception occured. See more in log file.");
				logger.error(e.toString());
			}
		}
		return null;
	}

	public String takeFileAndWriteToString(File inputFile, String onlyThese){
		String outputString = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			String line = "";
			while ((line = reader.readLine()) != null){
				if (!onlyThese.equals(null)) {
					if (line.startsWith(onlyThese)) {
						String lineArr[] = line.split("]");
						outputString += lineArr[1] + "\n";
					}
				} else {
					outputString += line + "\n";
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputString;
	}
}
