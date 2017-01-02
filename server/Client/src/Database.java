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
    private Object[][] databaseResults;
    public Object[] paymentColumns = new Object[]{"ID","Accepted","Type","Value","Begin Date","End Date","Owner","Subject","Document","Notes"};
    public Object[] agentColumns = new Object[]{"ID","Name","Phone","Email","Commission"};
    public Object[] subjectColumns = new Object[]{"ID","Name","Phone","Email","Address","End Date","Bill","Notes"};

    public DefaultTableModel defaultTableModel;
    public Vector<Payment> rowData = new Vector<>();



    public Database() {

        try{
            this.connectToServer();
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            rowData = (Vector<Payment>)getDataFromServer();
        } catch(IOException e){
            e.printStackTrace();
        }

        defaultTableModel = new DefaultTableModel(databaseResults, paymentColumns) {
            public Class getColumnClass(int column) { // Override the getColumnClass method to get the
                Class classToReturn;					// class types of the data retrieved from the database

                if((column >= 0) && column < getColumnCount()) {
                    classToReturn = getValueAt(0, column).getClass();
                } else {
                    classToReturn = Object.class;
                }
                return classToReturn;
            }
        };

        for(Payment item: rowData) {
            defaultTableModel.addRow(item.toVector());
        }
    }

    public void connectToServer() throws IOException {

        // Make connection and initialize streams
        Socket socket = new Socket(InetAddress.getLocalHost(), 9091);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ios = new ObjectInputStream(socket.getInputStream());
    }


    public void sendObject(Object input) throws IOException {

        oos.writeObject(input);
        Vector<Object> temp;
        temp = (Vector<Object>)input;
        if(temp.elementAt(0) instanceof Payment){ //tak sprawdzamy co przesylamy i moze zadziala
            System.out.println("tak");
        }
        else if(temp.elementAt(0) instanceof Agent){
            System.out.println("no nie za bardzo bo to agent");
        }
        else if(temp.elementAt(0) instanceof Subject){
            System.out.println("no nie za bardzo bo to subject:");
        }

    }

    public Object getDataFromServer() throws IOException{

        Object temp = null;
        try {
            temp = ios.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }

        return temp;
    }
}