package client; /**
 * Created by kamil on 30.12.16.
 * zachowanie się tego co się wyświetla
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.*;
import classes.Agent;
import classes.Payment;
import classes.Subject;
import client.Client;

/**
 * Klasa Model we wzorcu MVC odpowiadjąca za obsługę zdarzeń w GUI
 */
public class Model {

    ListenForMouse mouseListener = new ListenForMouse();
    ListenForFocus focusListener = new ListenForFocus();
    ListenForPaymentAction paymentActionListener  =new ListenForPaymentAction();
    ListenForAgentAction agentActionListener  =new ListenForAgentAction();
    ListenForSubjectAction subjectActionListener  =new ListenForSubjectAction();
    ListenForClickPayment paymentClickListener = new ListenForClickPayment();
    ListenForClickAgent agentClickListener = new ListenForClickAgent();
    ListenForClickSubject subjectClickListener = new ListenForClickSubject();

    static View gui;

    /**
     * Konstruktor klasy
     */
    public Model(){
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

        gui.panel2.tfName.addFocusListener(focusListener);
        gui.panel2.tfPhone.addFocusListener(focusListener);
        gui.panel2.tfEmail.addFocusListener(focusListener);
        gui.panel2.table.addMouseListener(mouseListener);
        gui.panel2.addRecord.addActionListener(agentActionListener);
        gui.panel2.removeRecord.addActionListener(agentActionListener);

        gui.panel3.tfName.addFocusListener(focusListener);
        gui.panel3.tfPhone.addFocusListener(focusListener);
        gui.panel3.tfEmail.addFocusListener(focusListener);
        gui.panel3.tfAddress.addFocusListener(focusListener);
        gui.panel3.tfNotes.addFocusListener(focusListener);
        gui.panel3.table.addMouseListener(mouseListener);
        gui.panel3.addRecord.addActionListener(subjectActionListener);
        gui.panel3.removeRecord.addActionListener(subjectActionListener);

        gui.panel1.table.getModel().addTableModelListener(paymentClickListener);
        gui.panel2.table.getModel().addTableModelListener(agentClickListener);
        gui.panel3.table.getModel().addTableModelListener(subjectClickListener);

        gui.panel1.errorMessage.setText("");
    }

