package Tests;

import static org.junit.Assert.*;

import model.Agent;
import model.Payment;
import model.Subject;
import server.DatabaseConnector;

import java.util.Vector;

/**
 * Created by kamil on 07.01.17.
 */
public class DatabaseControllerConnectorTest {

    DatabaseConnector testDatabase = new DatabaseConnector();

    @org.junit.Test
    public void readMysqlData() throws Exception {
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        assertNotNull(testDatabase.dataPayment);
        assertNotNull(testDatabase.dataAgent);
        assertNotNull(testDatabase.dataSubject);
    }

    @org.junit.Test
    public void writeMysqlData() throws Exception {
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Payment paymentToAdd  = new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes");
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));
        Subject subjectToAdd = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");

        Vector<Payment> paymentVectorToAdd = new Vector<>();
        Vector<Agent> agentVectorToAdd = new Vector<>();
        Vector<Subject> subjectVectorToAdd = new Vector<>();

        paymentVectorToAdd.addElement(paymentToAdd);
        agentVectorToAdd.addElement(agentToAdd);
        subjectVectorToAdd.addElement(subjectToAdd);

        testDatabase.writeMysqlData(agentVectorToAdd);
        testDatabase.writeMysqlData(subjectVectorToAdd);
        testDatabase.writeMysqlData(paymentVectorToAdd);

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

        assertEquals(agentToAdd.id,testDatabase.dataAgent.lastElement().id);
        assertEquals(agentToAdd.name,testDatabase.dataAgent.lastElement().name);
        assertEquals(agentToAdd.phone,testDatabase.dataAgent.lastElement().phone);
        assertEquals(agentToAdd.email,testDatabase.dataAgent.lastElement().email);
        assertEquals(agentToAdd.commission,testDatabase.dataAgent.lastElement().commission);

        assertEquals(subjectToAdd.id,testDatabase.dataSubject.lastElement().id);
        assertEquals(subjectToAdd.name,testDatabase.dataSubject.lastElement().name);
        assertEquals(subjectToAdd.phone,testDatabase.dataSubject.lastElement().phone);
        assertEquals(subjectToAdd.email,testDatabase.dataSubject.lastElement().email);
        assertEquals(subjectToAdd.address,testDatabase.dataSubject.lastElement().address);
        assertEquals(subjectToAdd.bill,testDatabase.dataSubject.lastElement().bill);
        assertEquals(subjectToAdd.notes,testDatabase.dataSubject.lastElement().notes);

        testDatabase.removeMysqlData(agentVectorToAdd);
        testDatabase.removeMysqlData(subjectVectorToAdd);
        testDatabase.removeMysqlData(paymentVectorToAdd);
    }

    @org.junit.Test
    public void removeMysqlData() throws Exception {
        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Payment paymentToAdd  = new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes");
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));
        Subject subjectToAdd = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");

        Vector<Payment> paymentVectorToAdd = new Vector<>();
        Vector<Agent> agentVectorToAdd = new Vector<>();
        Vector<Subject> subjectVectorToAdd = new Vector<>();

        paymentVectorToAdd.addElement(paymentToAdd);
        agentVectorToAdd.addElement(agentToAdd);
        subjectVectorToAdd.addElement(subjectToAdd);

        testDatabase.writeMysqlData(agentVectorToAdd);
        testDatabase.writeMysqlData(subjectVectorToAdd);
        testDatabase.writeMysqlData(paymentVectorToAdd);

        int lastPament = testDatabase.dataPayment.size();
        int lastAgent  = testDatabase.dataAgent.size();
        int lastSubject = testDatabase.dataSubject.size();

        testDatabase.removeMysqlData(agentVectorToAdd);
        testDatabase.removeMysqlData(subjectVectorToAdd);
        testDatabase.removeMysqlData(paymentVectorToAdd);

        assertEquals(lastPament-1,testDatabase.dataPayment.size());
        assertEquals(lastAgent-1,testDatabase.dataAgent.size());
        assertEquals(lastSubject-1,testDatabase.dataSubject.size());
    }

    @org.junit.Test
    public void updateMysqlData() throws Exception {

        testDatabase.connectMysql();
        testDatabase.readMysqlData();
        Payment paymentToAdd  = new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes");
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));
        Subject subjectToAdd = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");

        Vector<Payment> paymentVectorToAdd = new Vector<>();
        Vector<Agent> agentVectorToAdd = new Vector<>();
        Vector<Subject> subjectVectorToAdd = new Vector<>();

        paymentVectorToAdd.addElement(paymentToAdd);
        agentVectorToAdd.addElement(agentToAdd);
        subjectVectorToAdd.addElement(subjectToAdd);

        testDatabase.writeMysqlData(agentVectorToAdd);
        testDatabase.writeMysqlData(subjectVectorToAdd);
        testDatabase.writeMysqlData(paymentVectorToAdd);

        paymentVectorToAdd.lastElement().type = "Type1";
        agentVectorToAdd.lastElement().name = "name1";
        subjectVectorToAdd.lastElement().name = "name1";

        testDatabase.updateMysqlData(agentVectorToAdd);
        testDatabase.updateMysqlData(subjectVectorToAdd);
        testDatabase.updateMysqlData(paymentVectorToAdd);

        assertEquals(paymentVectorToAdd.lastElement().type,testDatabase.dataPayment.lastElement().type);
        assertEquals(agentVectorToAdd.lastElement().name,testDatabase.dataAgent.lastElement().name);
        assertEquals(subjectVectorToAdd.lastElement().name,testDatabase.dataSubject.lastElement().name);

        testDatabase.removeMysqlData(agentVectorToAdd);
        testDatabase.removeMysqlData(subjectVectorToAdd);
        testDatabase.removeMysqlData(paymentVectorToAdd);
    }

}