package server;

// odrzucanie zmian w wyścigu
// plik konfiguracyjny ze ściezką do bazy danych
// wykrywanie kolizji przy update wysyłamy zarówno co się zmieniło i z czego się zmieniło
// w widoku jakoś id reprezentować żeby było ładniej ale to niekoniecznie
// skrócić funkcje
// testy tylko logiki
// logika po stronie serwera
// krytyczne wartości
// więcej funkcji enkapsulacja
//adres ip servera i potr jako parametr uruchomienia

import model.Agent;
import model.Payment;
import model.Subject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {
    public static Vector<ClientHandler> clients = new Vector<>();

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
        }catch(IOException e){
                e.printStackTrace();
        }finally {
            listener.close();
        }
    }

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
                writeStartupData();
                while (true) { // za dużo tego tutaj i dodaj jeszcze wysyłanie stringa do kilenta żeby go poinformować o błędach różnych
                    Object incoming = ois.readObject();
                    if(incoming instanceof Integer){
                        Integer qualifier = (Integer) incoming;
                        if(qualifier==0){ //remove
                            incoming = ois.readObject();
                            Vector<Object> temp = (Vector<Object>) incoming;
                            if (temp.elementAt(0) instanceof Payment) {
                                database.removePaymentFromMysql((Vector<Payment>) incoming);
                                //update salaries
                            } else if (temp.elementAt(0) instanceof Agent) {
                                database.removeAgentFromMysql((Vector<Agent>) incoming);
                            } else if (temp.elementAt(0) instanceof Subject) {
                                database.removeSubjectFrommMysql((Vector<Subject>) incoming);
                            }
                            writeToClients(incoming, qualifier);
                        }
                        else if(qualifier==1){ //add
                            incoming = ois.readObject();
                            Vector<Object> temp = (Vector<Object>)incoming;
                            if (temp.elementAt(0) instanceof Payment) {
                                database.addPaymentToMysql((Vector<Payment>)incoming);
                                //update salaries
                            } else if (temp.elementAt(0) instanceof Agent) {
                                database.addAgentToMysql((Vector<Agent>)incoming);
                            } else if (temp.elementAt(0) instanceof Subject) {
                                database.addSubjectToMysql((Vector<Subject>)incoming);
                            }
                            writeToClients(incoming, qualifier);
                        }
                        else if(qualifier==2){ //update
                            incoming = ois.readObject();
                            Vector<Object> temp = (Vector<Object>) incoming;
                            incoming = ois.readObject();
                            Vector<Object> olds = (Vector<Object>) incoming;
                            if (temp.elementAt(0) instanceof Payment) {
                                database.updatePaymentInMysql((Vector<Payment>) incoming);
                                //update salaries
                            } else if (temp.elementAt(0) instanceof Agent) {
                                database.updateAgentInMysql((Vector<Agent>) incoming);
                            } else if (temp.elementAt(0) instanceof Subject) {
                                database.updateSubjectInMysql((Vector<Subject>) incoming);
                            }
                            writeToClients(incoming, qualifier);
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    closeSockets();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }

        private void writeStartupData() throws IOException {
            oos.writeObject(new Integer(1));
            oos.writeObject(database.dataPayment);
            oos.writeObject(new Integer(1));
            oos.writeObject(database.dataAgent);
            oos.writeObject(new Integer(1));
            oos.writeObject(database.dataSubject);
        }

        private void closeSockets() throws IOException {
            ois.close();
            oos.close();
            socket.close();
        }

        private void writeToClients(Object incoming, Integer qualifier) throws IOException {
            for(ClientHandler item :clients){
                if(item.isAlive()) {
                    item.oos.writeObject(qualifier);
                    item.oos.writeObject(incoming);
                }
            }
        }
    }
}