    /**
     * Klasa Listener obsługująca zdarzenia przycisków w panelu Payments
     */
    private class ListenForPaymentAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel1.addRecord) { // If the user clicks Add Record, add the information into the database

                Boolean foundAgent = Boolean.FALSE;
                Boolean foundSubject = Boolean.FALSE;
                String Type, Value, BeginDate, EndDate, Owner, Subject, Document, Notes;
                Value = gui.panel1.tfValue.getText();
                Type = gui.panel1.tfType.getText();
                BeginDate = gui.panel1.tfBeginDate.getText();
                EndDate = gui.panel1.tfEndDate.getText();
                Owner = gui.panel1.tfOwner.getText();
                Subject = gui.panel1.tfSubject.getText();
                Document = gui.panel1.tfDocument.getText();
                Notes = gui.panel1.tfNotes.getText();
                Vector<Agent> agentToUpdate = new Vector<>();
                Vector<Subject> subjectToUpdate = new Vector<>();
                Float numberValue;


                try{
                    numberValue = Float.valueOf(Value);
                }
                catch(NumberFormatException e1){
                    gui.panel1.errorMessage.setText("Incorrect Value");
                    return;
                }

                if(!BeginDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
                    gui.panel1.errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
                    return;
                }

                if(!EndDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
                    gui.panel1.errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
                    return;
                }

                try{
                    gui.panel1.dateBeginDate = getADate(BeginDate);
                    gui.panel1.dateEndDate = getADate(EndDate);
                }
                catch(ParseException e1){
                    gui.panel1.errorMessage.setText("Incorrect Date");
                }

                for(Agent item: Client.db.rowDataAgent){
                    if(item.id==Integer.valueOf(Owner)){
                        foundAgent = Boolean.TRUE;
                        item.commission +=numberValue;
                        agentToUpdate.addElement(item);
                    }
                }
                if(!foundAgent){
                    gui.panel1.errorMessage.setText("Owner doesn't exist");
                    return;
                }
                for(Subject item: Client.db.rowDataSubject){
                    if(item.id==Integer.valueOf(Subject)){
                        foundSubject = Boolean.TRUE;
                        item.bill +=numberValue;
                        subjectToUpdate.addElement(item);
                    }
                }
                if(!foundSubject){
                    gui.panel1.errorMessage.setText("Subject doesn't exist");
                    return;
                }

                int paymentID = 0;
                for(Payment item: Client.db.rowDataPayment){
                    if(item.id>paymentID)paymentID = item.id;
                }
                Payment toinsert = new Payment(paymentID+1,Boolean.FALSE, Type, numberValue, gui.panel1.dateBeginDate, gui.panel1.dateEndDate, Integer.valueOf(Owner), Integer.valueOf(Subject), Document, Notes);

                Vector<Payment> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend, new Integer(1));
                    if(foundAgent)Client.db.sendObject(agentToUpdate, new Integer(2));
                    if(foundSubject)Client.db.sendObject(subjectToUpdate, new Integer(2));
                } catch (IOException e1) {
                    gui.panel1.errorMessage.setText("Error while sending data to server");
                }
                gui.panel1.errorMessage.setText("");

            } else if (e.getSource() == gui.panel1.removeRecord) {

                Vector<Agent> agentToUpdate = new Vector<>();
                Vector<Subject> subjectToUpdate = new Vector<>();
                Vector<Payment> tosend = new Vector<>();
                int removeIndex = gui.panel1.table.getSelectedRow();
                tosend.addElement(Client.db.rowDataPayment.elementAt(removeIndex));
                for(Agent item: Client.db.rowDataAgent){
                    if(item.id==tosend.elementAt(0).owner_id){
                        item.commission -=tosend.elementAt(0).value;
                        agentToUpdate.addElement(item);
                    }
                }
                for(Subject item: Client.db.rowDataSubject){
                    if(item.id==tosend.elementAt(0).subject_id){
                        item.bill -=tosend.elementAt(0).value;
                        subjectToUpdate.addElement(item);
                    }
                }

                try{
                    Client.db.sendObject(tosend, new Integer(0));
                    Client.db.sendObject(agentToUpdate, new Integer(2));
                    Client.db.sendObject(subjectToUpdate, new Integer(2));
                } catch(ArrayIndexOutOfBoundsException | IOException e1) {
                    gui.panel1.errorMessage.setText("To delete an customer, you must first select a row.");
                }
            }
        }
    }

    /**
     * Klasa Listener obsługująca zdarzenia przycisków w panelu Agents
     */
    private class ListenForAgentAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel2.addRecord) { // If the user clicks Add Record, add the information into the database

                String Name, Phone, Email;
                Float Commission;
                Name = gui.panel2.tfName.getText();
                Phone = gui.panel2.tfPhone.getText();
                Email = gui.panel2.tfEmail.getText();
                Commission = new Float(0.0);

                // Check dependences

                int agentID=0;
                for(Agent item: Client.db.rowDataAgent){
                    if(item.id>agentID)agentID = item.id;
                }
                Agent toinsert = new Agent(agentID+1,Name,Phone,Email,Commission);
                Vector<Agent> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend, new Integer(1));
                } catch (IOException e1) {
                    gui.panel1.errorMessage.setText("Error while sending to server");
                }

                gui.panel2.errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == gui.panel2.removeRecord) {


                Vector<Agent> tosend = new Vector<>();
                int removeIndex = gui.panel2.table.getSelectedRow();
                tosend.addElement(Client.db.rowDataAgent.elementAt(removeIndex));
                for(Payment item: Client.db.rowDataPayment){
                    if(item.owner_id == tosend.elementAt(0).id){
                        gui.panel2.errorMessage.setText("Agent exist in Payment table");
                        return;
                    }
                }
                try{
                    Client.db.sendObject(tosend, new Integer(0));
                } catch(ArrayIndexOutOfBoundsException | IOException e1) {
                    gui.panel2.errorMessage.setText("To delete an agent, you must first select a row.");
                    return;
                }
            }
        }
    }

    /**
     * Klasa Listener obsługująca zdarzenia przycisków w panelu Subjects
     */
    private class ListenForSubjectAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel3.addRecord) { // If the user clicks Add Record, add the information into the database

                String Name, Phone, Email, Address, Notes;
                Float Bill;
                Name = gui.panel3.tfName.getText();
                Phone = gui.panel3.tfPhone.getText();
                Email = gui.panel3.tfEmail.getText();
                Address = gui.panel3.tfAddress.getText();
                Notes = gui.panel3.tfNotes.getText();
                Bill = new Float(0.0);

                int subjectID=0;
                for(Subject item: Client.db.rowDataSubject){
                    if(item.id>subjectID)subjectID = item.id;
                }
                Subject toinsert = new Subject(subjectID+1,Name,Phone,Email,Address,Bill,Notes);
                Vector<Subject> tosend = new Vector<>();
                tosend.addElement(toinsert);
                try {
                    Client.db.sendObject(tosend, new Integer(1));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                gui.panel3.errorMessage.setText(""); // Remove the error message if one was displayed

            } else if (e.getSource() == gui.panel3.removeRecord) {

                Vector<Subject> tosend = new Vector<>();
                int removeIndex = gui.panel3.table.getSelectedRow();
                tosend.addElement(Client.db.rowDataSubject.elementAt(removeIndex));
                for(Payment item: Client.db.rowDataPayment){
                    if(item.subject_id == tosend.elementAt(0).id){
                        gui.panel3.errorMessage.setText("Subject exist in Payment table");
                        return;
                    }
                }
                try{
                    Client.db.sendObject(tosend, new Integer(0));
                } catch(ArrayIndexOutOfBoundsException | IOException e1) {
                    e1.printStackTrace();
                    gui.panel3.errorMessage.setText("To delete a Subject, you must first select a row.");
                }
            }
        }
    }

    /**
     * Klasa Listener obsługująca zdarzenia pól tekstowych na wszystkich panelach
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
     * Klasa Listener obsługująca zdarzenia myszki
     * @deprecated
     */
    private class ListenForMouse extends MouseAdapter {
        public void mouseReleased(MouseEvent mouseEvent) {
            // If the mouse is released and the click was a right click
            if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                // Create a dialog for the user to enter new data
                String value = JOptionPane.showInputDialog(null, "Enter Cell Value:");
                if(value != null) { // If they entered info, update the database

                }
            }
        }
    }

    /**
     * Klasa Listener obsługująca zdarzenia edycji pól tabeli w panelu Payments
     */
    private class ListenForClickPayment implements TableModelListener{
        public void tableChanged(TableModelEvent e) {

            if(e.getType()==TableModelEvent.UPDATE){

                Vector<Agent> agentToUpdate = new Vector<>();
                Vector<Subject> subjectToUpdate = new Vector<>();
                Float numberValue;
                Object field = gui.panel1.table.getValueAt(e.getLastRow(),e.getColumn());


                Vector<Payment> tosend = new Vector<>();
                tosend.addElement(Client.db.rowDataPayment.elementAt(gui.panel1.table.getSelectedRow()));

                Boolean found;
                Boolean valueChanged = Boolean.FALSE;
                Boolean isUpdated = Boolean.FALSE;

                String updateColumn;
                updateColumn = Client.db.defaultTableModelPayment.getColumnName(gui.panel1.table.getSelectedColumn());

                switch(updateColumn) {
                    case "Type":
                        tosend.elementAt(0).type = (String)field;
                        isUpdated = Boolean.TRUE;
                        break;
                    case "Accepted":
                        tosend.elementAt(0).accepted = (Boolean)field;
                        isUpdated = Boolean.TRUE;
                        break;
                    case "Value":
                        try{
                            numberValue = Float.valueOf((String)field);
                        }
                        catch(NumberFormatException e1){
                            gui.panel1.errorMessage.setText("Incorrect Value");
                            return;
                        }
                        for(Agent item: Client.db.rowDataAgent){
                            if(item.id==tosend.elementAt(0).owner_id){
                                item.commission +=(numberValue-tosend.elementAt(0).value);
                                agentToUpdate.addElement(item);
                            }
                        }
                        for(Subject item: Client.db.rowDataSubject){
                            if(item.id==tosend.elementAt(0).subject_id){
                                item.bill +=(numberValue-tosend.elementAt(0).value);
                                subjectToUpdate.addElement(item);
                            }
                        }
                        tosend.elementAt(0).value = numberValue;
                        isUpdated = Boolean.TRUE;
                        valueChanged = Boolean.TRUE;
                        break;
                    case "Begin Date":
                        try{
                            tosend.elementAt(0).begin_date = getADate((String)field);
                            isUpdated = Boolean.TRUE;
                        }catch(ParseException e2){
                            gui.panel1.table.setValueAt(tosend.elementAt(0).begin_date,e.getLastRow(),e.getColumn());
                            gui.panel1.errorMessage.setText("Unparseable Date");
                        }
                        break;
                    case "End Date":
                        try{
                            tosend.elementAt(0).end_date = getADate((String)field);
                            isUpdated = Boolean.TRUE;
                        }catch(ParseException e2){
                            gui.panel1.table.setValueAt(tosend.elementAt(0).end_date,e.getLastRow(),e.getColumn());
                            gui.panel1.errorMessage.setText("Unparseable Date");
                        }
                        break;
                    case "Owner":
                        found = Boolean.FALSE;
                        for(Agent item: Client.db.rowDataAgent){
                            if(item.id==Integer.valueOf((String)field)){
                                tosend.elementAt(0).owner_id = Integer.valueOf((String)field);
                                found = Boolean.TRUE;
                                gui.panel1.errorMessage.setText("");
                                isUpdated = Boolean.TRUE;
                            }
                        }
                        if(!found){
                            gui.panel1.errorMessage.setText("Owner doesn't exist");
                            gui.panel1.table.setValueAt(tosend.elementAt(0).owner_id,e.getLastRow(),e.getColumn());
                            return;
                        }
                        break;
                    case "Subject":
                        found = Boolean.FALSE;
                        for(Subject item: Client.db.rowDataSubject){
                            if(item.id==Integer.valueOf((String)field)){
                                tosend.elementAt(0).subject_id = Integer.valueOf((String)field);
                                found = Boolean.TRUE;
                                gui.panel1.errorMessage.setText("");
                                isUpdated = Boolean.TRUE;
                            }
                        }
                        if(!found){
                            gui.panel1.errorMessage.setText("Subject doesn't exist");
                            gui.panel1.table.setValueAt(tosend.elementAt(0).subject_id,e.getLastRow(),e.getColumn());
                            return;
                        }
                        break;
                    case "Document":
                        tosend.elementAt(0).document_name = (String)field;
                        isUpdated = Boolean.TRUE;
                        break;
                    case "Notes":
                        tosend.elementAt(0).notes = (String)field;
                        isUpdated = Boolean.TRUE;
                        break;
                }
                gui.panel1.errorMessage.setText("");
                if(isUpdated){
                    try{
                        Client.db.sendObject(tosend, new Integer(2));
                        if(valueChanged){
                            Client.db.sendObject(agentToUpdate, new Integer(2));
                            Client.db.sendObject(subjectToUpdate, new Integer(2));
                        }
                    }catch(IOException e1){
                        gui.panel1.errorMessage.setText("Error while sending to server");
                    }
                }
            }
        }
    }
    /**
     * Klasa Listener obsługująca zdarzenia edycji pól tabeli w panelu Agents
     */
    private class ListenForClickAgent implements TableModelListener{
        public void tableChanged(TableModelEvent e) {

            if(e.getType()==TableModelEvent.UPDATE){
                System.out.println(gui.panel2.table.getValueAt(e.getLastRow(),e.getColumn()));
                Object field = gui.panel2.table.getValueAt(e.getLastRow(),e.getColumn());


                Vector<Agent> tosend = new Vector<>();
                tosend.addElement(Client.db.rowDataAgent.elementAt(gui.panel2.table.getSelectedRow()));

                String updateColumn;
                updateColumn = Client.db.defaultTableModelAgent.getColumnName(gui.panel2.table.getSelectedColumn());

                Boolean isupdated = false;

                switch(updateColumn) {
                    case "Name":
                        tosend.elementAt(0).name = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                    case "Phone":
                        tosend.elementAt(0).phone = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                    case "Email":
                        tosend.elementAt(0).email = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                }
                if(isupdated==Boolean.TRUE){
                    try{
                        Client.db.sendObject(tosend, new Integer(2));
                    }catch(IOException e1){
                        gui.panel1.errorMessage.setText("Error while sending to server");
                    }
                }
            }
        }
    }

    /**
     * Klasa Listener obsługująca zdarzenia edycji pól tabeli w panelu Subjects
     */
    private class ListenForClickSubject implements TableModelListener{
        public void tableChanged(TableModelEvent e) {

            if(e.getType()==TableModelEvent.UPDATE){
                Object field = gui.panel3.table.getValueAt(e.getLastRow(),e.getColumn());


                Vector<Subject> tosend = new Vector<>();
                tosend.addElement(Client.db.rowDataSubject.elementAt(gui.panel3.table.getSelectedRow()));

                String updateColumn;
                updateColumn = Client.db.defaultTableModelAgent.getColumnName(gui.panel3.table.getSelectedColumn());

                Boolean isupdated = false;

                switch(updateColumn) {
                    case "Name":
                        tosend.elementAt(0).name = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                    case "Phone":
                        tosend.elementAt(0).phone = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                    case "Email":
                        tosend.elementAt(0).email = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                    case "Address":
                        tosend.elementAt(0).address = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                    case "Notes":
                        tosend.elementAt(0).notes = (String)field;
                        isupdated = Boolean.TRUE;
                        break;
                }
                if(isupdated==Boolean.TRUE){
                    try{
                        Client.db.sendObject(tosend, new Integer(2));
                    }catch(IOException e1){
                        gui.panel1.errorMessage.setText("Error while sending to server");
                    }
                }
            }
        }
    }

    /**
     * Metoda konwertująca Datę
     * @param dateRegistered
     * @return Zwraca datę w formacie java.sql.Date
     * @throws ParseException
     */
    public java.util.Date getADate(String dateRegistered) throws ParseException{
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            gui.panel1.dateBeginDate = dateFormatter.parse(dateRegistered);
            gui.panel1.dateBeginDate = new java.sql.Date(gui.panel1.dateBeginDate.getTime());
        } catch (ParseException e1) {
            if(e1.getMessage().toString().startsWith("Unparseable date:")) {
                gui.panel1.errorMessage.setText("The date should be in the following format: YYYY-MM-DD");
            }
            throw new ParseException("Error",0);
        }
        return gui.panel1.dateBeginDate;
    }
}
