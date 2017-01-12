package Tests;

import client.Database;
import org.junit.Test;
import server.ServerMain;

import static org.junit.Assert.*;

import java.io.IOException;


/**
 * Created by kamil on 07.01.17.
 */
public class DatabaseTest {

    @Test
    public void removePayment() throws Exception {
        try {
            assertEquals("Error while updating data",new Database().removePayment(0));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }

    }

    @Test
    public void addPayment() throws Exception {
        try {
            assertEquals("Incorrect Value",new Database().addPayment("Type","Incorrect","2017-01-02","2017-01-03","1","1","Name","notes"));
            assertEquals("The date should be in the following format: YYYY-MM-DD",new Database().addPayment("Type","Incorrect","Date","2017-01-03","1","1","Name","notes"));
            assertEquals("The date should be in the following format: YYYY-MM-DD",new Database().addPayment("Type","Incorrect","2017-01-02","Date","1","1","Name","notes"));
            assertEquals("Owner doesn't exist",new Database().addPayment("Type","Incorrect","2017-01-02","2017-01-03","0","1","Name","notes"));
            assertEquals("Subject doesn't exist",new Database().addPayment("Type","Incorrect","2017-01-02","2017-01-03","1","0","Name","notes"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void updatePayment() throws Exception {
        try {
            assertEquals("Incorrect Value",new Database().updatePayment(0,"WrongValue","Value"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void removeAgent() throws Exception {
        try {
            assertEquals("Error while updating data",new Database().removeAgent(0));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void addAgent() throws Exception {
        try {
            assertEquals("Error while sending to data",new Database().addAgent("Name","email@email.com","500500100"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void updateAgent() throws Exception {
        try {
            assertEquals("Error while sending to server",new Database().updateAgent(0,"NewName","Name"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void removeSubject() throws Exception {
        try {
            assertEquals("Error while updating data",new Database().removeSubject(0));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void addSubject() throws Exception {
        try {
            assertEquals("Server sending error",new Database().addSubject("Name","5000000","email@email.com","Address","Notes"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void updateSubject() throws Exception {
        try {
            assertEquals("Error while sending to server",new Database().updateSubject(0,"NewName","Name"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void getPaymentRow() throws Exception {
        assertEquals(-1, Database.getPaymentRow(0));
    }

    @Test
    public void getAgentRow() throws Exception {
        assertEquals(-1, Database.getAgentRow(0));
    }

    @Test
    public void getSubjectRow() throws Exception {
        assertEquals(-1, Database.getSubjectRow(0));
    }

    @Test
    public void connectToServer() throws Exception {

        try{
             new Database();
        }catch(IOException e){
            System.out.println(e.getMessage());
            assertFalse(Boolean.FALSE);
        }
    }

    @Test
    public void sendObject() throws Exception {

        try{
            new Database().sendObject(new Object(), ServerMain.Qualifier.Remove);
            fail("expected IllegalArgumentException");
        } catch(IOException e){

        }
    }
}