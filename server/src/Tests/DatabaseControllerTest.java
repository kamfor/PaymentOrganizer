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
    public void removePayment() throws Exception {

    }

    @Test
    public void addPayment() throws Exception {

    }

    @Test
    public void updatePayment() throws Exception {

    }

    @Test
    public void removeAgent() throws Exception {

    }

    @Test
    public void addAgent() throws Exception {

    }

    @Test
    public void updateAgent() throws Exception {

    }

    @Test
    public void removeSubject() throws Exception {

    }

    @Test
    public void addSubject() throws Exception {

    }

    @Test
    public void updateSubject() throws Exception {

    }

    @Test
    public void getPaymentRow() throws Exception {

    }

    @Test
    public void getAgentRow() throws Exception {

    }

    @Test
    public void getSubjectRow() throws Exception {

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