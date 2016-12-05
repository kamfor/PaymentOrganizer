import javax.swing.JFrame;

/**
 * The main driver class for the application working with JTables and
 * MySQL to read, edit, create, and delete customer records.
 */
public class Customer {
    /**
     * The GUI object to display data.
     */
    static GUI gui;

    /**
     * The database object used to work with the database server.
     */
    static Database db;

    public static void main (String[] args) {
        db = new Database();
        gui = new GUI(db);

        gui.setColumnWidths(db.columns, 10, 60, 80, 100, 100, 80, 80, 80, 80);
        gui.setSize(1110,700);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }
}