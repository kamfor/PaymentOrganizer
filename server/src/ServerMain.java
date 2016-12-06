/**
 * Created by kamil on 21.11.16.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import classes.Payment;



public class ServerMain {
    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9091);
        Socket fromClientSocket;
        Payment incoming = null;
        DatabaseConnector data = new DatabaseConnector();
        data.readMysqlData();

        //System.out.println(data.data.get(0));

        String str_date="2016-12-15";
        DateFormat formatter ;
        Date date = new Date();
        formatter = new SimpleDateFormat("YYYY-MM-DD");
        try{
            date = formatter.parse(str_date);
        }
        catch(java.text.ParseException e){
            e.printStackTrace();
        }
        Payment outgoing = data.data.get(0);

        fromClientSocket = listener.accept();

        ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());

        oos.writeObject(outgoing);

        oos.close();

        fromClientSocket.close();
    }
}
