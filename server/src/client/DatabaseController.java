package client;

import model.Agent;
import model.Payment;
import model.Subject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * Server redirecting class, able to sending data through socket
 */
public class DatabaseController {

    private static Socket socket;
    protected static ObjectOutputStream oos;
    protected static ObjectInputStream ios;
    private static Object[][] databaseResults;
    public static DefaultTableModel defaultTableModelPayment;
    public static DefaultTableModel defaultTableModelAgent;
    public static DefaultTableModel defaultTableModelSubject;
    public static Vector<Payment> rowDataPayment = new Vector<>();
    public static Vector<Agent> rowDataAgent = new Vector<>();
    public static Vector<Subject> rowDataSubject = new Vector<>();
    public IncomingHandler receiver = new IncomingHandler();

    /**
     * Runnable class, event loop service
     */
    public static class IncomingHandler extends Thread { //make it more clear

        public void run() {
            try {

                Vector<Object> temp;
                while (true) {
                    Object incoming = ios.readObject();
                    if(incoming instanceof Integer){
                        Integer qualifier = (Integer)incoming;
                        if(qualifier==0){
                            temp= (Vector<Object>)ios.readObject();
                            if (temp.elementAt(0) instanceof Payment) {
                                System.out.println("Payment removed");
                                for (Object item : temp) {
                                    for (int i = 0; i < rowDataPayment.size(); i++) {
                                        if (rowDataPayment.elementAt(i).id == ((Payment)item).id) {
                                            rowDataPayment.removeElementAt(i);
                                            defaultTableModelPayment.removeRow(i);
                                        }
                                    }
                                }
                            } else if (temp.elementAt(0) instanceof Agent) {
                                System.out.println("Agent removed");
                                for (Object item : temp) {
                                    for (int i = 0; i < rowDataAgent.size(); i++) {
                                        if (rowDataAgent.elementAt(i).id == ((Agent)item).id) {
                                            rowDataAgent.removeElementAt(i);
                                            defaultTableModelAgent.removeRow(i);
                                        }
                                    }
                                }
                            } else if (temp.elementAt(0) instanceof Subject) {
                                System.out.println("Subject removed");
                                for (Object item : temp) {
                                    for (int i = 0; i < rowDataSubject.size(); i++) {
                                        if (rowDataSubject.elementAt(i).id == ((Subject)item).id) {
                                            rowDataSubject.removeElementAt(i);
                                            defaultTableModelSubject.removeRow(i);
                                        }
                                    }
                                }
                            }
                        }
                        else if(qualifier ==1){
                            temp= (Vector<Object>)ios.readObject();
                            if(temp.size()>0){
                                if(temp.elementAt(0) instanceof Payment){
                                    System.out.println("Payment added");
                                    for(Object item: temp){
                                        rowDataPayment.addElement((Payment)item);
                                        defaultTableModelPayment.addRow(((Payment)item).toVector());
                                    }
                                }
                                else if(temp.elementAt(0) instanceof Agent){
                                    System.out.println("Agent added");
                                    for(Object item: temp){
                                        rowDataAgent.addElement((Agent)item);
                                        defaultTableModelAgent.addRow(((Agent)item).toVector());
                                    }
                                }
                                else if(temp.elementAt(0) instanceof Subject) {
                                    System.out.println("Subject added");
                                    for(Object item: temp){
                                        rowDataSubject.addElement((Subject)item);
                                        defaultTableModelSubject.addRow(((Subject)item).toVector());
                                    }
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(ClientMain.ctrl.gui, "Empty client.DatabaseController");
                            }
                        }else if(qualifier==2){
                            temp= (Vector<Object>)ios.readObject();
                            if(temp.size()>0){
                                if(temp.elementAt(0) instanceof Payment){
                                    System.out.println("Payment updated");
                                    for(Object item: temp){
                                        for (int i = 0; i < rowDataPayment.size(); i++) {
                                            if (rowDataPayment.elementAt(i).id == ((Payment)item).id) {
                                                rowDataPayment.setElementAt((Payment)item,i);
                                                defaultTableModelPayment.removeRow(i);
                                                defaultTableModelPayment.insertRow(i,((Payment)item).toVector());
                                            }
                                        }
                                    }
                                }
                                else if(temp.elementAt(0) instanceof Agent){
                                    System.out.println("Agent updated");
                                    for(Object item: temp){
                                        for (int i = 0; i < rowDataAgent.size(); i++) {
                                            if (rowDataAgent.elementAt(i).id == ((Agent)item).id) {
                                                rowDataAgent.setElementAt((Agent) item,i);
                                                defaultTableModelAgent.removeRow(i);
                                                defaultTableModelAgent.insertRow(i,((Agent)item).toVector());
                                            }
                                        }
                                    }
                                }
                                else if(temp.elementAt(0) instanceof Subject) {
                                    System.out.println("Subject updated");
                                    for(Object item: temp){
                                        for (int i = 0; i < rowDataSubject.size(); i++) {
                                            if (rowDataSubject.elementAt(i).id == ((Subject)item).id) {
                                                rowDataSubject.setElementAt((Subject) item,i);
                                                defaultTableModelSubject.removeRow(i);
                                                defaultTableModelSubject.insertRow(i,((Subject)item).toVector());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        System.out.println("Unsupported data");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling data" + e);
                JOptionPane.showMessageDialog(ClientMain.ctrl.gui, "Read Data error", "Inane error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Class constructor, initialize server connection
     * @throws IOException
     */
    public DatabaseController() throws IOException{

        try{
            this.connectToServer();
            System.out.println("connecting to server");
        }catch(IOException e){
            System.out.println("connecting failed");
            throw new IOException();
        }

        defaultTableModelPayment = new DefaultTableModel(databaseResults, Payment.paymentColumns);
        defaultTableModelAgent = new DefaultTableModel(databaseResults, Agent.agentColumns);
        defaultTableModelSubject = new DefaultTableModel(databaseResults, Subject.subjectColumns);
    }

    /**
     * Socket opening method
     * @throws IOException
     */
    public void connectToServer() throws IOException {
        socket = new Socket(InetAddress.getLocalHost(), 9091);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ios = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sending data through socket method
     * @param input object to change in database
     * @param remove qualifier 0 - add to database, 1 - remove from database. 2 - record actualization
     * @throws IOException
     */
    public void sendObject(Object input, Integer remove) throws IOException {
        oos.writeObject(remove);
        oos.writeObject(input);
    }

    /**
     * Closing socket method
     */
    public void disconnect(){
        try{
            socket.close();
            oos.close();
            oos.close();
        }catch(IOException e1){
            System.out.println(e1.getMessage());
        }
    }

    public String removePayment(int rowIndex){

        Vector<Payment> tosend = new Vector<>();
        Vector<Agent> agentToUpdate = new Vector<>();
        Vector<Subject> subjectToUpdate = new Vector<>();
        tosend.addElement(this.rowDataPayment.elementAt(rowIndex));
        for(Agent item: this.rowDataAgent){
            if(item.id==tosend.elementAt(0).owner_id){
                item.commission -=tosend.elementAt(0).value;
                agentToUpdate.addElement(item);
            }
        }
        for(Subject item: this.rowDataSubject){
            if(item.id==tosend.elementAt(0).subject_id){
                item.bill -=tosend.elementAt(0).value;
                subjectToUpdate.addElement(item);
            }
        }
        try{
            this.sendObject(tosend, new Integer(0));
            this.sendObject(agentToUpdate, new Integer(2));
            this.sendObject(subjectToUpdate, new Integer(2));
        } catch(IOException e1) {
            return "Error while updating data ";
        }
        return "";
    }

    public String addPayment(String type, String value, String beginDate, String endDate, String owner, String subject, String document, String notes) {

        Boolean foundAgent = Boolean.FALSE;
        Boolean foundSubject = Boolean.FALSE;
        Vector<Agent> agentToUpdate = new Vector<>();
        Vector<Subject> subjectToUpdate = new Vector<>();
        java.util.Date dateBeginDate, dateEndDate;
        Float numberValue;

        try{
            numberValue = Float.valueOf(value);
        }
        catch(NumberFormatException e1){
            return "Incorrect Value";

        }

        if(!beginDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
            return "The date should be in the following format: YYYY-MM-DD";
        }

        if(!endDate.matches("[0-2][0-9]{3}-[0-1][0-2]-[0-3][0-9]")) {
            return "The date should be in the following format: YYYY-MM-DD";
        }

        try{
            dateBeginDate = getADate(beginDate);
            dateEndDate = getADate(endDate);
        }
        catch(ParseException e1){
            return "Incorrect Date format should be YYYY-MM-DD";
        }

        for(Agent item: this.rowDataAgent){
            if(item.id==Integer.valueOf(owner)){
                foundAgent = Boolean.TRUE;
                item.commission +=numberValue;
                agentToUpdate.addElement(item);
            }
        }
        if(!foundAgent){
            return "Owner doesn't exist";
        }
        for(Subject item: this.rowDataSubject){
            if(item.id==Integer.valueOf(subject)){
                foundSubject = Boolean.TRUE;
                item.bill +=numberValue;
                subjectToUpdate.addElement(item);
            }
        }
        if(!foundSubject){
            return "Subject doesn't exist";
        }

        int paymentID = 0;
        for(Payment item: this.rowDataPayment){
            if(item.id>paymentID)paymentID = item.id;
        }
        Payment toInsert = new Payment(paymentID+1,Boolean.FALSE, type, numberValue, dateBeginDate, dateEndDate, Integer.valueOf(owner), Integer.valueOf(subject), document, notes);

        Vector<Payment> toSend = new Vector<>();
        toSend.addElement(toInsert);
        try {
            this.sendObject(toSend, new Integer(1));
            if(foundAgent) this.sendObject(agentToUpdate, new Integer(2));
            if(foundSubject) this.sendObject(subjectToUpdate, new Integer(2));
        } catch (IOException e1) {
            return "Error while sending data to server";
        }
        return "";
    }

    public String updatePayment(int rowIndex, Object updatedField, String updatedColumn){

        Vector<Agent> agentToUpdate = new Vector<>();
        Vector<Subject> subjectToUpdate = new Vector<>();
        Float numberValue;

        Vector<Payment> toSend = new Vector<>();
        toSend.addElement(this.rowDataPayment.elementAt(rowIndex));

        Boolean found;
        Boolean valueChanged = Boolean.FALSE;
        Boolean isUpdated = Boolean.FALSE;

        switch(updatedColumn) {
            case "Type":
                toSend.elementAt(0).type = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Accepted":
                toSend.elementAt(0).accepted = (Boolean)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Value":
                try{
                    numberValue = Float.valueOf((String)updatedField);
                }
                catch(NumberFormatException e1){
                    return "Incorrect Value";
                }
                for(Agent item: this.rowDataAgent){
                    if(item.id==toSend.elementAt(0).owner_id){
                        item.commission +=(numberValue-toSend.elementAt(0).value);
                        agentToUpdate.addElement(item);
                    }
                }
                for(Subject item: this.rowDataSubject){
                    if(item.id==toSend.elementAt(0).subject_id){
                        item.bill +=(numberValue-toSend.elementAt(0).value);
                        subjectToUpdate.addElement(item);
                    }
                }
                toSend.elementAt(0).value = numberValue;
                isUpdated = Boolean.TRUE;
                valueChanged = Boolean.TRUE;
                break;
            case "Begin Date":
                try{
                    toSend.elementAt(0).begin_date = this.getADate((String)updatedField);
                    isUpdated = Boolean.TRUE;
                }catch(ParseException e2){
                    return"Unparseable Begin Date";
                }
                break;
            case "End Date":
                try{
                    toSend.elementAt(0).end_date = ClientMain.db.getADate((String)updatedField);
                    isUpdated = Boolean.TRUE;
                }catch(ParseException e2){
                    return"Unparseable End Date";
                }
                break;
            case "Owner":
                found = Boolean.FALSE;
                for(int i=0; i<this.rowDataAgent.size(); i++){
                    if(this.rowDataAgent.elementAt(i).id==Integer.valueOf((String)updatedField)){
                        updateAgent(toSend.elementAt(0).owner_id-1,-1*toSend.elementAt(0).value,"Commission");//replace to find agent function
                        toSend.elementAt(0).owner_id = Integer.valueOf((String)updatedField);
                        updateAgent(i,toSend.elementAt(0).value,"Commission");
                        found = Boolean.TRUE;
                        isUpdated = Boolean.TRUE;
                    }
                }
                if(!found){
                    return "Owner doesn't exist";
                }
                break;
            case "Subject":
                found = Boolean.FALSE;
                for(int i=0; i<this.rowDataSubject.size(); i++){
                    if(this.rowDataSubject.elementAt(i).id==Integer.valueOf((String)updatedField)){
                        updateSubject(toSend.elementAt(0).subject_id-1,-1*toSend.elementAt(0).value,"Bill");
                        toSend.elementAt(0).subject_id = Integer.valueOf((String)updatedField);
                        updateSubject(i,toSend.elementAt(0),"Bill");
                        found = Boolean.TRUE;
                        isUpdated = Boolean.TRUE;
                    }
                }
                if(!found){
                    return "Subject doesn't exist";
                }
                break;
            case "Document":
                toSend.elementAt(0).document_name = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Notes":
                toSend.elementAt(0).notes = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
        }
        if(isUpdated){
            try{
                this.sendObject(toSend, new Integer(2));
                if(valueChanged){
                    this.sendObject(agentToUpdate, new Integer(2));
                    this.sendObject(subjectToUpdate, new Integer(2));
                }
            }catch(IOException e1){
                return "Error while sending to server";
            }
        }
        return"";
    }

    public String removeAgent(int rowIndex){

        Vector<Agent> tosend = new Vector<>();
        tosend.addElement(this.rowDataAgent.elementAt(rowIndex));
        for(Payment item: this.rowDataPayment){
            if(item.owner_id == tosend.elementAt(0).id){
                return "Agent exist in Payment table";
            }
        }
        try{
            this.sendObject(tosend, new Integer(0));
        } catch( IOException e1) {
            return "Sending error";
        }
        return "";
    }

    public String addAgent(String name,String phone,String email){

        int agentID=0;

        for(Agent item: this.rowDataAgent){
            if(item.id>agentID)agentID = item.id;
        }
        Agent toInsert = new Agent(agentID+1,name,phone,email,new Float(0.0));
        Vector<Agent> toSend = new Vector<>();
        toSend.addElement(toInsert);
        try {
            this.sendObject(toSend, new Integer(1));
        } catch (IOException e1) {
            return "Error while sending to server";
        }
        return "";
    }

    public String updateAgent(int rowIndex, Object updatedField, String updatedColumn){

        Vector<Agent> toSend = new Vector<>();
        toSend.addElement(this.rowDataAgent.elementAt(rowIndex));

        Boolean isUpdated = false;

        switch(updatedColumn) {
            case "Name":
                toSend.elementAt(0).name = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Phone":
                toSend.elementAt(0).phone = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Email":
                toSend.elementAt(0).email = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Commission": //use only in logic
                toSend.elementAt(0).commission += (Float)updatedField;
                isUpdated = Boolean.TRUE;
                break;
        }
        if(isUpdated==Boolean.TRUE){
            try{
                this.sendObject(toSend, new Integer(2));
            }catch(IOException e1){
                return"Error while sending to server";
            }
        }
        return "";
    }

    public String removeSubject(int rowIndex){
        Vector<Subject> toSend = new Vector<>();
        toSend.addElement(this.rowDataSubject.elementAt(rowIndex));
        for(Payment item: this.rowDataPayment){
            if(item.subject_id == toSend.elementAt(0).id){
                return "Subject exist in Payment table";
            }
        }
        try{
            this.sendObject(toSend, new Integer(0));
        } catch(IOException e1) {
            return "Sending to server error";
        }
        return "";
    }

    public String addSubject(String name,String phone,String email,String address,String notes){

        int subjectID=0;
        for(Subject item: ClientMain.db.rowDataSubject){
            if(item.id>subjectID)subjectID = item.id;
        }
        Subject toInsert = new Subject(subjectID+1,name,phone,email,address,new Float(0.0),notes);
        Vector<Subject> toSend = new Vector<>();
        toSend.addElement(toInsert);
        try {
            this.sendObject(toSend, new Integer(1));
        } catch (IOException e1) {
            return"Server sending error";
        }
        return "";
    }

    public String updateSubject(int rowIndex, Object updatedField, String updatedColumn){

        Vector<Subject> toSend = new Vector<>();
        toSend.addElement(this.rowDataSubject.elementAt(rowIndex));

        Boolean isUpdated = false;

        switch(updatedColumn) {
            case "Name":
                toSend.elementAt(0).name = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Phone":
                toSend.elementAt(0).phone = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Email":
                toSend.elementAt(0).email = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Address":
                toSend.elementAt(0).address = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case "Notes":
                toSend.elementAt(0).notes = (String)updatedField;
                isUpdated = Boolean.TRUE;
                break;
            case"Bill":
                toSend.elementAt(0).bill += (float)updatedField;
                isUpdated = Boolean.TRUE;
                break;
        }
        if(isUpdated==Boolean.TRUE){
            try{
                this.sendObject(toSend, new Integer(2));
            }catch(IOException e1){
                return "Error while sending to server";
            }
        }
        return"";
    }

    /**
     * Date parsing method
     * @param dateRegistered
     * @return  java.sql.Date format
     * @throws ParseException
     */
    public java.util.Date getADate(String dateRegistered) throws ParseException{
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date toReturn;

        try {
            toReturn = dateFormatter.parse(dateRegistered);
            toReturn = new java.sql.Date(toReturn.getTime());
        } catch (ParseException e1) {
            throw new ParseException("Error",0);
        }
        return toReturn;
    }
}