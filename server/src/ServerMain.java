/**
 * Created by kamil on 21.11.16.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {
    /**
     * Runs the server.
     */
    public static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9091);
        DatabaseConnector data = new DatabaseConnector();

        int clientNumber = 0;
        data.readMysqlData();

        try {
            while (true) {
                clientNumber = clients.size();
                clients.addElement(new ClientHandler(listener.accept(), clientNumber, data));
                clients.lastElement().start();
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

                oos.writeObject(Boolean.FALSE);
                oos.writeObject(database.dataPayment);
                System.out.println("payment data sended");
                oos.writeObject(Boolean.FALSE);
                oos.writeObject(database.dataAgent);
                System.out.println("agent data sended");
                oos.writeObject(Boolean.FALSE);
                oos.writeObject(database.dataSubject);
                System.out.println("subject data sended");

                while (true) {
                    Object incoming = ois.readObject();
                    if(incoming instanceof Boolean){
                        Boolean removing = (Boolean)incoming;
                        if(removing){
                            incoming = ois.readObject();
                            database.removeMysqlData(incoming);
                            for(ClientHandler item :clients){
                                item.oos.writeObject(removing);
                                item.oos.writeObject(incoming);
                                System.out.println("removed from client"+item.clientNumber);
                            }
                        }
                        else{
                            incoming = ois.readObject();
                            database.writeMysqlData(incoming);
                            for(ClientHandler item :clients){
                                if(item.isAlive())
                                item.oos.writeObject(removing);
                                item.oos.writeObject(incoming);
                                System.out.println("added to client"+item.clientNumber);
                            }
                        }
                    }else if (incoming instanceof String){
                        System.out.println("Incoming massage");
                        String messag = (String)incoming;
                        System.out.println(messag);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    clients.removeElementAt(clientNumber);
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
