import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import classes.Payment;


/**
 * GUI class used to display and interact with information from
 *  database graphically.
 */
public class GUI extends JFrame {

    // Create buttons, labels, text fields, a new JTable, a Font, and the dates
    private JButton addRecord, removeRecord;
    private JLabel errorMessage;
    private JTextField tfType, tfValue, tfBeginDate, tfEndDate, tfOwner, tfSubject, tfDocument, tfNotes;
    private JTable table;
    private java.util.Date dateBeginDate, dateEndDate;
    private Font font;

    /**
     * Constructor for the GUI class.
     * @param db the database object used to manipulate the database.
     */
    public GUI(Database db) {
        super();
        table = new JTable(db.defaultTableModel);

        //font = new Font("Serif", Font.PLAIN, 18);
        //table.setFont(font);
        table.setRowHeight(table.getRowHeight() + 8);
        table.setAutoCreateRowSorter(true);

        // Create a new mouse listener and assign it to the table
        ListenForMouse mouseListener = new ListenForMouse();
        table.addMouseListener(mouseListener);

        // Create a JScrollPane and add it to the center of the window
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        // Set button values
        addRecord = new JButton("Add Record");
        removeRecord = new JButton("Remove Record");

        // Add action listeners to the buttons to listen for clicks
        ListenForAction actionListener = new ListenForAction();
        addRecord.addActionListener(actionListener);
        removeRecord.addActionListener(actionListener);

        // Set the text field widths and values
        tfType = new JTextField("Type", 6);
        tfValue = new JTextField("Value", 8);
        tfBeginDate= new JTextField("Begin Date", 9);
        tfEndDate = new JTextField("End Date", 9);
        tfOwner = new JTextField("Owner", 8);
        tfSubject = new JTextField("Subject", 8);
        tfDocument= new JTextField("Document", 8);
        tfNotes= new JTextField("Notes", 8);

        // Create a focus listener and add it to each text field to remove text when clicked on
        ListenForFocus focusListener = new ListenForFocus();
        tfType.addFocusListener(focusListener);
        tfValue.addFocusListener(focusListener);
        tfBeginDate.addFocusListener(focusListener);
        tfEndDate.addFocusListener(focusListener);
        tfOwner.addFocusListener(focusListener);
        tfSubject.addFocusListener(focusListener);
        tfDocument.addFocusListener(focusListener);
        tfNotes.addFocusListener(focusListener);

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

        // Center the ID column in the table
        DefaultTableCellRenderer centerColumns = new DefaultTableCellRenderer();
        centerColumns.setHorizontalAlignment(JLabel.CENTER);
        TableColumn tc = table.getColumn("ID");
        tc.setCellRenderer(centerColumns);
    }

