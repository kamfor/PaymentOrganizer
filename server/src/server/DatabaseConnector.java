package server;

import model.Payment;
import model.Agent;
import model.Subject;
import java.sql.*;
import java.util.Vector;

public class DatabaseConnector {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL = "jdbc:mysql://s52.hekko.net.pl/kfforex_java";

    static String USER = "kfforex_java";
    static String PASS = "fasada";

    private Connection conn;

    private ResultSet rowsPayment;
    private ResultSet rowsAgent;
    private ResultSet rowsSubject;
    public Vector<Payment> dataPayment;
    public Vector<Agent> dataAgent;
    public Vector<Subject> dataSubject;

    public DatabaseConnector(String dbUrl, String dbUser, String dbPass){
        if(!dbUrl.isEmpty())DB_URL = dbUrl;
        if(!dbUser.isEmpty())USER = dbUser;
        if(!dbPass.isEmpty())PASS = dbPass;
        dataPayment = new Vector<>();
        dataAgent = new Vector<>();
        dataSubject = new Vector<>();
    }

    public void connectMysql(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(SQLException | ClassNotFoundException e1){
            System.out.println("Database connection error" + e1.getMessage());
        }

    }

    public void readMysqlData() {
        try {
            readPaymentTable();
            readAgentTable();
            readSubjectTable();

        } catch (SQLException e) {
            System.out.println("Data read error" + e.getMessage());
        }
        System.out.println("Data read!");
    }

    private void readSubjectTable() throws SQLException {
        Statement sqlSubjectStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String selectSubject = "SELECT id, name, phone, email, address, bill, notes FROM subjects";
        rowsSubject = sqlSubjectStatement.executeQuery(selectSubject);
        dataSubject = new Vector<>();
        while (rowsSubject.next()) {
            Subject sample = new Subject(rowsSubject.getInt(1), rowsSubject.getString(2), rowsSubject.getString(3),
                    rowsSubject.getString(4), rowsSubject.getString(5), rowsSubject.getFloat(6),
                    rowsSubject.getString(7));
            dataSubject.addElement(sample);
        }
    }

    private void readAgentTable() throws SQLException {
        Statement sqlAgentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String selectAgent = "SELECT id, name, phone, email, commission FROM agents";
        rowsAgent = sqlAgentStatement.executeQuery(selectAgent);
        dataAgent = new Vector<>();
        while (rowsAgent.next()) {
            Agent sample = new Agent(rowsAgent.getInt(1),rowsAgent.getString(2),rowsAgent.getString(3),
                                    rowsAgent.getString(4),rowsAgent.getFloat(5));
            dataAgent.addElement(sample);
        }
    }

    private void readPaymentTable() throws SQLException {
        Statement sqlPaymentStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String selectPayment = "SELECT id, accepted, type, value, begin_date, end_date, owner_id," +
                                " subject_id, document_name, notes FROM payments";
        rowsPayment = sqlPaymentStatement.executeQuery(selectPayment);
        dataPayment = new Vector<>();
        while (rowsPayment.next()) {
            Payment sample = new Payment(rowsPayment.getInt(1), rowsPayment.getBoolean(2), rowsPayment.getString(3),
                    rowsPayment.getFloat(4), rowsPayment.getDate(5), rowsPayment.getDate(6),
                    rowsPayment.getInt(7), rowsPayment.getInt(8), rowsPayment.getString(9),
                    rowsPayment.getString(10));
            dataPayment.addElement(sample);
        }
    }

    public void addSubjectToMysql(Vector<Subject> data) {
        for (Subject toInsert : data) {
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
    }

    public void addAgentToMysql(Vector<Agent> data) {
        for (Agent toInsert : data) {
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
    }

    public void addPaymentToMysql(Vector<Payment> data) {
        for (Payment toInsert : data) {
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
    }

    public void removeSubjectFrommMysql(Vector<Subject> data) {
        Vector<Subject> incoming = data;
        for (Subject toInsert : incoming) {
            try {
                int rowToRemove = getSubjectRow(toInsert.id);
                dataSubject.removeElementAt(rowToRemove);
                rowsSubject.absolute(rowToRemove + 1);
                rowsSubject.deleteRow();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void removeAgentFromMysql(Vector<Agent> data) {
        Vector<Agent> incoming = data;
        for (Agent toInsert : incoming) {
            try {
                int rowToRemove = getAgentRow(toInsert.id);
                dataAgent.removeElementAt(rowToRemove);
                rowsAgent.absolute(rowToRemove + 1);
                rowsAgent.deleteRow();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void removePaymentFromMysql(Vector<Payment> data) {
        Vector<Payment> incoming = data;
        for (Payment toInsert : incoming) {
            try {
                int rowToRemove = getPaymentRow(toInsert.id);
                dataPayment.removeElementAt(rowToRemove);
                rowsPayment.absolute(rowToRemove + 1);
                rowsPayment.deleteRow();
                //update values
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateSubjectInMysql(Vector<Subject> data) {
        for (Subject toInsert : data) {
            try {
                int rowToUpdate = getSubjectRow(toInsert.id);
                dataSubject.setElementAt(toInsert,rowToUpdate);
                rowsSubject.absolute(rowToUpdate + 1);
                rowsSubject.updateInt("id", toInsert.id);
                rowsSubject.updateString("name", toInsert.name);
                rowsSubject.updateString("phone", toInsert.phone);
                rowsSubject.updateString("email", toInsert.email);
                rowsSubject.updateString("address", toInsert.address);
                rowsSubject.updateFloat("bill", toInsert.bill);
                rowsSubject.updateString("notes", toInsert.notes);
                rowsSubject.updateRow();
                System.out.println("Subject updated at mysql");
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updateAgentInMysql(Vector<Agent> data) {
        for (Agent toInsert : data) {
            try {
                int rowToUpdate = getAgentRow(toInsert.id);
                dataAgent.setElementAt(toInsert,rowToUpdate);
                rowsAgent.absolute(rowToUpdate + 1);
                rowsAgent.updateInt("id", toInsert.id);
                rowsAgent.updateString("name", toInsert.name);
                rowsAgent.updateString("phone", toInsert.phone);
                rowsAgent.updateString("email", toInsert.email);
                rowsAgent.updateFloat("commission", toInsert.commission);
                rowsAgent.updateRow();
                System.out.println("Agent updated at mysql");
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void updatePaymentInMysql(Vector<Payment> data) {
        for (Payment toInsert : data) {
            try {
                int rowToUpdate = getPaymentRow(toInsert.id);
                dataPayment.setElementAt(toInsert,rowToUpdate);
                rowsPayment.absolute(rowToUpdate + 1);
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
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private int getPaymentRow(int toFind){
        for (int i = 0; i < dataPayment.size(); i++) {
            if (dataPayment.elementAt(i).id == toFind) return i;
        }
        return  -1;
    }

    private int getAgentRow(int toFind){
        for (int i = 0; i < dataAgent.size(); i++) {
            if (dataAgent.elementAt(i).id == toFind) return i;
        }
        return  -1;
    }

    private int getSubjectRow(int toFind){
        for (int i = 0; i < dataSubject.size(); i++) {
            if (dataSubject.elementAt(i).id == toFind) return i;
        }
        return  -1;
    }

    public Agent getAgentFromPayment (Payment element){
        try{
            return dataAgent.elementAt(getAgentRow(element.owner_id));
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public Subject getSubjectFromPayment (Payment element){
        try{
            return dataSubject.elementAt(getSubjectRow(element.subject_id));
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }
}
