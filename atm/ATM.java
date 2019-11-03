package atm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ATM {
	public static void main(String[] args) {
		LoginFrame atm = null;
		AccountsDB accountsDB;
		Language lang;
		
		try {
			File file = new File("accountsDB.acc");
			
			/* 
			 * If the accountsDB.acc file exist, its contents will be read and then it will be saved into the database object.
			 * If it does not exist, it will create a new accountsDB.acc and then a new database will be written to it.
			 */
			
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object database = ois.readObject();
				Object savedLanguage = ois.readObject();
				accountsDB = (AccountsDB) database;
				lang = (Language) savedLanguage;
				ois.close();
			}
			else {
				accountsDB = new AccountsDB();
				accountsDB.createAdmin();
				
				lang = new Language();
				lang.setEnglish();
				
				FileOutputStream fos = new FileOutputStream("accountsDB.acc");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(accountsDB);
				oos.writeObject(lang);
				oos.close();
			}
			
			atm = new LoginFrame();
			
			// Managers the look and feel of the user interface.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			atm.setSize(400, 300);
			atm.setResizable(false);
			atm.setLocationRelativeTo(null);
			atm.setVisible(true);
		}
		
		/* 
		 * These are the catch statements that will catch the errors/exceptions that may possibly be thrown
		 * during runtime
		 */
		
		catch (NullPointerException e) {
			JOptionPane.showMessageDialog(atm, "NullPointerException:\n" + e.getMessage(), 
			"Null Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(atm, "IOException:\n" + e.getMessage(), 
			"IO Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(atm, "Look and Feel Exception:\n" 
		    + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
}
