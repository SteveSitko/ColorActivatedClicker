package clicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class ClickerWindow extends JFrame {

	private static final long serialVersionUID = -5203262599032919652L;

	// Frame attributes
	private JPanel content;
	private double versionNumber = 0.92;
	private String windowTitle = "Knight Clicker v." + versionNumber;
	private int width = 700;
	private int height = 450;

	// Layout panels
	private JPanel panelTop;
	private JPanel panelBottom;

	// Timer
	private TimerArgs timerArgs;
	private Timer timer;
	private int initialDelay;
	private TimerListener timerListener;
	private boolean timerIsOn;

	// Click data
	private int successfulClicks;
	private int failedClicks;

	// Color Finder
	private ColorFinder cf;

	// Timer Options Panel
	private JPanel panelTimerOptions;

	private String timerOptionsBorderName = "Timer Options";

	private JTextField fieldLongDelayChance;
	private JTextField fieldLongDelayMin;
	private JTextField fieldLongDelayMax;
	private JTextField fieldBaseDelay;
	private JTextField fieldDelayOffsetMin;
	private JTextField fieldDelayOffsetMax;

	private Color timerOffColor;
	private Color timerOnColor;
	
	// Click statistics panel
	private JPanel panelClickStatistics;

	private String clickStatisticsBorderName = "Statistics";

	private JTextField fieldSearchingFrom;
	private JTextField fieldSearchingTo;
	private JTextField fieldSuccessfulClicks;
	private JTextField fieldFailedClicks;
	private JTextField fieldDesiredColor;
	private JTextField fieldTimerStatus;

	// Button panel
	private JPanel panelButtons;

	private boolean boundsButtonClicked = false;
	private boolean colorButtonClicked = false;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ClickerWindow window = new ClickerWindow();
	}

	public ClickerWindow() {
		successfulClicks = 0;
		failedClicks = 0;

		initialize();

		// Set window attributes
		this.setTitle(windowTitle);
		this.setSize(width, height);
		this.setLocation(400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(content);
		this.setResizable(false);
		this.setVisible(true);
	}

	// Initialize all components and data
	private void initialize() {
		initializeTimer();
		initializeColorFinder();
		initializeWindow();
	}

	private void initializeColorFinder() {
		cf = new ColorFinder();
	}

	// Initialize TimerArgs
	private void initializeTimer() {
		timerArgs = new TimerArgs();

		timerListener = new TimerListener();
		initialDelay = 1000;
		timerIsOn = false;

		timer = new Timer(initialDelay, timerListener);
	}

	// Initialize window components
	private void initializeWindow() {
		content = new JPanel(new BorderLayout());
		content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		// Misc panels
		panelTop = new JPanel(new GridLayout(1, 2));
		panelBottom = new JPanel();

		// Timer options panel
//		populateWindowOptionsPanel();
		ParameterPanel panelParameters = new ParameterPanel();

		// Click statistics panel
		populateStatisticsPanel();

		// Button panel
		populateButtonPanel();

		// Assign default textfield values
		setDefaultTextFieldValues();

		// Add panels
		panelTop.add(panelParameters);
		panelTop.add(panelClickStatistics);

		panelBottom.add(panelButtons);

		content.add(panelTop, BorderLayout.CENTER);
		content.add(panelBottom, BorderLayout.SOUTH);

	}

	// Create and add all components to the window options panel
	private void populateWindowOptionsPanel() {
		panelTimerOptions = new JPanel(new GridBagLayout());
		panelTimerOptions.setPreferredSize(new Dimension((int) (width * 0.5), height));
		panelTimerOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder() ,timerOptionsBorderName));

		fieldBaseDelay = new JTextField();
		fieldLongDelayChance = new JTextField();
		fieldLongDelayMin = new JTextField();
		fieldLongDelayMax = new JTextField();
		fieldDelayOffsetMin = new JTextField();
		fieldDelayOffsetMax = new JTextField();

		int row = 0;
		addLabelToTimerOptions("Base delay:", row);
		addFieldToTimerOptions(fieldBaseDelay, row);

		row++;
		addLabelToTimerOptions("Long delay chance:", row);
		addFieldToTimerOptions(fieldLongDelayChance, row);

		row++;
		addLabelToTimerOptions("Minimum long delay duration:", row);
		addFieldToTimerOptions(fieldLongDelayMin, row);

		row++;
		addLabelToTimerOptions("Maximum long delay duration:", row);
		addFieldToTimerOptions(fieldLongDelayMax, row);

		row++;
		addLabelToTimerOptions("Minimum random offset:", row);
		addFieldToTimerOptions(fieldDelayOffsetMin, row);

		row++;
		addLabelToTimerOptions("Maximum random offset:", row);
		addFieldToTimerOptions(fieldDelayOffsetMax, row);

		row++;
		addButtonToTimerOptions("Apply", "applyTimerArguments", row);
		addButtonToTimerOptions("Defaults", "defaults", row);
	}

	/* ************************************ *
	 * populateWindowOptionsPanel() helpers *
	 * ************************************ */

	// Add a label to the Timer Options panel
	private void addLabelToTimerOptions(String msg, int row) {
		Insets labelInsets = new Insets(4, 28, 4, 8);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.gridy = row;
		c.insets = labelInsets;

		panelTimerOptions.add(new JLabel(msg), c);
	}

	// Add a textfield to the Timer Options panel
	private void addFieldToTimerOptions(JTextField field, int row) {
		Insets fieldInsets = new Insets(4, 16, 4, 28);
		GridBagConstraints c = new GridBagConstraints();

		field.setDisabledTextColor(Color.BLACK);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridy = row;
		c.insets = fieldInsets;

		panelTimerOptions.add(field, c);
	}

	// Add a button to Timer Options Panel
	private void addButtonToTimerOptions(String buttonLabel, String actionCommand, int row) {
		Insets buttonInsets = new Insets(12, 6, 12, 6);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridy = row;
		c.gridwidth = 1;
		c.insets = buttonInsets;

		panelTimerOptions.add(createButton(buttonLabel, actionCommand), c);
	}

	/* **************************************** *
	 * END populateWindowOptionsPanel() helpers *
	 * **************************************** */

	// Create and add all the components to the click statistics panel
	private void populateStatisticsPanel() {
		panelClickStatistics = new JPanel(new GridBagLayout());
		panelClickStatistics.setPreferredSize(new Dimension((int) (width * 0.5), height));
		panelClickStatistics.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder() ,clickStatisticsBorderName));

		fieldSearchingFrom = new JTextField();
		fieldSearchingTo = new JTextField();
		fieldSuccessfulClicks = new JTextField();
		fieldFailedClicks = new JTextField();
		fieldDesiredColor = new JTextField();
		fieldTimerStatus = new JTextField();

		int row = 0;
		addLabelToStatisticsPanel("Searching from:", row);
		addFieldToStatisticsPanel(fieldSearchingFrom, row);

		row++;
		addLabelToStatisticsPanel("Searching to:", row);
		addFieldToStatisticsPanel(fieldSearchingTo, row);

		row++;
		addLabelToStatisticsPanel("Successful clicks:", row);
		addFieldToStatisticsPanel(fieldSuccessfulClicks, row);

		row++;
		addLabelToStatisticsPanel("Failed clicks", row);
		addFieldToStatisticsPanel(fieldFailedClicks, row);

		row++;
		addLabelToStatisticsPanel("Seeking color:", row);
		addFieldToStatisticsPanel(fieldDesiredColor, row);
		
		row++;
		addLabelToStatisticsPanel("Timer status:", row);
		addFieldToStatisticsPanel(fieldTimerStatus, row);

	}

	/* ********************************* *
	 * populateStatisticsPanel() helpers *
	 * ********************************* */

	// Add a label to the statistics panel
	private void addLabelToStatisticsPanel(String labelText, int row) {
		GridBagConstraints c = new GridBagConstraints();
		Insets insets = new Insets(4, 28, 4, 8);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.gridy = row;
		c.insets = insets;

		panelClickStatistics.add(new JLabel(labelText), c);
	}

	private void addFieldToStatisticsPanel(JTextField field, int row) {
		GridBagConstraints c = new GridBagConstraints();
		Insets insets = new Insets(4, 16, 4, 28);

		field.setEnabled(false);
		field.setBackground(new Color(245, 245, 245));
		field.setDisabledTextColor(Color.BLACK);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.gridy = row;
		c.insets = insets;

		panelClickStatistics.add(field, c);
	}

	/* ************************************* *
	 * END populateStatisticsPanel() helpers *
	 * ************************************* */

	// Assign default field values of each textfield
	private void setDefaultTextFieldValues() {
//		fieldBaseDelay.setText(timerArgs.getNormalDelayBase() + "");
//		fieldLongDelayChance.setText(timerArgs.getLongDelayChance() + "");
//		fieldLongDelayMin.setText(timerArgs.getLongDelayDurationMin() + "");
//		fieldLongDelayMax.setText(timerArgs.getLongDelayDurationMax() + "");
//		fieldDelayOffsetMin.setText(timerArgs.getDelayOffsetMin() + "");
//		fieldDelayOffsetMax.setText(timerArgs.getDelayOffsetMax() + "");

		fieldSearchingFrom.setText(cf.getSearchFrom().x + ", " + cf.getSearchFrom().y);
		fieldSearchingTo.setText(cf.getSearchTo().x + ", " + cf.getSearchTo().y);
		fieldSuccessfulClicks.setText(successfulClicks + "");
		fieldFailedClicks.setText(failedClicks + "");

		Color c = cf.getDesiredColor();
		fieldDesiredColor.setText(c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
		fieldDesiredColor.setBackground(c);
		
		timerOnColor = new Color(223, 252, 219);
		timerOffColor = new Color(252, 225, 219);
		fieldTimerStatus.setBackground(timerOffColor);
		if (timerIsOn) fieldTimerStatus.setBackground(timerOnColor);
	}

	// Create button panel and add buttons to it
	private void populateButtonPanel() {
		panelButtons = new JPanel();
		GridLayout grid = new GridLayout(1, 2);
		grid.setHgap(12);
		grid.setVgap(12);
		panelButtons.setLayout(grid);

//		panelButtons.add(createButton("Set Color", "setColor"));
		panelButtons.add(createButton("Set Search Area", "setCheckLocation"));
		panelButtons.add(createButton("Toggle Timer", "toggleTimer"));
	}

	// Create a button and automatically add listener
	private JButton createButton(String buttonLabel, String actionCommand) {
		JButton button = new JButton(buttonLabel);
		ButtonListener bl = new ButtonListener();

		button.setActionCommand(actionCommand);
		button.addActionListener(bl);

		return button;
	}

	// Nested class for listening to all button clicks in the application
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();

			if (action.equals("applyTimerArguments")) {
				try {
					timerArgs.setNormalDelayBase(Integer.parseInt(fieldBaseDelay.getText()));
					timerArgs.setLongDelayChance(Integer.parseInt(fieldLongDelayChance.getText()));
					timerArgs.setLongDelayDurationMin(Integer.parseInt(fieldLongDelayMin.getText()));
					timerArgs.setLongDelayDurationMax(Integer.parseInt(fieldLongDelayMax.getText()));
					timerArgs.setDelayOffsetMin(Integer.parseInt(fieldDelayOffsetMin.getText()));
					timerArgs.setDelayOffsetMax(Integer.parseInt(fieldDelayOffsetMax.getText()));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error assigning values from fields.");
					ex.printStackTrace();
				}
			} else if (action.equals("setColor")) {	// Not used with threshhold checking
				if (!colorButtonClicked) {
					JOptionPane.showMessageDialog(null, "The application will wait " + cf.getActionDelay() + " milliseconds before using the position\n"
							+ "of your mouse cursor after the delay to set the color to be searched for.");
				}

				cf.setDesiredColorAtMouse();

				Color c = cf.getDesiredColor();
				fieldDesiredColor.setText(c.getRed() + ", " + c.getGreen() + ", " + c.getBlue());
				fieldDesiredColor.setBackground(c);

				colorButtonClicked = true;
			} else if (action.equals("setCheckLocation")) {
				if (!boundsButtonClicked) {
					JOptionPane.showMessageDialog(null, "The application will wait " + cf.getActionDelay() + " milliseconds before using the position\n"
							+ "of your mouse cursor after the delay to set the search-area origin, with a radius of 3.");
				}
				
				cf.setCheckLocationAtMouse();

				fieldSearchingFrom.setText(cf.getSearchFrom().x + ", " + cf.getSearchFrom().y);
				fieldSearchingTo.setText(cf.getSearchTo().x + ", " + cf.getSearchTo().y);

				boundsButtonClicked = true;
			} else if (action.equals("toggleTimer")) {
				if (timerIsOn) {
					timerIsOn = false;
					timer.stop();
					fieldTimerStatus.setBackground(timerOffColor);
				} else {
					timerIsOn = true;
					timer.start();
					fieldTimerStatus.setBackground(timerOnColor);
				}
			} else if (action.equals("defaults")) {
				if (JOptionPane.showConfirmDialog(null, "Reset timer values?") == JOptionPane.OK_OPTION) {
					timerArgs = new TimerArgs();			// Overwrite timer args with new object, containing defaults
					
					fieldBaseDelay.setText(timerArgs.getNormalDelayBase() + "");
					fieldLongDelayChance.setText(timerArgs.getLongDelayChance() + "");
					fieldLongDelayMin.setText(timerArgs.getLongDelayDurationMin() + "");
					fieldLongDelayMax.setText(timerArgs.getLongDelayDurationMax() + "");
					fieldDelayOffsetMin.setText(timerArgs.getDelayOffsetMin() + "");
					fieldDelayOffsetMax.setText(timerArgs.getDelayOffsetMax() + "");
				}
			}
		}
	}

	// Nested class for listening for and acting upon Timer intervals
	private class TimerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int delay = timerArgs.calculateTimerDelay();

			if (cf.colorIsPresent()) {
				cf.click();
				successfulClicks++;
				fieldSuccessfulClicks.setText(successfulClicks + "");
			} else {
				failedClicks++;
				fieldFailedClicks.setText(failedClicks + "");
			}

			timer.setDelay(delay);
		}

	}
	
	// UNUSED - Part of release 1.0
	// Nested class for entering delay parameters
	private class ParameterPanel extends JPanel {

		// Parameter labels
		private JLabel lblBaseDelay;
		private JLabel lblLongDelayChance;
		private JLabel lblLongDelayMin;
		private JLabel lblLongDelayMax;
		private JLabel lblDelayOffsetMin;
		private JLabel lblDelayOffsetMax;
		
		// Parameter fields
		private JTextField txfBaseDelay;
		private JTextField txfLongDelayChance;
		private JTextField txfLongDelayMin;
		private JTextField txfLongDelayMax;
		private JTextField txfDelayOffsetMin;
		private JTextField txfDelayOffsetMax;
		
		// Buttons
		private JButton btnApplyChanges;
		private JButton btnSetDefaults;
		private JPanel pnlButtons;
		
		// Class data
		private String delayParameterBorderName = "Delay Parameters";
		
		public ParameterPanel() {
			initializeAndAddComponents();
			setDefaults();
		}
		
		// Initialize and do setup work on all components
		private void initializeAndAddComponents() {
			this.setLayout(new GridBagLayout());
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), delayParameterBorderName));
			
			int row = 0;
			
			lblBaseDelay = new JLabel("Base Delay:");
			addLabel(lblBaseDelay, row);
			txfBaseDelay = new JTextField();
			addField(txfBaseDelay, row);
			row++;
			
			lblLongDelayChance = new JLabel("Long Delay Chance:");
			addLabel(lblLongDelayChance, row);
			txfLongDelayChance = new JTextField();
			addField(txfLongDelayChance, row);
			row++;
			
			lblLongDelayMin = new JLabel("Long Delay Minimum Duration:");
			addLabel(lblLongDelayMin, row);
			txfLongDelayMin = new JTextField();
			addField(txfLongDelayMin, row);
			row++;
			
			lblLongDelayMax = new JLabel("Long Delay Maxmimum Duration");
			addLabel(lblLongDelayMax, row);
			txfLongDelayMax = new JTextField();
			addField(txfLongDelayMax, row);
			row++;
			
			lblDelayOffsetMin = new JLabel("Delay Minimum Offset:");
			addLabel(lblDelayOffsetMin, row);
			txfDelayOffsetMin = new JTextField();
			addField(txfDelayOffsetMin, row);
			row++;
			
			lblDelayOffsetMax = new JLabel("Delay Maximum Offset");
			addLabel(lblDelayOffsetMax, row);
			txfDelayOffsetMax = new JTextField();
			addField(txfDelayOffsetMax, row);
			row++;
			
			addButtonPanel(row);
		}
		
		// Add a label to the panel
		private void addLabel(JLabel label, int row) {
			Insets labelInsets = new Insets(4, 12, 4, 8);
			GridBagConstraints c = new GridBagConstraints();

			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0.0;
			c.gridy = row;
			c.insets = labelInsets;

			this.add(label, c);
		}

		// Add a field to the panel
		private void addField(JTextField field, int row) {
			Insets fieldInsets = new Insets(4, 16, 4, 12);
			GridBagConstraints c = new GridBagConstraints();

			field.setDisabledTextColor(Color.BLACK);

			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.gridy = row;
			c.insets = fieldInsets;

			this.add(field, c);
		}
		
		// Add a button to the panel
		private void addButtonPanel(int row) {
			GridLayout layout = new GridLayout(1, 2);
			layout.setHgap(16);
			pnlButtons = new JPanel(layout);
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridy = row;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1.0;
			c.gridwidth = 2;
			
			btnApplyChanges = createButton("Apply", "apply");
			pnlButtons.add(btnApplyChanges);
			btnSetDefaults = createButton("Defaults", "defaults");
			pnlButtons.add(btnSetDefaults);
			
			this.add(pnlButtons, c);
		}
		
		// Create a button
		private JButton createButton(String label, String action) {
			JButton button = new JButton(label);
			
			button.setActionCommand(action);
			button.addActionListener(new ButtonListener());
			
			return button;
		}
		
		// Set the default values in the parameter fields
		private void setDefaults() {
			TimerArgs defaultArgs = new TimerArgs();
			
			txfBaseDelay.setText(defaultArgs.getNormalDelayBase() + "");
			txfLongDelayChance.setText(defaultArgs.getLongDelayChance() + "");
			txfLongDelayMin.setText(defaultArgs.getLongDelayDurationMin() + "");
			txfLongDelayMax.setText(defaultArgs.getLongDelayDurationMax() + "");
			txfDelayOffsetMin.setText(defaultArgs.getDelayOffsetMin() + "");
			txfDelayOffsetMax.setText(defaultArgs.getDelayOffsetMax() + "");
		}

		// Check that data contained in parameter fields are valid
		public boolean checkFieldData() {
			
			return false;
		}
	}
}