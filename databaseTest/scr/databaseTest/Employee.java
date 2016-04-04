package databaseTest;


import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class Employee extends AbstractTableModel {

	private int id;
	private String lastName;
	private String firstName;
	private int department;
	
	

	public static final int OBJECT_COL = -1;
	private static final int LAST_NAME_COL = 0;
	private static final int FIRST_NAME_COL = 1;
	private static final int DEPARTMENT_COL = 2;

	private String[] columnNames = { "Last Name", "First Name", "Department" };
	private List<Object> employees;

	public Employee(int id, String lastName, String firstName, int dept) {

		this.setId(id);
		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.setDepartment(dept);

	}
	public Employee( String lastName, String firstName, int dept) {

		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.setDepartment(dept);

	}

	public Employee(List<Object> theEmployee) {
		employees = theEmployee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getDepartment() {
		return department;
	}

	public void setDepartment(int department) {
		this.department = department;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return employees.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {

		Employee tempEmployee = (Employee)employees.get(row);

		switch (col) {
		case LAST_NAME_COL:
			return tempEmployee.getLastName();
		case FIRST_NAME_COL:
			return tempEmployee.getFirstName();
		case DEPARTMENT_COL:
			return tempEmployee.getDepartment();
		case OBJECT_COL:
			return tempEmployee;
		default:
			return tempEmployee.getLastName();
		}
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public String toString() {
		return String.format("Employee [id=%s, lastName=%s, firstName=%s, department=%s]", id, lastName, firstName,
				department);
	}
}
