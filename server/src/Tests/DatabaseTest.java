package Tests;

import client.Database;
import org.junit.Test;
import server.ServerMain;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;

public class DatabaseTest {

    @Test
    public void getADate() throws Exception {
        java.util.Date newDate=null;
        try{
            newDate = Database.getADate("2012-02-02");
        }catch(ParseException e){

        }
        assertEquals("2012-02-02",newDate.toString());

        try{
            newDate = Database.getADate("data");
            fail();
        }catch(ParseException e){

        }
    }

    @Test
    public void removePayment() throws Exception {
        try {
            assertEquals("Error while updating data",new Database("localhost").removePayment(0));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void addPayment() throws Exception {
        try {
            assertEquals("Incorrect Value",new Database("localhost").addPayment("Type","Incorrect","2017-01-02","2017-01-03","1","1","Name","notes"));
            assertEquals("The date should be in the following format: YYYY-MM-DD",new Database("localhost").addPayment("Type","Incorrect","Date","2017-01-03","1","1","Name","notes"));
            assertEquals("The date should be in the following format: YYYY-MM-DD",new Database("localhost").addPayment("Type","Incorrect","2017-01-02","Date","1","1","Name","notes"));
            assertEquals("Owner doesn't exist",new Database("localhost").addPayment("Type","Incorrect","2017-01-02","2017-01-03","0","1","Name","notes"));
            assertEquals("Subject doesn't exist",new Database("localhost").addPayment("Type","Incorrect","2017-01-02","2017-01-03","1","0","Name","notes"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void updatePayment() throws Exception {
        try {
            assertEquals("Incorrect Value",new Database("localhost").updatePayment(0,"WrongValue","Value"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void removeAgent() throws Exception {
        try {
            assertEquals("Error while updating data",new Database("localhost").removeAgent(0));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void addAgent() throws Exception {
        try {
            assertEquals("Error while sending to data",new Database("localhost").addAgent("Name","email@email.com","500500100"));
            fail();
        }catch (IOException e){

        }
    }

    @Test
    public void updateAgent() throws Exception {
        try {
            assertEquals("Error while sending to server",new Database("localhost").updateAgent(0,"NewName","Name"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void removeSubject() throws Exception {
        try {
            assertEquals("Error while updating data",new Database("localhost").removeSubject(0));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void addSubject() throws Exception {
        try {
            assertEquals("Server sending error",new Database("localhost").addSubject("Name","5000000","email@email.com","Address","Notes"));
            fail("expected IllegalArgumentException");
        }catch (IOException e){

        }
    }

    @Test
    public void updateSubject() throws Exception {
        try {
            assertEquals("Error while sending to server",new Database("localhost").updateSubject(0,"NewName","Name"));
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
             new Database("localhost");
        }catch(IOException e){
            System.out.println(e.getMessage());
            assertFalse(Boolean.FALSE);
        }
    }

    @Test
    public void sendObject() throws Exception {

        try{
            new Database("localhost").sendObject(new Object(), ServerMain.Qualifier.Remove);
            fail("expected IllegalArgumentException");
        } catch(IOException e){

        }
    }
}