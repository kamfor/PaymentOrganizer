/**
 * Created by kamil on 18.12.16.
 *
 *
 */

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class View extends JFrame{

    private JTabbedPane jtp;
    public paymentPanel panel1 = new paymentPanel();
    public agentPanel panel2 = new agentPanel();
    public subjectPanel panel3 = new subjectPanel();

    public View(){
        super();
        this.setSize(1920, 1080);
        jtp = new JTabbedPane();
        jtp.addTab("Payments",panel1);
        jtp.addTab("Agents",panel2);
        jtp.addTab("Subjects",panel3);
        add(jtp);
        jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        //tab1.setSize(this.getMaximumSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(jtp);
        this.setVisible(true);
    }
}

class paymentPanel extends JInternalFrame{ //only view elements, actions in other class

    protected JButton addRecord, removeRecord;
    protected JLabel errorMessage;
    protected JTextField tfType, tfValue, tfBeginDate, tfEndDate, tfOwner, tfSubject, tfDocument, tfNotes;
    protected JTable table;
    protected java.util.Date dateBeginDate, dateEndDate;

    /**
     * Constructor for the GUI class.
     * @param
     */
    public paymentPanel() {
        super();
        table = new JTable(Client.db.defaultTableModel);

        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        // Create a JScrollPane and add it to the center of the window
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // Set button values
        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        // Set the text field widths and values
        tfType = new JTextField("Type", 6);
        tfValue = new JTextField("Value", 8);
        tfBeginDate = new JTextField("Begin Date", 9);
        tfEndDate = new JTextField("End Date", 9);
        tfOwner = new JTextField("Owner", 8);
        tfSubject = new JTextField("Subject", 8);
        tfDocument = new JTextField("Document", 8);
        tfNotes = new JTextField("Notes", 8);

        // Create a new panel and add the text fields and add/remove buttons to it
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

        // Change settings and add the error message to the error panel
        errorMessage = new JLabel("");
        errorMessage.setForeground(Color.red);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorMessage);

        // set the input panel to the bottom and the error panel to the top
        this.add(inputPanel, BorderLayout.SOUTH);
        this.add(errorPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}

class agentPanel extends JInternalFrame{ //only view elements, actions in other class

    private JButton addRecord, removeRecord;
    private JLabel errorMessage;
    private JTextField tfName, tfSurname, tfAdress, tfEmail, tfPhone, tfWebsite, tfNotes;
    private JTable table;

    public agentPanel() {
        super();
        table = new JTable();

        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        // Create a new mouse listener and assign it to the table
        //ListenForMouse mouseListener = new ListenForMouse();
        //table.addMouseListener(mouseListener);

        // Create a JScrollPane and add it to the center of the window
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // Set button values
        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        // Add action listeners to the buttons to listen for clicks
        //ListenForAction actionListener = new ListenForAction();
        //addRecord.addActionListener(actionListener);
        //removeRecord.addActionListener(actionListener);

        // Set the text field widths and values
        tfName = new JTextField("Name", 6);
        tfSurname = new JTextField("Surname", 8);
        tfAdress = new JTextField("Adress", 9);
        tfEmail = new JTextField("Email", 9);
        tfPhone = new JTextField("Phone", 8);
        tfWebsite = new JTextField("Website", 8);
        tfNotes = new JTextField("Notes", 8);

        // Create a focus listener and add it to each text field to remove text when clicked on
        //ListenForFocus focusListener = new ListenForFocus();
        //tfType.addFocusListener(focusListener);
        // tfValue.addFocusListener(focusListener);
        //tfBeginDate.addFocusListener(focusListener);
        // tfEndDate.addFocusListener(focusListener);
        // tfOwner.addFocusListener(focusListener);
        // tfSubject.addFocusListener(focusListener);
        //  tfDocument.addFocusListener(focusListener);
        // tfNotes.addFocusListener(focusListener);

        // Create a new panel and add the text fields and add/remove buttons to it
        JPanel inputPanel = new JPanel();
        inputPanel.add(tfName);
        inputPanel.add(tfSurname);
        inputPanel.add(tfAdress);
        inputPanel.add(tfEmail);
        inputPanel.add(tfPhone);
        inputPanel.add(tfWebsite);
        inputPanel.add(tfNotes);

        inputPanel.add(addRecord);
        inputPanel.add(removeRecord);

        // Change settings and add the error message to the error panel
        errorMessage = new JLabel("");
        errorMessage.setForeground(Color.red);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorMessage);

        // set the input panel to the bottom and the error panel to the top
        this.add(inputPanel, BorderLayout.SOUTH);
        this.add(errorPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}

class subjectPanel extends JInternalFrame{ //only view elements, actions in other class

    private JButton addRecord, removeRecord;
    private JLabel errorMessage;
    private JTextField tfName, tfAdress, tfEmail, tfPhone, tfNotes;
    private JTable table;

    public subjectPanel() {
        super();
        table = new JTable();

        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        // Create a new mouse listener and assign it to the table
        //ListenForMouse mouseListener = new ListenForMouse();
        //table.addMouseListener(mouseListener);

        // Create a JScrollPane and add it to the center of the window
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // Set button values
        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        // Add action listeners to the buttons to listen for clicks
        //ListenForAction actionListener = new ListenForAction();
        //addRecord.addActionListener(actionListener);
        //removeRecord.addActionListener(actionListener);

        // Set the text field widths and values
        tfName = new JTextField("Name", 6);
        tfAdress = new JTextField("Adress", 9);
        tfEmail = new JTextField("Email", 9);
        tfPhone = new JTextField("Phone", 8);
        tfNotes = new JTextField("Notes", 8);

        // Create a focus listener and add it to each text field to remove text when clicked on
        //ListenForFocus focusListener = new ListenForFocus();
        //tfType.addFocusListener(focusListener);
        // tfValue.addFocusListener(focusListener);
        //tfBeginDate.addFocusListener(focusListener);
        // tfEndDate.addFocusListener(focusListener);
        // tfOwner.addFocusListener(focusListener);
        // tfSubject.addFocusListener(focusListener);
        //  tfDocument.addFocusListener(focusListener);
        // tfNotes.addFocusListener(focusListener);

        // Create a new panel and add the text fields and add/remove buttons to it
        JPanel inputPanel = new JPanel();
        inputPanel.add(tfName);
        inputPanel.add(tfAdress);
        inputPanel.add(tfEmail);
        inputPanel.add(tfPhone);
        inputPanel.add(tfNotes);

        inputPanel.add(addRecord);
        inputPanel.add(removeRecord);

        // Change settings and add the error message to the error panel
        errorMessage = new JLabel("");
        errorMessage.setForeground(Color.red);
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        errorPanel.add(errorMessage);

        // set the input panel to the bottom and the error panel to the top
        this.add(inputPanel, BorderLayout.SOUTH);
        this.add(errorPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}





