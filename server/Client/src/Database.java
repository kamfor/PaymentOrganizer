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

    private static ObjectOutputStream oos;
    private static ObjectInputStream ios;
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
    public IncomingHandler recriver = new IncomingHandler();

    public static class IncomingHandler extends Thread {

        public void run() {
            try {

                Vector<Object> temp;
                while (true) {
                    Object incoming = ios.readObject();
                    if(incoming instanceof Boolean){
                        Boolean removing = (Boolean)incoming;
                        if(removing){
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
                        else{
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
                                System.out.println("empty database");
                            }
                        }
                    }else if (incoming instanceof String){
                        System.out.println("incoming message");
                        String messag = (String)incoming;
                        System.out.println(messag);
                    }
                    else{
                        System.out.println("Unsupported data");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling data" + e);
            }
        }
    }

    public Database() {

        try{
            this.connectToServer();
            System.out.println("connecting to server");
        }catch(IOException e){
            System.out.println("connecting failed");
            e.printStackTrace();
        }

        defaultTableModelPayment = new DefaultTableModel(databaseResultsPayment, paymentColumns);
        defaultTableModelAgent = new DefaultTableModel(databaseResultsAgent, agentColumns);
        defaultTableModelSubject = new DefaultTableModel(databaseResultsSubject, subjectColumns);
    }

    public void connectToServer() throws IOException {
        // Make connection and initialize streams
        Socket socket = new Socket(InetAddress.getLocalHost(), 9091);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ios = new ObjectInputStream(socket.getInputStream());
    }

    public void sendObject(Object input, Boolean remove) throws IOException {
        oos.writeObject(remove);
        oos.writeObject(input);
    }
}