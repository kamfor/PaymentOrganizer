package server;
/**
 * Created by kamil on 23.11.16.
 */


import classes.Payment;
import classes.Agent;
import classes.Subject;
import java.sql.*;
import java.util.Vector;


/**
 * Klasa pośrednia zawierająca pola i metody umożliwiająca odczyt i zapis danych do serwera MySQL
 */
public class DatabaseConnector {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://s52.hekko.net.pl/kfforex_java";

    //  Database credentials
    static final String USER = "kfforex_java";
    static final String PASS = "fasada";

    private Connection conn;

    private ResultSet rowsPayment;
    private ResultSet rowsAgent;
    private ResultSet rowsSubject;
    public Vector<Payment> dataPayment;
    public Vector<Agent> dataAgent;
    public Vector<Subject> dataSubject;

    /**
     * Konstruktor klasy
     */
    public DatabaseConnector(){
        dataPayment = new Vector<>();
        dataAgent = new Vector<>();
        dataSubject = new Vector<>();
    }

    /**
     * Metoda inicjująca połączenie z bazą MySQL
     */
    public void connectMysql(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(SQLException | ClassNotFoundException e1){
            System.out.println("Database connection error" + e1.getMessage());
        }

    }

    /**
     * Metoda odczytująca dane z bazy do lokalnych wektorów
     */
    public void readMysqlData() {
        try {
            Statement sqlPaymentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String selectPayment = "SELECT id, accepted, type, value, begin_date, end_date, owner_id, subject_id, document_name, notes FROM payments";
            rowsPayment = sqlPaymentStatement.executeQuery(selectPayment);

            Statement sqlAgentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String selectAgent = "SELECT id, name, phone, email, commission FROM agents";
            rowsAgent = sqlAgentStatement.executeQuery(selectAgent);

            Statement sqlSubjectStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String selectSubject = "SELECT id, name, phone, email, address, bill, notes FROM subjects";
            rowsSubject = sqlSubjectStatement.executeQuery(selectSubject);

            dataPayment = new Vector<>();
            dataAgent = new Vector<>();
            dataSubject = new Vector<>();

            while (rowsPayment.next()) {
                Payment sample = new Payment(rowsPayment.getInt(1), rowsPayment.getBoolean(2), rowsPayment.getString(3),
                        rowsPayment.getFloat(4), rowsPayment.getDate(5), rowsPayment.getDate(6),
                        rowsPayment.getInt(7), rowsPayment.getInt(8), rowsPayment.getString(9),
                        rowsPayment.getString(10));
                dataPayment.addElement(sample);
            }

            while (rowsAgent.next()) {
                Agent sample = new Agent(rowsAgent.getInt(1), rowsAgent.getString(2), rowsAgent.getString(3), rowsAgent.getString(4), rowsAgent.getFloat(5));
                dataAgent.addElement(sample);
            }

            while (rowsSubject.next()) {
                Subject sample = new Subject(rowsSubject.getInt(1), rowsSubject.getString(2), rowsSubject.getString(3),
                        rowsSubject.getString(4), rowsSubject.getString(5), rowsSubject.getFloat(6),
                        rowsSubject.getString(7));
                dataSubject.addElement(sample);
            }
        } catch (SQLException e) {
            System.out.println("Data read error" + e.getMessage());
        }
        System.out.println("Data read!");
    }

