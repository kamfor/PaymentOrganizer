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
    public Vector<Payment> dataPayment;
    public Vector<Agent> dataAgent;
    public Vector<Subject> dataSubject;

    public DatabaseConnector(){
        dataPayment = new Vector<>();
        dataAgent = new Vector<>();
        dataSubject = new Vector<>();
    }



    public void readMysqlData() {
        Connection conn = null;
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement sqlPaymentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String selectPayment = "SELECT id, accepted, type, value, begin_date, end_date, owner_id, subject_id, document_name, notes FROM payments";
            rowsPayment = sqlPaymentStatement.executeQuery(selectPayment); // Execute Payment query

            Statement sqlAgentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String selectAgent = "SELECT id, name, phone, email, commission FROM agents";
            rowsAgent = sqlAgentStatement.executeQuery(selectAgent); // Execute Agent query

            Statement sqlSubjectStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String selectSubject = "SELECT id, name, phone, email, address, bill, notes FROM subjects";
            rowsSubject = sqlSubjectStatement.executeQuery(selectSubject); // Execute Subject query

            while (rowsPayment.next()) { // Read Payment information
                Payment sample = new Payment(rowsPayment.getInt(1), rowsPayment.getInt(2), rowsPayment.getString(3),
                        rowsPayment.getString(4), rowsPayment.getDate(5), rowsPayment.getDate(6),
                        rowsPayment.getInt(7), rowsPayment.getInt(8), rowsPayment.getString(9),
                        rowsPayment.getString(10));
                //sample.printPaymentObject();
                dataPayment.addElement(sample);
            }

            while (rowsAgent.next()) { // Read Agent information
                Agent sample = new Agent(rowsAgent.getInt(1), rowsAgent.getString(2), rowsAgent.getString(3), rowsAgent.getString(4), rowsAgent.getString(5));
                //sample.printObject();
                dataAgent.addElement(sample);
            }

            while (rowsSubject.next()) { // Read Subject information
                Subject sample = new Subject(rowsSubject.getInt(1), rowsSubject.getString(2), rowsSubject.getString(3),
                        rowsSubject.getString(4), rowsSubject.getString(5), rowsSubject.getString(6),
                        rowsSubject.getString(7));
                //sample.printObject();
                dataSubject.addElement(sample);
            }
        } catch (SQLException e) { // Print errors if exceptions occur
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Data read!");
    }

    public void writeMysqlData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toinsert : incoming) {
                try { // Attempt to insert the information into the database
                    rowsPayment.moveToInsertRow();
                    rowsPayment.updateInt("id", toinsert.id);
                    rowsPayment.updateInt("accepted", toinsert.accepted);
                    rowsPayment.updateString("type", toinsert.type);
                    rowsPayment.updateString("value", toinsert.value);
                    rowsPayment.updateDate("begin_date", (Date) toinsert.begin_date);
                    rowsPayment.updateDate("end_date", (Date) toinsert.end_date);
                    rowsPayment.updateInt("owner_id", toinsert.owner_id);
                    rowsPayment.updateInt("subject_id", toinsert.subject_id);
                    rowsPayment.updateString("document_name", toinsert.document_name);
                    rowsPayment.updateString("notes", toinsert.notes);

                    rowsPayment.insertRow();
                    rowsPayment.moveToCurrentRow();
                    dataPayment.addElement(toinsert);
                    System.out.println("Payment written to mysql");
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (temp.elementAt(0) instanceof Agent) {
            Vector<Agent> incoming = (Vector<Agent>) data;
            for (Agent toinsert : incoming) {
                try { // Attempt to insert the information into the database
                    rowsAgent.moveToInsertRow();
                    rowsAgent.updateInt("id", toinsert.id);
                    rowsAgent.updateString("name", toinsert.name);
                    rowsAgent.updateString("phone", toinsert.phone);
                    rowsAgent.updateString("email", toinsert.email);
                    rowsAgent.updateString("commission", toinsert.commission);

                    rowsAgent.insertRow();
                    rowsPayment.moveToCurrentRow();
                    dataAgent.addElement(toinsert);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            System.out.println("Agent written");
        } else if (temp.elementAt(0) instanceof Subject) {
            Vector<Subject> incoming = (Vector<Subject>) data;
            for (Subject toinsert : incoming) {
                try { // Attempt to insert the information into the database
                    rowsSubject.moveToInsertRow();
                    rowsSubject.updateInt("id", toinsert.id);
                    rowsSubject.updateString("name", toinsert.name);
                    rowsSubject.updateString("phone", toinsert.phone);
                    rowsSubject.updateString("email", toinsert.email);
                    rowsSubject.updateString("address", toinsert.address);
                    rowsSubject.updateString("bill", toinsert.bill);
                    rowsSubject.updateString("notes", toinsert.notes);

                    rowsSubject.insertRow();
                    rowsSubject.moveToCurrentRow();
                    dataSubject.addElement(toinsert);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            System.out.println("Subject written");
        }
    }

    public void removeMysqlData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toInsert : incoming) {
                try { // Attempt to remove the information into the database
                    for (int i = 0; i < dataPayment.size(); i++) {
                        if (dataPayment.elementAt(i).id == toInsert.id) {
                            dataPayment.removeElementAt(i);
                            rowsPayment.absolute(i + 1);
                        }
                    }
                    rowsPayment.deleteRow();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            System.out.println("Payment removed");
        } else if (temp.elementAt(0) instanceof Agent) {
            Vector<Agent> incoming = (Vector<Agent>) data;
            for (Agent toInsert : incoming) {
                try { // Attempt to remove the information into the database
                    for (int i = 0; i < dataAgent.size(); i++) {
                        if (dataAgent.elementAt(i).id == toInsert.id) {
                            dataAgent.removeElementAt(i);
                            rowsAgent.absolute(i + 1);
                        }
                    }
                    rowsAgent.deleteRow();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            System.out.println("Agent removed");
        } else if (temp.elementAt(0) instanceof Subject) {
            Vector<Subject> incoming = (Vector<Subject>) data;
            for (Subject toInsert : incoming) {
                try { // Attempt to remove the information into the database
                    for (int i = 0; i < dataSubject.size(); i++) {
                        if (dataSubject.elementAt(i).id == toInsert.id) {
                            dataSubject.removeElementAt(i);
                            rowsSubject.absolute(i + 1);
                        }
                    }
                    rowsSubject.deleteRow();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
            System.out.println("Subject removed");
        }
    }

    public void writeLocallData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toinsert : incoming){

            }
            System.out.println("Payment written to local");
        } else if (temp.elementAt(0) instanceof Agent) {
            Vector<Agent> incoming = (Vector<Agent>) data;
            for (Agent toinsert : incoming){

            }
            System.out.println("Agent written to local");
        } else if (temp.elementAt(0) instanceof Subject) {
            Vector<Subject> incoming = (Vector<Subject>) data;
            for (Subject toinsert : incoming){

            }
            System.out.println("Subject written");
        }
    }

    public void removeLocalData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toInsert : incoming) {

            }
            System.out.println("Payment removed from local");
        } else if (temp.elementAt(0) instanceof Agent) {
            Vector<Agent> incoming = (Vector<Agent>) data;
            for (Agent toInsert : incoming) {

            }
            System.out.println("Agent removed from local");
        } else if (temp.elementAt(0) instanceof Subject) {
            Vector<Subject> incoming = (Vector<Subject>) data;
            for (Subject toInsert : incoming) {

            }
            System.out.println("Subject removed from local");
        }
    }
}
