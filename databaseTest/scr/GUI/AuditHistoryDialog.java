package GUI;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import databaseTest.AuditHistory;
import databaseTest.Employee;
import databaseTest.Manager;
import databaseTest.Department;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class AuditHistoryDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JLabel auditHistoryLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AuditHistoryDialog dialog = new AuditHistoryDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void populate(Employee tempEmployee, List<AuditHistory> auditHistoryList) {
		
		auditHistoryLabel.setText(tempEmployee.getFirstName() + " " + tempEmployee.getLastName());
		
		AuditHistory model = new AuditHistory(auditHistoryList);
		
		table.setModel(model);
		
		// set up table renderer for date
		TableCellRenderer tableCellRenderer = new DateTimeCellRenderer();
		table.getColumnModel().getColumn(AuditHistory.DATE_TIME_COL).setCellRenderer(tableCellRenderer);		
	}
	
	public void populate(Object tempObject, List<AuditHistory> auditHistoryList, String type) {
		
		if(type == "employee"){
			auditHistoryLabel.setText("employee: " + (String)((Employee) tempObject.getClass().cast(tempObject)).getFirstName() + " " +
												(String)((Employee) tempObject.getClass().cast(tempObject)).getLastName());
			
			AuditHistory model = new AuditHistory(auditHistoryList);
			
			table.setModel(model);
			
			// set up table renderer for date
			TableCellRenderer tableCellRenderer = new DateTimeCellRenderer();
			table.getColumnModel().getColumn(AuditHistory.DATE_TIME_COL).setCellRenderer(tableCellRenderer);
		}
		if(type == "manager"){
			auditHistoryLabel.setText("manager: " +(String)((Manager) tempObject.getClass().cast(tempObject)).getFirstName() + " " +
												(String)((Manager) tempObject.getClass().cast(tempObject)).getLastName());
			
			AuditHistory model = new AuditHistory(auditHistoryList);
			
			table.setModel(model);
			
			// set up table renderer for date
			TableCellRenderer tableCellRenderer = new DateTimeCellRenderer();
			table.getColumnModel().getColumn(AuditHistory.DATE_TIME_COL).setCellRenderer(tableCellRenderer);
		}
		if(type == "department"){
			auditHistoryLabel.setText((String)((Department) tempObject.getClass().cast(tempObject)).getDeptName() + " department" );
			
			AuditHistory model = new AuditHistory(auditHistoryList);
			
			table.setModel(model);
			
			// set up table renderer for date
			TableCellRenderer tableCellRenderer = new DateTimeCellRenderer();
			table.getColumnModel().getColumn(AuditHistory.DATE_TIME_COL).setCellRenderer(tableCellRenderer);
		}
	}

	/**
	 * Create the dialog.
	 */
	public AuditHistoryDialog() {
		setTitle("History");
		setBounds(100, 100, 751, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 250));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblAuditHistoryFor = new JLabel("Audit History for");
			lblAuditHistoryFor.setBounds(10, 11, 94, 14);
			contentPanel.add(lblAuditHistoryFor);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 715, 181);
		contentPanel.add(scrollPane);
		{
			table = new JTable();
			scrollPane.setViewportView(table);
		}
		{
			auditHistoryLabel = new JLabel("");
			auditHistoryLabel.setBounds(114, 11, 207, 14);
			contentPanel.add(auditHistoryLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(255, 250, 250));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	private final class DateTimeCellRenderer extends DefaultTableCellRenderer {
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/yy hh:mm:ss");

		public Component getTableCellRendererComponent(JTable table,
		        Object value, boolean isSelected, boolean hasFocus,
		        int row, int column) {
		    if( value instanceof Date) {
		        value = f.format(value);
		    }
		    return super.getTableCellRendererComponent(table, value, isSelected,
		            hasFocus, row, column);
		}
	}
}
