package biorobot.threads;

import gui.pack.MainGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import biorobot.data.OutputMessageData;
import biorobot.pack.Calculations;
import biorobot.pack.Log;
import biorobot.pack.OperationProcessor;

public class StartRouteFromFile implements Runnable {

	MainGUI mainGUI;
	OperationProcessor operationProcessor;
	Calculations calculations = new Calculations();
	OutputMessageData outputMessageData = new OutputMessageData();
	public Thread startRouteFromFileThread;
	
	/* LOG */
	Logger logger = Logger.getLogger(StartRouteFromFile.class);
	Log log = new Log();
	
	File loadedFile;
	String task;
	public String outputMessage;
	public String fullMessage;
	private ArrayList<String> simpleList = new ArrayList<String>();
	private ArrayList<String> commandList = new ArrayList<String>();

	private boolean suspendFlag = true;

	public StartRouteFromFile(MainGUI mainGUI, OperationProcessor operationProcessor, File loadedFile) {
		this.mainGUI = mainGUI;
		this.operationProcessor = operationProcessor;
		startRouteFromFileThread = new Thread(this, "StartRouteFromFileThread");
		startRouteFromFileThread.start();
		this.loadedFile = loadedFile;
	}

	@Override
	public void run() {
		try {
			// TODO Policz przy starcie trasy jakie powinny byc koñcowe koordynaty. Jeœli siê pojawi¹ i lista siê zakoñczy to wyœwietl komunikat.
			mainGUI.progressBar.setValue(0);
			BufferedReader reader = new BufferedReader(new FileReader(loadedFile));
			String line = "";
			while ((line = reader.readLine()) != null){
				simpleList.add(line);
			}
			reader.close();
			// Now we've got a list of hole number + submerging time pairs. We must make a list of uC readable messages from this list.
			commandList = calculations.generateFullMessageFromRoute(simpleList);
			// Pass the command list, divide it into single messages and send them to OP.
			for (int i = 0; i < commandList.size(); i++) {
				outputMessage = commandList.get(i);
				// Sv9d000x10000y20000z20000t602e0h0E
				outputMessageData = calculations.processOutputMessage(outputMessage, outputMessageData);
				int[] directories = outputMessageData.getdOM();
				operationProcessor.writeData(outputMessageData.getvOM(), directories[0], directories[1], directories[2], outputMessageData.getxOM(), outputMessageData.getyOM(), outputMessageData.getzOM(), outputMessageData.gettOM(), outputMessageData.geteOM(), outputMessageData.gethOM());
				if (i != commandList.size()-1) {
					suspendFlag = true;
					log.log("Waiting for response...", null);
					synchronized(this){
						while (suspendFlag) {
							// Wait for OP to read a proper message
							wait();
						}
					}
				}
				int percentageDone = Math.round((i*100.0f)/commandList.size());
				System.out.println("%: " + percentageDone);
				mainGUI.progressBar.setValue(percentageDone);
				if (i == commandList.size()-1) {
					mainGUI.progressBar.setValue(0);
					JOptionPane.showMessageDialog(mainGUI, "Trasa zosta³a zakoñczona!", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.log("An exception occured. See more in log file.", "An exception occured. See more in log file.");
			logger.error(e.toString());
		} catch (IOException e) {
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
	
	public void reset() {
		simpleList.clear();
		commandList.clear();
		mainGUI.progressBar.setValue(0);
	}
}
