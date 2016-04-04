package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.AuditHistory;
import databaseTest.ConnectionHandler;
import databaseTest.Department;
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

public class DepartmentGUI extends JFrame {

	private ConnectionHandler c_handler;
	private JLabel loggedInUserLabel;
	private JLabel lblLoggedInAs;
	private JTable table;
	private JPanel contentPane;
	private JTextField deptNameTextField;
	
	private User user;
	private int userId;
	private boolean admin;

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
	public DepartmentGUI(int theUserId, boolean theAdmin, ConnectionHandler c_handler, User theUser) {
		setTitle("Departments Table");
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
		panel.setBounds(5, 31, 424, 33);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel);
		
		JLabel lblEnterDepartmentName = new JLabel("Enter Department Name");
		panel.add(lblEnterDepartmentName);
		
		deptNameTextField = new JTextField();
		panel.add(deptNameTextField);
		deptNameTextField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get last name from the text field

				// Call DAO and get departments for the last name

				// If last name is empty, then get all departments

				// display out departments
				try {
					String deptName = deptNameTextField.getText();

					List<Object> departments = null;

					if (deptName != null && deptName.trim().length() > 0) {
						departments = c_handler.searchRecords(deptName, "department");
					} else {
						departments = c_handler.getAllRecords("department");
					}

					// create the model and update the "table"
					Department model = new Department(departments);
					table.setModel(model);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(DepartmentGUI.this, "Error: " + exc, "Error",
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 65, 424, 158);
		contentPane.add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 250, 250));
		panel_1.setBounds(5, 223, 424, 33);
		contentPane.add(panel_1);
		
		JButton btnAddDepartment = new JButton("Add Department");
		btnAddDepartment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(true);
				// create dialog
				DepartmentDialog dialog = new DepartmentDialog(DepartmentGUI.this, c_handler);

				// show dialog
				dialog.setVisible(true);
				refreshEmployeesView();
			}
		});
		panel_1.add(btnAddDepartment);
		
		JButton btnUpdateDepartment = new JButton("Update Department");
		btnUpdateDepartment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//get the selected item
				int row = table.getSelectedRow();
				
				// make sure a row is selected
				if (row < 0) {
					JOptionPane.showMessageDialog(DepartmentGUI.this, "You must select a department", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;
				}
				// get the current employee
				Object tempDepartment = (Department) table.getValueAt(row, Department.OBJECT_COL);
				
				// create dialog
				DepartmentDialog dialog = new DepartmentDialog(DepartmentGUI.this, c_handler, tempDepartment, true, userId);

				// show dialog
				dialog.setVisible(true);
				refreshEmployeesView();
			}
		});
		panel_1.add(btnUpdateDepartment);
		
		JButton btnViewHistory = new JButton("View History");
		btnViewHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get the selected item
				int row = table.getSelectedRow();
				
				// make sure a row is selected
				if (row < 0) {
					JOptionPane.showMessageDialog(DepartmentGUI.this, "You must select a department", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;
				}
				
				// get the current employee
				Department tempDept = (Department) table.getValueAt(row, Department.OBJECT_COL);

				try {
					// get audit history for this employee
					int managerId = tempDept.getId();
					List<AuditHistory> auditHistoryList = c_handler.getAuditHistory(managerId);

					// show audit history dialog
					AuditHistoryDialog dialog = new AuditHistoryDialog();
					dialog.populate(tempDept, auditHistoryList, "department");
					
					dialog.setVisible(true);
				}
				catch (Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(DepartmentGUI.this, "Error retrieving audit history", "Error",
							JOptionPane.ERROR_MESSAGE);				
					return;					
				}
			}
		});
		panel_1.add(btnViewHistory);
		
		lblLoggedInAs = new JLabel("Logged In As: ");
		lblLoggedInAs.setBounds(10, 11, 95, 14);
		contentPane.add(lblLoggedInAs);
		
		loggedInUserLabel = new JLabel("");
		loggedInUserLabel.setBounds(89, 11, 95, 14);
		contentPane.add(loggedInUserLabel);
		scrollPane.setViewportView(table);
	}
	
	public void refreshEmployeesView() {

		try {
			List<Object> departments = c_handler.getAllRecords("department");

			// create the model and update the "table"
			Department model = new Department(departments);

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
