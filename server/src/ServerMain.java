/**
 * Created by kamil on 21.11.16.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {
    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9091);
        DatabaseConnector data = new DatabaseConnector();
        int clientNumber = 0;
        data.readMysqlData();

        try {
            while (true) {
                new ClientHandler(listener.accept(), clientNumber++, data).start();
            }
        }finally {
            listener.close();
        }
    }

    // therad handler
    private static class ClientHandler extends Thread {
        private Socket socket;
        private int clientNumber;
        private DatabaseConnector database;

        public ClientHandler(Socket socket, int clientNumber, DatabaseConnector tosend) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.database = tosend;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
        }

        public void run() {
            try {

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                // Send data to client
                oos.writeObject(database.dataPayment);
                System.out.println("payment data sended");
                oos.writeObject(database.dataAgent);
                System.out.println("agent data sended");
                oos.writeObject(database.dataSubject);
                System.out.println("subject data sended");

                //oos.close();

                //Get data from the client improve it add handshake protocol
                while (true) { //check data about removing
                    Object incoming = ois.readObject();
                    if(incoming instanceof Boolean){
                        Boolean removing = (Boolean)incoming;
                        if(removing){
                            incoming = ois.readObject();
                            database.removeMysqlData(incoming);
                        }
                        else{
                            incoming = ois.readObject();
                            System.out.println("adding to db");
                            database.writeMysqlData(incoming);
                        }
                        //add messaging with other clients
                    }else if (incoming instanceof String){
                        System.out.println("jakaś wiadomość przyszła");
                        String messag = (String)incoming;
                        System.out.println(messag);
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }
    }
}
