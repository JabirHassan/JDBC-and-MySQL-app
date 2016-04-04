package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import databaseTest.ConnectionHandler;
import databaseTest.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class UserLoginDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox userComboBox;
	private ConnectionHandler c_handler;
	private User user;
	private JPasswordField passwordField;

	public void setUser(User theUser) {
		user = theUser;
	}
	
	public void setConnectionHandler(ConnectionHandler theConnectionHandler) {
		c_handler = theConnectionHandler;
	}


	public void populateUsers(List<User> users) {
		userComboBox.setModel(new DefaultComboBoxModel(users.toArray(new User[0])));
	}

	/**
	 * Create the dialog.
	 */
	public UserLoginDialog() {
		setTitle("Login");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblUser = new JLabel("User");
			lblUser.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblUser.setBounds(40, 48, 46, 14);
			contentPanel.add(lblUser);
		}
		
		userComboBox = new JComboBox();
		userComboBox.setBounds(127, 47, 241, 20);
		contentPanel.add(userComboBox);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(40, 99, 76, 14);
		contentPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(127, 98, 241, 20);
		contentPanel.add(passwordField);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(255, 250, 250));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						performUserLogin();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Exit");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public JComboBox getUserComboBox() {
		return userComboBox;
	}
	
	private void performUserLogin() {
		
		try {
			// get the user id
			if (userComboBox.getSelectedIndex() == -1) {						
				JOptionPane.showMessageDialog(UserLoginDialog.this, "You must select a user.", "Error", JOptionPane.ERROR_MESSAGE);				
				return;
			}
			boolean isValidPassword = false;
			User user =(User) userComboBox.getSelectedItem() ;
			int userId = user.getId();
			boolean admin = user.isAdmin();
			// get the password
			String plainTextPassword = new String(passwordField.getPassword());
			user.setPassword(plainTextPassword);			

			// check the user's password against the encrypted version in the database
			 isValidPassword = user.authenticate(user);
			
			if (isValidPassword ) {
				// hide the login window
				setVisible(false);

				// now show the main app window
				MainGUI frame = new MainGUI(userId, admin, c_handler, user);
				//frame.setLoggedInUserName(theUser.getFirstName(), theUser.getLastName());

				frame.setVisible(true);
			}
			else {
				// show error message
				JOptionPane.showMessageDialog(this, "Invalid login", "Invalid Login",
						JOptionPane.ERROR_MESSAGE);

				return;			
			}
		}
		catch (Exception exc) {
			JOptionPane.showMessageDialog(this, "Error during login", "Error",
					JOptionPane.ERROR_MESSAGE);			
		}
	}
}
