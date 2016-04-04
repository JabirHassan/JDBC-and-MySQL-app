package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.ConnectionHandler;
import databaseTest.Employee;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class EmployeeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField firstNameTextField;
	private JTextField lastNameTextField;
	private JTextField departmentTextField;
	private ConnectionHandler c_handler;
	private EmployeeGUI employeeGUI;
	
	private Employee previousEmployee = null;
	private boolean updateMode = false;
	private int userId;

	public EmployeeDialog(EmployeeGUI theEmployeeGUI, ConnectionHandler thecHandler,Object thePreviousEmployee, boolean theUdateMode,int theUserId) {
		this();
		c_handler = thecHandler;
		employeeGUI = theEmployeeGUI;
		previousEmployee = (Employee) thePreviousEmployee;
		updateMode = theUdateMode;
		this.userId = theUserId;
		
		if(updateMode){
			setTitle("Update Employee");
			populateGUI(previousEmployee);
		}
	}
	
	private void populateGUI(Employee theEmployee) {

		firstNameTextField.setText(theEmployee.getFirstName());
		lastNameTextField.setText(theEmployee.getLastName());
		departmentTextField.setText(Integer.toString(theEmployee.getDepartment()));
			
	}
	public EmployeeDialog(EmployeeGUI theEmployeeGUI, ConnectionHandler thecHandler) {
		this();
		c_handler = thecHandler;
		employeeGUI = theEmployeeGUI;
	}

	/**
	 * Create the dialog.
	 */
	public EmployeeDialog() {
		setTitle("Add Employee");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(10, 37, 64, 14);
		contentPanel.add(lblFirstName);
		
		firstNameTextField = new JTextField();
		firstNameTextField.setBounds(92, 35, 258, 17);
		contentPanel.add(firstNameTextField);
		firstNameTextField.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(10, 63, 64, 14);
		contentPanel.add(lblLastName);
		
		lastNameTextField = new JTextField();
		lastNameTextField.setBounds(92, 60, 258, 20);
		contentPanel.add(lastNameTextField);
		lastNameTextField.setColumns(10);
		
		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setBounds(10, 95, 72, 14);
		contentPanel.add(lblDepartment);
		
		departmentTextField = new JTextField();
		departmentTextField.setBounds(92, 91, 258, 20);
		contentPanel.add(departmentTextField);
		departmentTextField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(255, 250, 250));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						saveEmployee();
						
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

	protected void saveEmployee() {
		// get the employee info from gui
				String firstName = firstNameTextField.getText();
				String lastName = lastNameTextField.getText();
				String department = departmentTextField.getText();
				int dept = Integer.parseInt(department);

				Object tempEmployee = null;
				
				if (updateMode) {
					tempEmployee = previousEmployee;
					
					((Employee) tempEmployee).setLastName(lastName);
					((Employee) tempEmployee).setFirstName(firstName);
					((Employee) tempEmployee).setDepartment(dept);
					
					
				} else {
					tempEmployee = new Employee(lastName, firstName, dept);
				}
				
				try {
					
					// save to the database
					if (updateMode) {
						c_handler.updateRecord(tempEmployee, userId, "employee");
					} else {
						c_handler.addRecord(tempEmployee, userId, "employee");
					}

					// close dialog
					setVisible(false);
					dispose();

					// refresh gui list
					employeeGUI.refreshEmployeesView();
					
					// show success message
					JOptionPane.showMessageDialog(employeeGUI,"Employee saved succesfully.",
							"Employee Added", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(
							employeeGUI,"Error saving employee: "+ exc.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
		
	}
	
}
