package gui.pack;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.Logger;

import biorobot.data.SystemParametersData;
import biorobot.pack.ActionProcessor;
import biorobot.pack.OperationProcessor;
import biorobot.pack.SystemParameters;

/**
 * The main class of the software.
 * Initializes the GUI and starts the software.
 * @author maciej.wojciga
 */

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public static Logger logger = Logger.getLogger(MainGUI.class);
	
	public static MainGUI mainGUIfrm;
	public static NewRouteGUI newRouteGUIfrm;
	public static OptionsGUI optionsGUIfrm;

	public File loadedFile;
	public JComboBox availablePorts;

	public JPanel mainPane;
	public JPanel controlManuallyPane;
	public RoutePanel routePanel;

	public static JTextArea txtarLogs;
	public static JTextArea txtarMessages;

	public JTextField txtChoosenRoutePath;
	public JTextPane txtpnRouteOverview;
	public JTextField actualPosition;
	public JLabel lblWaiting;
	public JTextField txtGoToHoleNo;

	public JLabel lblConnectionStatus;
	public JButton btnConnect;
	public JButton btnDisconnect;
	public JProgressBar progressBar;
	public JButton btnStart;
	public JButton btnStop;
	public JButton btnLoad;
	public JButton btnCalibrate;
	public JButton xLeft;
	public JButton xLeftMore;
	public JButton xRight;
	public JButton xRightMore;
	public JButton yLeft;
	public JButton yLeftMore;
	public JButton yRight;
	public JButton yRightMore;
	public JButton zLeft;
	public JButton zLeftMore;
	public JButton zRight;
	public JButton zRightMore;
	public JButton btnGetCoordinates;
	public JLabel lblGoToHole;
	public JButton btnGoToHole;
	public JLabel lblTotalNumberOfHolesNo;
	public JLabel lblTotalWaitingTimeNo;

	JSpinner xDir;
	JSpinner yDir;
	JSpinner zDir;

	Properties confProperties;

	OperationProcessor operationProcessor = null;
	ActionProcessor actionProcessor = null;
	SystemParametersData systemParametersData = new SystemParametersData();

	private void createObjects() {
		operationProcessor = new OperationProcessor(this);
		actionProcessor = new ActionProcessor(this);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGUIfrm = new MainGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public MainGUI() {
		configureProperties();
		initialize();
		disableAllButtons();
		setVisible(true);
		createObjects();
		operationProcessor.searchForPorts();
		newRouteGUIfrm = new NewRouteGUI(operationProcessor);
		optionsGUIfrm = new OptionsGUI();
		checkIfFirstLaunch();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ParseException 
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// Jakieœ logi?
			e.printStackTrace();
		}
		Image icon = Toolkit.getDefaultToolkit().getImage("./img/imim_logo.gif");
		setIconImage(icon);
		setTitle("Biorobot v." + confProperties.getProperty("version"));
		setBounds(01000, 01000, 751, 765);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);
		mainPane.setEnabled(false);

		/* MENU */

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 745, 21);
		mainPane.add(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);
		
		JMenuItem mntmOptions = new JMenuItem("Options");
		mnFile.add(mntmOptions);

		JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
		mnFile.add(mntmDisconnect);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnAdvanced = new JMenu("Advanced");
		menuBar.add(mnAdvanced);

		JMenuItem mntmResetThreads = new JMenuItem("Reset threads");
		mnAdvanced.add(mntmResetThreads);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

		/* MENU END */

		/* MAIN PANE */

		btnStart = new JButton("Start");
		btnStart.setBounds(10, 693, 89, 23);
		btnStart.setToolTipText("Start performing the specified route.");
		mainPane.add(btnStart);

		btnStop = new JButton("STOP");
		btnStop.setBounds(648, 693, 89, 23);
		btnStop.setToolTipText("Stop the robot movement and reset the actual route.");
		btnStop.setForeground(Color.RED);
		btnStop.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "stopRobot");
		mainPane.add(btnStop);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(351, 359, 89, 23);
		btnClear.setToolTipText("Clear the messages and logs text area.");
		mainPane.add(btnClear);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(109, 694, 430, 23);
		mainPane.add(progressBar);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 421, 727, 2);
		mainPane.add(separator);

		JButton btnPause = new JButton("Pause");
		btnPause.setBounds(549, 693, 89, 23);
		btnPause.setToolTipText("Pause the robot.");
		btnPause.setEnabled(false);
		mainPane.add(btnPause);

		btnLoad = new JButton("Position");
		btnLoad.setBounds(450, 359, 89, 23);
		btnLoad.setToolTipText("Move the robot towards you, so that you can easily load it.");
		mainPane.add(btnLoad);

		btnCalibrate = new JButton("Calibrate");
		btnCalibrate.setBounds(549, 359, 89, 23);
		btnCalibrate.setToolTipText("Perform a calibration.");
		mainPane.add(btnCalibrate);

		JButton btnWorkspaceCheck = new JButton("Workspace");
		btnWorkspaceCheck.setBounds(648, 359, 89, 23);
		btnWorkspaceCheck.setEnabled(false);
		btnWorkspaceCheck.setToolTipText("This will start a routine workspace checker.\r\n");
		mainPane.add(btnWorkspaceCheck);
		
		JLabel lblPosition = new JLabel("Position:");
		lblPosition.setBounds(549, 393, 41, 14);
		mainPane.add(lblPosition);

		actualPosition = new JTextField();
		actualPosition.setBounds(600, 390, 137, 20);
		actualPosition.setEditable(false);
		actualPosition.setColumns(10);
		mainPane.add(actualPosition);
		
		lblWaiting = new JLabel("Waiting: 0 seconds");
		lblWaiting.setBounds(351, 396, 140, 14);
		mainPane.add(lblWaiting);

		routePanel = new RoutePanel();
		routePanel.setBounds(20, 425, 519, 250);
		mainPane.add(routePanel);

		/* MAIN PANE END */

		/* BOTTOM TABBED PANE */

		JTabbedPane bottomTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		bottomTabbedPane.setBounds(351, 32, 386, 316);
		mainPane.add(bottomTabbedPane);

		JScrollPane scrollPane = new JScrollPane();
		bottomTabbedPane.addTab("Messages", null, scrollPane, null);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		txtarMessages = new JTextArea();
		scrollPane.setViewportView(txtarMessages);
		txtarMessages.setEditable(false);
		DefaultCaret caretMessages = (DefaultCaret)txtarMessages.getCaret();
		caretMessages.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane2 = new JScrollPane();
		bottomTabbedPane.addTab("Logs", null, scrollPane2, null);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		txtarLogs = new JTextArea();
		scrollPane2.setViewportView(txtarLogs);
		txtarLogs.setEditable(false);
		DefaultCaret caretLogs = (DefaultCaret)txtarLogs.getCaret();
		caretLogs.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		/* BOTTOM TABBED PANE END */

		/* MAIN TABBED PANE */

		JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.RIGHT);
		mainTabbedPane.setBounds(10, 32, 331, 378);
		mainPane.add(mainTabbedPane);

		/* MAIN TABBED PANE END */

		/* CONNECT PANE */

		JPanel connectPane = new JPanel();
		connectPane.setLayout(null);

		JLabel lblConnectTo = new JLabel("Connect to:");
		lblConnectTo.setBounds(10, 11, 71, 14);
		connectPane.add(lblConnectTo);

		availablePorts = new JComboBox();
		availablePorts.setBounds(91, 8, 149, 20);
		connectPane.add(availablePorts);

		btnConnect = new JButton("Connect");
		btnConnect.setBounds(151, 39, 89, 23);
		connectPane.add(btnConnect);

		lblConnectionStatus = new JLabel("Not connected!", JLabel.RIGHT);
		lblConnectionStatus.setForeground(Color.RED);
		lblConnectionStatus.setBounds(109, 141, 131, 20);
		connectPane.add(lblConnectionStatus);

		JTextPane txtpnConnectionInfo = new JTextPane();
		txtpnConnectionInfo.setEditable(false);
		txtpnConnectionInfo.setEnabled(false);
		txtpnConnectionInfo.setText("In order to connect, please choose a port and click \"Connect\" button.");
		txtpnConnectionInfo.setBounds(10, 36, 131, 48);
		connectPane.add(txtpnConnectionInfo);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(151, 107, 89, 23);
		btnDisconnect.setEnabled(false);
		connectPane.add(btnDisconnect);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(151, 73, 89, 23);
		connectPane.add(btnRefresh);

		/* CONNECT PANE END */

		/* CHOOSEN ROUTE PANE */

		JPanel choosenRoutePane = new JPanel();
		choosenRoutePane.setLayout(null);

		JLabel lblChoosenRoute = new JLabel("Choosen route:");
		lblChoosenRoute.setBounds(10, 9, 75, 14);
		choosenRoutePane.add(lblChoosenRoute);

		JLabel lblRouteOverview = new JLabel("Route overview:");
		lblRouteOverview.setBounds(10, 65, 80, 14);
		choosenRoutePane.add(lblRouteOverview);

		txtpnRouteOverview = new JTextPane();
		txtpnRouteOverview.setEnabled(false);
		txtpnRouteOverview.setEditable(false);
		txtpnRouteOverview.setText("Route overview...\n");
		txtpnRouteOverview.setBounds(10, 125, 247, 204);

		JScrollPane scrollPane3 = new JScrollPane(txtpnRouteOverview);
		scrollPane3.setBounds(10, 140, 250, 214);
		scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		choosenRoutePane.add(scrollPane3);

		txtChoosenRoutePath = new JTextField();
		txtChoosenRoutePath.setBounds(10, 34, 154, 20);
		txtChoosenRoutePath.setEditable(false);
		txtChoosenRoutePath.setText("No route specified!");
		txtChoosenRoutePath.setColumns(10);
		choosenRoutePane.add(txtChoosenRoutePath);

		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.setBounds(174, 33, 86, 23);
		choosenRoutePane.add(btnBrowse);
		
		JLabel lblTotalNumberOfHoles = new JLabel("Total number of holes: ");
		lblTotalNumberOfHoles.setBounds(20, 90, 111, 14);
		choosenRoutePane.add(lblTotalNumberOfHoles);
		
		JLabel lblTotalWaitingTime = new JLabel("Total waiting time: ");
		lblTotalWaitingTime.setBounds(20, 115, 111, 14);
		choosenRoutePane.add(lblTotalWaitingTime);
		
		lblTotalNumberOfHolesNo = new JLabel("N/A");
		lblTotalNumberOfHolesNo.setEnabled(false);
		lblTotalNumberOfHolesNo.setBounds(141, 90, 119, 14);
		choosenRoutePane.add(lblTotalNumberOfHolesNo);
		
		lblTotalWaitingTimeNo = new JLabel("N/A");
		lblTotalWaitingTimeNo.setEnabled(false);
		lblTotalWaitingTimeNo.setBounds(141, 115, 119, 14);
		choosenRoutePane.add(lblTotalWaitingTimeNo);

		/* CHOOSEN ROUTE PANE END */

		/* CONTROL MANUALLY PANE */

		controlManuallyPane = new JPanel();
		getContentPane().setLayout(null);
		controlManuallyPane.setLayout(null);
		controlManuallyPane.setEnabled(false);

		JLabel lblControlManually = new JLabel("Control manually:");
		lblControlManually.setBounds(10, 11, 87, 14);
		controlManuallyPane.add(lblControlManually);

		JLabel lblX = new JLabel("X:");
		lblX.setBounds(10, 40, 10, 14);
		controlManuallyPane.add(lblX);

		JLabel lblY = new JLabel("Y:");
		lblY.setBounds(10, 65, 10, 14);
		controlManuallyPane.add(lblY);

		JLabel lblZ = new JLabel("Z:");
		lblZ.setBounds(10, 90, 10, 14);
		controlManuallyPane.add(lblZ);

		xLeft = new JButton("<");
		xLeft.setBounds(97, 36, 45, 23);
		controlManuallyPane.add(xLeft);

		xRight = new JButton(">");
		xRight.setBounds(149, 36, 45, 23);
		controlManuallyPane.add(xRight);

		yLeft = new JButton("<");
		yLeft.setBounds(97, 61, 45, 23);
		controlManuallyPane.add(yLeft);

		yRight = new JButton(">");
		yRight.setBounds(149, 61, 45, 23);
		controlManuallyPane.add(yRight);

		zLeft = new JButton("<");
		zLeft.setBounds(97, 86, 45, 23);
		controlManuallyPane.add(zLeft);

		zRight = new JButton(">");
		zRight.setBounds(149, 86, 45, 23);
		controlManuallyPane.add(zRight);

		xLeftMore = new JButton("<<");
		xLeftMore.setBounds(30, 36, 57, 23);
		controlManuallyPane.add(xLeftMore);

		xRightMore = new JButton(">>");
		xRightMore.setBounds(204, 36, 57, 23);
		controlManuallyPane.add(xRightMore);

		yLeftMore = new JButton("<<");
		yLeftMore.setBounds(30, 61, 57, 23);
		controlManuallyPane.add(yLeftMore);

		yRightMore = new JButton(">>");
		yRightMore.setBounds(204, 61, 57, 23);
		controlManuallyPane.add(yRightMore);

		zLeftMore = new JButton("<<");
		zLeftMore.setBounds(30, 86, 57, 23);
		controlManuallyPane.add(zLeftMore);

		zRightMore = new JButton(">>");
		zRightMore.setBounds(204, 86, 57, 23);
		controlManuallyPane.add(zRightMore);

		btnGetCoordinates = new JButton("Get coordinates");
		btnGetCoordinates.setBounds(111, 341, 149, 23);
		btnGetCoordinates.setToolTipText("Get actual coordinates of the robot.");
		controlManuallyPane.add(btnGetCoordinates);

		txtGoToHoleNo = new JTextField();
		txtGoToHoleNo.setText("0");
		txtGoToHoleNo.setBounds(77, 121, 27, 20);
		txtGoToHoleNo.setColumns(10);
		controlManuallyPane.add(txtGoToHoleNo);
		
		lblGoToHole = new JLabel("Go to hole:");
		lblGoToHole.setBounds(10, 124, 57, 14);
		controlManuallyPane.add(lblGoToHole);
		
		btnGoToHole = new JButton("Go!");
		btnGoToHole.setBounds(111, 120, 57, 23);
		controlManuallyPane.add(btnGoToHole);
		
		/* CONTROL MANUALLY PANE END */

		mainTabbedPane.addTab("Connect", connectPane);
		mainTabbedPane.addTab("Route", choosenRoutePane);
		mainTabbedPane.addTab("Manual", controlManuallyPane);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 680, 725, 2);
		mainPane.add(separator_1);
		
		/* ACTIONS */

		mntmNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mntmNewActionPerformed(event);
			}
		});

		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mntmLoadActionPerformed(event);
			}
		});
		
		mntmOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mntmOptionsActionPerformed(event);
			}
		});

		mntmDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnDisconnectActionPerformed(event);
			}
		});
		
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnConnectActionPerformed(event);
			}
		});

		btnDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnDisconnectActionPerformed(event);
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnRefreshActionPerformed(event);
			}
		});

		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int close = JOptionPane.showConfirmDialog(mainGUIfrm, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (close == 0) {
					dispose();
				}
			}
		});

		mntmResetThreads.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mntmResetThreadsActionPerformed(event);
			}
		});

		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(mainGUIfrm, "Biorobot v" + confProperties.getProperty("version") + "\nWritten by: " + confProperties.getProperty("author") + "\n\nCooperator: dr. Roman Major\nInstitute of Metallurgy and Materials Science\nPolish Academy of Sciences", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnStartActionPerformed(event);
			}
		});

		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnStopActionPerformed(event);
			}
		});

		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnClearActionPerformed(event);
			}
		});

		btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnPauseActionPerformed(event);
			}
		});

		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnLoadActionPerformed(event);
			}
		});

		btnGetCoordinates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnGetCoordinatesActionPerformed(event);
			}
		});

		btnCalibrate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnCalibrateActionPerformed(event);
			}
		});

		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mntmLoadActionPerformed(event);
			}
		});

		xLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				xLeftActionPerformed(event);
			}
		});

		xRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				xRightActionPerformed(event);
			}
		});

		yLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				yLeftActionPerformed(event);
			}
		});

		yRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				yRightActionPerformed(event);
			}
		});

		zLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				zLeftActionPerformed(event);
			}
		});

		zRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				zRightActionPerformed(event);
			}
		});

		xLeftMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				xLeftMoreActionPerformed(event);
			}
		});

		xRightMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				xRightMoreActionPerformed(event);
			}
		});

		yLeftMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				yLeftMoreActionPerformed(event);
			}
		});

		yRightMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				yRightMoreActionPerformed(event);
			}
		});

		zLeftMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				zLeftMoreActionPerformed(event);
			}
		});

		zRightMore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				zRightMoreActionPerformed(event);
			}
		});

		/* ACTIONS END */
	}

	private void mntmNewActionPerformed(ActionEvent event) {
		logger.trace("[C]: mntmNew");
		newRouteGUIfrm.setVisible(true);
	}

	private void mntmLoadActionPerformed(ActionEvent event) {
		logger.trace("[C]: mntmLoad");
		if (loadedFile == null) {
			logger.debug("There wasn't any route loaded before.");
			loadedFile = actionProcessor.loadFile();
		} else {
			int confirmNewRoute = JOptionPane.showConfirmDialog(mainGUIfrm, "Another route was loaded before.\nDo you want to replace it with a new one?", "Note", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirmNewRoute == 0) {
				logger.debug("There was a route file loaded before: " + loadedFile.getAbsolutePath());
				loadedFile = null;
				loadedFile = actionProcessor.loadFile();
				logger.debug("Replaced route with a new one: " + loadedFile.getAbsolutePath());
			}
		}
	}
	
	private void mntmOptionsActionPerformed(ActionEvent event) {
		logger.trace("[C]: mntmOptions");
		optionsGUIfrm.setVisible(true);
	}

	private void mntmResetThreadsActionPerformed(ActionEvent event) {
		logger.trace("[C]: mntmReset");
		operationProcessor.resetThreads();
	}

	private void btnDisconnectActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnDisconnect");
		operationProcessor.disconnect();
	}
	
	private void btnRefreshActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnRefresh");
		operationProcessor.searchForPorts();
	}

	private void btnConnectActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnConnect");
		operationProcessor.connect();
		if (operationProcessor.isConnectedToPort() == true)
		{
			if (operationProcessor.initIOStream() == true)
			{
				operationProcessor.initListener();
			}
		}
	}

	private void btnStartActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnStart");
		operationProcessor.useLoadedFile(loadedFile);
	}

	private void btnStopActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnStop");
		operationProcessor.stopRobotMovement();
		operationProcessor.resetThreads();
	}

	private void btnClearActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnClear");
		txtarMessages.setText("");
		txtarLogs.setText("");
		operationProcessor.higlightExpectedHole(26500, 2201000, Color.red);
	}

	private void btnPauseActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnPause");
		operationProcessor.getActCoordinates();
	}

	private void btnLoadActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnLoad");
		operationProcessor.writeData(5, 0, 1, 1, 9990, 8500, 1500, 0, 0, 0);
	}

	private void btnGetCoordinatesActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnGetCoordinates");
		operationProcessor.getActCoordinates();
	}

	private void btnCalibrateActionPerformed(ActionEvent event) {
		logger.trace("[C]: btnCalibrate");
		operationProcessor.calibrate();
	}

	private void xLeftActionPerformed(ActionEvent event) {
		logger.trace("[C]: xLeft");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 0, 0, 0, SystemParameters.STEP, 0, 0, 0, 0, 0);
	}

	private void xRightActionPerformed(ActionEvent event) {
		logger.trace("[C]: xRight");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 1, 0, 0, SystemParameters.STEP, 0, 0, 0, 0, 0);
	}

	private void yLeftActionPerformed(ActionEvent event) {
		logger.trace("[C]: yLeft");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 0, 0, 0, 0, SystemParameters.STEP, 0, 0, 0, 0);
	}

	private void yRightActionPerformed(ActionEvent event) {
		logger.trace("[C]: yRight");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 0, 1, 0, 0, SystemParameters.STEP, 0, 0, 0, 0);
	}

	private void zLeftActionPerformed(ActionEvent event) {
		logger.trace("[C]: zLeft");
		operationProcessor.writeData(systemParametersData.getZvelocity(), 0, 0, 0, 0, 0, SystemParameters.STEP, 0, 0, 0);
	}

	private void zRightActionPerformed(ActionEvent event) {
		logger.trace("[C]: zRight");
		operationProcessor.writeData(systemParametersData.getZvelocity(), 0, 0, 1, 0, 0, SystemParameters.STEP, 0, 0, 0);
	}

	private void xLeftMoreActionPerformed(ActionEvent event) {
		logger.trace("[C]: xLeftMore");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 0, 0, 0, SystemParameters.STEPMORE, 0, 0, 0, 0, 0);
	}

	private void xRightMoreActionPerformed(ActionEvent event) {
		logger.trace("[C]: xRightMore");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 1, 0, 0, SystemParameters.STEPMORE, 0, 0, 0, 0, 0);
	}

	private void yLeftMoreActionPerformed(ActionEvent event) {
		logger.trace("[C]: yLeftMore");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 0, 0, 0, 0, SystemParameters.STEPMORE, 0, 0, 0, 0);
	}

	private void yRightMoreActionPerformed(ActionEvent event) {
		logger.trace("[C]: yRightMore");
		operationProcessor.writeData(systemParametersData.getXyvelocity(), 0, 1, 0, 0, SystemParameters.STEPMORE, 0, 0, 0, 0);
	}

	private void zLeftMoreActionPerformed(ActionEvent event) {
		logger.trace("[C]: zLeftMore");
		operationProcessor.writeData(systemParametersData.getZvelocity(), 0, 0, 0, 0, 0, SystemParameters.STEPMORE, 0, 0, 0);
	}

	private void zRightMoreActionPerformed(ActionEvent event) {
		logger.trace("[C]: zRightMore");
		operationProcessor.writeData(systemParametersData.getZvelocity(), 0, 0, 1, 0, 0, SystemParameters.STEPMORE, 0, 0, 0);
	}

	/* OTHER ACTIONS */

	public void enableAllButtons() {
		btnStart.setEnabled(true);
		btnStop.setEnabled(true);
		btnLoad.setEnabled(true);
		btnCalibrate.setEnabled(true);
		xLeft.setEnabled(true);
		xLeftMore.setEnabled(true);
		xRight.setEnabled(true);
		xRightMore.setEnabled(true);
		yLeft.setEnabled(true);
		yLeftMore.setEnabled(true);
		yRight.setEnabled(true);
		yRightMore.setEnabled(true);
		zLeft.setEnabled(true);
		zLeftMore.setEnabled(true);
		zRight.setEnabled(true);
		zRightMore.setEnabled(true);
		//btnGetCoordinates.setEnabled(true);
		//txtGoToHoleNo.setEnabled(true);
		//btnGoToHole.setEnabled(true);
	}

	public void disableAllButtons() {
		btnStart.setEnabled(false);
		btnStop.setEnabled(false);
		btnLoad.setEnabled(false);
		btnCalibrate.setEnabled(false);
		xLeft.setEnabled(false);
		xLeftMore.setEnabled(false);
		xRight.setEnabled(false);
		xRightMore.setEnabled(false);
		yLeft.setEnabled(false);
		yLeftMore.setEnabled(false);
		yRight.setEnabled(false);
		yRightMore.setEnabled(false);
		zLeft.setEnabled(false);
		zLeftMore.setEnabled(false);
		zRight.setEnabled(false);
		zRightMore.setEnabled(false);
		btnGetCoordinates.setEnabled(false);
		txtGoToHoleNo.setEnabled(false);
		btnGoToHole.setEnabled(false);
		
	}

	public void configureProperties() {
		confProperties = new Properties();
		try {
			FileInputStream propertiesFileIS =  new FileInputStream(SystemParameters.PROPERTYFILE);
			confProperties.load(propertiesFileIS);
			propertiesFileIS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkIfFirstLaunch() {
		try {
			if (confProperties.getProperty("first.time").equals("1")) {
				File changeLogFile = new File(SystemParameters.CHANGELOGFILE);
				String changelog = actionProcessor.takeFileAndWriteToString(changeLogFile, "[" + confProperties.getProperty("version") + "]");
				JOptionPane.showMessageDialog(mainGUIfrm, changelog + "\n\nNote that you can view this information later in the changelog.txt file in \"conf\" directory.", "What is new in version " + confProperties.getProperty("version") + "?", JOptionPane.INFORMATION_MESSAGE);
			}
			confProperties.setProperty("first.time", "0");
			confProperties.store(new FileOutputStream(new File(SystemParameters.PROPERTYFILE)), "Changed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
