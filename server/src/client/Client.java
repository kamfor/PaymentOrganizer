package client;

import javax.swing.*;
import java.io.IOException;

/**
 * <h1>Klient</h1>
 * Client database editor, calculating salary for agents.
 * <p>
 * <b>Note:</b> Could be more than one client at the same time
 * @author  Kamil Foryszewski
 * @version 1.1
 * @since   2017-01-02
 */
public class Client {

    static Model GUI;

    public static Database db;

    /**
     * Main GUI function parent for model and view
     * @param args
     */
    public static void main (String[] args) {

        while(true){
            try{
                db = new Database();
                break;
            }catch(IOException e1){
                JOptionPane.showMessageDialog(Client.GUI.gui, "Server connection error", "Connection Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
        GUI = new Model();
        db.receiver.run();
    }
}