/**
 * Created by kamil on 23.11.16.
 */
import classes.Payment;
import java.sql.*;
import java.util.Vector;

public class DatabaseConnector {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://s52.hekko.net.pl/kfforex_java";

    //  Database credentials
    static final String USER = "kfforex_java";
    static final String PASS = "fasada";

    private ResultSet rows;
    public Vector<Payment> data = new Vector<>();


    public void readMysqlData() {
       Connection conn = null;
       Statement stmt = null;
       try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);

           Statement sqlStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           String select = "SELECT id, type, value, begin_date, end_date, owner_id, subject_id, document_name, notes FROM payments";
           rows = sqlStatement.executeQuery(select); // Execute the query

           while(rows.next()) { // Add the information to the JTable
               Payment sample = new Payment(rows.getInt(1),rows.getString(2),rows.getString(3),rows.getDate(4),rows.getDate(5),rows.getInt(6),rows.getInt(7),rows.getString(8),rows.getString(9));
               //sample.printPaymentObject();
               data.addElement(sample);
           }
       } catch (SQLException e) { // Print errors if exceptions occur
           System.out.println(e.getMessage());
       } catch (ClassNotFoundException e) {
           System.out.println(e.getMessage());
       }
       System.out.println("Goodbye!");
   }

   public void writeMysqlData(Vector<Payment> data) {

       for(Payment toinsert: data) {
           try { // Attempt to insert the information into the database
               rows.last();
               toinsert.id = rows.getInt(1)+1;
               rows.moveToInsertRow();
               rows.updateInt("id",toinsert.id);
               rows.updateString("type", toinsert.type);
               rows.updateString("value", toinsert.value);
               rows.updateDate("begin_date", (Date)toinsert.begin_date);
               rows.updateDate("end_date", (Date)toinsert.end_date);
               rows.updateInt("owner_id",toinsert.owner_id);
               rows.updateInt("subject_id",toinsert.subject_id);
               rows.updateString("document_name", toinsert.document_name);
               rows.updateString("notes", toinsert.notes);

               rows.insertRow();
               rows.updateRow();



               rows.last();
           } catch (SQLException e2) {
               e2.printStackTrace();
           }
       }
   }
}