    /**
     * Metoda zapisująca Obiekt podany jako argument do odpowiedniej tabeli w bazie
     * @param data obiekt zawierający wektor z daymi do zapisania w bazie
     */
    public void writeMysqlData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toInsert : incoming) {
                try {
                    rowsPayment.moveToInsertRow();
                    rowsPayment.updateInt("id", toInsert.id);
                    rowsPayment.updateBoolean("accepted", toInsert.accepted);
                    rowsPayment.updateString("type", toInsert.type);
                    rowsPayment.updateFloat("value", toInsert.value);
                    rowsPayment.updateDate("begin_date", (Date) toInsert.begin_date);
                    rowsPayment.updateDate("end_date", (Date) toInsert.end_date);
                    rowsPayment.updateInt("owner_id", toInsert.owner_id);
                    rowsPayment.updateInt("subject_id", toInsert.subject_id);
                    rowsPayment.updateString("document_name", toInsert.document_name);
                    rowsPayment.updateString("notes", toInsert.notes);
                    rowsPayment.insertRow();
                    rowsPayment.moveToCurrentRow();
                    dataPayment.addElement(toInsert);
                    System.out.println("Payment written to mysql");
                } catch (SQLException e2) {
                    System.out.println("Payment add error"+ e2.getMessage());
                }
            }
        } else if (temp.elementAt(0) instanceof Agent) {
            Vector<Agent> incoming = (Vector<Agent>) data;
            for (Agent toInsert : incoming) {
                try {
                    rowsAgent.moveToInsertRow();
                    rowsAgent.updateInt("id", toInsert.id);
                    rowsAgent.updateString("name", toInsert.name);
                    rowsAgent.updateString("phone", toInsert.phone);
                    rowsAgent.updateString("email", toInsert.email);
                    rowsAgent.updateFloat("commission", toInsert.commission);
                    rowsAgent.insertRow();
                    rowsPayment.moveToCurrentRow();
                    dataAgent.addElement(toInsert);
                } catch (SQLException e2) {
                    System.out.println("Agent add error"+ e2.getMessage());
                }
            }
            System.out.println("Agent written");
        } else if (temp.elementAt(0) instanceof Subject) {
            Vector<Subject> incoming = (Vector<Subject>) data;
            for (Subject toInsert : incoming) {
                try {
                    rowsSubject.moveToInsertRow();
                    rowsSubject.updateInt("id", toInsert.id);
                    rowsSubject.updateString("name", toInsert.name);
                    rowsSubject.updateString("phone", toInsert.phone);
                    rowsSubject.updateString("email", toInsert.email);
                    rowsSubject.updateString("address", toInsert.address);
                    rowsSubject.updateFloat("bill", toInsert.bill);
                    rowsSubject.updateString("notes", toInsert.notes);
                    rowsSubject.insertRow();
                    rowsSubject.moveToCurrentRow();
                    dataSubject.addElement(toInsert);
                } catch (SQLException e2) {
                    System.out.println("Subject add error"+ e2.getMessage());
                }
            }
            System.out.println("Subject written");
        }
    }

    /**
     * Metoda usuwająca z bazy danych elementy podane w argumencie
     * @param data obiekt zawierający wektor z danymi do usunięcia
     */
    public void removeMysqlData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toInsert : incoming) {
                try {
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
                try {
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
                try {
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

    /**
     * Metoda aktualizująca obiekty w bazie
     * @param data obiekt zawierający wektor z danymi do aktualizacji
     */
    public void updateMysqlData(Object data) {

        Vector<Object> temp = (Vector<Object>) data;

        if (temp.elementAt(0) instanceof Payment) {
            Vector<Payment> incoming = (Vector<Payment>) data;
            for (Payment toInsert : incoming) {
                try {
                    for (int i = 0; i < dataPayment.size(); i++) {
                        if (dataPayment.elementAt(i).id == toInsert.id) {
                            dataPayment.setElementAt(toInsert,i);
                            rowsPayment.absolute(i + 1);
                            rowsPayment.updateInt("id", toInsert.id);
                            rowsPayment.updateBoolean("accepted", toInsert.accepted);
                            rowsPayment.updateString("type", toInsert.type);
                            rowsPayment.updateFloat("value", toInsert.value);
                            rowsPayment.updateDate("begin_date", (Date) toInsert.begin_date);
                            rowsPayment.updateDate("end_date", (Date) toInsert.end_date);
                            rowsPayment.updateInt("owner_id", toInsert.owner_id);
                            rowsPayment.updateInt("subject_id", toInsert.subject_id);
                            rowsPayment.updateString("document_name", toInsert.document_name);
                            rowsPayment.updateString("notes", toInsert.notes);
                            rowsPayment.updateRow();
                            System.out.println("Payment updated at mysql");
                        }
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (temp.elementAt(0) instanceof Agent) {
            Vector<Agent> incoming = (Vector<Agent>) data;
            for (Agent toInsert : incoming) {
                try {
                    for (int i = 0; i < dataAgent.size(); i++) {
                        if (dataAgent.elementAt(i).id == toInsert.id) {
                            dataAgent.setElementAt(toInsert,i);
                            rowsAgent.absolute(i + 1);
                            rowsAgent.updateInt("id", toInsert.id);
                            rowsAgent.updateString("name", toInsert.name);
                            rowsAgent.updateString("phone", toInsert.phone);
                            rowsAgent.updateString("email", toInsert.email);
                            rowsAgent.updateFloat("commission", toInsert.commission);
                            rowsAgent.updateRow();
                            System.out.println("Agent updated at mysql");
                        }
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (temp.elementAt(0) instanceof Subject) {
            Vector<Subject> incoming = (Vector<Subject>) data;
            for (Subject toInsert : incoming) {
                try {
                    for (int i = 0; i < dataSubject.size(); i++) {
                        if (dataSubject.elementAt(i).id == toInsert.id) {
                            dataSubject.setElementAt(toInsert,i);
                            rowsSubject.absolute(i + 1);
                            rowsSubject.updateInt("id", toInsert.id);
                            rowsSubject.updateString("name", toInsert.name);
                            rowsSubject.updateString("phone", toInsert.phone);
                            rowsSubject.updateString("email", toInsert.email);
                            rowsSubject.updateString("address", toInsert.address);
                            rowsSubject.updateFloat("bill", toInsert.bill);
                            rowsSubject.updateString("notes", toInsert.notes);
                            rowsSubject.updateRow();
                            System.out.println("Subject updated at mysql");
                        }
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

}
