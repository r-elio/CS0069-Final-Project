package atm; // the name of the package

/*
 * Imports all the different packages that is used in the program
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class UserFrame extends JFrame {
	
	/*
	 * In this part of the code, the variables to be used are all declared. 
	 */
	
	private static final long serialVersionUID = 1L;
	private JButton withdrawButton;
	private JButton depositButton;
	private JButton transferButton;
	private JButton balanceInquiryButton;
	private JButton settingButton;
	private JButton logoutButton;
	private JButton backButton;
	private JButton panelButton;
	private JPanel userPanel;
	private JPanel withdrawPanel;
	private JPanel depositPanel;
	private JPanel transferPanel;
	private JPanel balanceInquiryPanel;
	private JPanel settingPanel;
	private JPanel buttonPanel;
	private JLabel title;
	private Border raisedBevel;
	private Border loweredBevel;
	private Border textFieldBorder;
	private Border buttonBorder;
	private Font labelFont;
	private Font titleFont;
	private Dimension buttonDimension;
	private Dimension panelButtonDimension;
	private Dimension textFieldDimension;
	private GridBagConstraints constraints = new GridBagConstraints();
	private AccountsDB accountsDB;
	private Language lang;
	private int accountNo;
	
	/*
	 * This part is the Class Constructor in which all the declared variables are initialized.
	 */
	
	public UserFrame(int accountNo) {
		loadAccountsDB(); // loads the account data from the database
		
		setTitle(lang.user.get("title"));
		
		setIconImage(new ImageIcon(getClass().getResource("atm_logo.png")).getImage());
		
		this.accountNo = accountNo; // using the keyword "this", the accountNo will refer to the current object
		
		// This part initializes the variables that will be used generally so that the design will be in unison
		raisedBevel = BorderFactory.createRaisedBevelBorder();			// creating a 'raised bevel' border to be used as general
		loweredBevel = BorderFactory.createLoweredBevelBorder();		// creating a 'lowered bevel' border to be used as general
		textFieldBorder = BorderFactory.createLineBorder(Color.GRAY,2);	// same as the previous, this sets the Border variable textField Border's value
		buttonBorder = BorderFactory.createLineBorder(Color.GRAY,2);	// same as previous, however this is used for Buttons.
		
		labelFont = new Font("Consolas",Font.PLAIN,15);		// setting the font size and font style for all labels 
		titleFont = new Font("Consolas",Font.BOLD,30);		// setting the font size and font style for all titles 
		
		buttonDimension = new Dimension(180,25);		// setting the general dimension for all Buttons in the User panel
		panelButtonDimension = new Dimension(80,25);	// setting the general dimension for all buttons inside each panel
		textFieldDimension = new Dimension(90,20);		// setting the general dimension for all text fields
		
		// This part sets the Text that will be shown in each object
		title = new JLabel(lang.user.get("panelTitle"));
		withdrawButton = new JButton(lang.user.get("withdrawButton"));
		depositButton = new JButton(lang.user.get("depositButton"));
		transferButton = new JButton(lang.user.get("transferButton"));
		balanceInquiryButton = new JButton(lang.user.get("balanceInquiryButton"));
		settingButton = new JButton(lang.user.get("settingButton"));
		logoutButton = new JButton(lang.user.get("logoutButton"));
		backButton = new JButton(lang.user.get("backButton"));
		
		title.setFont(titleFont);
		
		/* 
		 * In this part, the panel to be used is initialized along the layout it will use 
		 * Each individual objects are added manually with a given coordinate and position
		 * The userPanel.add(component,constraint) means that the Component will be added
		 * to the panel with the current constraints that was set.
		 */
		
		userPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(10, 10, 10, 10);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		userPanel.add(title,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		userPanel.add(withdrawButton,constraints);
		
		constraints.gridy = 2;
		userPanel.add(transferButton,constraints);
		
		constraints.gridy = 3;
		userPanel.add(settingButton,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		userPanel.add(depositButton,constraints);
		
		constraints.gridy = 2;
		userPanel.add(balanceInquiryButton,constraints);
		
		constraints.gridy = 3;
		userPanel.add(logoutButton,constraints);
		
		userPanel.setBorder(BorderFactory.createCompoundBorder(loweredBevel, raisedBevel)); // Adds the border to the panel
		add(userPanel); // This adds the userPanel that was created to the current frame.
		
		/* 
		 * Adding each button's action listener using an inner class that implements the interface "Action Listener" and
         * adding their corresponding tool tip so when the mouse cursor hovers over the button, a certain tip will be 
         * seen by the user.
         */
		
		/* 
		 * In this part, all the buttons are added their corresponding ActionListeners through the use of Inner Class.
		 * The line of code <button>.setToolTipText(language.<button>Tooltip) sets the language of the tool tip of the
		 * button. The languages used are inside the Language class.
		 */
		
		withdrawButton.addActionListener(new WithdrawListener());
		withdrawButton.setToolTipText(lang.user.get("withdrawToolTip"));
		
		depositButton.addActionListener(new DepositListener());
		depositButton.setToolTipText(lang.user.get("depositToolTip"));
		
		transferButton.addActionListener(new TransferListener());
		transferButton.setToolTipText(lang.user.get("transferToolTip"));
		
		balanceInquiryButton.addActionListener(new BalanceInquiryListener());
		balanceInquiryButton.setToolTipText(lang.user.get("balanceInquiryToolTip"));
		
		settingButton.addActionListener(new SettingListener());
		settingButton.setToolTipText(lang.user.get("settingToolTip"));
		
		logoutButton.addActionListener(new LogoutListener());
		logoutButton.setToolTipText(lang.user.get("logoutToolTip"));
		
		/* 
		 * Creating an array of buttons to hold the buttons to set all of the button's border and preferred size
         * It also makes the button have the same design. An array was created also for code efficiency
         */
		
		JButton[] buttons = {withdrawButton,depositButton,transferButton,
							 balanceInquiryButton,settingButton,logoutButton,
							 backButton};
		
		for (int i = 0; i < buttons.length; ++i) {
			buttons[i].setFocusable(false);
			buttons[i].setBorder(buttonBorder);
			buttons[i].setPreferredSize(buttonDimension);
		}
		backButton.setPreferredSize(panelButtonDimension);
		
		pack(); // this makes the window to be sized to fit the preferred size and layouts of its subcomponents.
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // sets the operation that will happen when the user initiates a "close" on this frame.
		
		/* 
		 * Adds the window listener to receive window events from this window.
		 */
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int response = JOptionPane.showConfirmDialog(rootPane, lang.user.get("exitQuestion"), 
						lang.user.get("exitTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					createLoginFrame();
				}	
			}
		});
	}
	
	/* 
	 * The saveAccountsDB() saves the account to the accountsDB.acc in which it saves the changes made in the account that was accessed.
	 * The loadAccountsDB() loads all the account from the database that will be accessed by the users.
	 */
	
	private void saveAccountsDB() {
		try {
			FileOutputStream fos = new FileOutputStream("accountsDB.acc"); // responsible for writing raw data into a file
			ObjectOutputStream oos = new ObjectOutputStream(fos); // The ObjectOutputStream saves the objects and instances and then serialized
			oos.writeObject(accountsDB); // writes the acccountsDB to a serialized file
			oos.writeObject(lang); // writes the lang / language to a serialized file
			oos.close();
		}
		
		/*
		 * The catch statements for the errors that might happen during runtime
		 */
		
		catch (NullPointerException e) {
			JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
			"Null Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(rootPane, "IOException:\n" + e.getMessage(), 
			"IO Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
			"Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
	 * This method loads / reads the contents of the accountsDB.acc file
	 */
	
	private void loadAccountsDB() {
		try {
			File file = new File("accountsDB.acc"); // reads the raw data of the accountsDB.acc file
			FileInputStream fis = new FileInputStream(file); // Creates an ObjectInputStream that reads from the specified InputStream.A serialization stream header is read from the stream and verified.
			ObjectInputStream ois = new ObjectInputStream(fis);  // Read an object from the ObjectInputStream
			Object database = ois.readObject();
			Object savedLanguage = ois.readObject();
			accountsDB = (AccountsDB) database;
			lang = (Language) savedLanguage;
			ois.close();
		}
		
		/*
		 * The catch statements for the errors that might happen during runtime
		 */
		
		catch (NullPointerException e) {
			JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
			"Null Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(rootPane, "IOException:\n" + e.getMessage(), 
			"IO Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(),
			"Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
	 *  creates a login frame with a given size 
	 */
	
	private void createLoginFrame() {
		LoginFrame login = new LoginFrame();
		login.setSize(400, 300);
        login.setResizable(false);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        dispose();
	}
	
	/* 
	 * This initializes the dimension and border of the panelButtons that will be used for each created panel
	 * the setFocusable(false) makes the button not automatically highlighted / focused
	 */
	
	private void initializePanelButton() {
		panelButton.setFocusable(false);
		panelButton.setBorder(buttonBorder);
		panelButton.setPreferredSize(panelButtonDimension);
	}
	
	/*	
	 * This method is used when changing the panel. It adds the panel that will be used and the 
	 * userPanel.setVisible(false) hides the userPanel. 
	 */
	
	private void changePanel(JPanel panel) {
		panel.setBorder(BorderFactory.createCompoundBorder(loweredBevel, raisedBevel));
		add(panel);
		userPanel.setVisible(false);
	}
	
	/*
	 * This is used to go back to the main panel on the userFrame through the use of the setVisible(true)
	 * the remove(panel) removes the panel
	 */
	
	private void back(JPanel panel) {
		userPanel.setVisible(true);
		remove(panel);
	}
	
	/* 
	 * This method is called when the withdrawButton was performed an action or when clicked. This creates the subcomponents of 
	 * the withdrawPanel. The subcomponents' language, their position, their font styles and sizes,
	 * their border, and their dimension are initialized. The subcomponents' position in the panel are also set in this part.
	 */
	
	private void createWithdrawPanel() {
		title = new JLabel(lang.user.get("withdrawTitle")); 						// The title of the Panel. The language used is dependent on what the user has chosen.
		JLabel label = new JLabel(lang.user.get("amount"),SwingConstants.CENTER);	// The label for the amount field. It is set to the center of its container.
		JTextField amountField = new JTextField();									// Initializes the amountField.
		panelButton = new JButton(lang.user.get("withdraw"));						// Sets the language of the button to the Current Panel
		
		title.setFont(titleFont); // Sets the title's font
		label.setFont(labelFont); // Set the label's font
		
		amountField.setBorder(textFieldBorder); 			// Sets the border of the amountField
		amountField.setPreferredSize(textFieldDimension);	// Sets the dimension of the amountField
		
		initializePanelButton(); // initializes the panel buttons in this panel by calling this method.
		
		/*	
		 * This part creates a panel that will contain the two buttons.
		 */
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(panelButton);
		buttonPanel.add(backButton);
		
		/*	
		 * This part creates the panel that will contain the prompt for the user. 
		 */
		
		JPanel promptPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		promptPanel.add(label);
		promptPanel.add(amountField);
		
		/*
		 * This part creates the main panel with the GridBagLayout that will contain the previous panels that was initialized. 
		 * Its subcomponents' position and constraints are set here.
		 */
		
		withdrawPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(10, 10, 10, 10);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		withdrawPanel.add(title,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		withdrawPanel.add(promptPanel,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridheight = 1;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		withdrawPanel.add(buttonPanel,constraints);
		
		/* 
		 * The actionListener for the buttons are added in this part. The backButton used an inner class for its actionListener.
		 * The panelButton for the withdrawPanel uses an anonymous class to implement its action listener.
		 */
		
		backButton.addActionListener(new BackListener(withdrawPanel));
		panelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				
				// The process is surrounded by a try-catch to handle all the exceptions that will happen in runtime.
				
				try {
					// If the amount field is empty, a warning message will be shown that will ask the user to fill all the fields
					if (amountField.getText().equals("")) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("nullMessage"),
						lang.user.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// The contents of the amountField are parsed into Double and then assigned to a new variable called amount
					double amount = Double.parseDouble(amountField.getText());
					
					/* 
					 * If the amount is 0 or is negative, 
					 * a warning message will be shown that will tell the user that the user cannot withdraw an amount of 0 or a negative amount.
					 */
					
					if (amount <= 0) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("invalidAmountMessage"), 
						lang.user.get("invalidAmountTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					if (amount % 100 != 0) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("restrictAmountMessage"), 
						lang.user.get("invalidAmountTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					/* 
					 * This shows a confirmDialog for the user when all conditions for withdrawing are met.
					 * It will confirm the user to finalize the decision and if the response was the YES_OPTION,
					 * the amount that was input will be withdrawn from the user's balance using the method
					 * withdraw(accountNo,<amount>). It will then pop-up a message if the transaction was a success.
					 * The back(<panel>) removes the current panel and turns back to the main panel.
					 */
					
					int response = JOptionPane.showConfirmDialog(rootPane, lang.user.get("withdrawQuestion"), 
					lang.user.get("withdraw"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if (response == JOptionPane.YES_OPTION) {
						accountsDB.withdraw(accountNo, amount);
						JOptionPane.showMessageDialog(rootPane, lang.user.get("withdrawMessage"), 
						lang.user.get("withdraw"), JOptionPane.INFORMATION_MESSAGE);
						back(withdrawPanel);
					}
				}
				
				/* 
				 * These are the catch statements for all the possible exceptions that will be thrown during runtime.
				 * When an exception is caught, it will display an ERROR_MESSAGE to the user. 
				 */
				
				catch (InvalidAccountException e) {
					JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidAccountMessage"), 
					lang.user.get("invalidAccountMessage"), JOptionPane.ERROR_MESSAGE);
				}
				catch(InvalidBalanceException e){
				    JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidBalanceMessage"), 
				    lang.user.get("invalidBalanceMessage"), JOptionPane.ERROR_MESSAGE);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
					+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
					"Error!", JOptionPane.ERROR_MESSAGE);
				}
				// The finally statement are always executed even when an exception is not thrown. This empties the contents of the amountField.
				finally {
					amountField.setText("");
				}
			}
		});
		
		changePanel(withdrawPanel); // This changes the panel back into the main panel.
	}
	
	/* 
	 * This method is called when the depositButton was performed an action or when clicked. This creates the subcomponents of 
	 * the depositPanel. The subcomponents' language, their position, their font styles and sizes,
	 * their border, and their dimension are initialized. The subcomponents' position in the panel are also set in this part.
	 */
	
	private void createDepositPanel() {
		title = new JLabel(lang.user.get("depositTitle"));
		JLabel label = new JLabel(lang.user.get("amount"),SwingConstants.CENTER);
		JTextField amountField = new JTextField();
		panelButton = new JButton(lang.user.get("deposit"));
		
		title.setFont(titleFont);
		label.setFont(labelFont);
		
		amountField.setBorder(textFieldBorder);
		amountField.setPreferredSize(textFieldDimension);
		
		initializePanelButton();
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(panelButton);
		buttonPanel.add(backButton);
		
		JPanel promptPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		promptPanel.add(label);
		promptPanel.add(amountField);
		
		depositPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(10, 10, 10, 10);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		depositPanel.add(title,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		depositPanel.add(promptPanel,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridheight = 1;
		constraints.gridwidth = 4;
		constraints.anchor = GridBagConstraints.CENTER;
		depositPanel.add(buttonPanel,constraints);
		
		/* 
		 * The actionListener for the buttons are added in this part. The backButton used an inner class for its actionListener.
		 * The panelButton for the withdrawPanel uses an anonymous class to implement its action listener.
		 */
		
		backButton.addActionListener(new BackListener(depositPanel));
		panelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// The process is surrounded by a try-catch to handle all the exceptions that will happen in runtime.
					
					/* 
					 * If the amountField is equal to "" or in other terms, is empty, it will display a warning message that will ask 
					 * the user to fill all the required fields.
					 */
					
					if (amountField.getText().equals("")) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("nullMessage"),
						lang.user.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// The contents of the amountField are parsed into Double and then assigned to a new variable called amount.
					double amount = Double.parseDouble(amountField.getText());
					
					/* 
					 * If the amount is 0 or is negative, 
					 * a warning message will be shown that will tell the user that the user cannot withdraw an amount of 0 
					 * or a negative amount.
					 */
					
					if (amount <= 0) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("invalidAmountMessage"), 
						lang.user.get("invalidAmountTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					if (amount % 100 != 0) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("restrictAmountMessage"), 
						lang.user.get("invalidAmountTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					/* 
					 * This shows a confirmDialog for the user when all conditions for depositing are met.
					 * It will confirm the user to finalize the decision and if the response was the YES_OPTION,
					 * the amount that was input will be deposited to the user's account using the method
					 * deposit(accountNo,<amount>). It will then pop-up a message if the transaction was a success.
					 * The back(<panel>) removes the current panel and turns back to the main panel.
					 */
					
					int response = JOptionPane.showConfirmDialog(rootPane, lang.user.get("depositQuestion"), 
					lang.user.get("deposit"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if (response == JOptionPane.YES_OPTION) {
						accountsDB.deposit(accountNo, amount);
						JOptionPane.showMessageDialog(rootPane, lang.user.get("depositMessage"), 
						lang.user.get("deposit"), JOptionPane.INFORMATION_MESSAGE);
						back(depositPanel);
					}
				}
				
				/* 
				 * These are the catch statements for all the possible exceptions that will be thrown during runtime.
				 * When an exception is caught, it will display an ERROR_MESSAGE to the user. 
				 */
				
				catch (InvalidAccountException e) { // The InvalidAccountException will be thrown when the AccountNo. does not exist.
					JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidAccountMessage"), 
					lang.user.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
				}
				catch(InvalidBalanceException e){ // The InvalidBalanceException will be thrown when the balance of the account is less than 0
				    JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidBalanceMessage"), 
				    lang.user.get("invalidBalanceTitle"), JOptionPane.ERROR_MESSAGE);
				}
				catch (NumberFormatException e) { // This will throw when a string or a character is inputed. 
					JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
					+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e) { // This will catch whenever an unknown exception is thrown.
					e.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
					"Error!", JOptionPane.ERROR_MESSAGE);
				}
				finally {
					amountField.setText(""); // Clears the contents of the field
				}
			}
		});
		
		changePanel(depositPanel); // Switches back to the main panel.
	}
	
	/* 
	 * This method is called when the transferButton was performed an action or when clicked. This creates the subcomponents of 
	 * the transferPanel. The subcomponents' language, their position, their font styles and sizes,
	 * their border, and their dimension are initialized. The subcomponents' position in the panel are also set in this part.
	 */
	
	private void createTransferPanel() {
		title = new JLabel(lang.user.get("transferTitle"));
		JLabel transferLabel = new JLabel(lang.user.get("transferLabel"));
		JLabel amountLabel = new JLabel(lang.user.get("amountLabel"));
		JTextField transferField = new JTextField();
		JTextField amountField = new JTextField();
		panelButton = new JButton(lang.user.get("transfer"));
		
		title.setFont(titleFont);
		transferLabel.setFont(labelFont);
		amountLabel.setFont(labelFont);
		
		transferField.setBorder(textFieldBorder);
		amountField.setBorder(textFieldBorder);
		transferField.setPreferredSize(textFieldDimension);
		amountField.setPreferredSize(textFieldDimension);
		
		initializePanelButton();
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(panelButton);
		buttonPanel.add(backButton);
		
		JPanel promptPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
        constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		promptPanel.add(transferLabel,constraints);
		
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		promptPanel.add(amountLabel,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		promptPanel.add(transferField,constraints);
		
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		promptPanel.add(amountField,constraints);
		
		transferPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(10, 10, 10, 10);
		
		constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.gridwidth = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        transferPanel.add(title,constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        transferPanel.add(promptPanel,constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        transferPanel.add(buttonPanel,constraints);
        
        /* 
         * The actionListener for the buttons are added in this part. The backButton used an inner class for its actionListener.
		 * The panelButton for the withdrawPanel uses an anonymous class to implement its action listener.
		 */
        
        backButton.addActionListener(new BackListener(transferPanel));
		panelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// The process is surrounded by a try-catch to handle all the exceptions that will happen in runtime.
					
					/* 
					 * If the amountField or transferField is equal to "" or in other terms, empty,  it will display a warning message that will ask 
					 * the user to fill all the required fields.
					 */
					
					if (transferField.getText().equals("") || amountField.getText().equals("")) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("nullMessage"),
						lang.user.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					/* 
					 * The contents of the amountField are parsed into Double and then assigned to a new variable called amount.
					 * The contents of the transferField are parsed into Integer and then assigned to a new variable called tranferNo
					 */
					
					int transferNo = Integer.parseInt(transferField.getText());
					double amount = Double.parseDouble(amountField.getText());
					
					/* 
					 * If the amount is 0 or is negative, 
					 * a warning message will be shown that will tell the user that the user cannot withdraw an amount of 0 
					 * or a negative amount.
					 */
					
					if (amount <= 0) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("invalidAmountMessage"), 
						lang.user.get("invalidAmountTitle"), JOptionPane.WARNING_MESSAGE);
						amountField.setText("");
						return;
					}
					
					/* 
					 * If the accountNo of the current user is equals to the transferNo, it will display an ERROR_MESSAGE
					 * that tells tells the user The transferee account or the target account cannot be the same account
					 */
					
					if (accountNo == transferNo) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("invalidTransfereeMessage"), 
						lang.user.get("invalidTransfereeTitle"), JOptionPane.ERROR_MESSAGE);
						transferField.setText("");
						return;
					}
					
					/* 
					 * This shows a confirmDialog for the user when all conditions for transferring are met.
					 * It will confirm the user to finalize the decision and if the response was the YES_OPTION,
					 * the amount that was input will be transfer to the transferee's account using the method
					 * transfer(accountNo,<transferNo>,<amount>) The accountNo is the source and the transferNo is the destination. 
					 * It will then pop-up a message if the transaction was a success.
					 * The back(<panel>) removes the current panel and turns back to the main panel.
					 */
					
					int response = JOptionPane.showConfirmDialog(rootPane, lang.user.get("transferQuestion"), 
					lang.user.get("transfer"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if (response == JOptionPane.YES_OPTION) {
						accountsDB.transfer(accountNo, transferNo, amount);
						JOptionPane.showMessageDialog(rootPane, lang.user.get("transferMessage"), 
						lang.user.get("transfer"), JOptionPane.INFORMATION_MESSAGE);
						back(transferPanel);
					}
				}
				
				/* 
				 * These are the catch statements for all the possible exceptions that will be thrown during runtime.
				 * When an exception is caught, it will display an ERROR_MESSAGE to the user. 
				 */
				
				catch (InvalidAccountException e) { // The InvalidAccountException will be thrown when either the AccountNo. or the TransfereeNo. does not exist.
					JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidAccountMessage"), 
					lang.user.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
					transferField.setText(""); // Clears the contents of the transferfield
				}
				catch(InvalidBalanceException e){ // The InvalidBalanceException will be thrown when the balance of the account is less than 0
				    JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidBalanceMessage"), 
				    lang.user.get("invalidBalanceTitle"), JOptionPane.ERROR_MESSAGE);
				    amountField.setText(""); // Clears the contents of the amountfield
				}
				catch (NumberFormatException e) { // This will throw when a string or a character is inputed. 
					JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
					+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception e) { // This will catch whenever an unknown exception is thrown.
					e.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
					"Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		changePanel(transferPanel); // switches back to the main panel
	}
	
	/* 
	 * This method is called when the balanceInquiry was performed an action or when clicked. This creates the subcomponents of 
	 * the BalanceInquiryPanel. The subcomponents' language, their position, their font styles and sizes,
	 * their border, and their dimension are initialized. The subcomponents' position in the panel are also set in this part.
	 */
	
	private void createBalanceInquiryPanel() {
		try {
			title = new JLabel(lang.user.get("balanceInquiryTitle"));
			JLabel accNoLabel = new JLabel(lang.user.get("accNoLabel"));
			JLabel accNameLabel = new JLabel(lang.user.get("accNameLabel"));
			JLabel balanceLabel = new JLabel(lang.user.get("balanceLabel"));
			
			/*
			 *  The contents of each label will be taken from the account using the methods such as
			 *  getAccountNo(), getAccountName(), and getBalance()
			 */
			
			JLabel accNo = new JLabel(String.valueOf(accountsDB.getAccount(accountNo).getAccountNo()));
			JLabel accName = new JLabel(String.valueOf(accountsDB.getAccount(accountNo).getAccountName()));
			JLabel balance = new JLabel(String.format("%.2f", accountsDB.getAccount(accountNo).getBalance()));
			
			title.setFont(titleFont);
			accNoLabel.setFont(labelFont);
			accNameLabel.setFont(labelFont);
			balanceLabel.setFont(labelFont);
			accNo.setFont(labelFont);
			accName.setFont(labelFont);
			balance.setFont(labelFont);
			
			JPanel infoPanel = new JPanel(new GridBagLayout());
			constraints.insets = new Insets(5, 5, 5, 5);
			
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridheight = 1;
	        constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.EAST;
			infoPanel.add(accNoLabel,constraints);
			
			constraints.gridy = 1;
			constraints.anchor = GridBagConstraints.EAST;
			infoPanel.add(accNameLabel,constraints);
			
			constraints.gridy = 2;
			constraints.anchor = GridBagConstraints.EAST;
			infoPanel.add(balanceLabel,constraints);
			
			constraints.gridx = 1;
			constraints.gridy = 0;
			constraints.anchor = GridBagConstraints.WEST;
			infoPanel.add(accNo,constraints);
			
			constraints.gridy = 1;
			constraints.anchor = GridBagConstraints.WEST;
			infoPanel.add(accName,constraints);
			
			constraints.gridy = 2;
			constraints.anchor = GridBagConstraints.WEST;
			infoPanel.add(balance,constraints);
			
			balanceInquiryPanel = new JPanel(new GridBagLayout());
			constraints.insets = new Insets(10, 10, 10, 10);
			
			constraints.gridx = 0;
			constraints.gridy = 0;
			constraints.gridheight = 2;
			constraints.gridwidth = 4;
			constraints.anchor = GridBagConstraints.CENTER;
			balanceInquiryPanel.add(title,constraints);
			
			constraints.gridx = 0;
	        constraints.gridy = 2;
	        constraints.gridheight = 1;
	        constraints.anchor = GridBagConstraints.CENTER;
	        balanceInquiryPanel.add(infoPanel,constraints);
	        
	        constraints.gridx = 0;
	        constraints.gridy = 3;
	        constraints.anchor = GridBagConstraints.CENTER;
	        balanceInquiryPanel.add(backButton,constraints);
			
	        // Only the backButton was added an actionListener because it is the only button in the panel.
			backButton.addActionListener(new BackListener(balanceInquiryPanel));
			
			changePanel(balanceInquiryPanel);
		}
		
		/* 
		 * These are the catch statements for all the possible exceptions that will be thrown during runtime.
		 */
		
		catch (InvalidAccountException e) { // When the account does not exist
			JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidAccountMessage"), 
			lang.user.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
		}
		catch (NullPointerException e) { // When it points to a null value
			JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
			"Null Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) { // Catches unknown exceptions.
			e.printStackTrace();
			JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
			"Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/* 
	 * This method is called when the settingButton was performed an action or when clicked. This creates the subcomponents of 
	 * the settingPanel. The subcomponents' language, their position, their font styles and sizes,
	 * their border, and their dimension are initialized. The subcomponents' position in the panel are also set in this part.
	 */
	
	private void createSettingPanel() {
		title = new JLabel(lang.user.get("settingTitle"));
		JLabel oldLabel = new JLabel(lang.user.get("oldLabel"));
		JLabel newLabel = new JLabel(lang.user.get("newLabel"));
		JLabel confirmLabel = new JLabel(lang.user.get("confirmLabel"));
		JPasswordField oldPIN = new JPasswordField();
		JPasswordField newPIN = new JPasswordField();
		JPasswordField confirmPIN = new JPasswordField();
		panelButton = new JButton(lang.user.get("change"));
		
		title.setFont(titleFont);
		oldLabel.setFont(labelFont);
		newLabel.setFont(labelFont);
		confirmLabel.setFont(labelFont);
		
		oldPIN.setBorder(textFieldBorder);
		newPIN.setBorder(textFieldBorder);
		confirmPIN.setBorder(textFieldBorder);
		oldPIN.setPreferredSize(textFieldDimension);
		newPIN.setPreferredSize(textFieldDimension);
		confirmPIN.setPreferredSize(textFieldDimension);
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(panelButton);
		buttonPanel.add(backButton);
		
		initializePanelButton();
		
		JPanel promptPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
        constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		promptPanel.add(oldLabel,constraints);
		
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		promptPanel.add(newLabel,constraints);
		
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		promptPanel.add(confirmLabel,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		promptPanel.add(oldPIN,constraints);
		
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		promptPanel.add(newPIN,constraints);
		
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		promptPanel.add(confirmPIN,constraints);
		
		settingPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(10, 10, 10, 10);
		
		constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.gridwidth = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        settingPanel.add(title,constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        settingPanel.add(promptPanel,constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        settingPanel.add(buttonPanel,constraints);
        
        /* 
         * The actionListener for the buttons are added in this part. The backButton used an inner class for its actionListener.
		 * The panelButton for the withdrawPanel uses an anonymous class to implement its action listener.
		 */
        
        backButton.addActionListener(new BackListener(settingPanel));
		panelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// The process is surrounded by a try-catch to handle all the exceptions that will happen in runtime.
					
					/* 
					 * If any of the fields have length of 0, a warning message will be displayed to the user asking
					 * the user to fill all required fields 					
					 */
					
					if (oldPIN.getPassword().length == 0 || 
						newPIN.getPassword().length == 0 || 
						confirmPIN.getPassword().length == 0) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("nullMessage"),
						lang.user.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					/* 
					 * This puts the contents of each field to an array of characters that will be later used for validation.
					 */
					
					char[] oldPass = oldPIN.getPassword();
					char[] newPass = newPIN.getPassword();
					char[] confirmPass = confirmPIN.getPassword();
					char[] currentPass = accountsDB.getAccount(accountNo).getPassword().toCharArray();
					
					// Throws an invalid password exception when the contents of the oldPIN is not equal to the current password of the Account
					if (!(AccountsDB.passwordChecker(oldPass, currentPass))) {
						throw new InvalidPasswordException();
					}
					
					// Displays a message when the new password is not equal to the confirm password. It will tell the user a PIN mismatch
					if (!(AccountsDB.passwordChecker(newPass, confirmPass))) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("invalidPINMessage"),
						lang.user.get("invalidPINTitle"), JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Displays a message telling that the current Password is the same as the new Password.
					if (AccountsDB.passwordChecker(currentPass, newPass)) {
						JOptionPane.showMessageDialog(rootPane, lang.user.get("samePINMessage"),
						lang.user.get("samePINTitle"), JOptionPane.WARNING_MESSAGE);
						newPIN.setText("");
						confirmPIN.setText("");
						return;
					}
					
					/* 
					 * This shows a confirmDialog for the user when all conditions for changing the password are met.
					 * It will confirm the user to finalize the decision and if the response was the YES_OPTION,
					 * the account's password will be changed into the new password using the method changePassword(accountNo,newPass)
					 * It will then pop-up a message if the change was a success.
					 * The back(<panel>) removes the current panel and turns back to the main panel.
					 */
					
					int response = JOptionPane.showConfirmDialog(rootPane, lang.user.get("settingQuestion"), 
					lang.user.get("setting"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					if (response == JOptionPane.YES_OPTION) {
						accountsDB.changePassword(accountNo, newPass);
						JOptionPane.showMessageDialog(rootPane, lang.user.get("settingMessage"), 
						lang.user.get("setting"), JOptionPane.INFORMATION_MESSAGE);
						back(settingPanel);
					}
				}
				catch (InvalidPasswordException e) { // It is caught when an incorrect pin was entered.
					JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.user.get("invalidPasswordMessage"), 
					lang.user.get("invalidPasswordTitle"), JOptionPane.ERROR_MESSAGE);
					oldPIN.setText("");
				}
				catch (Exception e) { // This catches unknown exceptions in runtime.
					e.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
					"Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		changePanel(settingPanel); // Switches back to the main panel.
	}
	
	/* 
	 * In this part, the inner classes that are defined implements the ActionListener for the
	 * buttons they correspond to. 
	 */
	
	private class WithdrawListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			createWithdrawPanel(); // calls the method createWithdrawPanel when the withdrawButton is clicked.
		}
	}
	
	private class DepositListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			createDepositPanel(); // calls the method createDepositPanel when the depositButton is clicked.
		}
	}
	
	private class TransferListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			createTransferPanel(); // calls the method createTransferPanel when the transferButton is clicked.
		}
	}
	
	private class BalanceInquiryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			createBalanceInquiryPanel(); // calls the method createBalanceInquiryPanel when the balanceInquiryButton is clicked.
		}
	}
	
	private class SettingListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			createSettingPanel(); // calls the method createSettingsPanel when the settingButton is clicked.
		}
	}
	
	private class LogoutListener implements ActionListener {
		@Override
		// Shows a confirm dialog for the user when the logout button is clicked.
		public void actionPerformed(ActionEvent event) {
			int response = JOptionPane.showConfirmDialog(rootPane, lang.user.get("logoutQuestion"), 
					lang.user.get("logoutButton"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION) { // Takes place when the user clicks YES_OPTION.
				saveAccountsDB(); // saves the changes in the account to the database.
				createLoginFrame(); //creates a new login frame for the user.
			}
		}
	}
	
	private class BackListener implements ActionListener {
		private JPanel panel;
		
		public BackListener(JPanel panel) { // refers to the current panel being used.
			this.panel = panel;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			back(panel); // calls the method back when the backButton is clicked.
		}
	}
}