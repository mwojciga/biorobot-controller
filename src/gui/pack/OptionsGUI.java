package gui.pack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import biorobot.data.SystemParametersData;
import biorobot.pack.SystemParameters;

public class OptionsGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	JFrame optionsGUIfrm;
	private JTextField tf_hole025y;
	private JTextField tf_gap;
	private JTextField tf_immersion;
	private JTextField tf_transportheight;
	private JTextField tf_xyspeed;
	private JTextField tf_zspeed;
	private JTextField tf_hole0x;
	private JTextField tf_hole25x;
	private JTextField tf_hole1x;
	private JTextField tf_hole1y;
	private JTextField tf_normalstep;
	private JTextField tf_largestep;
	
	SystemParametersData systemParametersData = new SystemParametersData();
	
	public OptionsGUI() {
		initialize();
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		optionsGUIfrm = new JFrame();
		setTitle("Options");
		setBounds(100, 100, 574, 563);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblSystemParameters = new JLabel("System parameters:");
		lblSystemParameters.setBounds(10, 11, 97, 14);
		getContentPane().add(lblSystemParameters);
		
		JLabel lblGap = new JLabel("Gap:");
		lblGap.setBounds(10, 49, 46, 14);
		getContentPane().add(lblGap);
		
		JLabel lblImmersion = new JLabel("Immersion:");
		lblImmersion.setBounds(10, 74, 64, 14);
		getContentPane().add(lblImmersion);
		
		JLabel lblTransportHeight = new JLabel("Transport height:");
		lblTransportHeight.setBounds(10, 99, 84, 14);
		getContentPane().add(lblTransportHeight);
		
		JLabel lblXAndY = new JLabel("X and Y speed:");
		lblXAndY.setBounds(10, 124, 84, 14);
		getContentPane().add(lblXAndY);
		
		JLabel lblZSpeed = new JLabel("Z speed:");
		lblZSpeed.setBounds(10, 149, 46, 14);
		getContentPane().add(lblZSpeed);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 36, 538, 2);
		getContentPane().add(separator);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(459, 491, 89, 23);
		btnSave.setEnabled(false);
		getContentPane().add(btnSave);
		
		JButton btnDefaults = new JButton("Defaults");
		btnDefaults.setBounds(360, 491, 89, 23);
		btnDefaults.setEnabled(false);
		getContentPane().add(btnDefaults);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(261, 491, 89, 23);
		getContentPane().add(btnCancel);
		
		JLabel lblHole0x = new JLabel("Hole 0 X coordinate:");
		lblHole0x.setBounds(10, 174, 97, 14);
		getContentPane().add(lblHole0x);
		
		JLabel lblHole25x = new JLabel("Hole 25 X coordinate:");
		lblHole25x.setBounds(10, 199, 103, 14);
		getContentPane().add(lblHole25x);
		
		JLabel lblHole025y = new JLabel("Hole 0 and 25 Y coordinate:");
		lblHole025y.setBounds(10, 224, 133, 14);
		getContentPane().add(lblHole025y);
		
		JLabel lblHole1x = new JLabel("Hole 1 X coordinate:");
		lblHole1x.setBounds(10, 249, 103, 14);
		getContentPane().add(lblHole1x);
		
		JLabel lblHole1y = new JLabel("Hole 1 Y coordinate:");
		lblHole1y.setBounds(10, 274, 103, 14);
		getContentPane().add(lblHole1y);
		
		JLabel lblNormalStep = new JLabel("Normal step:");
		lblNormalStep.setBounds(10, 299, 64, 14);
		getContentPane().add(lblNormalStep);
		
		JLabel lblLargeStep = new JLabel("Large step:");
		lblLargeStep.setBounds(10, 324, 64, 14);
		getContentPane().add(lblLargeStep);
		
		tf_hole025y = new JTextField();
		tf_hole025y.setEditable(false);
		tf_hole025y.setBounds(153, 221, 46, 20);
		getContentPane().add(tf_hole025y);
		tf_hole025y.setColumns(10);
		tf_hole025y.setText(Integer.toString(systemParametersData.getBigholeycoord()));
		
		tf_gap = new JTextField();
		tf_gap.setEditable(false);
		tf_gap.setColumns(10);
		tf_gap.setBounds(153, 46, 46, 20);
		getContentPane().add(tf_gap);
		tf_gap.setText(Integer.toString(systemParametersData.getGap()));
		
		
		tf_immersion = new JTextField();
		tf_immersion.setEditable(false);
		tf_immersion.setColumns(10);
		tf_immersion.setBounds(153, 71, 46, 20);
		getContentPane().add(tf_immersion);
		tf_immersion.setText(SystemParameters.IMMERSION);
		
		tf_transportheight = new JTextField();
		tf_transportheight.setEditable(false);
		tf_transportheight.setColumns(10);
		tf_transportheight.setBounds(153, 96, 46, 20);
		getContentPane().add(tf_transportheight);
		tf_transportheight.setText(SystemParameters.ZTRANSPORT);
		
		tf_xyspeed = new JTextField();
		tf_xyspeed.setEditable(false);
		tf_xyspeed.setColumns(10);
		tf_xyspeed.setBounds(153, 121, 46, 20);
		getContentPane().add(tf_xyspeed);
		tf_xyspeed.setText(Integer.toString(systemParametersData.getXyvelocity()));
		
		tf_zspeed = new JTextField();
		tf_zspeed.setEditable(false);
		tf_zspeed.setColumns(10);
		tf_zspeed.setBounds(153, 146, 46, 20);
		getContentPane().add(tf_zspeed);
		tf_zspeed.setText(Integer.toString(systemParametersData.getZvelocity()));
		
		tf_hole0x = new JTextField();
		tf_hole0x.setEditable(false);
		tf_hole0x.setColumns(10);
		tf_hole0x.setBounds(153, 171, 46, 20);
		getContentPane().add(tf_hole0x);
		tf_hole0x.setText(Integer.toString(systemParametersData.getBighole1xcoord()));
		
		tf_hole25x = new JTextField();
		tf_hole25x.setEditable(false);
		tf_hole25x.setColumns(10);
		tf_hole25x.setBounds(153, 196, 46, 20);
		getContentPane().add(tf_hole25x);
		tf_hole25x.setText(Integer.toString(systemParametersData.getBighole2xcoord()));
		
		tf_hole1x = new JTextField();
		tf_hole1x.setEditable(false);
		tf_hole1x.setColumns(10);
		tf_hole1x.setBounds(153, 246, 46, 20);
		getContentPane().add(tf_hole1x);
		tf_hole1x.setText(Integer.toString(systemParametersData.getXposoffirst()));
		
		tf_hole1y = new JTextField();
		tf_hole1y.setEditable(false);
		tf_hole1y.setColumns(10);
		tf_hole1y.setBounds(153, 271, 46, 20);
		getContentPane().add(tf_hole1y);
		tf_hole1y.setText(Integer.toString(systemParametersData.getYposoffirst()));
		
		tf_normalstep = new JTextField();
		tf_normalstep.setEditable(false);
		tf_normalstep.setColumns(10);
		tf_normalstep.setBounds(153, 296, 46, 20);
		getContentPane().add(tf_normalstep);
		//tf_normalstep.setText(SystemParameters.STEP);
		
		tf_largestep = new JTextField();
		tf_largestep.setEditable(false);
		tf_largestep.setColumns(10);
		tf_largestep.setBounds(153, 321, 46, 20);
		getContentPane().add(tf_largestep);
		//tf_largestep.setText(SystemParameters.STEPMORE);
		
		/* ACTIONS */

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				btnCancelActionPerformed(event);
			}
		});
		
	}
	
	private void btnCancelActionPerformed(ActionEvent event) {
		optionsGUIfrm.setVisible(false);
	}
}
