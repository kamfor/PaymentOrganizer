/**
 * Created by kamil on 21.11.16.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import classes.Payment;



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
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {

                //Set streams

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
                            System.out.println("removing from db");
                            database.removeMysqlData(incoming);
                        }
                        else{
                            incoming = ois.readObject();
                            System.out.println("adding to db");
                            database.writeMysqlData(incoming);
                        }
                    }

                }

            } catch (IOException | ClassNotFoundException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        private void log(String message) {System.out.println(message);
        }
    }
}
