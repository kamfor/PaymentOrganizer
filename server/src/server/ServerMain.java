package server;
/**
 * Created by kamil on 21.11.16.
 * * <h1>Server</h1>
 * Client handling application
 * <p>
 * <b>Note:</b> There is no GUI interface 
 *
 * @author  Kamil Foryszewski
 * @version 1.1
 * @since   2017.01.02
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {
    public static Vector<ClientHandler> clients = new Vector<>();

    /**
     * Main function
     * Creating socket server and handling incoming connections in new threads
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9091);
        DatabaseConnector data = new DatabaseConnector();
        int clientNumber;
        data.connectMysql();

        try {
            while (true) {
                clientNumber = clients.size();
                clients.addElement(new ClientHandler(listener.accept(), clientNumber, data));
                data.readMysqlData();
                clients.lastElement().start();
            }
        }finally {
            listener.close();
        }
    }

    /**
     * Thread extension to create new runnable
     * New one for every incoming client connection
     */
    private static class ClientHandler extends Thread {
        private Socket socket;
        private int clientNumber;
        private DatabaseConnector database;
        protected ObjectOutputStream oos;
        protected ObjectInputStream ois;

        public ClientHandler(Socket socket, int clientNumber, DatabaseConnector tosend) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.database = tosend;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);

            try{
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            }catch (IOException e1) {
                System.out.println("Error handling client# " + clientNumber + ": " + e1);
            }
        }

        public void run() {
            try {

                oos.writeObject(new Integer(1));
                oos.writeObject(database.dataPayment);
                System.out.println("payment data sended");
                oos.writeObject(new Integer(1));
                oos.writeObject(database.dataAgent);
                System.out.println("agent data sended");
                oos.writeObject(new Integer(1));
                oos.writeObject(database.dataSubject);
                System.out.println("subject data sended");

                while (true) {
                    Object incoming = ois.readObject();
                    if(incoming instanceof Integer){
                        Integer qualifier = (Integer) incoming;
                        if(qualifier==0){
                            incoming = ois.readObject();
                            database.removeMysqlData(incoming);
                            for(ClientHandler item :clients){
                                if(item.isAlive()) {
                                    item.oos.writeObject(qualifier);
                                    item.oos.writeObject(incoming);
                                    System.out.println("removed from client: " + item.clientNumber);
                                }
                            }
                        }
                        else if(qualifier==1){
                            incoming = ois.readObject();
                            database.writeMysqlData(incoming);
                            for(ClientHandler item :clients){
                                if(item.isAlive()) {
                                    item.oos.writeObject(qualifier);
                                    item.oos.writeObject(incoming);
                                    System.out.println("added to client: " + item.clientNumber);
                                }
                            }
                        }
                        else if(qualifier==2){
                            incoming = ois.readObject();
                            database.updateMysqlData(incoming); //change to update
                            for(ClientHandler item :clients){
                                if(item.isAlive()) {
                                    item.oos.writeObject(qualifier);
                                    item.oos.writeObject(incoming);
                                    System.out.println("Update send to client: " + item.clientNumber);
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }
    }
}
