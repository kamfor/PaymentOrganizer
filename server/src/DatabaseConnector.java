/**
 * Created by kamil on 23.11.16.
 */
import classes.Payment;
import classes.Agent;
import classes.Subject;
import java.sql.*;
import java.util.Vector;

public class DatabaseConnector {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://s52.hekko.net.pl/kfforex_java";

    //  Database credentials
    static final String USER = "kfforex_java";
    static final String PASS = "fasada";

    private ResultSet rowsPayment;
    private ResultSet rowsAgent;
    private ResultSet rowsSubject;
    public Vector<Payment> dataPayment = new Vector<>();
    public Vector<Agent> dataAgent = new Vector<>();
    public Vector<Subject> dataSubject = new Vector<>();


    public void readMysqlData() {
       Connection conn = null;
       try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);

           Statement sqlPaymentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           String selectPayment = "SELECT id, accepted, type, value, begin_date, end_date, owner_id, subject_id, document_name, notes FROM payments";
           rowsPayment = sqlPaymentStatement.executeQuery(selectPayment); // Execute Payment query

           Statement sqlAgentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           String selectAgent = "SELECT id, name, phone, email, commission FROM agents";
           rowsAgent = sqlAgentStatement.executeQuery(selectAgent); // Execute Agent query

           Statement sqlSubjectStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           String selectSubject = "SELECT id, name, phone, email, address, bill, notes FROM subjects";
           rowsSubject = sqlSubjectStatement.executeQuery(selectSubject); // Execute Subject query

           while(rowsPayment.next()) { // Read Payment information
               Payment sample = new Payment(rowsPayment.getInt(1),rowsPayment.getInt(2),rowsPayment.getString(3),
                                            rowsPayment.getString(4),rowsPayment.getDate(5),rowsPayment.getDate(6),
                                            rowsPayment.getInt(7),rowsPayment.getInt(8),rowsPayment.getString(9),
                                            rowsPayment.getString(10));
               //sample.printPaymentObject();
               dataPayment.addElement(sample);
           }

           while(rowsAgent.next()) { // Read Agent information
               Agent sample = new Agent(rowsAgent.getInt(1),rowsAgent.getString(2),rowsAgent.getString(3),rowsAgent.getString(4),rowsAgent.getString(5));
               //sample.printObject();
               dataAgent.addElement(sample);
           }

           while(rowsSubject.next()) { // Read Subject information
               Subject sample = new Subject(rowsSubject.getInt(1),rowsSubject.getString(2),rowsSubject.getString(3),
                                            rowsSubject.getString(4),rowsSubject.getString(5),rowsSubject.getString(6),
                                            rowsSubject.getString(7));
               //sample.printObject();
               dataSubject.addElement(sample);
           }
       } catch (SQLException e) { // Print errors if exceptions occur
           System.out.println(e.getMessage());
       } catch (ClassNotFoundException e) {
           System.out.println(e.getMessage());
       }
       System.out.println("Goodbye!");
   }

   public void writeMysqlData(Vector<Payment> data) {


       //check if database is opened();
       for(Payment toinsert: data) {
           try { // Attempt to insert the information into the database
               rowsPayment.last();
               toinsert.id = rowsPayment.getInt(1)+1;
               rowsPayment.moveToInsertRow();
               rowsPayment.updateInt("id",toinsert.id);
               rowsPayment.updateInt("accepted",toinsert.accepted);
               rowsPayment.updateString("type", toinsert.type);
               rowsPayment.updateString("value", toinsert.value);
               rowsPayment.updateDate("begin_date", (Date)toinsert.begin_date);
               rowsPayment.updateDate("end_date", (Date)toinsert.end_date);
               rowsPayment.updateInt("owner_id",toinsert.owner_id);
               rowsPayment.updateInt("subject_id",toinsert.subject_id);
               rowsPayment.updateString("document_name", toinsert.document_name);
               rowsPayment.updateString("notes", toinsert.notes);

               rowsPayment.insertRow();
               rowsPayment.updateRow();
               rowsPayment.last();
           } catch (SQLException e2) {
               e2.printStackTrace();
           }
       }
   }
}
