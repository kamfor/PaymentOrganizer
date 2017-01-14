package server;

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
        Remove, Add, Update, Message
    }

    public static void main(String[] args) throws IOException { //catch parameters from args
        String url="";
        String user="";
        String pass="";
        ServerSocket listener = new ServerSocket(9091);
        DatabaseConnector data = new DatabaseConnector(url,user,pass);
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

    private static class ClientHandler extends Thread { //make tests
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

        private void updateIncomingRecords(Object incoming,Object incomingOld) throws IOException {
            Vector<Object> temp = (Vector<Object>) incoming;
            Vector<Object> oldOne = (Vector<Object>) incomingOld;
            if (temp.elementAt(0) instanceof Payment) {
                database.updatePaymentInMysql((Vector<Payment>) incoming);

                if(((Payment) temp.elementAt(0)).value!=((Payment) oldOne.elementAt(0)).value){
                    Vector<Agent> agentToUpdate = getAgentsValueDependent(temp, oldOne);
                    Vector<Subject> subjectToUpdate = getSubjectsValueDependent(temp, oldOne);
                    database.updateAgentInMysql(agentToUpdate);
                    database.updateSubjectInMysql(subjectToUpdate);
                    writeToClients(agentToUpdate,Qualifier.Update);
                    writeToClients(subjectToUpdate,Qualifier.Update);
                }
                if(((Payment) temp.elementAt(0)).owner_id!=((Payment) oldOne.elementAt(0)).owner_id){
                    Vector<Agent> agentToUpdate = getAgentsOwnerDependent(temp, oldOne);
                    database.updateAgentInMysql(agentToUpdate);
                    writeToClients(agentToUpdate,Qualifier.Update);
                }
                if(((Payment) temp.elementAt(0)).subject_id!=((Payment) oldOne.elementAt(0)).subject_id){
                    Vector<Subject> subjectToUpdate = getSubjectsOwnerDependent(temp, oldOne);
                    database.updateSubjectInMysql(subjectToUpdate);
                    writeToClients(subjectToUpdate,Qualifier.Update);
                }

            } else if (temp.elementAt(0) instanceof Agent) {
                database.updateAgentInMysql((Vector<Agent>) incoming);
            } else if (temp.elementAt(0) instanceof Subject) {
                database.updateSubjectInMysql((Vector<Subject>) incoming);
            }
            writeToClients(incoming, Qualifier.Update);
        }

        private Vector<Subject> getSubjectsOwnerDependent(Vector<Object> temp, Vector<Object> oldOne) {
            Subject tempSubject;
            Vector<Subject> subjectToUpdate = new Vector<>();
            tempSubject = database.getSubjectFromPayment((Payment)temp.elementAt(0));
            tempSubject.bill+=((Payment)temp.elementAt(0)).value;
            subjectToUpdate.addElement(new Subject(tempSubject.id,tempSubject.name,tempSubject.phone,tempSubject.email,tempSubject.address,tempSubject.bill,tempSubject.notes));
            tempSubject = database.getSubjectFromPayment((Payment)oldOne.elementAt(0));
            tempSubject.bill-=((Payment)temp.elementAt(0)).value;
            subjectToUpdate.addElement(new Subject(tempSubject.id,tempSubject.name,tempSubject.phone,tempSubject.email,tempSubject.address,tempSubject.bill,tempSubject.notes));
            return subjectToUpdate;
        }

        private Vector<Agent> getAgentsOwnerDependent(Vector<Object> temp, Vector<Object> oldOne) {
            Agent tempAgent;
            Vector<Agent> agentToUpdate = new Vector<>();
            tempAgent = database.getAgentFromPayment((Payment)temp.elementAt(0));
            tempAgent.commission +=((Payment)temp.elementAt(0)).value;
            agentToUpdate.addElement(new Agent(tempAgent.id,tempAgent.name,tempAgent.phone,tempAgent.email,tempAgent.commission));
            tempAgent = database.getAgentFromPayment((Payment)oldOne.elementAt(0));
            tempAgent.commission -=((Payment)temp.elementAt(0)).value;
            agentToUpdate.addElement(new Agent(tempAgent.id,tempAgent.name,tempAgent.phone,tempAgent.email,tempAgent.commission));
            return agentToUpdate;
        }

        private Vector<Subject> getSubjectsValueDependent(Vector<Object> temp, Vector<Object> oldOne) {
            Subject tempSubject;
            Vector<Subject> subjectToUpdate = new Vector<>();
            tempSubject = database.getSubjectFromPayment((Payment)temp.elementAt(0));
            tempSubject.bill+=(((Payment)temp.elementAt(0)).value-((Payment)oldOne.elementAt(0)).value);
            subjectToUpdate.addElement(new Subject(tempSubject.id,tempSubject.name,tempSubject.phone,tempSubject.email,tempSubject.address,tempSubject.bill,tempSubject.notes));
            return subjectToUpdate;
        }

        private Vector<Agent> getAgentsValueDependent(Vector<Object> temp, Vector<Object> oldOne) {
            Agent tempAgent;
            Vector<Agent> agentToUpdate = new Vector<>();
            tempAgent = database.getAgentFromPayment((Payment)temp.elementAt(0));
            tempAgent.commission +=(((Payment)temp.elementAt(0)).value-((Payment)oldOne.elementAt(0)).value);
            agentToUpdate.addElement(new Agent(tempAgent.id,tempAgent.name,tempAgent.phone,tempAgent.email,tempAgent.commission));
            return agentToUpdate;
        }

        private void addIncomingRecords(Object incoming) throws IOException {
            Vector<Object> temp = (Vector<Object>)incoming;
            if (temp.elementAt(0) instanceof Payment) {
                database.addPaymentToMysql((Vector<Payment>)incoming);
                Vector<Agent> agentToUpdate = getAgentsPaymentAddDependent(temp);
                Vector<Subject> subjectToUpdate = getSubjectsPaymentAddDependent(temp);
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

        private Vector<Subject> getSubjectsPaymentAddDependent(Vector<Object> temp) {
            Subject tempSubject;
            Vector<Subject> subjectToUpdate = new Vector<>();
            tempSubject = database.getSubjectFromPayment((Payment)temp.elementAt(0));
            tempSubject.bill=tempSubject.bill+((Payment)temp.elementAt(0)).value;
            subjectToUpdate.addElement(new Subject(tempSubject.id,tempSubject.name,tempSubject.phone,tempSubject.email,tempSubject.address,tempSubject.bill,tempSubject.notes));
            return subjectToUpdate;
        }

        private Vector<Agent> getAgentsPaymentAddDependent(Vector<Object> temp) {
            Agent tempAgent;
            Vector<Agent> agentToUpdate = new Vector<>();
            tempAgent = database.getAgentFromPayment((Payment)temp.elementAt(0));
            tempAgent.commission=tempAgent.commission+((Payment)temp.elementAt(0)).value;
            agentToUpdate.addElement(new Agent(tempAgent.id,tempAgent.name,tempAgent.phone,tempAgent.email,tempAgent.commission));
            return agentToUpdate;
        }

        private void removeIncomingRecords(Object incoming) throws IOException {
            Vector<Object> temp = (Vector<Object>) incoming;
            if (temp.elementAt(0) instanceof Payment) {
                database.removePaymentFromMysql((Vector<Payment>) incoming);
                Vector<Agent> agentToUpdate = getAgentsPaymentRemoveDependent(temp);
                Vector<Subject> subjectToUpdate = getSubjectsPaymentRemoveDependent(temp);
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

        private Vector<Subject> getSubjectsPaymentRemoveDependent(Vector<Object> temp) {
            Subject tempSubject;
            Vector<Subject> subjectToUpdate = new Vector<>();
            tempSubject = database.getSubjectFromPayment((Payment)temp.elementAt(0));
            tempSubject.bill=tempSubject.bill-((Payment)temp.elementAt(0)).value;
            subjectToUpdate.addElement(new Subject(tempSubject.id,tempSubject.name,tempSubject.phone,tempSubject.email,tempSubject.address,tempSubject.bill,tempSubject.notes));
            return subjectToUpdate;
        }

        private Vector<Agent> getAgentsPaymentRemoveDependent(Vector<Object> temp) {
            Agent tempAgent;
            Vector<Agent> agentToUpdate = new Vector<>();
            tempAgent = database.getAgentFromPayment((Payment)temp.elementAt(0));
            tempAgent.commission=tempAgent.commission-((Payment)temp.elementAt(0)).value;
            agentToUpdate.addElement(new Agent(tempAgent.id,tempAgent.name,tempAgent.phone,tempAgent.email,tempAgent.commission));
            return agentToUpdate;
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
