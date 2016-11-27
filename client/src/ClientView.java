import oracle.jrockit.jfr.JFR;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.Vector;


public class ClientView {
    public static void main(String args[]) {

        Vector<Vector> rowData = new Vector<Vector>();

        Payment sample = new Payment(12,"bill","100","12-11-2016","29-11-2016",1,1,"FV123/2","utd");
        Payment sample1 = new Payment(112,"bilhjkl","1050","12-11-2016","29-11-2016",2,2,"FV153/2","ulktd");

        Vector<String> rowOne = new Vector<String>();
        sample.toString(rowOne);
        rowData.addElement(new Vector(rowOne));
        sample1.toString(rowOne);
        rowData.addElement(new Vector(rowOne));

        Vector<String> columnNames = new Vector<String>();
        columnNames.addElement("ID");
        columnNames.addElement("Type");
        columnNames.addElement("Value");
        columnNames.addElement("Begin Date");
        columnNames.addElement("End Date");
        columnNames.addElement("Owner ID");
        columnNames.addElement("Subject ID");
        columnNames.addElement("Document");
        columnNames.addElement("Notes");

        final JTable table = new JTable(rowData,columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                System.out.println(e);
            }
        });

        table.setValueAt("",0,0);
        JFrame frame = new JFrame("Resizing Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(300, 150);
        frame.setVisible(true);

    }
}
