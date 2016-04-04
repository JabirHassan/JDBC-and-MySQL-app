package databaseTest;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import GUI.UserLoginDialog;

public class ConnectionHandler {

	private Connection myConn;

	public ConnectionHandler() throws Exception {
		// get db properties
		Properties props = new Properties();
		props.load(new FileInputStream("sql//db.properties"));

		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");

		// connect to database
		myConn = DriverManager.getConnection(dburl, user, password );
	}

	public List<Object> getAllRecords(String type) throws Exception {
		List<Object> list = new ArrayList<>();
		Statement statement = null;
		ResultSet myRes = null;
		try {
			statement = myConn.createStatement();
			switch (type) {
			case ("employee"): {
				myRes = statement.executeQuery("select * from employees");
				break;
			}
			case ("manager"): {
				myRes = statement.executeQuery("select * from managers");
				break;
			}
			case ("department"): {
				myRes = statement.executeQuery("select * from departments");
				break;
			}

			}

			while (myRes.next()) {
				Object temp = convertRowToRecord(myRes, type);
				list.add(temp);
			}

			

		} finally {
			close(statement, myRes);
		}
		
		return list;

	}
	
	public List<Object> searchRecords(String lastName, String type) throws Exception {
		List<Object> list = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {
			switch (type) {
			case ("employee"): {
				statement = myConn.prepareStatement("select * from employees where last_name like ?");
				statement.setString(1, lastName);
				break;
			}
			case ("manager"): {
				statement = myConn.prepareStatement("select * from managers where last_name like ?");
				statement.setString(1, lastName);
				break;
			}
			case ("department"): {
				statement = myConn.prepareStatement("select * from departments where dept_name like ?");
				statement.setString(1, lastName);
				break;
			}
			}

			results = statement.executeQuery();

			while (results.next()) {
				Object tempEmployee = convertRowToRecord(results, type);
				list.add(tempEmployee);
			}
			return list;
		} finally {
			close(statement, results);
		}
	}
	
	public void updateRecord(Object newObject, int userId, String type) throws SQLException {
		PreparedStatement myStmt = null;
		if(type == "employee"){
			try {
				// prepare statement
				myStmt = myConn.prepareStatement("update employees set first_name=?, last_name=?, department=? where employee_id=?");
	
				// set params
				myStmt.setString(1, (String)((Employee) newObject.getClass().cast(newObject)).getFirstName());
				myStmt.setString(2, (String)((Employee) newObject.getClass().cast(newObject)).getLastName());
				myStmt.setInt(3, (Integer)((Employee) newObject.getClass().cast(newObject)).getDepartment());
				myStmt.setInt(4, (Integer)((Employee) newObject.getClass().cast(newObject)).getId());
				
				// execute SQL
				myStmt.executeUpdate();
				close(myStmt);
				//
				// Add audit history
				//
	
				// prepare statement
				myStmt = myConn.prepareStatement("insert into audit_history"
						+ " (user_id, employee_id, action)"
						+ " values (?, ?, ?)");
				
				// set params
				myStmt.setInt(1, userId);
				myStmt.setInt(2, (Integer)((Employee) newObject.getClass().cast(newObject)).getId());
				myStmt.setString(3, "Updated employee.");			
				//myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	
				// execute SQL
				myStmt.executeUpdate();
			} finally {
				close(myStmt);
			}
		}
		else if(type == "manager"){
			try {
				// prepare statement
				myStmt = myConn.prepareStatement("update managers set first_name=?, last_name=? where manager_id=?");
				// set params
				myStmt.setString(1, (String)((Manager) newObject.getClass().cast(newObject)).getFirstName());
				myStmt.setString(2, (String)((Manager) newObject.getClass().cast(newObject)).getLastName());
				myStmt.setInt(3, (Integer)((Manager) newObject.getClass().cast(newObject)).getId());
				
				// execute SQL
				myStmt.executeUpdate();
				close(myStmt);
				//
				// Add audit history
				//
				
				// prepare statement
				myStmt = myConn.prepareStatement("insert into audit_history"
						+ " (user_id, employee_id, action)"
						+ " values (?, ?, ?)");
				
				// set params
				myStmt.setInt(1, userId);
				myStmt.setInt(2, (Integer)((Manager) newObject.getClass().cast(newObject)).getId());
				myStmt.setString(3, "Updated manager.");			
	
				// execute SQL
				myStmt.executeUpdate();
			} finally {
				close(myStmt);
			}
		}
		else if(type == "department"){
			try {
				// prepare statement
				myStmt = myConn.prepareStatement("update departments set dept_name=?, dept_manager=? where department_id=?");
	
				// set params
				myStmt.setString(1, (String)((Department) newObject.getClass().cast(newObject)).getDeptName());
				myStmt.setInt(2, (Integer)((Department) newObject.getClass().cast(newObject)).getDeptManager());
				myStmt.setInt(3, (Integer)((Department) newObject.getClass().cast(newObject)).getId());
				
				// execute SQL
				myStmt.executeUpdate();
				close(myStmt);
				//
				// Add audit history
				//
	
				// prepare statement
				myStmt = myConn.prepareStatement("insert into audit_history"
						+ " (user_id, employee_id, action)"
						+ " values (?, ?, ?)");
				
				// set params
				myStmt.setInt(1, userId);
				myStmt.setInt(2, (Integer)((Department) newObject.getClass().cast(newObject)).getId());
				myStmt.setString(3, "Updated department.");			
				//myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
	
				// execute SQL
				myStmt.executeUpdate();
			} finally {
				close(myStmt);
			}
		}

	}
	
