package databaseTest;

import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AuditHistory extends AbstractTableModel {

	public static final int OBJECT_COL = -1;
	public static final int DATE_TIME_COL = 0;
	private static final int ACTION_COL = 1;
	private static final int USER_FIRST_NAME_COL = 2;
	private static final int USER_LAST_NAME_COL = 3;

	private int userId;
	private int employeeId;
	private String action;
	private Date actionDateTime;

	private String userFirstName;
	private String userLastName;

	private String[] columnNames = { "Date/Time", "Action", "User First Name", "User Last Name" };

	private List<AuditHistory> auditHistoryList;

	public AuditHistory(List<AuditHistory> theAuditHistoryList) {
		auditHistoryList = theAuditHistoryList;
	}

	public AuditHistory(int userId, int employeeId, String action, Date actionDateTime, String userFirstName,
			String userLastName) {
		super();
		this.userId = userId;
		this.employeeId = employeeId;
		this.action = action;
		this.actionDateTime = actionDateTime;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActionDateTime() {
		return actionDateTime;
	}

	public void setActionDateTime(Date actionDateTime) {
		this.actionDateTime = actionDateTime;
	}

	@Override
	public String toString() {
		return String.format(
				"AuditHistory [userId=%s, employeeId=%s, action=%s, actionDateTime=%s, userFirstName=%s, userLastName=%s]",
				userId, employeeId, action, actionDateTime, userFirstName, userLastName);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return auditHistoryList.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {

		AuditHistory tempAuditHistory = auditHistoryList.get(row);

		switch (col) {
		case DATE_TIME_COL:
			return tempAuditHistory.getActionDateTime();
		case ACTION_COL:
			return tempAuditHistory.getAction();
		case USER_FIRST_NAME_COL:
			return tempAuditHistory.getUserFirstName();
		case USER_LAST_NAME_COL:
			return tempAuditHistory.getUserLastName();
		case OBJECT_COL:
			return tempAuditHistory;
		default:
			return tempAuditHistory.getUserLastName();
		}
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

}
