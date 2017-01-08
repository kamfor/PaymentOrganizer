package client;

import classes.Agent;
import classes.Payment;
import classes.Subject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * Server redirecting class, able to sending data through socket
 */
public class Database {

    private static Socket socket;
    protected static ObjectOutputStream oos;
    protected static ObjectInputStream ios;
    private static Object[][] databaseResultsPayment;
    private static Object[][] databaseResultsSubject;
    private static Object[][] databaseResultsAgent;
    public static Object[] paymentColumns = new Object[]{"Accepted","Type","Value","Begin Date","End Date","Owner","Subject","Document","Notes"};
    public static Object[] agentColumns = new Object[]{"ID","Name","Phone","Email","Commission"};
    public static Object[] subjectColumns = new Object[]{"ID","Name","Phone","Email","Address","Bill","Notes"};

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
    public static class IncomingHandler extends Thread {

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
                                            defaultTableModelPayment.removeRow(i); // popraw to jakos
                                        }
                                    }
                                }
                            } else if (temp.elementAt(0) instanceof Agent) {
                                System.out.println("Agent removed");
                                for (Object item : temp) {
                                    for (int i = 0; i < rowDataAgent.size(); i++) {
                                        if (rowDataAgent.elementAt(i).id == ((Agent)item).id) {
                                            rowDataAgent.removeElementAt(i);
                                            defaultTableModelAgent.removeRow(i); //to też popraw
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
                                JOptionPane.showMessageDialog(Client.GUI.gui, "Empty client.Database");
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
                JOptionPane.showMessageDialog(Client.GUI.gui, "Read Data error", "Inane error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Class constructor, initialize server connection
     * @throws IOException
     */
    public Database() throws IOException{

        try{
            this.connectToServer();
            System.out.println("connecting to server");
        }catch(IOException e){
            System.out.println("connecting failed");
            throw new IOException();
        }

        defaultTableModelPayment = new DefaultTableModel(databaseResultsPayment, paymentColumns);
        defaultTableModelAgent = new DefaultTableModel(databaseResultsAgent, agentColumns);
        defaultTableModelSubject = new DefaultTableModel(databaseResultsSubject, subjectColumns);
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
}