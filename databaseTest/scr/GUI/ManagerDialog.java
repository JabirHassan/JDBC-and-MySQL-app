package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.ConnectionHandler;
import databaseTest.Employee;
import databaseTest.Manager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ManagerDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private ConnectionHandler c_handler;
	private ManagerGUI managerGUI;
	private Manager previousManager = null;
	private boolean updateMode = false;
	private int userId;

	public ManagerDialog(ManagerGUI theManagerGUI, ConnectionHandler thecHandler,Object thePreviousManager, boolean theUdateMode,int theUserId) {
		this();
		c_handler = thecHandler;
		managerGUI = theManagerGUI;
		previousManager = (Manager) thePreviousManager;
		updateMode = theUdateMode;
		this.userId = theUserId;
		
		if(updateMode){
			setTitle("Update Employee");
			populateGUI(previousManager);
		}
	}
	
	public ManagerDialog(ManagerGUI theManagerGUI, ConnectionHandler thecHandler) {
		this();
		c_handler = thecHandler;
		managerGUI = theManagerGUI;
	}

	private void populateGUI(Manager theManager) {

		firstNameTextField.setText(theManager.getFirstName());
		lastNameTextField.setText(theManager.getLastName());
			
	}
	
	public ManagerDialog() {
		setTitle("Add Manager");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblEnterFirstName = new JLabel("First Name");
			lblEnterFirstName.setBounds(10, 33, 71, 14);
			contentPanel.add(lblEnterFirstName);
		}
		
		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(79, 30, 274, 20);
		contentPanel.add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(10, 75, 59, 14);
		contentPanel.add(lblLastName);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setBounds(79, 72, 274, 20);
		contentPanel.add(lastNameTextField);
		lastNameTextField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(255, 250, 250));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveManager();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void saveManager() {
		// get the manager info from gui
		String firsName = firstNameTextField.getText();
		String lastName = lastNameTextField.getText();

		Object tempManager = null;
		
		if (updateMode) {
			tempManager = previousManager;
			
			((Manager) tempManager).setLastName(lastName);
			((Manager) tempManager).setFirstName(firsName);
			
			
		} else {
			tempManager = new Manager(lastName, firsName);
		}
		
		try {
			
			// save to the database
			if (updateMode) {
				c_handler.updateRecord(tempManager, userId, "manager");
			} else {
				c_handler.addRecord(tempManager, userId, "manager");
			}

			// close dialog
			setVisible(false);
			dispose();

			// refresh gui list
			managerGUI.refreshManagersView();
			
			// show success message
			JOptionPane.showMessageDialog(managerGUI,"Manager saved succesfully.",
					"Manager Added", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception exc) {
			JOptionPane.showMessageDialog(
					managerGUI,"Error saving manager: "+ exc.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		
	}
	
	
}