	public void addRecord(Object newObject, int userId, String type) throws Exception {
		PreparedStatement myStmt = null;
		if (type == "employee") {
			try {
				// prepare statement
				myStmt = myConn.prepareStatement(
						"insert into employees" + " (first_name, last_name, department)" + " values (?, ?, ?)");

				// set params
				myStmt.setString(1, (String) ((Employee) newObject.getClass().cast(newObject)).getFirstName());
				myStmt.setString(2, (String) ((Employee) newObject.getClass().cast(newObject)).getLastName());
				myStmt.setInt(3, (Integer) ((Employee) newObject.getClass().cast(newObject)).getDepartment());

				close(myStmt);
				//
				// Add audit history
				//

				// prepare statement
				myStmt = myConn.prepareStatement("insert into audit_history"
						+ " (user_id, employee_id, action)" + " values (?, ?, ?)");

				// set params
				myStmt.setInt(1, userId);
				myStmt.setInt(2, (Integer) ((Employee) newObject.getClass().cast(newObject)).getId());
				myStmt.setString(3, "Added new employee.");
				myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

				// execute SQL
				myStmt.executeUpdate();

			}

			finally {
				close(myStmt);
			}
		}
		if (type == "manager") {
			try {
				// prepare statement
				myStmt = myConn.prepareStatement(
						"insert into managers" + " (first_name, last_name)" + " values (?, ?)");

				// set params
				myStmt.setString(1, (String) ((Manager) newObject.getClass().cast(newObject)).getFirstName());
				myStmt.setString(2, (String) ((Manager) newObject.getClass().cast(newObject)).getLastName());

				close(myStmt);
				//
				// Add audit history
				//

				// prepare statement
				myStmt = myConn.prepareStatement("insert into audit_history"
						+ " (user_id, manager_id, action)" + " values (?, ?, ?)");

				// set params
				myStmt.setInt(1, userId);
				myStmt.setInt(2, (Integer) ((Manager) newObject.getClass().cast(newObject)).getId());
				myStmt.setString(3, "Added new manager.");
				myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

				// execute SQL
				myStmt.executeUpdate();

			}

			finally {
				close(myStmt);
			}
		}
		if (type == "department") {
			try {
				// prepare statement
				myStmt = myConn.prepareStatement(
						"insert into departments" + " (dept_name, dept_manager)" + " values (?, ?)");

				// set params
				myStmt.setString(1, (String) ((Department) newObject.getClass().cast(newObject)).getDeptName());
				myStmt.setInt(2, (Integer) ((Department) newObject.getClass().cast(newObject)).getDeptManager());

				close(myStmt);
				//
				// Add audit history
				//

				// prepare statement
				myStmt = myConn.prepareStatement("insert into audit_history"
						+ " (user_id, department_id, action)" + " values (?, ?, ?)");

				// set params
				myStmt.setInt(1, userId);
				myStmt.setInt(2, (Integer) ((Department) newObject.getClass().cast(newObject)).getId());
				myStmt.setString(3, "Added new Department.");
				myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

				// execute SQL
				myStmt.executeUpdate();

			}

			finally {
				close(myStmt);
			}
		}

	}

