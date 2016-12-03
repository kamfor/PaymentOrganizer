/**
 * Created by kamil on 21.11.16.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import classes.Payment;



public class ServerMain {
    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);
        Socket fromClientSocket;
        Payment incoming = null;

        fromClientSocket = listener.accept();

        ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());

        ObjectInputStream ois = new ObjectInputStream(fromClientSocket.getInputStream());

        try {
            incoming = (Payment)ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(incoming!=null)incoming.printPaymentObject();
        else System.out.println("Not working");

        oos.close();

        fromClientSocket.close();
    }
}
