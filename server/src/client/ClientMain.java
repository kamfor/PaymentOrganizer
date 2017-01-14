package client;

import javax.swing.*;
import java.io.IOException;

public class ClientMain {

    static Controller ctrl;

    public static Database db;

    public static void main (String[] args) {

        for(int i=0; i<5; i++){
            try{
                db = new Database();
                break;
            }catch(IOException e1){
                JOptionPane.showMessageDialog(ClientMain.ctrl.gui, Messages.connectionError, Messages.error, JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
        ctrl = new Controller();
        try {
            db.receiver.run();
        }catch (NullPointerException e1){
            ClientMain.ctrl.gui.getDefaultCloseOperation();
        }
    }
}