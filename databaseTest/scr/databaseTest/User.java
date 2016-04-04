package databaseTest;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.table.AbstractTableModel;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class User extends AbstractTableModel {

	public static final int OBJECT_COL = -1;
	private static final int LAST_NAME_COL = 0;
	private static final int FIRST_NAME_COL = 1;
	private static final int IS_ADMIN_COL = 2;

	private int id;
	private String lastName;
	private String firstName;
	private boolean admin;
	private String password;

	private String[] columnNames = { "Last Name", "First Name", "Is Admin" };
	private List<User> users;
	private Connection myConn;
	Properties props;

	String user = null;
	String d_password = null;
	String dburl = null;

	public User(int id, String lastName, String firstName, boolean admin, String password) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.admin = admin;
		this.password = password;
	}

	public User(int id, String lastName, String firstName, String password, boolean admin) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.admin = admin;
		this.password = password;
	}

	public User() throws Exception {
		// get db properties
		Properties props = new Properties();
		props.load(new FileInputStream("sql/db.properties"));

		user = props.getProperty("user");
		d_password = props.getProperty("password");
		dburl = props.getProperty("dburl");

		// connect to database
		myConn = DriverManager.getConnection(dburl, user, password);

	}

	public boolean authenticate(User theUser)  {

		boolean result = false;
		String plainTextPassword = theUser.getPassword();
		// get the encrypted password from database for this user
		String encryptedPasswordFromDatabase;
		try {
			encryptedPasswordFromDatabase = getEncrpytedPassword(theUser.getId());
			// compare the passwords
			result = PasswordUtils.checkPassword(plainTextPassword, encryptedPasswordFromDatabase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String getEncrpytedPassword(int id) throws Exception {
		String encryptedPassword = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		Properties props = new Properties();
		props.load(new FileInputStream("sql/db.properties"));
		user = props.getProperty("user");
		d_password = props.getProperty("password");
		dburl = props.getProperty("dburl");
		try {
			myConn = DriverManager.getConnection(dburl, user, d_password);
			myStmt = myConn.createStatement();
			
			myRs = myStmt.executeQuery("select password from users where user_id=" + id);
			if (myRs.next()) {
				encryptedPassword = myRs.getString("password");
			} else {
				throw new Exception("User id not found: " + id);
			}
		} finally {
			myStmt.close();
			myRs.close();
		}
		return encryptedPassword;

	}

	private User convertRowToUser(ResultSet myRs) throws Exception {

		int id = myRs.getInt("user_id");
		String lastName = myRs.getString("last_name");
		String firstName = myRs.getString("first_name");

		boolean admin = myRs.getBoolean("is_admin");
		User tempUser = new User(id, lastName, firstName, password, admin);

		return tempUser;
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

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return users.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public List<User> getUsers() {
		return users;
	}

	public List<User> getUsers(boolean admin, int userId) throws Exception {
		List<User> list = new ArrayList<User>();

		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			myStmt = myConn.createStatement();

			String sql = null;

			if (admin) {
				// get all users
				sql = "select * from users order by last_name";
			} else {
				// only the current user
				sql = "select * from users where user_id=" + userId + " order by last_name";
			}

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {

				User tempUser = convertRowToUser(myRs);
				list.add(tempUser);
			}
			return list;
		} finally {
			myStmt.close();
			myRs.close();
		}

	}

	@Override
	public Object getValueAt(int row, int col) {

		User tempUser = users.get(row);

		switch (col) {
		case LAST_NAME_COL:
			return tempUser.getLastName();
		case FIRST_NAME_COL:
			return tempUser.getFirstName();
		case IS_ADMIN_COL:
			return tempUser.isAdmin();
		case OBJECT_COL:
			return tempUser;
		default:
			return tempUser.getLastName();
		}
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
