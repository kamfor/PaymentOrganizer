/**
 * Created by kamil on 30.12.16.
 * zachowanie się trgo co się wyświetla
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import classes.Payment;


public class Model {

    ListenForMouse mouseListener = new ListenForMouse();
    ListenForFocus focusListener = new ListenForFocus();
    ListenForAction actionListener  =new ListenForAction();

    static View gui;

    public Model(){
        //create

        gui = new View();

        // Create a focus listener and add it to each text field to remove text when clicked on
        gui.panel1.tfType.addFocusListener(focusListener);
        gui.panel1.tfValue.addFocusListener(focusListener);
        gui.panel1.tfBeginDate.addFocusListener(focusListener);
        gui.panel1.tfEndDate.addFocusListener(focusListener);
        gui.panel1.tfOwner.addFocusListener(focusListener);
        gui.panel1.tfSubject.addFocusListener(focusListener);
        gui.panel1.tfDocument.addFocusListener(focusListener);
        gui.panel1.tfNotes.addFocusListener(focusListener);
        // Create a new mouse listener and assign it to the table
        gui.panel1.table.addMouseListener(mouseListener);
        // Add action listeners to the buttons to listen for clicks
        gui.panel1.addRecord.addActionListener(actionListener);
        gui.panel1. removeRecord.addActionListener(actionListener);
    }

    private class ListenForAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel1.addRecord) { // If the user clicks Add Record, add the information into the database
                // Create variables to hold information to be inserted, and get the info from the text fields
                String Type, Value, BeginDate, EndDate, Owner, Subject, Document, Notes;
                Value = gui.panel1.tfValue.getText();
                Type = gui.panel1.tfType.getText();
                BeginDate = gui.panel1.tfBeginDate.getText();
                EndDate = gui.panel1.tfEndDate.getText();
                Owner = gui.panel1.tfOwner.getText();
                Subject = gui.panel1.tfSubject.getText();
                Document = gui.panel1.tfDocument.getText();
                Notes = gui.panel1.tfNotes.getText();


                // Check that the date matches the required format, if not display an error and return
                if(!BeginDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
                    gui.panel1.errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
                    return;
                }

                // Check that the date matches the required format, if not display an error and return
                if(!EndDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
                    gui.panel1.errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
                    return;
                }

                // Convert the date
                gui.panel1.dateBeginDate = getADate(BeginDate);
                gui.panel1.dateEndDate = getADate(EndDate);

                int paymentID = 0;
                Payment toinsert = new Payment(paymentID, Type, Value, gui.panel1.dateBeginDate, gui.panel1.dateEndDate, Integer.valueOf(Owner), Integer.valueOf(Subject), Document, Notes);

                // Attempt to insert the information into the database

                Client.db.rowData.addElement(toinsert);
                Vector<Payment> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend);
                    //Customer.db.rows.last(); //try to get id from sqlquery
                    //customerID = Customer.db.rows.getInt(1);
                    //Object[] customer = {customerID, firstName, lastName, phoneNumber, emailAddress, city, state, sqlDateRegistered};
                    //Customer.db.defaultTableModel.addRow(customer); // Add the row to the screen

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Client.db.defaultTableModel.addRow(toinsert.toVector()); // Add the row to the screen
                gui.panel1.errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == gui.panel1.removeRecord) {
                try { // If the user clicked remove record, delete from database and remove from table
                    Client.db.defaultTableModel.removeRow(gui.panel1.table.getSelectedRow());
                    Client.db.rowData.remove(gui.panel1.table.getSelectedRow());
                } catch(ArrayIndexOutOfBoundsException e1) {
                    System.out.println(e1.getMessage());
                    gui.panel1.errorMessage.setText("To delete a customer, you must first select a row.");
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
            if(gui.panel1.tfType.getText().equals("Type") && e.getSource() == gui.panel1.tfType) {
                gui.panel1.tfType.setText("");
            } else if(gui.panel1.tfValue.getText().equals("Value") && e.getSource() == gui.panel1.tfValue) {
                gui.panel1.tfValue.setText("");
            } else if(gui.panel1.tfBeginDate.getText().equals("Begin Date") && e.getSource() == gui.panel1.tfBeginDate) {
                gui.panel1.tfBeginDate.setText("");
            } else if(gui.panel1.tfEndDate.getText().equals("End Date") && e.getSource() == gui.panel1.tfEndDate) {
                gui.panel1.tfEndDate.setText("");
            } else if(gui.panel1.tfOwner.getText().equals("Owner") && e.getSource() == gui.panel1.tfOwner) {
                gui.panel1.tfOwner.setText("");
            } else if(gui.panel1.tfSubject.getText().equals("Subject") && e.getSource() == gui.panel1.tfSubject) {
                gui.panel1.tfSubject.setText("");
            } else if(gui.panel1.tfDocument.getText().equals("Document") && e.getSource() == gui.panel1.tfDocument) {
                gui.panel1.tfDocument.setText("");
            } else if(gui.panel1.tfNotes.getText().equals("Notes") && e.getSource() == gui.panel1.tfNotes) {
                gui.panel1.tfNotes.setText("");
            }
        }

        public void focusLost(FocusEvent e) { // If the text field loses focus and is blank, set the default text back
            if(gui.panel1.tfType.getText().equals("") && e.getSource() == gui.panel1.tfType) {
                gui.panel1.tfType.setText("Type");
            } else if(gui.panel1.tfValue.getText().equals("") && e.getSource() == gui.panel1.tfValue) {
                gui.panel1.tfValue.setText("Value");
            } else if(gui.panel1.tfBeginDate.getText().equals("") && e.getSource() == gui.panel1.tfBeginDate) {
                gui.panel1.tfBeginDate.setText("Begin Date");
            } else if(gui.panel1.tfEndDate.getText().equals("") && e.getSource() == gui.panel1.tfEndDate) {
                gui.panel1.tfEndDate.setText("End Date");
            } else if(gui.panel1.tfOwner.getText().equals("") && e.getSource() == gui.panel1.tfOwner) {
                gui.panel1.tfOwner.setText("Owner");
            } else if(gui.panel1.tfSubject.getText().equals("") && e.getSource() == gui.panel1.tfSubject) {
                gui.panel1.tfSubject.setText("Subject");
            } else if(gui.panel1.tfDocument.getText().equals("") && e.getSource() == gui.panel1.tfDocument) {
                gui.panel1.tfDocument.setText("Document");
            }else if(gui.panel1.tfNotes.getText().equals("") && e.getSource() == gui.panel1.tfNotes) {
                gui.panel1.tfNotes.setText("Notes");
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
                    gui.panel1.table.setValueAt(value, gui.panel1.table.getSelectedRow(), gui.panel1.table.getSelectedColumn());

                    String updateColumn;
                    updateColumn = Client.db.defaultTableModel.getColumnName(gui.panel1.table.getSelectedColumn());

                    switch(updateColumn) {
                        // if the column was date_registered, convert date update using a Date
                        case "Date_Registered":
                            gui.panel1.dateBeginDate = getADate(value);
                            //Client.db.rows.updateDate(updateColumn, (Date) dateBeginDate);
                            //Client.db.rows.updateRow();
                            break;
                        default: // otherwise update using a String
                            //Client.db.rows.updateString(updateColumn, value);
                            //Client.db.rows.updateRow();
                            //when updating create new record and remove old one
                            break;
                    }
                }
            }
        }
    }

    public void setColumnWidths(int...widths) {
        TableColumn column;
        for(int i = 0; i < 8; i++) {
            column = gui.panel1.table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }


    public void setErrorMessage(String message) {
        gui.panel1.errorMessage.setText(message);
    }


    public java.util.Date getADate(String dateRegistered) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            gui.panel1.dateBeginDate = dateFormatter.parse(dateRegistered);
            gui.panel1.dateBeginDate = new java.sql.Date(gui.panel1.dateBeginDate.getTime());
        } catch (ParseException e1) {
            System.out.println(e1.getMessage());
            if(e1.getMessage().toString().startsWith("Unparseable date:")) {
                this.setErrorMessage("The date should be in the following format: YYYY-MM-DD");
            }
        }
        return gui.panel1.dateBeginDate;
    }
}
