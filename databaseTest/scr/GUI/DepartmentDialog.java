package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import databaseTest.ConnectionHandler;
import databaseTest.Department;
import databaseTest.Manager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class DepartmentDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField deptNameTextField;
	private JTextField deptManagerTextField;
	
	private ConnectionHandler c_handler;
	private DepartmentGUI departmentGUI;
	private Department previousDepartment = null;
	private boolean updateMode = false;
	private int userId;
	
	public DepartmentDialog(DepartmentGUI theDepartmentGUI, ConnectionHandler thecHandler,Object thePreviousDept, boolean theUdateMode,int theUserId) {
		this();
		c_handler = thecHandler;
		departmentGUI = theDepartmentGUI;
		previousDepartment = (Department) thePreviousDept;
		updateMode = theUdateMode;
		this.userId = theUserId;
		
		if(updateMode){
			setTitle("Update Employee");
			populateGUI(previousDepartment);
		}
	}
	
	public DepartmentDialog(DepartmentGUI theDeptGUI, ConnectionHandler thecHandler) {
		this();
		c_handler = thecHandler;
		departmentGUI = theDeptGUI;
	}

	private void populateGUI(Department theDept) {

		deptNameTextField.setText(theDept.getDeptName());
		deptManagerTextField.setText(Integer.toString(theDept.getDeptManager()));
			
	}

	/**
	 * Create the dialog.
	 */
	public DepartmentDialog() {
		setTitle("Add Department");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblDepartmentName = new JLabel("Department Name");
			lblDepartmentName.setBounds(10, 28, 100, 14);
			contentPanel.add(lblDepartmentName);
		}
		{
			deptNameTextField = new JTextField();
			deptNameTextField.setBounds(127, 25, 244, 20);
			contentPanel.add(deptNameTextField);
			deptNameTextField.setColumns(10);
		}
		{
			JLabel lblDepartmentManager = new JLabel("Department Manager");
			lblDepartmentManager.setBounds(10, 71, 119, 14);
			contentPanel.add(lblDepartmentManager);
		}
		{
			deptManagerTextField = new JTextField();
			deptManagerTextField.setBounds(129, 68, 242, 20);
			contentPanel.add(deptManagerTextField);
			deptManagerTextField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(255, 250, 250));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveDepartment();
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

	public void setLoggedInUserName(String firstName, String lastName) {
		//loggedInUserLabel.setText(firstName + " " + lastName);
	}
	
	protected void saveDepartment() {
		// get the department info from gui
				String deptName = deptNameTextField.getText();
				int managerId = Integer.parseInt(deptManagerTextField.getText());

				Object tempDept = null;
				
				if (updateMode) {
					tempDept = previousDepartment;
					
					((Department) tempDept).setDeptName(deptName);
					((Department) tempDept).setDeptManager(managerId);
					
					
				} else {
					tempDept = new Department(deptName, managerId);
				}
				
				try {
					
					// save to the database
					if (updateMode) {
						c_handler.updateRecord(tempDept, userId, "department");
					} else {
						c_handler.addRecord(tempDept, userId, "department");
					}

					// close dialog
					setVisible(false);
					dispose();

					// refresh gui list
					departmentGUI.refreshEmployeesView();
					
					// show success message
					JOptionPane.showMessageDialog(departmentGUI,"Department saved succesfully.",
							"Employee Added", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(
							departmentGUI,"Error saving department: "+ exc.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			
	}
}
