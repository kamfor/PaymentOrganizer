package Tests;

import org.junit.Test;

import static org.junit.Assert.*;
import client.DatabaseController;
import java.io.IOException;
import java.net.SocketException;


/**
 * Created by kamil on 07.01.17.
 */
public class DatabaseControllerTest {

    @Test
    public void removePayment() throws Exception { //check this test
        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.addPayment("Type","100","2017-01-02","2017-01-03","1","1","Name","notes");
        assertEquals("",testDatabaseController.removePayment(DatabaseController.rowDataPayment.size()-1));
    }

    @Test
    public void addPayment() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        assertEquals("",testDatabaseController.addPayment("Type","100","2017-01-02","2017-01-03","1","1","Name","notes"));
        testDatabaseController.removePayment(DatabaseController.rowDataPayment.size()-1);
        assertEquals("Incorrect Value",testDatabaseController.addPayment("Type","Incorrect","2017-01-02","2017-01-03","1","1","Name","notes"));
        assertEquals("The date should be in the following format: YYYY-MM-DD",testDatabaseController.addPayment("Type","Incorrect","Date","2017-01-03","1","1","Name","notes"));
        assertEquals("The date should be in the following format: YYYY-MM-DD",testDatabaseController.addPayment("Type","Incorrect","2017-01-02","Date","1","1","Name","notes"));
        assertEquals("Owner doesn't exist",testDatabaseController.addPayment("Type","Incorrect","2017-01-02","2017-01-03","0","1","Name","notes"));
        assertEquals("Subject doesn't exist",testDatabaseController.addPayment("Type","Incorrect","2017-01-02","2017-01-03","1","0","Name","notes"));
    }

    @Test
    public void updatePayment() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.addPayment("Type","100","2017-01-02","2017-01-03","1","1","Name","notes");
        assertEquals("",testDatabaseController.updatePayment(DatabaseController.rowDataPayment.size()-1,"NewType","Type"));
        assertEquals(DatabaseController.rowDataPayment.lastElement().type,"NewType");
        testDatabaseController.removePayment(DatabaseController.rowDataPayment.size()-1);
        //check all dependence this way
    }

    @Test
    public void removeAgent() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.addAgent("Name","email@email.com","500500100");
        assertEquals("",testDatabaseController.removeAgent(DatabaseController.rowDataAgent.size()-1));
    }

    @Test
    public void addAgent() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        assertEquals("",testDatabaseController.addAgent("Name","email@email.com","500500100"));
        testDatabaseController.removeAgent(DatabaseController.rowDataAgent.size()-1);
    }

    @Test
    public void updateAgent() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.addAgent("Name","email@email.com","500500100");
        assertEquals("",testDatabaseController.updateAgent(DatabaseController.rowDataAgent.size()-1,"NewName","Name"));
        assertEquals(DatabaseController.rowDataAgent.lastElement().name,"NewName");
        testDatabaseController.removeAgent(DatabaseController.rowDataAgent.size()-1);
    }

    @Test
    public void removeSubject() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.addSubject("Name","5000000","email@email.com","Address","Notes");
        assertEquals("",testDatabaseController.removeSubject(DatabaseController.rowDataSubject.size()-1));
    }

    @Test
    public void addSubject() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        assertEquals("",testDatabaseController.addSubject("Name","5000000","email@email.com","Address","Notes"));
        testDatabaseController.removeSubject(DatabaseController.rowDataSubject.size()-1);
    }

    @Test
    public void updateSubject() throws Exception {
        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.addSubject("Name","5000000","email@email.com","Address","Notes");
        assertEquals("",testDatabaseController.updateSubject(DatabaseController.rowDataSubject.size()-1,"NewName","Name"));
        assertEquals(DatabaseController.rowDataSubject.lastElement().name,"NewName");
        testDatabaseController.removeSubject(DatabaseController.rowDataSubject.size()-1);
    }

    @Test
    public void getPaymentRow() throws Exception {
        assertEquals(-1,DatabaseController.getPaymentRow(0));
    }

    @Test
    public void getAgentRow() throws Exception {
        assertEquals(-1,DatabaseController.getAgentRow(0));
    }

    @Test
    public void getSubjectRow() throws Exception {
        assertEquals(-1,DatabaseController.getSubjectRow(0));
    }

    @Test
    public void connectToServer() throws Exception {

        try{
             new DatabaseController();
        }catch(IOException e){
            System.out.println(e.getMessage());
            assertFalse(Boolean.TRUE);
        }
    }

    @Test
    public void sendObject() throws Exception {

        DatabaseController testDatabaseController = new DatabaseController();
        testDatabaseController.disconnect();
        try{
            testDatabaseController.sendObject(new Object(),new Integer(0));

            fail("expected IllegalArgumentException");

        } catch(SocketException e){
            //ignore, this exception is expected.
        }
    }
}