    /**
     * ActionListener implementation to listen for actions such
     * as button clicks.
     * @author Casey Scarborough
     */
    private class ListenForAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == addRecord) { // If the user clicks Add Record, add the information into the database
                // Create variables to hold information to be inserted, and get the info from the text fields
                String Type, Value, BeginDate, EndDate, Owner, Subject, Document, Notes;
                Value = tfValue.getText();
                Type = tfType.getText();
                BeginDate = tfBeginDate.getText();
                EndDate = tfEndDate.getText();
                Owner = tfOwner.getText();
                Subject = tfSubject.getText();
                Document = tfDocument.getText();
                Notes = tfNotes.getText();


                // Check that the date matches the required format, if not display an error and return
                if(!BeginDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
                    errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
                    return;
                }

                // Check that the date matches the required format, if not display an error and return
                if(!EndDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
                    errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
                    return;
                }

                // Convert the date
                dateBeginDate = getADate(BeginDate);
                dateEndDate = getADate(EndDate);

                int paymentID = 0;
                Payment toinsert = new Payment(paymentID, Type, Value, dateBeginDate, dateEndDate, Integer.valueOf(Owner), Integer.valueOf(Subject), Document, Notes);

                // Attempt to insert the information into the database

                Customer.db.rowData.addElement(toinsert);

                Customer.db.defaultTableModel.addRow(toinsert.toVector()); // Add the row to the screen
                errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == removeRecord) {
                try { // If the user clicked remove record, delete from database and remove from table
                    Customer.db.defaultTableModel.removeRow(table.getSelectedRow());
                    Customer.db.rowData.remove(table.getSelectedRow());
                } catch(ArrayIndexOutOfBoundsException e1) {
                    System.out.println(e1.getMessage());
                    errorMessage.setText("To delete a customer, you must first select a row.");
                }
            }
        }
    }


    /**
     * FocusListener implementation used to listen for JTextFields
     * being focused on.
     */
    private class ListenForFocus implements FocusListener {
        public void focusGained(FocusEvent e) { // If a text field gains focus and has the default text, remove the text
            if(tfType.getText().equals("Type") && e.getSource() == tfType) {
                tfType.setText("");
            } else if(tfValue.getText().equals("Value") && e.getSource() == tfValue) {
                tfValue.setText("");
            } else if(tfBeginDate.getText().equals("Begin Date") && e.getSource() == tfBeginDate) {
                tfBeginDate.setText("");
            } else if(tfEndDate.getText().equals("End Date") && e.getSource() == tfEndDate) {
                tfEndDate.setText("");
            } else if(tfOwner.getText().equals("Owner") && e.getSource() == tfOwner) {
                tfOwner.setText("");
            } else if(tfSubject.getText().equals("Subject") && e.getSource() == tfSubject) {
                tfSubject.setText("");
            } else if(tfDocument.getText().equals("Document") && e.getSource() == tfDocument) {
                tfDocument.setText("");
            } else if(tfNotes.getText().equals("Notes") && e.getSource() == tfNotes) {
            tfNotes.setText("");
            }
        }

        public void focusLost(FocusEvent e) { // If the text field loses focus and is blank, set the default text back
            if(tfType.getText().equals("") && e.getSource() == tfType) {
                tfType.setText("Type");
            } else if(tfValue.getText().equals("") && e.getSource() == tfValue) {
                tfValue.setText("Value");
            } else if(tfBeginDate.getText().equals("") && e.getSource() == tfBeginDate) {
                tfBeginDate.setText("Begin Date");
            } else if(tfEndDate.getText().equals("") && e.getSource() == tfEndDate) {
                tfEndDate.setText("End Date");
            } else if(tfOwner.getText().equals("") && e.getSource() == tfOwner) {
                tfOwner.setText("Owner");
            } else if(tfSubject.getText().equals("") && e.getSource() == tfSubject) {
                tfSubject.setText("Subject");
            } else if(tfDocument.getText().equals("") && e.getSource() == tfDocument) {
                tfDocument.setText("Document");
            }else if(tfNotes.getText().equals("") && e.getSource() == tfNotes) {
                tfNotes.setText("Notes");
            }
        }

    }

    /**
     * ListenForMouse class that listens for mouse events on cells so that
     * they can be updated.
     *
     */
    private class ListenForMouse extends MouseAdapter {
        public void mouseReleased(MouseEvent mouseEvent) {
            // If the mouse is released and the click was a right click
            if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                // Create a dialog for the user to enter new data
                String value = JOptionPane.showInputDialog(null, "Enter Cell Value:");
                if(value != null) { // If they entered info, update the database
                    table.setValueAt(value, table.getSelectedRow(), table.getSelectedColumn());

                    String updateColumn;
                    updateColumn = Customer.db.defaultTableModel.getColumnName(table.getSelectedColumn());

                    switch(updateColumn) {
                        // if the column was date_registered, convert date update using a Date
                        case "Date_Registered":
                            dateBeginDate = getADate(value);
                            //Customer.db.rows.updateDate(updateColumn, (Date) dateBeginDate);
                            //Customer.db.rows.updateRow();
                            break;
                        default: // otherwise update using a String
                            //Customer.db.rows.updateString(updateColumn, value);
                            //Customer.db.rows.updateRow();
                            //when updating create new record and remove old one
                            break;
                    }
                }
            }
        }
    }

    /**
     * Method used to set the column widths of the JTable being displayed.
     * @param columns the Object array of column names.
     * @param widths the specified widths to set the columns to.
     */
    public void setColumnWidths(Object[] columns, int...widths) {
        TableColumn column;
        for(int i = 0; i < columns.length; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    /**
     * Used to set the message on the errorPanel.
     * @param message the message to display.
     */
    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    /**
     * Converts a date into one that can be recorded into the database.
     * @param dateRegistered the date that the user inputs in the Date Registered field.
     * @return the newly converted date.
     */
    public java.util.Date getADate(String dateRegistered) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            dateBeginDate = dateFormatter.parse(dateRegistered);
            dateBeginDate = new java.sql.Date(dateBeginDate.getTime());
        } catch (ParseException e1) {
            System.out.println(e1.getMessage());
            if(e1.getMessage().toString().startsWith("Unparseable date:")) {
                this.setErrorMessage("The date should be in the following format: YYYY-MM-DD");
            }
        }
        return dateBeginDate;
    }
}