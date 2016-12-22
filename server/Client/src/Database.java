import classes.Payment;

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
    public Object[] columns = new Object[]{"ID", "Type", "Value", "Begin Date", "End Date", "Owner", "Subject", "Document", "Notes"};
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

        defaultTableModel = new DefaultTableModel(databaseResults, columns) {
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

    }

    public Object getDataFromServer() throws IOException{

        Vector<Payment> temp = null;
        try {
            temp = (Vector<Payment>)ios.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }

        return temp;
    }
}