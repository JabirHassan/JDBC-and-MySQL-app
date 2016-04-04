package databaseTest;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class Department extends AbstractTableModel{

	public static final int OBJECT_COL = -1;
	private static final int DEPARTMENT_NAME_COL = 0;
	private static final int MANAGER_NAME_COL = 1;
	
	private int id;
	private String deptName;
	private int deptManager;
	
	private String[] columnNames = { "Department Name", "Manager Id"};
	private List<Object> departments;
	
	public Department(int id, String deptName, int deptManager){
		this.setId(id);
		this.setDeptName(deptName);
		this.setDeptManager(deptManager);
	}
	public Department( String deptName, int deptManager){
		this.setId(id);
		this.setDeptName(deptName);
		this.setDeptManager(deptManager);
	}
	
	public Department(List<Object> theManagers) {
		departments = theManagers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return departments.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {

		Department tempDepartment = (Department) departments.get(row);

		switch (col) {
		case DEPARTMENT_NAME_COL:
			return tempDepartment.getDeptName();
		case MANAGER_NAME_COL:
			return tempDepartment.getDeptManager();
		case OBJECT_COL:
			return tempDepartment;
		default:
			return tempDepartment.getDeptName();
		}
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	@Override
	public String toString() {
		return String.format("Department [ dept_name=%s, manager_name=%s]", deptName, deptManager);
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getDeptManager() {
		return deptManager;
	}

	public void setDeptManager(int deptManager) {
		this.deptManager = deptManager;
	}

}

