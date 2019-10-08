package gui.pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import biorobot.data.SystemParametersData;
import biorobot.pack.OperationProcessor;
import biorobot.pack.SystemParameters;

public class NewRouteGUI extends JFrame {

	private static final long serialVersionUID = -4631407592664611937L;

	JFrame newRoutefrm;
	JTextPane txtpnActualRoute;

	List<String> commandText = new ArrayList<String>();
	List<String> commandList = new ArrayList<String>();

	OperationProcessor operationProcessor = null;
	JFileChooser fileChooser = new JFileChooser();
	SystemParametersData systemParametersData = new SystemParametersData();

	public NewRouteGUI(OperationProcessor operationProcessor) {
		this.operationProcessor = operationProcessor;
		initialize();
		fillTheRouteText();
	}

	private void fillTheRouteText() {
		String actualRouteTmp = "";
		for (int i = 0; i < commandText.size(); i++) {
			actualRouteTmp = actualRouteTmp + commandText.get(i) + "\n";
		}
		txtpnActualRoute.setText(actualRouteTmp);
	}

	private void buttonPressed(int whichButton) {
		// Prompt for time to keep the robot arm submerged.
		int timeInt = Integer.parseInt((String) JOptionPane.showInputDialog(newRoutefrm, "How much time do you want the robot\nto keep the samples submerged?\nHint: Type \"999\" to keep the samples submerged.", "New route", JOptionPane.QUESTION_MESSAGE, null, null, 1));
		commandList.add(whichButton + "," + timeInt + ";");
		commandText.add("[" + whichButton + "]: " + timeInt + " seconds;");
		fillTheRouteText();
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		newRoutefrm = new JFrame();
		setTitle("New route");
		setBounds(100, 100, 574, 563);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel lblActualRoute = new JLabel("Actual route:");
		lblActualRoute.setBounds(10, 11, 63, 14);
		getContentPane().add(lblActualRoute);

		JButton btnDeleteLast = new JButton("Delete last");
		btnDeleteLast.setBounds(256, 491, 89, 23);
		btnDeleteLast.setEnabled(false);
		getContentPane().add(btnDeleteLast);

		JButton btnDeleteAll = new JButton("Delete all");
		btnDeleteAll.setBounds(356, 491, 89, 23);
		getContentPane().add(btnDeleteAll);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(455, 491, 89, 23);
		getContentPane().add(btnSave);

		JSeparator separator = new JSeparator();
		separator.setBounds(256, 478, 288, 2);
		getContentPane().add(separator);

		JTextPane txtpnThisIsThe = new JTextPane();
		txtpnThisIsThe.setEnabled(false);
		txtpnThisIsThe.setEditable(false);
		txtpnThisIsThe.setText("This is the route wizard. Each button represents a hole in the palette. In order to create a new route, click on the buttons in a specified order and click save. After that you can load this route in the main window.");
		txtpnThisIsThe.setBounds(256, 406, 288, 61);
		getContentPane().add(txtpnThisIsThe);

		JButton button = new JButton("0");
		button.setBounds(356, 36, 60, 25);
		getContentPane().add(button);

		JButton button_3 = new JButton("3");
		button_3.setBounds(282, 72, 60, 25);
		getContentPane().add(button_3);

		JButton button_2 = new JButton("2");
		button_2.setBounds(356, 72, 60, 25);
		getContentPane().add(button_2);

		JButton button_1 = new JButton("1");
		button_1.setBounds(426, 72, 60, 25);
		getContentPane().add(button_1);

		JButton button_6 = new JButton("6");
		button_6.setBounds(282, 108, 60, 25);
		getContentPane().add(button_6);

		JButton button_5 = new JButton("5");
		button_5.setBounds(356, 108, 60, 25);
		getContentPane().add(button_5);

		JButton button_4 = new JButton("4");
		button_4.setBounds(426, 108, 60, 25);
		getContentPane().add(button_4);

		JButton button_9 = new JButton("9");
		button_9.setBounds(282, 144, 60, 25);
		getContentPane().add(button_9);

		JButton button_8 = new JButton("8");
		button_8.setBounds(356, 144, 60, 25);
		getContentPane().add(button_8);

		JButton button_7 = new JButton("7");
		button_7.setBounds(426, 144, 60, 25);
		getContentPane().add(button_7);

		JButton button_12 = new JButton("12");
		button_12.setBounds(282, 180, 60, 25);
		getContentPane().add(button_12);

		JButton button_11 = new JButton("11");
		button_11.setBounds(356, 180, 60, 25);
		getContentPane().add(button_11);

		JButton button_10 = new JButton("10");
		button_10.setBounds(426, 180, 60, 25);
		getContentPane().add(button_10);

		JButton button_15 = new JButton("15");
		button_15.setBounds(282, 216, 60, 25);
		getContentPane().add(button_15);

		JButton button_14 = new JButton("14");
		button_14.setBounds(356, 216, 60, 25);
		getContentPane().add(button_14);

		JButton button_13 = new JButton("13");
		button_13.setBounds(426, 216, 60, 25);
		getContentPane().add(button_13);

		JButton button_18 = new JButton("18");
		button_18.setBounds(282, 252, 60, 25);
		getContentPane().add(button_18);

		JButton button_17 = new JButton("17");
		button_17.setBounds(356, 252, 60, 25);
		getContentPane().add(button_17);

		JButton button_16 = new JButton("16");
		button_16.setBounds(426, 252, 60, 25);
		getContentPane().add(button_16);

		JButton button_21 = new JButton("21");
		button_21.setBounds(282, 288, 60, 25);
		getContentPane().add(button_21);

		JButton button_20 = new JButton("20");
		button_20.setBounds(356, 288, 60, 25);
		getContentPane().add(button_20);

		JButton button_19 = new JButton("19");
		button_19.setBounds(426, 288, 60, 25);
		getContentPane().add(button_19);

		JButton button_24 = new JButton("24");
		button_24.setBounds(282, 324, 60, 25);
		getContentPane().add(button_24);

		JButton button_23 = new JButton("23");
		button_23.setBounds(356, 324, 60, 25);
		getContentPane().add(button_23);

		JButton button_22 = new JButton("22");
		button_22.setBounds(426, 324, 60, 25);
		getContentPane().add(button_22);

		JButton button_25 = new JButton("25");
		button_25.setBounds(356, 360, 60, 25);
		getContentPane().add(button_25);

		JButton btnCalibration = new JButton("Calibration");
		btnCalibration.setBounds(455, 372, 89, 23);
		btnCalibration.setEnabled(false);
		getContentPane().add(btnCalibration);

		txtpnActualRoute = new JTextPane();
		txtpnActualRoute.setText("Actual route will be here...");
		txtpnActualRoute.setBounds(10, 36, 236, 478);
		getContentPane().add(txtpnActualRoute);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_0ActionPerformed(event);
			}
		});

		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_1ActionPerformed(event);
			}
		});

		button_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_2ActionPerformed(event);
			}
		});

		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_3ActionPerformed(event);
			}
		});

		button_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_4ActionPerformed(event);
			}
		});

		button_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_5ActionPerformed(event);
			}
		});

		button_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_6ActionPerformed(event);
			}
		});

		button_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_7ActionPerformed(event);
			}
		});

		button_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_8ActionPerformed(event);
			}
		});

		button_9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_9ActionPerformed(event);
			}
		});

		button_10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_10ActionPerformed(event);
			}
		});

		button_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_11ActionPerformed(event);
			}
		});

		button_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_12ActionPerformed(event);
			}
		});

		button_13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_13ActionPerformed(event);
			}
		});

		button_14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_14ActionPerformed(event);
			}
		});

		button_15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_15ActionPerformed(event);
			}
		});

		button_16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_16ActionPerformed(event);
			}
		});

		button_17.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_17ActionPerformed(event);
			}
		});

		button_18.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_18ActionPerformed(event);
			}
		});

		button_19.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_19ActionPerformed(event);
			}
		});

		button_20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_20ActionPerformed(event);
			}
		});

		button_21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_21ActionPerformed(event);
			}
		});

		button_22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_22ActionPerformed(event);
			}
		});

		button_23.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_23ActionPerformed(event);
			}
		});

		button_24.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_24ActionPerformed(event);
			}
		});

		button_25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				button_25ActionPerformed(event);
			}
		});

		btnCalibration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnCalibrationActionPerformed(event);
			}
		});

		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnSaveActionPerformed(event);
			}
		});

		btnDeleteAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnDeleteAllActionPerformed(event);
			}
		});

		btnDeleteLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnDeleteLastActionPerformed(event);
			}
		});

		setVisible(false);
	}

	private void button_0ActionPerformed(ActionEvent event) {
		buttonPressed(0);
	}

	private void button_1ActionPerformed(ActionEvent event) {
		buttonPressed(1);
	}

	private void button_2ActionPerformed(ActionEvent event) {
		buttonPressed(2);
	}

	private void button_3ActionPerformed(ActionEvent event) {
		buttonPressed(3);
	}

	private void button_4ActionPerformed(ActionEvent event) {
		buttonPressed(4);
	}

	private void button_5ActionPerformed(ActionEvent event) {
		buttonPressed(5);
	}

	private void button_6ActionPerformed(ActionEvent event) {
		buttonPressed(6);
	}

	private void button_7ActionPerformed(ActionEvent event) {
		buttonPressed(7);
	}

	private void button_8ActionPerformed(ActionEvent event) {
		buttonPressed(8);
	}

	private void button_9ActionPerformed(ActionEvent event) {
		buttonPressed(9);
	}

	private void button_10ActionPerformed(ActionEvent event) {
		buttonPressed(10);
	}

	private void button_11ActionPerformed(ActionEvent event) {
		buttonPressed(11);
	}

	private void button_12ActionPerformed(ActionEvent event) {
		buttonPressed(12);
	}

	private void button_13ActionPerformed(ActionEvent event) {
		buttonPressed(13);
	}

	private void button_14ActionPerformed(ActionEvent event) {
		buttonPressed(14);
	}

	private void button_15ActionPerformed(ActionEvent event) {
		buttonPressed(15);
	}

	private void button_16ActionPerformed(ActionEvent event) {
		buttonPressed(16);
	}

	private void button_17ActionPerformed(ActionEvent event) {
		buttonPressed(17);
	}

	private void button_18ActionPerformed(ActionEvent event) {
		buttonPressed(18);
	}

	private void button_19ActionPerformed(ActionEvent event) {
		buttonPressed(19);
	}

	private void button_20ActionPerformed(ActionEvent event) {
		buttonPressed(20);
	}

	private void button_21ActionPerformed(ActionEvent event) {
		buttonPressed(21);
	}

	private void button_22ActionPerformed(ActionEvent event) {
		buttonPressed(22);
	}

	private void button_23ActionPerformed(ActionEvent event) {
		buttonPressed(23);
	}

	private void button_24ActionPerformed(ActionEvent event) {
		buttonPressed(24);
	}

	private void button_25ActionPerformed(ActionEvent event) {
		buttonPressed(25);
	}

	private void btnCalibrationActionPerformed(ActionEvent event) {
		commandText.add("Calibration;");
		commandList.add("Sv" + systemParametersData.getZvelocity() + "d000x99999y99999z99999t000e3h1E");
		fillTheRouteText();
	}

	private void btnSaveActionPerformed(ActionEvent event) {
		File routeFile = null;
		int returnVal = fileChooser.showSaveDialog(newRoutefrm);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			routeFile = fileChooser.getSelectedFile();
			if (!routeFile.getName().endsWith(".txt")) {
				routeFile = new File(routeFile.getAbsolutePath().concat(".txt"));
			}
			String actualRouteTmp = "";
			for (int i = 0; i < commandList.size(); i++) {
				actualRouteTmp = actualRouteTmp + commandList.get(i) + "\n";
			}
			try {
				if (!routeFile.exists()) {
					routeFile.createNewFile();
				}
				FileWriter fw = new FileWriter(routeFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(actualRouteTmp);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void btnDeleteAllActionPerformed(ActionEvent event) {
		commandList.clear();
		commandText.clear();
		fillTheRouteText();
	}

	private void btnDeleteLastActionPerformed(ActionEvent event) {
		commandList.remove(commandList.size() - 1);
		commandText.remove(commandText.size() - 1);
		fillTheRouteText();
	}
}
