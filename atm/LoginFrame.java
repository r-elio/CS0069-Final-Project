package atm;

/*
 * Imports all the packages that is used by the program
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.border.Border;

public class LoginFrame extends JFrame { // the class inherits the JFrame class
	
	/*
	 * In this part, all the variables are declared.
	 */
	
	private static final long serialVersionUID = 1L;
	private JLabel accountNoLabel;
	private JLabel passwordLabel;
	private JTextField accountNoField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private MenuBar menuBar;
	private Menu aboutMenu;
	private Menu settingMenu;
	private Menu languageMenu;
	private MenuItem aboutATM;
	private MenuItem aboutUs;
	private MenuItem english;
	private MenuItem tagalog;
	private JPanel loginPanel;
	private Border raisedBevel;
	private Border loweredBevel;
	private Font labelFont;
	private AccountsDB accountsDB;
	private Language lang;
	
	public LoginFrame() { // the constructor of the class
		loadAccountsDB(); // loads the contents of the database.
		
		setTitle(lang.login.get("title")); // setting the language of the title 
		
		setIconImage(new ImageIcon(getClass().getResource("atm_logo.png")).getImage()); // setting the Icon of the Frame
		
		/* 
		 * In this part, all the variables declared previously initialized and given a value. It includes all the different
		 * sub components of the frame such as the menuBar, the buttons, the textFields , and also their corresponding
		 * actionListener. The buttons' and textFields' dimension and border are also assigned. A key listener is also added.
		 */
		
		raisedBevel = BorderFactory.createRaisedBevelBorder();
        loweredBevel = BorderFactory.createLoweredBevelBorder();
		
		menuBar = new MenuBar();
		
		aboutMenu = new Menu(lang.login.get("aboutMenu"));
		settingMenu = new Menu(lang.login.get("settingMenu"));
		languageMenu = new Menu(lang.login.get("languageMenu"));
		
		aboutATM = new MenuItem(lang.login.get("aboutATM"));
		aboutUs = new MenuItem(lang.login.get("aboutUs"));
		english = new MenuItem(lang.login.get("english"));
		tagalog = new MenuItem(lang.login.get("tagalog"));
		
		/* 
		 * This part sets the language that will be displayed, if it is English, the language will be English.
		 * Otherwise, it will be set to Filipino or  Tagalog. 
		 * The default language is English.
		 */
		
		if (lang.getLanguage().equalsIgnoreCase("English")) {
			english.setEnabled(false);
		}
		else {
			tagalog.setEnabled(false);
		}
		
		/*
		 * adds the action listener for each buttons in the settings menu and about meunu
		 */
		
		aboutATM.addActionListener(new AboutListener());
		aboutUs.addActionListener(new AboutListener());
		
		english.addActionListener(new EnglishListener());
		tagalog.addActionListener(new TagalogListener());

		aboutMenu.add(aboutATM);
		aboutMenu.add(aboutUs);
		
		languageMenu.add(english);
		languageMenu.add(tagalog);
		
		settingMenu.add(languageMenu);
		
		menuBar.add(aboutMenu);
		menuBar.add(settingMenu);
		
		setMenuBar(menuBar);
		
		/*
		 * Initialization of the subcomponents of the frame
		 */
		
		labelFont = new Font("Times New Roman",Font.PLAIN,14);
		accountNoField = new JTextField();
		passwordField = new JPasswordField();
		accountNoField.setPreferredSize(new Dimension(150,20));
		passwordField.setPreferredSize(new Dimension(150,20));
		accountNoField.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
		passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
		
		accountNoField.addKeyListener(new FieldListener());
		passwordField.addKeyListener(new FieldListener());
		
		accountNoLabel = new JLabel(lang.login.get("accountNoLabel"));
		passwordLabel = new JLabel(lang.login.get("passwordLabel"));
		
		accountNoLabel.setFont(labelFont);
		passwordLabel.setFont(labelFont);
		
		loginButton = new JButton(lang.login.get("loginButton"));
		loginButton.setPreferredSize(new Dimension(65,25));
		loginButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		loginButton.setFocusable(false);
		
		loginButton.addActionListener(new LoginListener());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		loginPanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(5, 5, 5, 5);
		
		/* 
		 * In this part, the panel to be used is initialized along the layout it will use 
		 * Each individual objects are added manually with a given coordinate and position
		 * The userPanel.add(component,constraint) means that the Component will be added
		 * to the panel with the current constraints that was set.
		 */
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		loginPanel.add(accountNoLabel,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		loginPanel.add(accountNoField,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		loginPanel.add(passwordLabel,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		loginPanel.add(passwordField,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 2;
		loginPanel.add(loginButton,constraints);
		
		loginPanel.setBorder(BorderFactory.createCompoundBorder(loweredBevel, raisedBevel));
		add(loginPanel);
		
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			/*
			 * Displays a confirm message when the close button is initialized. If the user chose the YES option, the change made in the account 
			 * will be saved and then the frame will be disposed/removed.
			 */
			
			@Override
			public void windowClosing(WindowEvent e) {
				int response = JOptionPane.showConfirmDialog(rootPane, lang.login.get("exitQuestion"), 
				lang.login.get("exitTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					saveAccountsDB();
					dispose();
				}
			}
		});
	}
	
	/*
	 * This method saves the changes made in the account by writing the data into the accountsDB.acc file.
	 */
	
	private void saveAccountsDB() {
		try {
			FileOutputStream fos = new FileOutputStream("accountsDB.acc"); // responsible for writing raw data into a file
			ObjectOutputStream oos = new ObjectOutputStream(fos); // The ObjectOutputStream saves the objects and instances and then serialized
			oos.writeObject(accountsDB); // writes the acccountsD to a serialized file
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
			File file = new File("accountsDB.acc");
			FileInputStream fis = new FileInputStream(file); // reads the raw data of the accountsDB.acc file
			ObjectInputStream ois = new ObjectInputStream(fis); // Creates an ObjectInputStream that reads from the specified InputStream.A serialization stream header is read from the stream and verified. 
			Object database = ois.readObject(); // Read an object from the ObjectInputStream
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
	 * Creates a userFrame.
	 */
	
	private void createUserFrame(int accountNo) {
		UserFrame user = new UserFrame(accountNo);
		user.setSize(500, 300);
		user.setResizable(false);
		user.setLocationRelativeTo(null);
		user.setVisible(true);
		dispose();
	}
	
	private void createAdminFrame() {
		AdminFrame admin = new AdminFrame();
		admin.setSize(800, 550);
		admin.setResizable(false);
		admin.setLocationRelativeTo(null);
		admin.setVisible(true);
		dispose();
	}
	
	/* 
	 * The loginProcess() is the method that validates if the password inputed in the corresponding fields are correct.
	 */
	
	private void loginProcess() {
		String accField = accountNoField.getText(); 	// get the contents of the accountField and assign it into the variable named accField
		char[] passField = passwordField.getPassword(); // get the contents of the passwordField and assign it into the variable named passField
		
		/* 
		 * If the contents of the accField is empty or equals to ("") or the passField.length == 0 it will display a message
		 * that tells the user that all fields must be filled. 
		 */
		
		if (accField.equals("") || passField.length == 0) {
			JOptionPane.showMessageDialog(rootPane, lang.login.get("nullMessage"), 
			lang.login.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try {
			//The process is surrounded by a try-catch to handle all the exceptions that will happen in runtime.
			int accountNo = Integer.parseInt(accField); // parses the content of the accField into integer and assigns it into the accountNo
			
			/* 
			 * If the accountsDB or the accounts database contains a matching accountNo it will proceed to validate
			 * the password. If the password was a match, it will proceed to creating a userFrame.
			 */
			
			if (accountsDB.containsAccount(accountNo)) {
				
				/* 
				 * Sets the account number that was matched into a new Account variable named account
				 * Converts the password of the account which is a string into 
				 * a Character Array and assigns it into a char[] array named password.
				 */
				
				Account account = accountsDB.getAccount(accountNo);
				char[] password = account.getPassword().toCharArray();
				if (AccountsDB.passwordChecker(password, passField)) {
					JOptionPane.showMessageDialog(rootPane, lang.login.get("loginMessage"), 
					lang.login.get("loginTitle"), JOptionPane.INFORMATION_MESSAGE);
					
					/* 
					 * If the accounts credentials entered matches the admin's credentials, it will call the method createAdminFrame() thus,
					 * creating a new Admin Frame. When the credentials entered matches that of an account in the database, it will call the 
					 * method createUserFrame(<accountNo>) and the contents are associated with the accountNo entered. 
					 * Otherwise, if the password does not match, it will throw a custom exception named InvalidPasswordException.
					 * If the accountNo entered does not match any account number in the database, it will throw a custom exception named
					 * InvalidAccountException. 
					 */
					
					if (accountsDB.isAdmin(account)) {
						createAdminFrame();
					}
					else {
						createUserFrame(accountNo);
					}
				}
				else {
					throw new InvalidPasswordException();
				}
			}
			else {
				throw new InvalidAccountException();
			}
		}

		/*
		 * These catch statements handles the exception thrown in during runtime.
		 */
		
		catch (InvalidAccountException e) {
			JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.login.get("invalidAccountMessage"), 
			lang.login.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
			accountNoField.setText("");
		}
		catch (InvalidPasswordException e) {
			JOptionPane.showMessageDialog(rootPane, e.getMessage() + lang.login.get("invalidPasswordMessage"), 
			lang.login.get("invalidPasswordTitle"), JOptionPane.ERROR_MESSAGE);
		}
		catch (NumberFormatException e) { // The NumberFormatException is thrown when the user inputs a string or character to the accountNo field
			JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" + e.getMessage(), 
			"Invalid Input", JOptionPane.ERROR_MESSAGE);
			accountNoField.setText("");
		}
		catch (Exception e) { // Handles the unknown exceptions that may possibly be encountered.
			e.printStackTrace();
			JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
			"Error!", JOptionPane.ERROR_MESSAGE);
		}
		finally {
			passwordField.setText(""); // clears the content of the password field.
		}
	}
	
	/*
	 * These are the created ActionListener for each buttons in the LoginFrame.
	 */
	
	private class AboutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			
			/* 
			 * If the aboutATM item menu is clicked, it will display information about the program
			 * and it's features. If the aboutUs is clicked, it will display information about the creator
			 * of the program.
			 */
			
			if (event.getSource() == aboutATM) {
				JOptionPane.showMessageDialog(rootPane, lang.login.get("aboutATMMessage1") 
				+ AccountsDB.getAdminCode() + lang.login.get("aboutATMMessage2")
				+ lang.login.get("aboutATMMessage3"), lang.login.get("aboutATM"), JOptionPane.INFORMATION_MESSAGE);
			}
			else if (event.getSource() == aboutUs) {
				JOptionPane.showMessageDialog(rootPane, lang.login.get("aboutUsMessage"), 
				lang.login.get("aboutUs"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
	}
	
	/* 
	 * The next two listeners are for changing the language, when the English button is clicked, 
	 * it will display a message that tells the user the Language will be changed and asks the user
	 * to re-open the program. It will then display the user interface in English language. If the tagalog
	 * button is clicked, it will also display the same message and then asks the user to re-open the program.
	 * It will then display the user interface in Filipino Language. The database is still saved when changing the language
	 * of the system.
	 */
	
	private class EnglishListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(rootPane, lang.login.get("languageMessage"), 
			lang.login.get("languageTitle"), JOptionPane.INFORMATION_MESSAGE);
			english.setEnabled(false);
			tagalog.setEnabled(true);
			lang.setEnglish();
			saveAccountsDB();
			dispose();
		}
		
	}
	
	private class TagalogListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(rootPane, lang.login.get("languageMessage"), 
			lang.login.get("languageTitle"), JOptionPane.INFORMATION_MESSAGE);
			english.setEnabled(true);
			tagalog.setEnabled(false);
			lang.setTagalog();
			saveAccountsDB();
			dispose();
		}
		
	}
	
	private class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			loginProcess(); // calls the method loginProcess() when the Login button is clicked.
		}
	}
	
	// This class inherits from the KeyAdapater Class. If the user presses the enter key it will still call the loginProcess() method. 
	private class FieldListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent event) {
			if ((int)event.getKeyChar() == 10) {
				loginProcess();
			}
		}
	}
}
