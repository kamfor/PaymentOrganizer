/**
 * Created by kamil on 30.12.16.
 * zachowanie się tego co się wyświetla
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import classes.Agent;
import classes.Payment;
import classes.Subject;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.xerces.internal.util.EncodingMap;


public class Model {

    ListenForMouse mouseListener = new ListenForMouse();
    ListenForFocus focusListener = new ListenForFocus();
    ListenForPaymentAction paymentActionListener  =new ListenForPaymentAction();
    ListenForAgentAction agentActionListener  =new ListenForAgentAction();
    ListenForSubjectAction subjectActionListener  =new ListenForSubjectAction();

    //ListenForPaymentAction subjectActionListener  =new ListenForSubjectAction();

    static View gui;

    public Model(){
        //create

        gui = new View();

        gui.panel1.tfType.addFocusListener(focusListener);
        gui.panel1.tfValue.addFocusListener(focusListener);
        gui.panel1.tfBeginDate.addFocusListener(focusListener);
        gui.panel1.tfEndDate.addFocusListener(focusListener);
        gui.panel1.tfOwner.addFocusListener(focusListener);
        gui.panel1.tfSubject.addFocusListener(focusListener);
        gui.panel1.tfDocument.addFocusListener(focusListener);
        gui.panel1.tfNotes.addFocusListener(focusListener);
        gui.panel1.table.addMouseListener(mouseListener);
        gui.panel1.addRecord.addActionListener(paymentActionListener);
        gui.panel1.removeRecord.addActionListener(paymentActionListener);
        gui.panel1.refresh.addActionListener(paymentActionListener);
        gui.panel2.refresh.addActionListener(paymentActionListener);
        gui.panel3.refresh.addActionListener(paymentActionListener);


        gui.panel2.tfName.addFocusListener(focusListener);
        gui.panel2.tfPhone.addFocusListener(focusListener);
        gui.panel2.tfEmail.addFocusListener(focusListener);
        //gui.panel1.table.addMouseListener(mouseListener);
        gui.panel2.addRecord.addActionListener(agentActionListener);
        gui.panel2.removeRecord.addActionListener(agentActionListener);


        gui.panel3.tfName.addFocusListener(focusListener);
        gui.panel3.tfPhone.addFocusListener(focusListener);
        gui.panel3.tfEmail.addFocusListener(focusListener);
        gui.panel3.tfAddress.addFocusListener(focusListener);
        gui.panel3.tfNotes.addFocusListener(focusListener);
        //gui.panel1.table.addMouseListener(mouseListener);
        gui.panel3.addRecord.addActionListener(subjectActionListener);
        gui.panel3.removeRecord.addActionListener(subjectActionListener);

    }

    private class ListenForPaymentAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel1.addRecord) { // If the user clicks Add Record, add the information into the database

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

                //check Owner and subject and add waiting message dialog.

                int paymentID = 0;
                for(Payment item: Client.db.rowDataPayment){
                    if(item.id>paymentID)paymentID = item.id;
                }
                Payment toinsert = new Payment(paymentID+1,0, Type, Value, gui.panel1.dateBeginDate, gui.panel1.dateEndDate, Integer.valueOf(Owner), Integer.valueOf(Subject), Document, Notes);

                // Attempt to insert the information into the database

                Client.db.rowDataPayment.addElement(toinsert);
                Vector<Payment> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend, Boolean.FALSE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Client.db.defaultTableModelPayment.addRow(toinsert.toVector()); // Add the row to the screen
                gui.panel1.errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == gui.panel1.removeRecord) {

                Vector<Payment> tosend = new Vector<>();
                int removeIndex = gui.panel1.table.getSelectedRow();
                int removeId = Client.db.rowDataPayment.elementAt(removeIndex).id;
                tosend.addElement(Client.db.rowDataPayment.elementAt(removeIndex));
                try{// If the user clicked remove record, delete from database and remove from table

                    Client.db.sendObject(tosend, Boolean.TRUE);
                    for(int i =0; i<Client.db.rowDataPayment.size(); i++)if(Client.db.rowDataPayment.elementAt(i).id == removeId)Client.db.rowDataPayment.removeElementAt(i);

                    Client.db.defaultTableModelPayment.removeRow(removeIndex);
                } catch(ArrayIndexOutOfBoundsException | IOException e1) {
                    e1.printStackTrace();
                    System.out.println(e1.getMessage());
                    gui.panel1.errorMessage.setText("To delete an customer, you must first select a row.");
                }
            } else if (e.getSource() == gui.panel1.refresh||e.getSource() == gui.panel2.refresh||e.getSource() == gui.panel3.refresh) {
                System.out.println("Refreshed");
                String messag = "osz ty gnoju dawaj mi tu dane";
                Vector<String> information = new Vector<>();
                information.addElement(messag);
                try{
                    Client.db.sendObject(information, Boolean.FALSE);
                }catch (java.io.IOException e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    private class ListenForAgentAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel2.addRecord) { // If the user clicks Add Record, add the information into the database

                String Name, Phone, Email, Commission;
                Name = gui.panel2.tfName.getText();
                Phone = gui.panel2.tfPhone.getText();
                Email = gui.panel2.tfEmail.getText();
                Commission = "default";

                // Check dependences

                //check Owner and subject and add waiting message dialog.
                int agentID=0;
                for(Agent item: Client.db.rowDataAgent){
                    if(item.id>agentID)agentID = item.id;
                }
                Agent toinsert = new Agent(agentID+1,Name,Phone,Email,Commission);

                // Attempt to insert the information into the database
                Client.db.rowDataAgent.addElement(toinsert);
                Vector<Agent> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend, Boolean.FALSE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Client.db.defaultTableModelAgent.addRow(toinsert.toVector()); // Add the row to the screen
                gui.panel2.errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == gui.panel2.removeRecord) {

                System.out.println("Refreshed");

                Vector<Agent> tosend = new Vector<>();
                int removeIndex = gui.panel2.table.getSelectedRow();
                int removeId = Client.db.rowDataAgent.elementAt(removeIndex).id;
                tosend.addElement(Client.db.rowDataAgent.elementAt(removeIndex));
                try{// If the user clicked remove record, delete from database and remove from table

                    Client.db.sendObject(tosend, Boolean.TRUE);
                    for(int i =0; i<Client.db.rowDataAgent.size(); i++)if(Client.db.rowDataAgent.elementAt(i).id == removeId)Client.db.rowDataAgent.removeElementAt(i);

                    Client.db.defaultTableModelAgent.removeRow(removeIndex);
                } catch(ArrayIndexOutOfBoundsException | IOException e1) {
                    e1.printStackTrace();
                    System.out.println(e1.getMessage());
                    gui.panel2.errorMessage.setText("To delete an agent, you must first select a row.");
                }
            }
        }
    }

    private class ListenForSubjectAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel3.addRecord) { // If the user clicks Add Record, add the information into the database

                String Name, Phone, Email, Address, Bill, Notes;
                Name = gui.panel3.tfName.getText();
                Phone = gui.panel3.tfPhone.getText();
                Email = gui.panel3.tfEmail.getText();
                Address = gui.panel3.tfAddress.getText();
                Notes = gui.panel3.tfNotes.getText();
                Bill = "default";

                // Check dependences

                //check Owner and subject and add waiting message dialog.
                int subjectID=0;
                for(Subject item: Client.db.rowDataSubject){
                    if(item.id>subjectID)subjectID = item.id;
                }
                Subject toinsert = new Subject(subjectID+1,Name,Phone,Email,Address,Bill,Notes);

                // Attempt to insert the information into the database
                Client.db.rowDataSubject.addElement(toinsert);
                Vector<Subject> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend, Boolean.FALSE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Client.db.defaultTableModelSubject.addRow(toinsert.toVector()); // Add the row to the screen
                gui.panel3.errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == gui.panel3.removeRecord) {

                Vector<Subject> tosend = new Vector<>();
                int removeIndex = gui.panel3.table.getSelectedRow();
                int removeId = Client.db.rowDataSubject.elementAt(removeIndex).id;
                tosend.addElement(Client.db.rowDataSubject.elementAt(removeIndex));
                try{// If the user clicked remove record, delete from database and remove from table

                    Client.db.sendObject(tosend, Boolean.TRUE);
                    for(int i =0; i<Client.db.rowDataSubject.size(); i++)if(Client.db.rowDataSubject.elementAt(i).id == removeId)Client.db.rowDataSubject.removeElementAt(i);

                    Client.db.defaultTableModelSubject.removeRow(removeIndex);
                } catch(ArrayIndexOutOfBoundsException | IOException e1) {
                    e1.printStackTrace();
                    System.out.println(e1.getMessage());
                    gui.panel3.errorMessage.setText("To delete a Subject, you must first select a row.");
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
            } else if(gui.panel2.tfName.getText().equals("Name") && e.getSource() == gui.panel2.tfName) {
                gui.panel2.tfName.setText("");
            } else if(gui.panel2.tfPhone.getText().equals("Phone") && e.getSource() == gui.panel2.tfPhone) {
                gui.panel2.tfPhone.setText("");
            } else if(gui.panel2.tfEmail.getText().equals("Email") && e.getSource() == gui.panel2.tfEmail) {
                gui.panel2.tfEmail.setText("");
            } else if(gui.panel3.tfName.getText().equals("Name") && e.getSource() == gui.panel3.tfName) {
                gui.panel3.tfName.setText("");
            } else if(gui.panel3.tfPhone.getText().equals("Phone") && e.getSource() == gui.panel3.tfPhone) {
                gui.panel3.tfPhone.setText("");
            } else if(gui.panel3.tfEmail.getText().equals("Email") && e.getSource() == gui.panel3.tfEmail) {
                gui.panel3.tfEmail.setText("");
            } else if(gui.panel3.tfAddress.getText().equals("Address") && e.getSource() == gui.panel3.tfAddress) {
                gui.panel3.tfAddress.setText("");
            } else if(gui.panel3.tfNotes.getText().equals("Notes") && e.getSource() == gui.panel3.tfNotes) {
                gui.panel3.tfNotes.setText("");
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
            } else if(gui.panel1.tfNotes.getText().equals("") && e.getSource() == gui.panel1.tfNotes) {
                gui.panel1.tfNotes.setText("Notes");
            } else if(gui.panel2.tfName.getText().equals("") && e.getSource() == gui.panel2.tfName) {
                gui.panel2.tfName.setText("Name");
            } else if(gui.panel2.tfPhone.getText().equals("") && e.getSource() == gui.panel2.tfPhone) {
                gui.panel2.tfPhone.setText("Phone");
            } else if(gui.panel2.tfEmail.getText().equals("") && e.getSource() == gui.panel2.tfEmail) {
                gui.panel2.tfEmail.setText("Email");
            } else if(gui.panel3.tfName.getText().equals("") && e.getSource() == gui.panel3.tfName) {
                gui.panel3.tfName.setText("Name");
            } else if(gui.panel3.tfPhone.getText().equals("") && e.getSource() == gui.panel3.tfPhone) {
                gui.panel3.tfPhone.setText("Phone");
            } else if(gui.panel3.tfEmail.getText().equals("") && e.getSource() == gui.panel3.tfEmail) {
                gui.panel3.tfEmail.setText("Email");
            } else if(gui.panel3.tfAddress.getText().equals("") && e.getSource() == gui.panel3.tfAddress) {
                gui.panel3.tfAddress.setText("Address");
            } else if(gui.panel3.tfNotes.getText().equals("") && e.getSource() == gui.panel3.tfNotes) {
                gui.panel3.tfNotes.setText("Notes");
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
                    updateColumn = Client.db.defaultTableModelPayment.getColumnName(gui.panel1.table.getSelectedColumn());

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
