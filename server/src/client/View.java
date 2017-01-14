package client;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

public class View extends JFrame{

    private JTabbedPane jtp;
    public paymentPanel panel1 = new paymentPanel();
    public agentPanel panel2 = new agentPanel();
    public subjectPanel panel3 = new subjectPanel();

    public View(){
        super();
        setTitle("Baza danych");
        this.setSize(1200, 600);
        jtp = new JTabbedPane();
        jtp.addTab("Płatności",panel1);
        jtp.addTab("Agenci",panel2);
        jtp.addTab("Podmioty",panel3);
        add(jtp);
        jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(jtp);
        this.setVisible(true);
    }

    public class paymentPanel extends JInternalFrame{

        public JButton addRecord, removeRecord;
        public JLabel errorMessage;
        public JTextField tfType, tfValue, tfBeginDate, tfEndDate, tfOwner, tfSubject, tfDocument, tfNotes;
        public JTable table;

        public paymentPanel() {
            super();
            preparePaymentTableView();

            JScrollPane scrollPane = getjScrollPane();
            JPanel inputPanel = getjPanelInput();
            JPanel errorPanel = getjPanelError();

            this.add(inputPanel, BorderLayout.SOUTH);
            this.add(errorPanel, BorderLayout.NORTH);
            this.add(scrollPane, BorderLayout.CENTER);
        }

        private JPanel getjPanelError() {
            errorMessage = new JLabel("");
            errorMessage.setForeground(Color.red);
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            errorPanel.add(errorMessage);
            return errorPanel;
        }

        private JScrollPane getjScrollPane() {
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane, BorderLayout.CENTER);
            return scrollPane;
        }

        private JPanel getjPanelInput() {
            addRecord = new JButton("Dodaj rekord");
            removeRecord = new JButton("Usuń rekord");

            tfType = new JTextField("Typ", 8);
            tfValue = new JTextField("Wartość", 6);
            tfBeginDate = new JTextField("Data wystawienia", 9);
            tfEndDate = new JTextField("Termin płatności", 9);
            tfOwner = new JTextField("Właściciel", 5);
            tfSubject = new JTextField("Podmiot", 5);
            tfDocument = new JTextField("Nazwa dokumentu", 8);
            tfNotes = new JTextField("Notatki", 20);

            JPanel inputPanel = new JPanel();
            inputPanel.add(tfType);
            inputPanel.add(tfValue);
            inputPanel.add(tfBeginDate);
            inputPanel.add(tfEndDate);
            inputPanel.add(tfOwner);
            inputPanel.add(tfSubject);
            inputPanel.add(tfDocument);
            inputPanel.add(tfNotes);
            inputPanel.add(addRecord);
            inputPanel.add(removeRecord);
            return inputPanel;
        }

        private void preparePaymentTableView() {
            table = new JTable(Database.defaultTableModelPayment){
                @Override
                public Class getColumnClass(int column) {
                    switch (column) {
                        case 0:
                            return Boolean.class;
                        default:
                            return String.class;
                    }
                }
            };

            table.setRowHeight(table.getRowHeight() + 8);
            table.setAutoCreateRowSorter(true);
        }

    }

    public class agentPanel extends JInternalFrame{
        public JButton addRecord, removeRecord;
        public JLabel errorMessage;
        public JTextField tfName, tfPhone, tfEmail;
        public JTable table;

        public agentPanel() {
            super();
            prepareAgentTableView();
            JScrollPane scrollPane = getjScrollPane();
            JPanel inputPanel = getjPanelInput();
            JPanel errorPanel = getjPanelError();

            this.add(inputPanel, BorderLayout.SOUTH);
            this.add(errorPanel, BorderLayout.NORTH);
            this.add(scrollPane, BorderLayout.CENTER);
        }

        private JPanel getjPanelError() {
            errorMessage = new JLabel("");
            errorMessage.setForeground(Color.red);
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            errorPanel.add(errorMessage);
            return errorPanel;
        }

        private JPanel getjPanelInput() {
            addRecord = new JButton("Dodaj rekord");
            removeRecord = new JButton("Usuń rekord");

            tfName = new JTextField("Nazwa", 10);
            tfEmail = new JTextField("Email", 9);
            tfPhone = new JTextField("Telefon", 9);

            JPanel inputPanel = new JPanel();
            inputPanel.add(tfName);
            inputPanel.add(tfEmail);
            inputPanel.add(tfPhone);
            inputPanel.add(addRecord);
            inputPanel.add(removeRecord);
            return inputPanel;
        }

        private JScrollPane getjScrollPane() {
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane, BorderLayout.CENTER);
            return scrollPane;
        }

        private void prepareAgentTableView() {
            table = new JTable(Database.defaultTableModelAgent){
                public boolean isCellEditable(int row, int column) {
                    return column == 1 || column == 2 || column == 3;
                }
            };
            table.setRowHeight(table.getRowHeight() + 8);
            table.setAutoCreateRowSorter(true);
        }

    }

    public class subjectPanel extends JInternalFrame{
        public JButton addRecord, removeRecord;
        public JLabel errorMessage;
        public JTextField tfName, tfPhone, tfEmail, tfAddress, tfNotes;
        public JTable table;

        public subjectPanel() {
            super();
            prepareSubjectTableView();

            JScrollPane scrollPane = getjScrollPane();
            JPanel inputPanel = getjPanelInput();
            JPanel errorPanel = getjPanelError();

            this.add(inputPanel, BorderLayout.SOUTH);
            this.add(errorPanel, BorderLayout.NORTH);
            this.add(scrollPane, BorderLayout.CENTER);
        }

        private JPanel getjPanelError() {
            errorMessage = new JLabel("");
            errorMessage.setForeground(Color.red);
            JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            errorPanel.add(errorMessage);
            return errorPanel;
        }

        private JPanel getjPanelInput() {
            addRecord = new JButton("Dodaj rekord");
            removeRecord = new JButton("Usuń rekord");

            tfName = new JTextField("Nazwa", 10);
            tfPhone = new JTextField("Telefon", 9);
            tfEmail = new JTextField("Email", 9);
            tfAddress = new JTextField("Adres", 20);
            tfNotes = new JTextField("Notatki", 20);

            JPanel inputPanel = new JPanel();
            inputPanel.add(tfName);
            inputPanel.add(tfPhone);
            inputPanel.add(tfEmail);
            inputPanel.add(tfAddress);
            inputPanel.add(tfNotes);
            inputPanel.add(addRecord);
            inputPanel.add(removeRecord);
            return inputPanel;
        }

        private JScrollPane getjScrollPane() {
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane, BorderLayout.CENTER);
            return scrollPane;
        }

        private void prepareSubjectTableView() {
            table = new JTable(Database.defaultTableModelSubject){
                public boolean isCellEditable(int row, int column) {
                    return column ==1 || column == 2 || column == 3 || column == 4 || column == 6;
                }
            };
            table.setRowHeight(table.getRowHeight() + 8);
            table.setAutoCreateRowSorter(true);
        }
    }
}








