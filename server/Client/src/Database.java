import classes.Agent;
import classes.Payment;
import classes.Subject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * Class for handling interaction with the server database
 */
public class Database {

    private ObjectOutputStream oos;
    private ObjectInputStream ios;
    private Object[][] databaseResultsPayment;
    private Object[][] databaseResultsSubject;
    private Object[][] databaseResultsAgent;
    public Object[] paymentColumns = new Object[]{"Accepted","Type","Value","Begin Date","End Date","Owner","Subject","Document","Notes"};
    public Object[] agentColumns = new Object[]{"ID","Name","Phone","Email","Commission"};
    public Object[] subjectColumns = new Object[]{"ID","Name","Phone","Email","Address","Bill","Notes"};

    public DefaultTableModel defaultTableModelPayment;
    public DefaultTableModel defaultTableModelAgent;
    public DefaultTableModel defaultTableModelSubject;
    public Vector<Payment> rowDataPayment = new Vector<>();
    public Vector<Agent> rowDataAgent = new Vector<>();
    public Vector<Subject> rowDataSubject = new Vector<>();



    public Database() {

        try{
            this.connectToServer();
            System.out.println("connecting to server");
        }catch(IOException e){
            System.out.println("connecting failed");
            e.printStackTrace();
        }

        try{
            Vector<Object> temp;

            for(int i=0;i<3;i++){

                temp = (Vector<Object>)getDataFromServer();
                if(temp.elementAt(0) instanceof Payment){
                    System.out.println("Payment");
                    rowDataPayment = (Vector<Payment>)temp.clone();
                }
                else if(temp.elementAt(0) instanceof Agent){
                    System.out.println("Agent");
                    rowDataAgent = (Vector<Agent>)temp.clone();
                }
                else if(temp.elementAt(0) instanceof Subject){
                    System.out.println("Subject");
                    rowDataSubject = (Vector<Subject>)temp.clone();
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        defaultTableModelPayment = new DefaultTableModel(databaseResultsPayment, paymentColumns);
        defaultTableModelAgent = new DefaultTableModel(databaseResultsAgent, agentColumns);
        defaultTableModelSubject = new DefaultTableModel(databaseResultsSubject, subjectColumns);

        for(Payment item: rowDataPayment) {
            defaultTableModelPayment.addRow(item.toVector());
        }
        for(Agent item: rowDataAgent) {
            defaultTableModelAgent.addRow(item.toVector());
        }
        for(Subject item: rowDataSubject) {
            defaultTableModelSubject.addRow(item.toVector());
        }
    }

    public void connectToServer() throws IOException {
        // Make connection and initialize streams
        Socket socket = new Socket(InetAddress.getLocalHost(), 9091);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ios = new ObjectInputStream(socket.getInputStream());
    }


    public void sendObject(Object input, Boolean remove) throws IOException {
        Vector<Object> temp;
        temp = (Vector<Object>)input;
        if(temp.elementAt(0) instanceof Payment){
            System.out.println("tak, Payment");
            if(remove)System.out.println("Usuwamy Payment");
            oos.writeObject(remove);
            oos.writeObject(input);
        }
        else if(temp.elementAt(0) instanceof Agent){
            System.out.println("no nie za bardzo bo to agent");
            if(remove)System.out.println("Usuwamy Agent");
            oos.writeObject(remove);
            oos.writeObject(input);
        }
        else if(temp.elementAt(0) instanceof Subject){
            System.out.println("no nie za bardzo bo to subject:");
            if(remove)System.out.println("Usuwamy Subject");
            oos.writeObject(remove);
            oos.writeObject(input);
        }

    }

    public Object getDataFromServer() throws IOException{

        Object temp = null;
        try {
            temp = ios.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Getting data failad");
            e.printStackTrace ();
        }
        return temp;
    }
}