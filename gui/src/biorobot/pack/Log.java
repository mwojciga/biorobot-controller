package biorobot.pack;

import gui.pack.MainGUI;

/**
 * Writes logs and messages.
 * @author maciej.wojciga
 * @author klaudia.trembecka
 */
public class Log {

	public void log(String messages, String logs) {
		if (messages != null) {
			// Write to messages.
			MainGUI.txtarMessages.append(messages + "\n");
		}
		if (logs != null) {
			// Write to logs.
			MainGUI.txtarLogs.append(logs + "\n");
		}
	}
}
