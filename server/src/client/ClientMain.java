package client;

import javax.swing.*;
import java.io.IOException;

public class ClientMain {

    static Controller ctrl;

    public static Database db;

    public static void main (String[] args) {


        String serverIp;

        for(int i=0; i<2; i++){
            try{
                serverIp = JOptionPane.showInputDialog("Enter Input:","localhost");
                db = new Database(serverIp);
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