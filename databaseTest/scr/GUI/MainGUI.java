package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.ConnectionHandler;
import databaseTest.User;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MainGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainGUI(int theUserId, boolean admin, ConnectionHandler c_handler, User theUser) {
		setTitle("Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 250, 250));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnEmployeesTable = new JButton("Employees Table");
		btnEmployeesTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create employees table
				EmployeeGUI frame = new EmployeeGUI(theUserId, admin, c_handler, theUser);
				frame.setLoggedInUserName(theUser.getFirstName(), theUser.getLastName());
				//show employees table
				frame.setVisible(true);
				
				
			}
		});
		btnEmployeesTable.setBounds(130, 56, 149, 23);
		panel.add(btnEmployeesTable);
		
		JLabel lblChooseATable = new JLabel("Choose a table to view");
		lblChooseATable.setForeground(new Color(0, 0, 0));
		lblChooseATable.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblChooseATable.setBounds(130, 11, 149, 14);
		panel.add(lblChooseATable);
		
		JButton btnManagersTable = new JButton("Managers Table");
		btnManagersTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ManagerGUI frame = new ManagerGUI(theUserId, admin, c_handler, theUser);
				frame.setLoggedInUserName(theUser.getFirstName(), theUser.getLastName());
				
				//show employees table
				frame.setVisible(true);
				
			}
		});
		btnManagersTable.setBounds(130, 90, 149, 23);
		panel.add(btnManagersTable);
		
		JButton btnDepartmentTable = new JButton("Departments Table");
		btnDepartmentTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DepartmentGUI frame = new DepartmentGUI(theUserId, admin, c_handler, theUser);
				frame.setLoggedInUserName(theUser.getFirstName(), theUser.getLastName());
				//show employees table
				frame.setVisible(true);
			}
		});
		btnDepartmentTable.setBounds(130, 124, 149, 23);
		panel.add(btnDepartmentTable);
		
		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				
			}
		});
		btnLogOut.setBounds(130, 158, 149, 23);
		panel.add(btnLogOut);
	}
}
