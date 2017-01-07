package Tests;

import org.junit.Test;

import static org.junit.Assert.*;
import client.Database;
import java.io.IOException;
import java.net.SocketException;


/**
 * Created by kamil on 07.01.17.
 */
public class DatabaseTest {

    @Test
    public void connectToServer() throws Exception {

        try{
             new Database();
        }catch(IOException e){
            System.out.println(e.getMessage());
            assertFalse(Boolean.TRUE);
        }
    }

    @Test
    public void sendObject() throws Exception {

        Database testDatabase = new Database();
        testDatabase.disconnect();
        try{
            testDatabase.sendObject(new Object(),new Integer(0));

            fail("expected IllegalArgumentException");

        } catch(SocketException e){
            //ignore, this exception is expected.
        }
    }
}