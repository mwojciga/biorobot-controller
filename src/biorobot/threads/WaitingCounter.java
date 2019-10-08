package biorobot.threads;

import gui.pack.MainGUI;

/**
 * Desc: 
 * @author maciej.wojciga
 * @author klaudia.trembecka
 *
 */
public class WaitingCounter implements Runnable {
	
	public Thread waitingCounterThread;
	int time = 0;
	MainGUI mainGUI;
	long timeToWait = 1000;
	
	public WaitingCounter(int time, MainGUI mainGUI) {
		this.time = time;
		this.mainGUI = mainGUI;
		waitingCounterThread = new Thread(this, "WaitingCounterThread");
		waitingCounterThread.start();
	}
	
	@Override
	public void run() {
		try {
		mainGUI.lblWaiting.setEnabled(true);
		for (int i = 0; i < time; i++) {
			mainGUI.lblWaiting.setText("Waiting for " + (time - i) + " seconds...");
			Thread.sleep(timeToWait);
		}
		mainGUI.lblWaiting.setText("Not waiting.");
		mainGUI.lblWaiting.setEnabled(false);
		} catch (InterruptedException e) {
			// TODO Catch to logs.
			e.printStackTrace();
		}
	}
}
