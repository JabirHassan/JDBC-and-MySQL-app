package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.AuditHistory;
import databaseTest.ConnectionHandler;
import databaseTest.Employee;
import databaseTest.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class EmployeeGUI extends JFrame {

	private JTable table;
	private JPanel contentPane;
	private JLabel lblLoggedIn;
	private JLabel loggedInUserLabel;
	
	private JTextField lastNameTextField;
	private ConnectionHandler c_handler;
	
	private User user;
	
	private int userId;
	private boolean admin;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				/*try {
					ConnectionHandler c_handler = new ConnectionHandler();
					User user = new User();
					// Get users
					List<User> users = user.getUsers(true, 0);

					// Show login dialog
					UserLoginDialog dialog = new UserLoginDialog();
					dialog.populateUsers(users);
					dialog.setConnectionHandler(c_handler);
					
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public EmployeeGUI(int theUserId, boolean theAdmin, ConnectionHandler c_handler, User theUser) {
		setTitle("Employees Table");
		this.userId = theUserId;
		this.c_handler = c_handler;
		this.admin = theAdmin;
		this.user = theUser;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 502, 339);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 250, 250));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 250, 250));
		panel.setBounds(5, 41, 474, 33);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);

		JLabel lblEnterLastName = new JLabel("Enter Last Name");
		panel.add(lblEnterLastName);

		lastNameTextField = new JTextField();
		panel.add(lastNameTextField);
		lastNameTextField.setColumns(15);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Get last name from the text field

				// Call DAO and get employees for the last name

				// If last name is empty, then get all employees

				// Print out employees
				try {
					String lastName = lastNameTextField.getText();

					List<Object> employees = null;

					if (lastName != null && lastName.trim().length() > 0) {
						employees = c_handler.searchRecords(lastName, "employee");
					} else {
						employees = c_handler.getAllRecords("employee");
					}

					// create the model and update the "table"
					Employee model = new Employee(employees);
					table.setModel(model);

				} catch (Exception exc) {
					JOptionPane.showMessageDialog(EmployeeGUI.this, "Error: " + exc, "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		panel.add(btnSearch);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				setVisible(false);
			}
		});
		panel.add(btnCancel);
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setBounds(5, 79, 474, 181);
		contentPane.add(scrollPane);
		
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 250, 250));
		panel_1.setBounds(5, 256, 474, 33);
		contentPane.add(panel_1);
		
		JButton btnAddEmployee = new JButton("Add Employee");
		btnAddEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(true);
				// create dialog
				EmployeeDialog dialog = new EmployeeDialog(EmployeeGUI.this, c_handler);

				// show dialog
				dialog.setVisible(true);
				refreshEmployeesView();
			}
		});
		panel_1.add(btnAddEmployee);
		
		JButton btnUpdateEmployee = new JButton("Update Employee");
		btnUpdateEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// get the selected item
				int row = table.getSelectedRow();
				
				// make sure a row is selected
				if (row < 0) {
					JOptionPane.showMessageDialog(EmployeeGUI.this, "You must select an employee", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;
				}
				// get the current employee
				Object tempEmployee = (Employee) table.getValueAt(row, Employee.OBJECT_COL);
				
				// create dialog
				EmployeeDialog dialog = new EmployeeDialog(EmployeeGUI.this, c_handler, tempEmployee, true, userId);

				// show dialog
				dialog.setVisible(true);
				refreshEmployeesView();
			}
		});
		panel_1.add(btnUpdateEmployee);
		
		JButton btnViewHistory = new JButton("View history");
		btnViewHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get the selected item
				int row = table.getSelectedRow();
				
				// make sure a row is selected
				if (row < 0) {
					JOptionPane.showMessageDialog(EmployeeGUI.this, "You must select an employee", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;
				}
				
				// get the current employee
				Employee tempEmployee = (Employee) table.getValueAt(row, Employee.OBJECT_COL);

				try {
					// get audit history for this employee
					int employeeId = tempEmployee.getId();
					List<AuditHistory> auditHistoryList = c_handler.getAuditHistory(employeeId);

					// show audit history dialog
					AuditHistoryDialog dialog = new AuditHistoryDialog();
					dialog.populate(tempEmployee, auditHistoryList, "employee");
					
					dialog.setVisible(true);
				}
				catch (Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(EmployeeGUI.this, "Error retrieving audit history", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;					
				}
				
			}
			
		});
		panel_1.add(btnViewHistory);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 250, 250));
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_2.setBounds(5, 11, 474, 26);
		contentPane.add(panel_2);
		
		lblLoggedIn = new JLabel("Logged in as :  ");
		panel_2.add(lblLoggedIn);
		
		loggedInUserLabel = new JLabel("");
		panel_2.add(loggedInUserLabel);
	}
	public void refreshEmployeesView() {

		try {
			List<Object> employees = c_handler.getAllRecords("employee");

			// create the model and update the "table"
			Employee model = new Employee(employees);

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
