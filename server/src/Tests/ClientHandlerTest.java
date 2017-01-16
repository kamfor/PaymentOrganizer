package Tests;

import model.Agent;
import model.Payment;
import model.Subject;
import org.junit.Test;
import server.DatabaseConnector;
import server.ServerMain;

import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import static org.junit.Assert.*;

public class ClientHandlerTest {
    @Test
    public void getSubjectUpdated() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Subject subjectToUpdate = new Subject(1000000,"Name","500500500","email@email.com","Address",new Float(100.0),"Notes");
        assertEquals(new Float(0),test.getSubjectUpdated(subjectToUpdate,-100).bill);
    }

    @Test
    public void getAgentUpdated() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Agent agentToUpdate = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));
        assertEquals(new Float(0),test.getAgentUpdated(agentToUpdate,-100).commission);
    }


    @Test
    public void updateIncomingRecords() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        try{
            test.updateIncomingRecords(new Object(),new Object());
            fail("expected IOException");
        }catch(IOException|ClassCastException e){

        }
    }

    @Test
    public void getSubjectsOwnerDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1000000),new Integer(1000000),"Doc name","notes"));
        try{
            Vector<Subject> subjectToUpdate = test.getSubjectsOwnerDependent(testPayment,testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }

    @Test
    public void getAgentsOwnerDependent() throws Exception {
        Agent agentToAdd = new Agent(1000000,"Name","500500500","email@email.com",new Float(100.0));
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Agent> agentToUpdate = test.getAgentsOwnerDependent(testPayment,testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }

    @Test
    public void getSubjectsValueDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Subject> SubjectToUpdate = test.getSubjectsValueDependent(testPayment,testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }

    @Test
    public void getAgentsValueDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Agent> agentToUpdate = test.getAgentsValueDependent(testPayment,testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }

    @Test
    public void getSubjectsPaymentAddDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Subject> SubjectToUpdate = test.getSubjectsPaymentAddDependent(testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }

    @Test
    public void getAgentsPaymentAddDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Agent> agentToUpdate = test.getAgentsPaymentAddDependent(testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }

    @Test
    public void getSubjectsPaymentRemoveDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Subject> SubjectToUpdate = test.getSubjectsPaymentRemoveDependent(testPayment);
            fail();
        }catch(NullPointerException e){

        }

    }

    @Test
    public void getAgentsPaymentRemoveDependent() throws Exception {
        DatabaseConnector data = new DatabaseConnector("","","");
        ServerMain.ClientHandler test = new ServerMain.ClientHandler(new Socket(),1,data);
        Vector<Object> testPayment = new Vector<>();
        testPayment.addElement(new Payment(1000000,Boolean.FALSE,"type",new Float(100.0),new java.sql.Date(100),new java.sql.Date(150),new Integer(1),new Integer(1),"Doc name","notes"));
        try{
            Vector<Agent> agentToUpdate = test.getAgentsPaymentRemoveDependent(testPayment);
            fail();
        }catch(NullPointerException e){

        }
    }
}