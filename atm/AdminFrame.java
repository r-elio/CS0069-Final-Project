package atm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.PatternSyntaxException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class AdminFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Object[][] data;
	private JPanel tablePanel;
	private JTable table;
	private DefaultTableModel model;
	private DefaultTableCellRenderer renderer;
	private TableRowSorter<DefaultTableModel> sorter;
	private JLabel searchLabel;
	private JTextField searchField;
	private JComboBox<String> searchType;
	private JButton addButton;
	private JButton removeButton;
	private JButton updateButton;
	private JButton finishButton;
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem importAccount;
	private MenuItem exportAccount;
	private MenuItem appendAccount;
	private Border raisedBevel;
	private Border loweredBevel;
	private AccountsDB accountsDB;
	private Language lang;
	
	public AdminFrame() {
		
		/*
		 * This part is the Class Constructor in which all the declared variables are initialized.
		 */
		
		loadAccountsDB(); //loads the account
		
		setTitle(lang.admin.get("title"));
		
		setIconImage(new ImageIcon(getClass().getResource("atm_logo.png")).getImage());
		
		raisedBevel = BorderFactory.createRaisedBevelBorder();
        loweredBevel = BorderFactory.createLoweredBevelBorder();
		
		data = accountsDB.getAccountsTable();
		
		menuBar = new MenuBar();
		
		fileMenu = new Menu(lang.admin.get("fileMenu"));
		
		importAccount = new MenuItem(lang.admin.get("importAccount"));
		exportAccount = new MenuItem(lang.admin.get("exportAccount"));
		appendAccount = new MenuItem(lang.admin.get("appendAccount"));
		
		importAccount.addActionListener(new ImportListener());
		exportAccount.addActionListener(new ExportListener());
		appendAccount.addActionListener(new AppendListener());
		
		fileMenu.add(importAccount);
		fileMenu.add(exportAccount);
		fileMenu.add(appendAccount);
		
		menuBar.add(fileMenu);
		
		setMenuBar(menuBar);
		
		addButton = new JButton(lang.admin.get("addButton"));
		removeButton = new JButton(lang.admin.get("removeButton"));
		updateButton = new JButton(lang.admin.get("updateButton"));
		finishButton = new JButton(lang.admin.get("finishButton"));
		
		addButton.addActionListener(new AddListener());
		removeButton.addActionListener(new RemoveListener());
		updateButton.addActionListener(new UpdateListener());
		finishButton.addActionListener(new FinishListener());
		
		table = new JTable();
		model = new DefaultTableModel(data,lang.columnNames) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false; // makes the cell of the tables not editable
			}
			
			@Override
			public Class<?> getColumnClass(int col){
				try {
					return getValueAt(0,col).getClass();
				}
				catch (IndexOutOfBoundsException e) {
					// Purposely comment out the statement due to expected error if the user search
					// without any contents on the table.
					/*
					JOptionPane.showMessageDialog(rootPane, "IndexOutOfBoundsException:\n" 
					+ e.getMessage(), "Invalid Action", JOptionPane.ERROR_MESSAGE);
					*/
				}
				catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
					"Error!", JOptionPane.ERROR_MESSAGE);
				}
				return null;
			}
		};
		
		/*
		 * Different attributes for the table are set in this part.
		 */
		
		sorter = new TableRowSorter<DefaultTableModel>(model);
		model.setColumnIdentifiers(lang.columnNames);
		table.setModel(model);
		table.setPreferredScrollableViewportSize(new Dimension(700,300));
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setRowSorter(sorter);
		
		renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		table.getColumnModel().getColumn(1).setCellRenderer(renderer);
		table.getColumnModel().getColumn(2).setCellRenderer(renderer);
		
		searchLabel = new JLabel(lang.admin.get("searchLabel"));
		
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(300,20));
		searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY,2));
		
		// adds a keyListener to the search Field
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent event) {
				if ((int)event.getKeyChar() == 10) {
					search();
				}
			}
		});
		
		// adds an actionListener to the searchField.
		searchField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}
		});
		
		/*
		 * The different sub components of the AdminFrame are set and given their corresponding coordinates.
		 */
		
		searchType = new JComboBox<String>(lang.columnNames);
		searchType.setSelectedIndex(0);
		
		searchType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				search();
			}
		});
		
		/* 
		 * In this part, the panel to be used is initialized along the layout it will use 
		 * Each individual objects are added manually with a given coordinate and position
		 * The userPanel.add(component,constraint) means that the Component will be added
		 * to the panel with the current constraints that was set.
		 */
		
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(searchType);
		
		GridBagConstraints constraints = new GridBagConstraints();
		tablePanel = new JPanel(new GridBagLayout());
		constraints.insets = new Insets(10,10,10,10);
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 4;
		tablePanel.add(searchPanel,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 4;
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane,constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 1;
		tablePanel.add(addButton,constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 1;
		tablePanel.add(removeButton,constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 1;
		tablePanel.add(updateButton,constraints);
		
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = 1;
		tablePanel.add(finishButton,constraints);
		
		tablePanel.setBorder(BorderFactory.createCompoundBorder(loweredBevel, raisedBevel));
		add(tablePanel);
		
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int response = JOptionPane.showConfirmDialog(rootPane, lang.admin.get("exitQuestion"), 
				lang.admin.get("exitTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					createLoginFrame();
				}	
			}
		});
	}
	
	/*
	 * The search method allows the user to search for a certain data 
	 */
	
	private void search() {
		try {
			RowFilter<DefaultTableModel, Object> rowFilter = null;
			rowFilter = RowFilter.regexFilter("(?i)" + searchField.getText(),searchType.getSelectedIndex());
			sorter.setRowFilter(rowFilter); // Sets the filter that determines which rows, if any, should behidden from the view.
		}
		catch (NullPointerException e) {
			// Purposely comment out the statement due to expected error if the user search
			// without any contents on the table.
			/*
			JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
			"Null Error", JOptionPane.ERROR_MESSAGE);
			*/
		}
		catch (PatternSyntaxException e) {
			JOptionPane.showMessageDialog(rootPane, "PatternSyntaxException:\n" + e.getMessage(), 
			"Pattern Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" + e.getMessage(), 
			"Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/*
	 * Writes the changes made to the account into the file named accountsDB.acc
	 */
	
	private void saveAccountsDB() {
		try {
			FileOutputStream fos = new FileOutputStream("accountsDB.acc"); // responsible for writing raw data into a file
			ObjectOutputStream oos = new ObjectOutputStream(fos); // The ObjectOutputStream saves the objects and instances and then serialized
			oos.writeObject(accountsDB); // writes the acccountsDB to a serialized file
			oos.writeObject(lang);  // writes the lang / language to a serialized file
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
	 * Loads the content of the accountsDB.acc and is saved to the Account variable accountsDB
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
	
	private void createAddDialog() {
		JTextField[] inputFields;
		inputFields = new JTextField[Account.getAttributeNo()];
		for (int i = 0; i < inputFields.length; ++i) {
			inputFields[i] = new JTextField();
		}
		
		Object[] prompt = {lang.admin.get("accountNumber"),inputFields[0],lang.admin.get("password"),inputFields[1],
						   lang.admin.get("accountName"),inputFields[2],lang.admin.get("balance"),inputFields[3]};
		
		Object[] options = {lang.admin.get("saveButton"),lang.admin.get("cancelButton")};
		
		JOptionPane optionPane = new JOptionPane(prompt,JOptionPane.PLAIN_MESSAGE,
				                                 JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
		
		JDialog dialog = new JDialog(this,lang.admin.get("addButton"),true);
		dialog.setContentPane(optionPane);
		
		optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (JOptionPane.VALUE_PROPERTY.equals(event.getPropertyName())) {
					if (optionPane.getValue().equals(options[0])) {
						optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						try {
							String[] dataText = new String[inputFields.length];
							
							for (int i = 0; i < dataText.length; ++i) {
								dataText[i] = inputFields[i].getText();
							}
							
							/*
							 * If one of the fields are empty, it will show a warning message asking the user to
							 * fill all the fields.
							 */
							
							if (dataText[0].equals("") || dataText[1].equals("") ||
								dataText[2].equals("") || dataText[3].equals("")) {
								JOptionPane.showMessageDialog(rootPane, lang.admin.get("nullMessage"),
								lang.admin.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							/* 
							 * The length of the accountNo. which is in the accessed through the array dataText with an index 0 
							 * must not exceed 9
							 */
							
							if (dataText[0].length() > 9) {
								JOptionPane.showMessageDialog(rootPane, lang.admin.get("invalidAccountNoMessage"),
								lang.admin.get("invalidAccountNoTitle"), JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							/*
							 * When all the conditions are met, the contents of the fields are then added into the database and
							 * is created as a new Account object named account.
							 */
							
							int accountNo = Integer.parseInt(dataText[0]);
							String password = dataText[1];
							String accountName = dataText[2];
							double balance = Double.parseDouble(dataText[3]);
							
							Account account = new Account(accountNo,password,accountName,balance);
							
							accountsDB.addAccount(account);
							Object[] rowData = account.getData();
							model.addRow(rowData);
							dialog.dispose();
							JOptionPane.showMessageDialog(rootPane, lang.admin.get("addMessage"), 
							lang.admin.get("addButton"), JOptionPane.INFORMATION_MESSAGE);
						}
						
						/* 
						 * These are the catch statements that will catch the expected exceptions thrown during runtime.
						 */
						
						catch (InvalidAccountException e) {
							JOptionPane.showMessageDialog(rootPane, e.getMessage() 
							+ lang.admin.get("invalidAccountMessage"), lang.admin.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
						}
						catch(InvalidBalanceException e){
						    JOptionPane.showMessageDialog(rootPane, e.getMessage()
						    + lang.admin.get("invalidBalanceMessage"), lang.admin.get("invalidBalanceTitle"), JOptionPane.ERROR_MESSAGE);
						}
						catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
							+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
						}
						catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
							+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if (optionPane.getValue().equals(options[1])) {
						optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						dialog.dispose();
					}
				}
			}
		});
		dialog.pack();
		dialog.setLocationRelativeTo(rootPane);
		dialog.setVisible(true);
	}
	
	private void createUpdateDialog(int row) throws NumberFormatException, IndexOutOfBoundsException {
		String[] currentData = new String[Account.getAttributeNo()];
		for (int i = 0; i < currentData.length; ++i) {
			currentData[i] = String.valueOf(table.getValueAt(row,i));
		}
		
		JTextField[] inputFields = new JTextField[Account.getAttributeNo()];
		for (int i = 0; i < inputFields.length; ++i) {
			inputFields[i] = new JTextField(currentData[i]);
		}
		
		int currentNo = Integer.parseInt(currentData[0]);
		
		Object[] prompt = {lang.admin.get("accountNumber"),inputFields[0],lang.admin.get("password"),inputFields[1],
						   lang.admin.get("accountName"),inputFields[2],lang.admin.get("balance"),inputFields[3]};
		
		Object[] options = {lang.admin.get("saveButton"),lang.admin.get("cancelButton")};
		
		JOptionPane optionPane = new JOptionPane(prompt,JOptionPane.PLAIN_MESSAGE,
				                                 JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
		
		JDialog dialog = new JDialog(this,lang.admin.get("updateButton"),true);
		dialog.setContentPane(optionPane);
		
		optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (JOptionPane.VALUE_PROPERTY.equals(event.getPropertyName())) {
					if (optionPane.getValue().equals(options[0])) {
						optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						
						try {
							String[] dataText = new String[inputFields.length];
							
							for (int i = 0; i < dataText.length; ++i) {
								dataText[i] = inputFields[i].getText();
							}
							
							/*
							 * If one of the fields are empty, it will show a warning message asking the user to
							 * fill all the fields.
							 */
							
							if (dataText[0].equals("") || dataText[1].equals("") ||
								dataText[2].equals("") || dataText[3].equals("")) {
								JOptionPane.showMessageDialog(rootPane, lang.admin.get("nullMessage"),
								lang.admin.get("nullTitle"), JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							/* 
							 * The length of the accountNo. which is in the accessed through the array dataText with an index 0 
							 * must not exceed 9
							 */
							
							if (dataText[0].length() > 9) {
								JOptionPane.showMessageDialog(rootPane, lang.admin.get("invalidAccountNoMessage"),
								lang.admin.get("invalidAccountNoTitle"), JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							/*
							 * When all the conditions are met, the contents of an existing account will be updated according to 
							 * the contents of the different fields for updating.
							 */
							
							int accountNo = Integer.parseInt(dataText[0]);
							String password = dataText[1];
							String accountName = dataText[2];
							double balance = Double.parseDouble(dataText[3]);
							
							if (accountsDB.containsAccount(accountNo)) {
								Account currAccount = accountsDB.getAccount(accountNo);
								
								/* 
								 * If the input of the user is equal to the values to be changed, it will then dispose the dialog and then return.
								 */
								
								if (currAccount.getAccountNo() == accountNo &&
									currAccount.getPassword().equals(password) &&
									currAccount.getAccountName().equals(accountName) &&
									currAccount.getBalance() == balance) {
									dialog.dispose();
									return;
								}
								if (accountNo != currentNo) {
									throw new InvalidAccountException();
								}
							}
							
							// The balance must be strictly a postive number
							if (balance < 0) {
								throw new InvalidBalanceException();
							}

							Account account = new Account(accountNo,password,accountName,balance); // creates a new account variable
							
							Object[] rowdata = account.getData(); // gets the data of each row
							
							int response = JOptionPane.showConfirmDialog(rootPane, lang.admin.get("updateQuestion") 
							+ currentNo + "?", lang.admin.get("updateButton"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							
							if (response == JOptionPane.YES_OPTION) {
								accountsDB.updateAccount(currentNo, account);
								for (int i = 0; i < Account.getAttributeNo(); ++i) {
									table.setValueAt(rowdata[i],row,i);
								}
								dialog.dispose();
								JOptionPane.showMessageDialog(rootPane, lang.admin.get("updateMessage"), 
								lang.admin.get("updateButton"), JOptionPane.INFORMATION_MESSAGE);
							}
						}
						
						/* 
						 * These are the catch statements that will catch the expected exceptions thrown during runtime.
						 */
						
						catch (InvalidAccountException e) {
							JOptionPane.showMessageDialog(rootPane, e.getMessage() 
							+ lang.admin.get("invalidAccountMessage"), lang.admin.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
						}
						catch(InvalidBalanceException e){
						    JOptionPane.showMessageDialog(rootPane, e.getMessage()
						    + lang.admin.get("invalidBalanceMessage"), lang.admin.get("invalidBalanceTitle"), JOptionPane.ERROR_MESSAGE);
						}
						catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
							+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
						}
						catch (Exception e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
							+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
						}
					}
					else if (optionPane.getValue().equals(options[1])) {
						optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
						dialog.dispose();
					}
				}
			}
		});
		dialog.pack();
		dialog.setLocationRelativeTo(rootPane);
		dialog.setVisible(true);
	}
	
	//calls the method addDialog() when the add account button is clicked.
	private class AddListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			createAddDialog();
		}
	}
	
	/*
	 * If the remove account button is clicked, the remove listener then removes the contents of the row that was selected
	 * and it also removes it in the current database.
	 */
	
	private class RemoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				int[] rows = table.getSelectedRows(); // get the contents of the selected row.
				
				if (rows.length == 0) {
					return;
				}
				
				String[] tableText = new String[rows.length];
				for (int i = 0; i < tableText.length; ++i) {
					tableText[i] = String.valueOf(table.getValueAt(rows[i],0)); // Returns an Integer object holding the value of the specified String
				}
				
				int[] tableAccNo = new int[tableText.length];
				for (int i = 0; i < tableAccNo.length; ++i) {
					tableAccNo[i] = Integer.valueOf(tableText[i]); // Returns an Integer object holding the value of the specified String
				}
				
				int response = JOptionPane.showConfirmDialog(rootPane, lang.admin.get("removeQuestion"), 
				lang.admin.get("removeButton"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				
				if (response == JOptionPane.YES_OPTION) {
					for (int i = 0; i < tableAccNo.length; ++i) {
						for (int j = 0; j < model.getRowCount(); ++j) { // Returns the number of rows in this data table.
							String modelText = String.valueOf(model.getValueAt(j,0));
							int modelAccNo = Integer.parseInt(modelText);
							if (tableAccNo[i] == modelAccNo) {
								accountsDB.removeAccount(accountsDB.getAccount(modelAccNo));
								model.removeRow(j); // Removes the row at row from the model
								break;
							}
						}
					}
					JOptionPane.showMessageDialog(rootPane, lang.admin.get("removeMessage"), 
					lang.admin.get("removeButton"), JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch (IndexOutOfBoundsException e) {
				// Purposely comment out the statement due to the expected error if the user
				// pressed the remove account button without selecting a row.
				/*
				JOptionPane.showMessageDialog(rootPane, "IndexOutOfBoundsException:\n" 
				+ e.getMessage(), "Invalid Action", JOptionPane.ERROR_MESSAGE);
				*/
			}
			catch (NullPointerException e) {
				JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
				"Null Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
				+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/*
	 * When multiple rows are selected, they will be put into an array and then they will be individually updated.
	 */
	
	private class UpdateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				int rows[] = table.getSelectedRows();
				for (int i = 0; i < rows.length; ++i) {
					createUpdateDialog(rows[i]);
				}
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
				+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
			catch (IndexOutOfBoundsException e) {
				// Purposely comment out the statement due to the expected error if the user
				// pressed the update account button without selecting a row.
				/*
				JOptionPane.showMessageDialog(rootPane, "IndexOutOfBoundsException:\n" 
				+ e.getMessage(), "Invalid Action", JOptionPane.ERROR_MESSAGE);
				*/
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
				+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/*
	 * The finish listener asks the admin if they want to save the changes made in the accounts and
	 * also asks if they want to exit.
	 */
	
	private class FinishListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int response = JOptionPane.showConfirmDialog(rootPane, lang.admin.get("saveQuestion"), 
			lang.admin.get("finishButton"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				saveAccountsDB();
				JOptionPane.showMessageDialog(rootPane, lang.admin.get("saveMessage"), 
				lang.admin.get("saveButton"), JOptionPane.INFORMATION_MESSAGE);
			}
			
			response = JOptionPane.showConfirmDialog(rootPane, lang.admin.get("exitQuestion"), 
			lang.admin.get("finishButton"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.YES_OPTION) {
				createLoginFrame();
			}
		}
	}
	
	/*
	 * The import listener allows the admin to import a database. It must have a format of
	 * AccountNo/Password/Name/Balance to be accepted and read. Otherwise, it will throw an error.
	 */
	
	private class ImportListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				JFileChooser importFile = new JFileChooser();
				importFile.showOpenDialog(rootPane); // Pops up an "Open File" file chooser dialog. Note that thetext that appears in the approve button is determined bythe L&F.
				FileReader freader = new FileReader(importFile.getSelectedFile()); // Returns the selected file. This can be set either by theprogrammer via setSelectedFile or by a user action
				BufferedReader breader = new BufferedReader(freader); //Creates a buffering character-input stream that uses a default-sized input buffer.
				
				AccountsDB importDB = new AccountsDB();
				importDB.createAdmin();
				
				String line = null;
				while ((line = breader.readLine()) != null) {
					String[] accountStr = line.split("/");
					
					for (int i = 0; i < accountStr.length; ++i) {
						if (accountStr[i].equals("")) {
							breader.close();
							throw new InvalidFileException();
						}
					}
					
					if (accountStr[0].length() > 9) {
						breader.close();
						throw new InvalidFileException();
					}
					
					int accountNo = Integer.parseInt(accountStr[0]);
					String password = accountStr[1];
					String accountName = accountStr[2];
					double balance = Double.parseDouble(accountStr[3]);
					
					Account account = new Account(accountNo, password, accountName, balance);
					importDB.addAccount(account);
				}
				
				accountsDB = importDB;
				
				Object[][] rawdata = accountsDB.getAccountsTable();
				
				model.setDataVector(rawdata, Account.getAttributes()); // Replaces the value in the dataVector instance variable with the values in the array dataVector.
				
				model.fireTableDataChanged();   // Notifies all listeners that all cell values in the table'srows may have changed. 
												// The number of rows may also have changed and the JTable should redraw the table from scratch.
				
				// Forces the column Password and Account Name to align right
				table.getColumnModel().getColumn(1).setCellRenderer(renderer);
				table.getColumnModel().getColumn(2).setCellRenderer(renderer);
				
				breader.close();
			}
			
			/*
			 * Catch statements for the errors that might be encountered
			 */
			
			catch (InvalidFileException e) {
				JOptionPane.showMessageDialog(rootPane, e.getMessage() 
				+ lang.admin.get("invalidFileMessage"), lang.admin.get("invalidFileTitle"), JOptionPane.ERROR_MESSAGE);
			}
			catch (InvalidAccountException e) {
				JOptionPane.showMessageDialog(rootPane, e.getMessage()
				+ lang.admin.get("invalidAccountMessage"), lang.admin.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
			}
			catch (InvalidBalanceException e) {
				JOptionPane.showMessageDialog(rootPane, e.getMessage() 
				+ lang.admin.get("invalidBalanceMessage"), lang.admin.get("invalidBalanceTitle"), JOptionPane.ERROR_MESSAGE);
			}
			catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(rootPane, "IndexOutOfBoundsException:\n" 
				+ e.getMessage(), "Invalid Action", JOptionPane.ERROR_MESSAGE);
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
				+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
				+ e.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (NullPointerException e) {
				// Purposely comment out the statement due to the expected error if the file chooser do not select a file
				/*
				JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
				"Null Error", JOptionPane.ERROR_MESSAGE);
				*/
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
				+ e.getMessage() + "\n" + e.getStackTrace(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	/*
	 * The export listener allows the user to export the current contents of the table into a file that can be read.
	 */
	
	private class ExportListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				JFileChooser exportFile = new JFileChooser(); // Constructs a JFileChooser pointing to the user'sdefault directory. This default depends on the operating system.
				exportFile.showSaveDialog(rootPane); // Pops up a "Save File" file chooser dialog.

				FileWriter fwriter = new FileWriter(exportFile.getSelectedFile()); // Constructs a FileWriter given the File to write
				BufferedWriter bwriter = new BufferedWriter(fwriter); // Creates a buffered character-output stream that uses a default-sizedoutput buffer.
				
				Object[][] database = accountsDB.getAccountsTable();
				
				for (int i = 0; i < database.length; ++i) {
					for (int j = 0; j < database[i].length; ++j) {
						String dataStr = String.valueOf(database[i][j]);
						bwriter.write(dataStr);
						if (j < database[i].length - 1) {
							bwriter.write("/");
						}
					}
					bwriter.write("\n");
				}
				
				bwriter.close();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
				+ e.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (NullPointerException e) {
				// Purposely comment out the statement due to the expected error if the file chooser do not select a file
				/*
				JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
				"Null Error", JOptionPane.ERROR_MESSAGE);
				*/
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
				+ e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/*
	 * The append listener allows the user to append a database to the current database of the adminframe.
	 * It is similar to the import, their only difference is that importing will overwrite all the data while
	 * append combines the two. 
	 */
	
	private class AppendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				JFileChooser appendFile = new JFileChooser();
				appendFile.showOpenDialog(rootPane); // Pops up an "Open File" file chooser dialog. Note that the text that appears in the approve button is determined by the L&F.
				FileReader freader = new FileReader(appendFile.getSelectedFile()); // Returns the selected file 
				BufferedReader breader = new BufferedReader(freader); // Creates a buffering character-input stream that uses a default-sized input buffer

				AccountsDB tempDB = new AccountsDB();
				
				String line = null;
				while ((line = breader.readLine()) != null) {   // Reads a line of text. A line is considered to be terminated by any one of a line feed ('\n'), 
																//a carriage return ('\r'), a carriage return followed immediately by a line feed, or by reaching the end-of-file(EOF).
					String[] accountStr = line.split("/"); // This method works as if by invoking the two-argument split method with the given expression and a limit argument of zero
					
					// if there is an empty string it will throw an invalid file exception
					for (int i = 0; i < accountStr.length; ++i) {
						if (accountStr[i].equals("")) {
							breader.close(); // Closes the stream and releases any system resources associated with it
							throw new InvalidFileException();
						}
					}
					
					if (accountStr[0].length() > 9) {
						breader.close();
						throw new InvalidFileException();
					}
					
					int accountNo = Integer.parseInt(accountStr[0]); // parses the content of accountStr[0] into an integer variable named acountNo
					String password = accountStr[1]; // assigns the content of accountStr[1] to a string variable
					String accountName = accountStr[2]; // assigns the content of accountStr[2] to a string variable
					double balance = Double.parseDouble(accountStr[3]); //parses the content of accountStr[0] into an integer variable named acountNo
					
					Account account = new Account(accountNo, password, accountName, balance); // creates a new account object
					
					// if there are two same accounts, it will throw an invalidaccountexception
					if (accountsDB.containsAccount(accountNo)) {
						breader.close();
						throw new InvalidAccountException();
					}
					
					tempDB.addAccount(account);
				}
				
				for (Integer key : tempDB.getAccountsDB().keySet()) {
					accountsDB.addAccount(tempDB.getAccount(key));
					Object[] rowData = accountsDB.getAccount(key).getData();
					model.addRow(rowData);
				}

				breader.close();
			}
			
			/*
			 * Catch statements for the errors that might be encountered
			 */
			
			catch (InvalidFileException e) {
				JOptionPane.showMessageDialog(rootPane, e.getMessage() 
				+ lang.admin.get("invalidFileMessage"), lang.admin.get("invalidFileTitle"), JOptionPane.ERROR_MESSAGE);
			}
			catch (InvalidAccountException e) {
				JOptionPane.showMessageDialog(rootPane, e.getMessage()
				+ lang.admin.get("invalidAccountMessage"), lang.admin.get("invalidAccountTitle"), JOptionPane.ERROR_MESSAGE);
			}
			catch (InvalidBalanceException e) {
				JOptionPane.showMessageDialog(rootPane, e.getMessage() 
				+ lang.admin.get("invalidBalanceMessage"), lang.admin.get("invalidBalanceTitle"), JOptionPane.ERROR_MESSAGE);
			}
			catch (IndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(rootPane, "IndexOutOfBoundsException:\n" 
				+ e.getMessage(), "Invalid Action", JOptionPane.ERROR_MESSAGE);
			}
			catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(rootPane, "NumberFormatException:\n" 
				+ e.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(rootPane, "IOException:\n" 
				+ e.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (NullPointerException e) {
				// Purposely comment out the statement due to the expected error if the file chooser do not select a file
				/*
				JOptionPane.showMessageDialog(rootPane, "NullPointerException:\n" + e.getMessage(), 
				"Null Error", JOptionPane.ERROR_MESSAGE);
				*/
			}
			catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(rootPane, "Unknown Exception:\n" 
				+ e.getMessage() + "\n" + e.getStackTrace(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}