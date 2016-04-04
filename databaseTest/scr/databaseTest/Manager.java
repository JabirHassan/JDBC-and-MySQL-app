package databaseTest;

import java.util.List;
import javax.swing.table.AbstractTableModel;


public class Manager extends AbstractTableModel{
	
	public static final int OBJECT_COL = -1;
	private static final int LAST_NAME_COL = 0;
	private static final int FIRST_NAME_COL = 1;
	
	private int id;
	private String lastName;
	private String firstName;
	
	private String[] columnNames = { "Last Name", "First Name" };
	private List<Object> managers;
	
	public Manager(int id, String lastName, String firstName){
		this.setId(id);
		this.setLastName(lastName);
		this.setFirstName(firstName);
	}
	
	public Manager(String theFirstName, String theLastName){
		this.firstName = theFirstName;
		this.lastName = theLastName;
	}
	
	public Manager(List<Object> theManagers) {
		managers = theManagers;
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
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return managers.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {

		Manager tempManager = (Manager) managers.get(row);

		switch (col) {
		case LAST_NAME_COL:
			return tempManager.getLastName();
		case FIRST_NAME_COL:
			return tempManager.getFirstName();
		case OBJECT_COL:
			return tempManager;
		default:
			return tempManager.getLastName();
		}
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	@Override
	public String toString() {
		return String.format("Manager [ lastName=%s, firstName=%s]", lastName, firstName);
	}

}
