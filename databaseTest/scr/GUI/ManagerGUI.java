package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.AuditHistory;
import databaseTest.ConnectionHandler;
import databaseTest.Employee;
import databaseTest.Manager;
import databaseTest.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ManagerGUI extends JFrame {
	
	private ConnectionHandler c_handler;
	private JLabel loggedInUserLabel;
	private JLabel lblLoggedIn;
	private JTable table;
	private JPanel contentPane;
	private JTextField lastNameTextField;
	
	private User user;
	private int userId;
	private boolean admin;
	public boolean isActive = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManagerGUI(int theUserId, boolean theAdmin, ConnectionHandler c_handler, User theUser) {
		setTitle("Managers Table");
		isActive = true;
		this.userId = theUserId;
		this.c_handler = c_handler;
		this.admin = theAdmin;
		this.user = theUser;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 250, 250));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 250, 250));
		panel.setBounds(5, 28, 424, 33);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		JLabel lblEnterLastName = new JLabel("Enter Last Name");
		panel.add(lblEnterLastName);
		
		lastNameTextField = new JTextField();
		panel.add(lastNameTextField);
		lastNameTextField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get last name from the text field

				// Call DAO and get managers for the last name

				// If last name is empty, then get all manager

				// display out managers
				try {
					String lastName = lastNameTextField.getText();

					List<Object> managers = null;

					if (lastName != null && lastName.trim().length() > 0) {
						managers = c_handler.searchRecords(lastName, "manager");
					} else {
						managers = c_handler.getAllRecords("manager");
					}

					// create the model and update the "table"
					Manager model = new Manager(managers);
					table.setModel(model);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(ManagerGUI.this, "Error: " + exc, "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(btnSearch);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isActive = false;
				setVisible(false);
				
			}
		});
		panel.add(btnCancel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 64, 424, 159);
		contentPane.add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 250, 250));
		panel_1.setBounds(5, 223, 424, 33);
		contentPane.add(panel_1);
		
		JButton btnAddManager = new JButton("Add Manager");
		btnAddManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
				// create dialog
				ManagerDialog dialog = new ManagerDialog(ManagerGUI.this, c_handler);

				// show dialog
				dialog.setVisible(true);
				refreshManagersView();
				
			}
		});
		panel_1.add(btnAddManager);
		
		JButton btnUpdateManager = new JButton("Update Manager");
		btnUpdateManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get the selected item
				int row = table.getSelectedRow();
				
				// make sure a row is selected
				if (row < 0) {
					JOptionPane.showMessageDialog(ManagerGUI.this, "You must select a manager", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;
				}
				// get the current employee
				Object tempManager = (Manager) table.getValueAt(row, Manager.OBJECT_COL);
				
				// create dialog
				ManagerDialog dialog = new ManagerDialog(ManagerGUI.this, c_handler, tempManager, true, userId);

				// show dialog
				dialog.setVisible(true);
				refreshManagersView();
			}
		});
		panel_1.add(btnUpdateManager);
		
		JButton btnViewHistory = new JButton("View History");
		btnViewHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get the selected item
				int row = table.getSelectedRow();
				
				// make sure a row is selected
				if (row < 0) {
					JOptionPane.showMessageDialog(ManagerGUI.this, "You must select a manager", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;
				}
				
				// get the current employee
				Manager tempManager = (Manager) table.getValueAt(row, Manager.OBJECT_COL);

				try {
					// get audit history for this employee
					int managerId = tempManager.getId();
					List<AuditHistory> auditHistoryList = c_handler.getAuditHistory(managerId);

					// show audit history dialog
					AuditHistoryDialog dialog = new AuditHistoryDialog();
					dialog.populate(tempManager, auditHistoryList, "manager");
					
					dialog.setVisible(true);
				}
				catch (Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(ManagerGUI.this, "Error retrieving audit history", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;					
				}
			}
		});
		panel_1.add(btnViewHistory);
		
		lblLoggedIn = new JLabel("Logged In As");
		lblLoggedIn.setBounds(10, 11, 79, 14);
		contentPane.add(lblLoggedIn);
		//table = new JTable();
		loggedInUserLabel = new JLabel("");
		loggedInUserLabel.setBounds(95, 11, 133, 14);
		contentPane.add(loggedInUserLabel);
		scrollPane.setViewportView(table);
	}
	
	public void refreshManagersView() {

		try {
			List<Object> managers = c_handler.getAllRecords("manager");

			// create the model and update the "table"
			Manager model = new Manager(managers);

			table.setModel(model);
			table.setVisible(true);
		} catch (Exception exc) {
		
			JOptionPane.showMessageDialog(this, "Error: " + exc, "Error",JOptionPane.ERROR_MESSAGE);
			
		}
		
	}
	public void setLoggedInUserName(String firstName, String lastName) {
		loggedInUserLabel.setText(firstName + " " + lastName);
	}
}
