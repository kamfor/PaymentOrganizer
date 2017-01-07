package client;

import javax.swing.*;
import java.io.IOException;

/**
 * <h1>Klient</h1>
 * Aplikacja klient unożliwia wyświetlanie i edycje danych pobranych z serwera
 * <p>
 * <b>Note:</b> Może zostać uruchomionych wiele aplikacji klienckich
 * @author  Kamil Foryszewski
 * @version 1.1
 * @since   2017-01-02
 */
public class Client {

    static Model GUI;

    public static Database db;

    /**
     * Funkcja główna klasy Klient powołująca interfejs graficzny, jego model oraz obsługę daanych z serwera
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