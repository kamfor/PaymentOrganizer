package client;

import javax.swing.*;
import java.io.IOException;

/**
 * <h1>Client</h1>
 * ClientMain database editor, calculating salary for agents.
 * <p>
 * <b>Note:</b> Could be more than one client at the same time
 * @author  Kamil Foryszewski
 * @version 1.1
 * @since   2017-01-02
 */
public class ClientMain {

    static Controller ctrl;

    public static DatabaseController db;

    /**
     * Main GUI function parent for model, view and controller
     * @param args
     */
    public static void main (String[] args) {

        for(int i=0; i<5; i++){
            try{
                db = new DatabaseController();
                break;
            }catch(IOException e1){
                JOptionPane.showMessageDialog(ClientMain.ctrl.gui, "Błąd połączenia z serwerem", "Błąd połączenia", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
        ctrl = new Controller();
        try {
            db.receiver.run();
        }catch (NullPointerException e1){
            ClientMain.ctrl.gui.getDefaultCloseOperation(); // zam
        }
    }
}