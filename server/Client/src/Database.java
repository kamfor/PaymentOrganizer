import classes.Payment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * Class for handling interaction with the server database
 */
public class Database {

    /**
     * Object array used to hold the results retrieved from the database.
     */
    private Object[][] databaseResults;
    /**
     * String array used to hold the column names for the table.
     */
    public Object[] columns = new Object[]{"ID", "Type", "Value", "Begin Date", "End Date", "Owner", "Subject", "Document", "Notes"};
    /**
     * The table model used for manipulation of the JTable.
     */
    public DefaultTableModel defaultTableModel;

    public Vector<Vector> rowData = new Vector<>();

    /**
     * Database constructor
     */
    public Database() {

        String str_date="2016-12-15";
        DateFormat formatter ;
        Date date = new Date();
        formatter = new SimpleDateFormat("YYYY-MM-DD");
        try{
            date = formatter.parse(str_date);
        }
        catch(java.text.ParseException e){
            e.printStackTrace();
        }


        Payment sample = new Payment(12,"bill","100", date,date,1,1,"FV123/2","utd");
        Payment sample1 = new Payment(112,"bilhjkl","1050",date,date,2,2,"FV153/2","ulktd");

        Vector<String> rowOne = new Vector<String>();
        sample.toString(rowOne);
        rowData.addElement(new Vector(rowOne));
        sample1.toString(rowOne);
        rowData.addElement(new Vector(rowOne));


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



        for(Vector item: rowData) {
            defaultTableModel.addRow(item);
        }
    }

    public void sendObject(Object input) throws IOException {


        Socket s = new Socket(InetAddress.getLocalHost(), 9090);
        //String str = "";
        //Payment sample = new Payment(12,"bill","100","12-11-2016","29-11-2016",1,1,"FV123/2","utd");

        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

        oos.writeObject(input);

        //try {
        //    str = (String)ois.readObject();
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace ();
        //}

        //System.out.println(str);

        ois.close();
        oos.close();
        s.close();
    }
}