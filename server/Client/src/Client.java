import javax.swing.JFrame;

/**
 * The main driver class for the application working with JTables and
 * MySQL to read, edit, create, and delete customer records.
 */
public class Client {
    /**
     * The GUI object to display data.
     */
    //static View gui;
    static Model GUI;

    /**
     * The database object used to work with the database server.
     */
    public static Database db;

    public static void main (String[] args) {
        db = new Database();
        GUI = new Model();
        db.receiver.run();
    }
}