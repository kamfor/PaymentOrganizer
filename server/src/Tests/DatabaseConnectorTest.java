package Tests;

import static org.junit.Assert.*;

import model.Agent;
import model.Payment;
import model.Subject;
import org.junit.Test;
import server.DatabaseConnector;

import java.util.Vector;

public class DatabaseConnectorTest {
    @Test
    public void connectMysql() throws Exception {

    }

    @Test
    public void addSubjectToMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Subject subjectToAdd = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");

        Vector<Subject> subjectVectorToAdd = new Vector<>();

        subjectVectorToAdd.addElement(subjectToAdd);

        testDatabase.addSubjectToMysql(subjectVectorToAdd);


        assertEquals(subjectToAdd.id,testDatabase.dataSubject.lastElement().id);
        assertEquals(subjectToAdd.name,testDatabase.dataSubject.lastElement().name);
        assertEquals(subjectToAdd.phone,testDatabase.dataSubject.lastElement().phone);
        assertEquals(subjectToAdd.email,testDatabase.dataSubject.lastElement().email);
        assertEquals(subjectToAdd.address,testDatabase.dataSubject.lastElement().address);
        assertEquals(subjectToAdd.bill,testDatabase.dataSubject.lastElement().bill);
        assertEquals(subjectToAdd.notes,testDatabase.dataSubject.lastElement().notes);

        testDatabase.removeSubjectFrommMysql(subjectVectorToAdd);
    }

    @Test
    public void addAgentToMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));

        Vector<Agent> agentVectorToAdd = new Vector<>();

        agentVectorToAdd.addElement(agentToAdd);

        testDatabase.addAgentToMysql(agentVectorToAdd);

        assertEquals(agentToAdd.id,testDatabase.dataAgent.lastElement().id);
        assertEquals(agentToAdd.name,testDatabase.dataAgent.lastElement().name);
        assertEquals(agentToAdd.phone,testDatabase.dataAgent.lastElement().phone);
        assertEquals(agentToAdd.email,testDatabase.dataAgent.lastElement().email);
        assertEquals(agentToAdd.commission,testDatabase.dataAgent.lastElement().commission);

        testDatabase.removeAgentFromMysql(agentVectorToAdd);
    }

    @Test
    public void addPaymentToMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Payment paymentToAdd  = new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes");

        Vector<Payment> paymentVectorToAdd = new Vector<>();

        paymentVectorToAdd.addElement(paymentToAdd);

        testDatabase.addPaymentToMysql(paymentVectorToAdd);

        assertEquals(paymentToAdd.id,testDatabase.dataPayment.lastElement().id);
        assertEquals(paymentToAdd.type,testDatabase.dataPayment.lastElement().type);
        assertEquals(paymentToAdd.accepted,testDatabase.dataPayment.lastElement().accepted);
        assertEquals(paymentToAdd.value,testDatabase.dataPayment.lastElement().value);
        assertEquals(paymentToAdd.begin_date,testDatabase.dataPayment.lastElement().begin_date);
        assertEquals(paymentToAdd.end_date,testDatabase.dataPayment.lastElement().end_date);
        assertEquals(paymentToAdd.owner_id,testDatabase.dataPayment.lastElement().owner_id);
        assertEquals(paymentToAdd.subject_id,testDatabase.dataPayment.lastElement().subject_id);
        assertEquals(paymentToAdd.notes,testDatabase.dataPayment.lastElement().notes);
        assertEquals(paymentToAdd.document_name,testDatabase.dataPayment.lastElement().document_name);

        testDatabase.removePaymentFromMysql(paymentVectorToAdd);
    }

    @Test
    public void removeSubjectFrommMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Subject subjectToAdd = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");

        Vector<Subject> subjectVectorToAdd = new Vector<>();


        subjectVectorToAdd.addElement(subjectToAdd);

        testDatabase.addSubjectToMysql(subjectVectorToAdd);

        int lastSubject = testDatabase.dataSubject.size();

        testDatabase.removeSubjectFrommMysql(subjectVectorToAdd);

        assertEquals(lastSubject-1,testDatabase.dataSubject.size());
    }

    @Test
    public void removeAgentFromMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));

        Vector<Agent> agentVectorToAdd = new Vector<>();

        agentVectorToAdd.addElement(agentToAdd);

        testDatabase.addAgentToMysql(agentVectorToAdd);

        int lastAgent = testDatabase.dataAgent.size();

        testDatabase.removeAgentFromMysql(agentVectorToAdd);

        assertEquals(lastAgent-1,testDatabase.dataAgent.size());
    }

    @Test
    public void removePaymentFromMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Payment paymentToAdd  = new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes");

        Vector<Payment> paymentVectorToAdd = new Vector<>();

        paymentVectorToAdd.addElement(paymentToAdd);

        testDatabase.addPaymentToMysql(paymentVectorToAdd);
        int lastPayment = testDatabase.dataPayment.size();

        testDatabase.removePaymentFromMysql(paymentVectorToAdd);

        assertEquals(lastPayment-1,testDatabase.dataPayment.size());
    }

    @Test
    public void updateSubjectInMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Subject subjectToAdd = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");

        Vector<Subject> subjectVectorToAdd = new Vector<>();

        subjectVectorToAdd.addElement(subjectToAdd);

        testDatabase.addSubjectToMysql(subjectVectorToAdd);

        subjectVectorToAdd.lastElement().name = "name1";

        testDatabase.updateSubjectInMysql(subjectVectorToAdd);

        assertEquals(subjectVectorToAdd.lastElement().name,testDatabase.dataSubject.lastElement().name);

        testDatabase.removeSubjectFrommMysql(subjectVectorToAdd);
    }

    @Test
    public void updateAgentInMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));

        Vector<Agent> agentVectorToAdd = new Vector<>();

        agentVectorToAdd.addElement(agentToAdd);

        testDatabase.addAgentToMysql(agentVectorToAdd);

        agentVectorToAdd.lastElement().name = "name1";

        testDatabase.updateAgentInMysql(agentVectorToAdd);

        assertEquals(agentVectorToAdd.lastElement().name,testDatabase.dataAgent.lastElement().name);

        testDatabase.removeAgentFromMysql(agentVectorToAdd);
    }

    @Test
    public void updatePaymentInMysql() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Payment paymentToAdd  = new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes");

        Vector<Payment> paymentVectorToAdd = new Vector<>();

        paymentVectorToAdd.addElement(paymentToAdd);

        testDatabase.addPaymentToMysql(paymentVectorToAdd);

        paymentVectorToAdd.lastElement().type = "Type1";

        testDatabase.updatePaymentInMysql(paymentVectorToAdd);

        assertEquals(paymentVectorToAdd.lastElement().type,testDatabase.dataPayment.lastElement().type);

        testDatabase.removePaymentFromMysql(paymentVectorToAdd);
    }

    @Test
    public void getAgentFromPayment() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        assertNull(testDatabase.getAgentFromPayment(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1000),new Integer(1000),"Doc name","notes")));
    }

    @Test
    public void getSubjectFromPayment() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        assertNull(testDatabase.getAgentFromPayment(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1000),new Integer(1000),"Doc name","notes")));
    }


    @org.junit.Test
    public void readMysqlData() throws Exception {
        DatabaseConnector testDatabase = new DatabaseConnector("","","");
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        assertNotNull(testDatabase.dataPayment);
        assertNotNull(testDatabase.dataAgent);
        assertNotNull(testDatabase.dataSubject);
    }
}