	private Object convertRowToRecord(ResultSet results, String type) throws SQLException {
		
		Object temp = null;
		if (type == "employee") {
			int id = results.getInt("employee_id");
			String lastName = results.getString("last_name");
			String firstName = results.getString("first_name");
			int department = results.getInt("department");

			temp = new Employee(id, lastName, firstName, department);
		}
		if (type == "manager") {
			int id = results.getInt("manager_id");
			String lastName = results.getString("last_name");
			String firstName = results.getString("first_name");

			temp = new Manager(id,lastName, firstName);
		}
		if (type == "department") {
			int id = results.getInt("department_id");
			String deptName = results.getString("dept_name");
			int managerId = results.getInt("dept_manager");

			temp = new Department(id,deptName, managerId);
		}
		return temp;
	}

	private static void close(Connection myConn, Statement myStmt, ResultSet myRs) throws SQLException {

		if (myRs != null) {
			myRs.close();
		}

		if (myStmt != null) {

		}

		if (myConn != null) {
			myConn.close();
		}
	}

	private void close(Statement myStmt, ResultSet myRs) throws SQLException {
		close(null, myStmt, myRs);
	}

	private void close(Statement myStmt) throws SQLException {
		close(null, myStmt, null);
	}
	
	/*private User convertRowToUser(ResultSet myRs) throws SQLException {
		
		int id = myRs.getInt("user_id");
		String lastName = myRs.getString("last_name");
		String firstName = myRs.getString("first_name");
		String password = myRs.getString("password");
		boolean admin = myRs.getBoolean("is_admin");

		
		User tempUser = new User(id, lastName, firstName, password,admin);
		
		return tempUser;
	}
	public List<User> getUsers() throws Exception {
		List<User> list = new ArrayList<User>();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from users order by last_name");
			
			while (myRs.next()) {
				User tempUser = convertRowToUser(myRs);
				list.add(tempUser);
			}

			return list;		
		}
		finally {
			close(myStmt, myRs);
		}
	}*/
	
	public List<AuditHistory> getAuditHistory(int employeeId) throws Exception {
		List<AuditHistory> list = new ArrayList<AuditHistory>();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myStmt = myConn.createStatement();
			
			String sql = "SELECT audit_history.user_id, audit_history.employee_id, audit_history.action, audit_history.action_date_time, users.first_name, users.last_name  "
					+ "FROM audit_history, users users "
					+ "WHERE audit_history.user_id=users.user_id AND audit_history.employee_id=" + employeeId;
			
			myRs = myStmt.executeQuery(sql);
			
			while (myRs.next()) {
				
				int userId = myRs.getInt("audit_history.user_id");
				String action = myRs.getString("audit_history.action");
				
				Timestamp timestamp = myRs.getTimestamp("audit_history.action_date_time");
				java.util.Date actionDateTime = new java.util.Date(timestamp.getTime());
				
				String userFirstName = myRs.getString("users.first_name");
				String userLastName = myRs.getString("users.last_name");
				
				AuditHistory temp = new AuditHistory(userId, employeeId, action, actionDateTime, userFirstName, userLastName);
				
				list.add(temp);
			}

			return list;		
		}
		finally {
			close(myStmt, myRs);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
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
		}
		
	}

}
