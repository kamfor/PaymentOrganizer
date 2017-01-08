package client;
/**
 * Created by kamil on 18.12.16.
 * MVC view class
 */

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

public class View extends JFrame{

    private JTabbedPane jtp;
    public paymentPanel panel1 = new paymentPanel();
    public agentPanel panel2 = new agentPanel();
    public subjectPanel panel3 = new subjectPanel();

    public View(){
        super();
        setTitle("Payment client.Database");
        this.setSize(1200, 600);
        jtp = new JTabbedPane();
        jtp.addTab("Payments",panel1);
        jtp.addTab("Agents",panel2);
        jtp.addTab("Subjects",panel3);
        add(jtp);
        jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(jtp);
        this.setVisible(true);
    }
}

/**
 * Payment frame panel
 * */
class paymentPanel extends JInternalFrame{

    protected JButton addRecord, removeRecord;
    protected JLabel errorMessage;
    protected JTextField tfType, tfValue, tfBeginDate, tfEndDate, tfOwner, tfSubject, tfDocument, tfNotes;
    protected JTable table;
    protected java.util.Date dateBeginDate, dateEndDate;

    public paymentPanel() {
        super();
        table = new JTable(Client.db.defaultTableModelPayment){
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };

        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        tfType = new JTextField("Type", 8);
        tfValue = new JTextField("Value", 6);
        tfBeginDate = new JTextField("Begin Date", 9);
        tfEndDate = new JTextField("End Date", 9);
        tfOwner = new JTextField("Owner", 5);
        tfSubject = new JTextField("Subject", 5);
        tfDocument = new JTextField("Document", 8);
        tfNotes = new JTextField("Notes", 20);

        JPanel inputPanel = new JPanel();
        inputPanel.add(tfType);
        inputPanel.add(tfValue);
        inputPanel.add(tfBeginDate);
        inputPanel.add(tfEndDate);
        inputPanel.add(tfOwner);
        inputPanel.add(tfSubject);
        inputPanel.add(tfDocument);
        inputPanel.add(tfNotes);
        inputPanel.add(addRecord);
        inputPanel.add(removeRecord);

        errorMessage = new JLabel("");
        errorMessage.setForeground(Color.red);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorMessage);

        this.add(inputPanel, BorderLayout.SOUTH);
        this.add(errorPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}
/**
 * Agent frame panel
 */
class agentPanel extends JInternalFrame{
    protected JButton addRecord, removeRecord;
    protected JLabel errorMessage;
    protected JTextField tfName, tfPhone, tfEmail;
    protected JTable table;

    public agentPanel() {
        super();
        table = new JTable(Client.db.defaultTableModelAgent){
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2 || column == 3;
            }
        };

        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        tfName = new JTextField("Name", 10);
        tfEmail = new JTextField("Email", 9);
        tfPhone = new JTextField("Phone", 9);

        JPanel inputPanel = new JPanel();
        inputPanel.add(tfName);
        inputPanel.add(tfEmail);
        inputPanel.add(tfPhone);
        inputPanel.add(addRecord);
        inputPanel.add(removeRecord);

        errorMessage = new JLabel("");
        errorMessage.setForeground(Color.red);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorMessage);

        this.add(inputPanel, BorderLayout.SOUTH);
        this.add(errorPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}
/**
 * Subject frame panel
 */
class subjectPanel extends JInternalFrame{
    protected JButton addRecord, removeRecord;
    protected JLabel errorMessage;
    protected JTextField tfName, tfPhone, tfEmail, tfAddress, tfNotes;
    protected JTable table;

    public subjectPanel() {
        super();
        table = new JTable(Client.db.defaultTableModelSubject){
            public boolean isCellEditable(int row, int column) {
                return column ==1 || column == 2 || column == 3 || column == 4 || column == 6;
            }
        };

        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        tfName = new JTextField("Name", 10);
        tfPhone = new JTextField("Phone", 9);
        tfEmail = new JTextField("Email", 9);
        tfAddress = new JTextField("Address", 20);
        tfNotes = new JTextField("Notes", 20);

        JPanel inputPanel = new JPanel();
        inputPanel.add(tfName);
        inputPanel.add(tfPhone);
        inputPanel.add(tfEmail);
        inputPanel.add(tfAddress);
        inputPanel.add(tfNotes);
        inputPanel.add(addRecord);
        inputPanel.add(removeRecord);

        errorMessage = new JLabel("");
        errorMessage.setForeground(Color.red);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorMessage);

        this.add(inputPanel, BorderLayout.SOUTH);
        this.add(errorPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}





