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
import javax.swing.plaf.synth.SynthButtonUI;
import javax.swing.table.DefaultTableModel;


/**
 * Server redirecting class, able to sending data through socket,
 * operate on local data agreed in vectors
 */
public class Database {

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
                                    int rowPaymentToRemove = getPaymentRow(((Payment)item).id);
                                    rowDataPayment.removeElementAt(rowPaymentToRemove);
                                    defaultTableModelPayment.removeRow(rowPaymentToRemove);
                                }
                            } else if (temp.elementAt(0) instanceof Agent) {
                                System.out.println("Agent removed");
                                for (Object item : temp) {
                                    int rowAgentToRemove = getAgentRow(((Agent)item).id);
                                    rowDataAgent.removeElementAt(rowAgentToRemove);
                                    defaultTableModelAgent.removeRow(rowAgentToRemove);
                                }
                            } else if (temp.elementAt(0) instanceof Subject) {
                                System.out.println("Subject removed");
                                for (Object item : temp) {
                                    int rowSubjectToRemove = getSubjectRow(((Subject)item).id);
                                    rowDataSubject.removeElementAt(rowSubjectToRemove);
                                    defaultTableModelSubject.removeRow(rowSubjectToRemove);
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
                                JOptionPane.showMessageDialog(ClientMain.ctrl.gui, "Empty client.Database");
                            }
                        }else if(qualifier==2){
                            temp= (Vector<Object>)ios.readObject();
                            if(temp.size()>0){
                                if(temp.elementAt(0) instanceof Payment){
                                    System.out.println("Payment updated");
                                    for(Object item: temp){
                                        int rowPaymentToUpdate = getPaymentRow(((Payment)item).id);
                                        rowDataPayment.setElementAt((Payment)item,rowPaymentToUpdate);
                                        defaultTableModelPayment.removeRow(rowPaymentToUpdate);
                                        defaultTableModelPayment.insertRow(rowPaymentToUpdate,((Payment)item).toVector());
                                    }
                                }
                                else if(temp.elementAt(0) instanceof Agent){
                                    System.out.println("Agent updated");
                                    for(Object item: temp){
                                        int rowAgentToUpdate = getAgentRow(((Agent)item).id);
                                        rowDataAgent.setElementAt((Agent) item,rowAgentToUpdate);
                                        defaultTableModelAgent.removeRow(rowAgentToUpdate);
                                        defaultTableModelAgent.insertRow(rowAgentToUpdate,((Agent)item).toVector());
                                    }
                                }
                                else if(temp.elementAt(0) instanceof Subject) {
                                    System.out.println("Subject updated");
                                    for(Object item: temp){
                                        int rowSubjectToUpdate = getSubjectRow(((Subject)item).id);
                                        rowDataSubject.setElementAt((Subject) item,rowSubjectToUpdate);
                                        defaultTableModelSubject.removeRow(rowSubjectToUpdate);
                                        defaultTableModelSubject.insertRow(rowSubjectToUpdate,((Subject)item).toVector());
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
                JOptionPane.showMessageDialog(ClientMain.ctrl.gui, "Read Data error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Database() throws IOException{

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

    public void connectToServer() throws IOException {
        socket = new Socket(InetAddress.getLocalHost(), 9091);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ios = new ObjectInputStream(socket.getInputStream());
    }

    public void sendObjectUpdate(Object input,Object olds, Integer qualifier) throws IOException {
        oos.writeObject(qualifier);
        oos.writeObject(input);
        oos.writeObject(olds);
    }

    public void sendObject(Object input,Integer qualifier) throws IOException {
        oos.writeObject(qualifier);
        oos.writeObject(input);
    }

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

        Vector<Payment> toSend = new Vector<>();
        toSend.addElement(rowDataPayment.elementAt(rowIndex));

        /*Vector<Agent> agentToUpdate = new Vector<>();
        Vector<Subject> subjectToUpdate = new Vector<>();
        Agent tempAgent;
        Subject tempSubject;

        tempAgent = rowDataAgent.elementAt(getAgentRow(toSend.elementAt(0).owner_id));
        tempAgent.commission-=toSend.elementAt(0).value;
        agentToUpdate.addElement(tempAgent);

        tempSubject = rowDataSubject.elementAt(getSubjectRow(toSend.elementAt(0).subject_id));
        tempSubject.bill-=toSend.elementAt(0).value;
        subjectToUpdate.addElement(tempSubject);*/

        try{
            this.sendObject(toSend,new Integer(0));
            //this.sendObject(agentToUpdate, new Integer(2));
            //this.sendObject(subjectToUpdate, new Integer(2));
        } catch(IOException e1) {
            return "Error while updating data";
        }
        return "";
    }

    public String addPayment(String type, String value, String beginDate, String endDate, String owner, String subject, String document, String notes) {


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

        int agentRow = getAgentRow(Integer.valueOf(owner));
        if(agentRow>=0);
        else return "Owner doesn't exist"; //change to value receive from server

        int subjectRow = getSubjectRow(Integer.valueOf(subject));
        if(subjectRow>=0);
        else return "Subject doesn't exist";

        int paymentID = 0;
        for(Payment item: rowDataPayment){
            if(item.id>paymentID)paymentID = item.id;
        }
        Payment toInsert = new Payment(paymentID+1,Boolean.FALSE, type, numberValue, dateBeginDate, dateEndDate, Integer.valueOf(owner), Integer.valueOf(subject), document, notes);

        Vector<Payment> toSend = new Vector<>();
        toSend.addElement(toInsert);
        try {
            this.sendObject(toSend,new Integer(1));
            //this.sendObject(agentToUpdate, new Integer(2));
            //this.sendObject(subjectToUpdate, new Integer(2));
        } catch (IOException e1) {
            return "Error while sending data to server";
        }
        return "";
    }

    public String updatePayment(int rowIndex, Object updatedField, String updatedColumn){

        Vector<Agent> agentToUpdate = new Vector<>();
        Vector<Subject> subjectToUpdate = new Vector<>();
        Agent tempAgent;
        Subject tempSubject;
        Float numberValue;

        Vector<Payment> toSend = new Vector<>();
        toSend.addElement(rowDataPayment.elementAt(rowIndex));
        Vector<Payment> oldOne = new Vector<>();
        oldOne.addElement(rowDataPayment.elementAt(rowIndex));

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
                tempAgent = rowDataAgent.elementAt(getAgentRow(toSend.elementAt(0).owner_id));
                tempAgent.commission +=(numberValue-toSend.elementAt(0).value);
                agentToUpdate.addElement(tempAgent);

                tempSubject = rowDataSubject.elementAt(getSubjectRow(toSend.elementAt(0).subject_id));
                tempSubject.bill+=(numberValue-toSend.elementAt(0).value);
                subjectToUpdate.addElement(tempSubject);

                toSend.elementAt(0).value = numberValue;
                isUpdated = Boolean.TRUE;
                valueChanged = Boolean.TRUE;
                break;
            case "Begin Date":
                try{
                    toSend.elementAt(0).begin_date = getADate((String)updatedField);
                    isUpdated = Boolean.TRUE;
                }catch(ParseException e2){
                    return"Unparseable Begin Date";
                }
                break;
            case "End Date":
                try{
                    toSend.elementAt(0).end_date = getADate((String)updatedField);
                    isUpdated = Boolean.TRUE;
                }catch(ParseException e2){
                    return"Unparseable End Date";
                }
                break;
            case "Owner":
                int updateAgentRow = getAgentRow(Integer.valueOf((String)updatedField));
                if(updateAgentRow>=0){
                    updateAgent(getAgentRow(toSend.elementAt(0).owner_id),-1*toSend.elementAt(0).value,"Commission");
                    toSend.elementAt(0).owner_id = Integer.valueOf((String)updatedField);
                    updateAgent(updateAgentRow,toSend.elementAt(0).value,"Commission");
                    isUpdated = Boolean.TRUE;
                }else return "Owner doesn't exist";

                break;
            case "Subject":
                int updateSubjectRow = getSubjectRow(Integer.valueOf((String)updatedField));
                if(updateSubjectRow>=0){
                    updateSubject(getSubjectRow(toSend.elementAt(0).subject_id),-1*toSend.elementAt(0).value,"Bill");
                    toSend.elementAt(0).subject_id = Integer.valueOf((String)updatedField);
                    updateSubject(updateSubjectRow,toSend.elementAt(0),"Bill");
                    isUpdated = Boolean.TRUE;
                }
                else return "Subject doesn't exist";

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
                this.sendObjectUpdate(toSend,oldOne,new Integer(2));
                if(valueChanged){
                    this.sendObjectUpdate(agentToUpdate,agentToUpdate,new Integer(2));
                    this.sendObjectUpdate(subjectToUpdate,agentToUpdate,new Integer(2));
                }
            }catch(IOException e1){
                return "Error while sending to server";
            }
        }
        return"";
    }

    public String removeAgent(int rowIndex){

        Vector<Agent> tosend = new Vector<>();
        tosend.addElement(rowDataAgent.elementAt(rowIndex));

        for(Payment item:rowDataPayment){
            if(item.owner_id == tosend.elementAt(0).id){
                return "Agent exist in Payment table";
            }
        }
        try{
            this.sendObject(tosend,new Integer(0));
        } catch( IOException e1) {
            return "Error while updating data";
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
            this.sendObject(toSend,new Integer(1));
        } catch (IOException e1) {
            return "Error while sending to data";
        }
        return "";
    }

    public String updateAgent(int rowIndex, Object updatedField, String updatedColumn){

        Vector<Agent> toSend = new Vector<>();
        toSend.addElement(this.rowDataAgent.elementAt(rowIndex));

        Vector<Agent>  oldOne = new Vector<>();
        oldOne.addElement(this.rowDataAgent.elementAt(rowIndex));

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
                this.sendObjectUpdate(toSend,oldOne, new Integer(2));
            }catch(IOException e1){
                return"Error while sending to server";
            }
        }
        return "";
    }

    public String removeSubject(int rowIndex){
        Vector<Subject> toSend = new Vector<>();
        toSend.addElement(rowDataSubject.elementAt(rowIndex));
        for(Payment item:rowDataPayment){
            if(item.subject_id == toSend.elementAt(0).id){
                return "Subject exist in Payment table";
            }
        }
        try{
            this.sendObject(toSend, new Integer(0));
        } catch(IOException e1) {
            return "Error while updating data";
        }
        return "";
    }

    public String addSubject(String name,String phone,String email,String address,String notes){

        int subjectID=0;
        for(Subject item: rowDataSubject){
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

        Vector<Subject> oldOne = new Vector<>();
        oldOne.addElement(this.rowDataSubject.elementAt(rowIndex));

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
                this.sendObjectUpdate(toSend,oldOne, new Integer(2));
            }catch(IOException e1){
                return "Error while sending to server";
            }
        }
        return"";
    }

    public static int getPaymentRow(int id){
        for(int i=0; i<rowDataPayment.size(); i++){
            if(rowDataPayment.elementAt(i).id==id)return i;
        }
        return -1;
    }

    public static int getAgentRow(int id){
        for(int i=0; i<rowDataAgent.size(); i++){
            if(rowDataAgent.elementAt(i).id==id)return i;
        }
        return -1;
    }

    public static int getSubjectRow(int id){
        for(int i=0; i<rowDataSubject.size(); i++){
            if(rowDataSubject.elementAt(i).id==id)return i;
        }
        return -1;
    }

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