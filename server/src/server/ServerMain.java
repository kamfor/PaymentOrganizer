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
    public enum Qualifier {
        Remove, Add, Update, Message;
    }

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



        public ClientHandler(Socket socket, int clientNumber, DatabaseConnector toSend) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.database = toSend;
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
                while (true) {
                    Object incoming = ois.readObject();
                    if(incoming instanceof Qualifier){
                        Qualifier qualifier = (Qualifier)incoming;
                        switch(qualifier){
                            case Remove:
                                incoming = ois.readObject();
                                removeIncomingRecords(incoming);
                            break;
                            case Add:
                                incoming = ois.readObject();
                                addIncomingRecords(incoming);
                            break;
                            case Update:
                                incoming = ois.readObject();
                                Object incomingOld = ois.readObject();
                                updateIncomingRecords(incoming, incomingOld);
                            break;
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                    closeSockets();
            }
        }

        private void updateIncomingRecords(Object incoming,Object incomingOld) throws IOException { //recognize changes
            Vector<Object> temp = (Vector<Object>) incoming;
            Vector<Object> olds = (Vector<Object>) incomingOld;
            if (temp.elementAt(0) instanceof Payment) {
                database.updatePaymentInMysql((Vector<Payment>) incoming);
            } else if (temp.elementAt(0) instanceof Agent) {
                database.updateAgentInMysql((Vector<Agent>) incoming);
            } else if (temp.elementAt(0) instanceof Subject) {
                database.updateSubjectInMysql((Vector<Subject>) incoming);
            }
            writeToClients(incoming, Qualifier.Update);
        }

        private void addIncomingRecords(Object incoming) throws IOException {
            Vector<Object> temp = (Vector<Object>)incoming;
            if (temp.elementAt(0) instanceof Payment) {
                database.addPaymentToMysql((Vector<Payment>)incoming);
                Vector<Agent> agentToUpdate = new Vector<>();
                Vector<Subject> subjectToUpdate = new Vector<>();
                Agent tempAgent;
                Subject tempSubject;

                tempAgent = database.getAgentFromPayment((Payment)temp.elementAt(0));
                tempAgent.commission=tempAgent.commission+((Payment)temp.elementAt(0)).value;
                agentToUpdate.addElement(tempAgent);

                tempSubject = database.getSubjectFromPayment((Payment)temp.elementAt(0));
                tempSubject.bill=tempSubject.bill+((Payment)temp.elementAt(0)).value;
                subjectToUpdate.addElement(tempSubject);

                updateIncomingRecords(agentToUpdate,agentToUpdate);
                updateIncomingRecords(subjectToUpdate,subjectToUpdate);

                database.updateAgentInMysql(agentToUpdate);
                database.updateSubjectInMysql(subjectToUpdate);

                writeToClients(agentToUpdate,Qualifier.Update);
                writeToClients(subjectToUpdate,Qualifier.Update);

            } else if (temp.elementAt(0) instanceof Agent) {
                database.addAgentToMysql((Vector<Agent>)incoming);
            } else if (temp.elementAt(0) instanceof Subject) {
                database.addSubjectToMysql((Vector<Subject>)incoming);
            }
            writeToClients(incoming, Qualifier.Add);
        }

        private void removeIncomingRecords(Object incoming) throws IOException {
            Vector<Object> temp = (Vector<Object>) incoming;
            if (temp.elementAt(0) instanceof Payment) {
                database.removePaymentFromMysql((Vector<Payment>) incoming);
                Vector<Agent> agentToUpdate = new Vector<>();
                Vector<Subject> subjectToUpdate = new Vector<>();
                Agent tempAgent;
                Subject tempSubject;

                tempAgent = database.getAgentFromPayment((Payment)temp.elementAt(0));
                tempAgent.commission=tempAgent.commission-((Payment)temp.elementAt(0)).value;
                agentToUpdate.addElement(tempAgent);

                tempSubject = database.getSubjectFromPayment((Payment)temp.elementAt(0));
                tempSubject.bill=tempSubject.bill-((Payment)temp.elementAt(0)).value;
                subjectToUpdate.addElement(tempSubject);

                database.updateAgentInMysql(agentToUpdate);
                database.updateSubjectInMysql(subjectToUpdate);

                writeToClients(agentToUpdate,Qualifier.Update);
                writeToClients(subjectToUpdate,Qualifier.Update);
            } else if (temp.elementAt(0) instanceof Agent) {
                database.removeAgentFromMysql((Vector<Agent>) incoming);
            } else if (temp.elementAt(0) instanceof Subject) {
                database.removeSubjectFrommMysql((Vector<Subject>) incoming);
            }
            writeToClients(incoming, Qualifier.Remove);
        }

        private void writeStartupData() throws IOException {
            oos.writeObject(Qualifier.Add);
            oos.writeObject(database.dataPayment);
            oos.writeObject(Qualifier.Add);
            oos.writeObject(database.dataAgent);
            oos.writeObject(Qualifier.Add);
            oos.writeObject(database.dataSubject);
        }

        private void closeSockets() {
            try {
                ois.close();
                oos.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close a socket");
            }
        }

        private void writeToClients(Object incoming, Qualifier qualifier) throws IOException {
            for(ClientHandler item :clients){
                if(item.isAlive()) {
                    item.oos.writeObject(qualifier);
                    item.oos.writeObject(incoming);
                }
            }
        }
    }
}
