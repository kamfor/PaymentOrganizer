package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.*;
import javax.swing.plaf.synth.SynthEditorPaneUI;

import model.Agent;
import model.Payment;
import model.Subject;

public class Controller {

    private ListenForFocus focusListener = new ListenForFocus();
    private ListenForPaymentAction paymentActionListener  =new ListenForPaymentAction();
    private ListenForAgentAction agentActionListener  =new ListenForAgentAction();
    private ListenForSubjectAction subjectActionListener  =new ListenForSubjectAction();
    private ListenForClickPayment paymentClickListener = new ListenForClickPayment();
    private ListenForClickAgent agentClickListener = new ListenForClickAgent();
    private ListenForClickSubject subjectClickListener = new ListenForClickSubject();

    static View gui;

    public Controller(){
        gui = new View();
        setPaymentPanelListeners();
        setAgentPanelListeners();
        setSubjectPanelListeners();
        gui.panel1.errorMessage.setText("");
    }

    private void setSubjectPanelListeners() {
        gui.panel3.tfName.addFocusListener(focusListener);
        gui.panel3.tfPhone.addFocusListener(focusListener);
        gui.panel3.tfEmail.addFocusListener(focusListener);
        gui.panel3.tfAddress.addFocusListener(focusListener);
        gui.panel3.tfNotes.addFocusListener(focusListener);
        gui.panel3.addRecord.addActionListener(subjectActionListener);
        gui.panel3.removeRecord.addActionListener(subjectActionListener);
        gui.panel3.table.getModel().addTableModelListener(subjectClickListener);
    }

    private void setAgentPanelListeners() {
        gui.panel2.tfName.addFocusListener(focusListener);
        gui.panel2.tfPhone.addFocusListener(focusListener);
        gui.panel2.tfEmail.addFocusListener(focusListener);
        gui.panel2.addRecord.addActionListener(agentActionListener);
        gui.panel2.removeRecord.addActionListener(agentActionListener);
        gui.panel2.table.getModel().addTableModelListener(agentClickListener);
    }

    private void setPaymentPanelListeners() {
        gui.panel1.tfType.addFocusListener(focusListener);
        gui.panel1.tfValue.addFocusListener(focusListener);
        gui.panel1.tfBeginDate.addFocusListener(focusListener);
        gui.panel1.tfEndDate.addFocusListener(focusListener);
        gui.panel1.tfOwner.addFocusListener(focusListener);
        gui.panel1.tfSubject.addFocusListener(focusListener);
        gui.panel1.tfDocument.addFocusListener(focusListener);
        gui.panel1.tfNotes.addFocusListener(focusListener);
        gui.panel1.addRecord.addActionListener(paymentActionListener);
        gui.panel1.removeRecord.addActionListener(paymentActionListener);
        gui.panel1.table.getModel().addTableModelListener(paymentClickListener);
    }

    private class ListenForPaymentAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel1.addRecord) {
                gui.panel1.errorMessage.setText(ClientMain.db.addPayment(gui.panel1.tfType.getText(),
                                                                            gui.panel1.tfValue.getText(),
                                                                            gui.panel1.tfBeginDate.getText(),
                                                                            gui.panel1.tfEndDate.getText(),
                                                                            gui.panel1.tfOwner.getText(),
                                                                            gui.panel1.tfSubject.getText(),
                                                                            gui.panel1.tfDocument.getText(),
                                                                            gui.panel1.tfNotes.getText()));

            } else if (e.getSource() == gui.panel1.removeRecord) {
                gui.panel1.errorMessage.setText(ClientMain.db.removePayment(gui.panel1.table.getSelectedRow()));
            }
        }
    }

    private class ListenForAgentAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel2.addRecord) {
                gui.panel2.errorMessage.setText(ClientMain.db.addAgent(gui.panel2.tfName.getText(),
                                                                    gui.panel2.tfPhone.getText(),
                                                                    gui.panel2.tfEmail.getText()));
            } else if (e.getSource() == gui.panel2.removeRecord) {
                gui.panel2.errorMessage.setText(ClientMain.db.removeAgent(gui.panel2.table.getSelectedRow()));
            }
        }
    }

    private class ListenForSubjectAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gui.panel3.addRecord) {
                gui.panel3.errorMessage.setText(ClientMain.db.addSubject(gui.panel3.tfName.getText(),
                                                                        gui.panel3.tfPhone.getText(),
                                                                        gui.panel3.tfEmail.getText(),
                                                                        gui.panel3.tfAddress.getText(),
                                                                        gui.panel3.tfNotes.getText()));
            } else if (e.getSource() == gui.panel3.removeRecord) {
                gui.panel3.errorMessage.setText(ClientMain.db.removeSubject(gui.panel3.table.getSelectedRow()));
            }
        }
    }

    private class ListenForFocus implements FocusListener {
        public void focusGained(FocusEvent e) { // If a text field gains focus and has the default text, remove the text
            if(gui.panel1.tfType.getText().equals("Type") && e.getSource() == gui.panel1.tfType) {
                gui.panel1.tfType.setText("");
            } else if(gui.panel1.tfValue.getText().equals("Value") && e.getSource() == gui.panel1.tfValue) {
                gui.panel1.tfValue.setText("");
            } else if(gui.panel1.tfBeginDate.getText().equals("Begin Date") && e.getSource() == gui.panel1.tfBeginDate) {
                gui.panel1.tfBeginDate.setText("");
            } else if(gui.panel1.tfEndDate.getText().equals("End Date") && e.getSource() == gui.panel1.tfEndDate) {
                gui.panel1.tfEndDate.setText("");
            } else if(gui.panel1.tfOwner.getText().equals("Owner") && e.getSource() == gui.panel1.tfOwner) {
                gui.panel1.tfOwner.setText("");
            } else if(gui.panel1.tfSubject.getText().equals("Subject") && e.getSource() == gui.panel1.tfSubject) {
                gui.panel1.tfSubject.setText("");
            } else if(gui.panel1.tfDocument.getText().equals("Document") && e.getSource() == gui.panel1.tfDocument) {
                gui.panel1.tfDocument.setText("");
            } else if(gui.panel1.tfNotes.getText().equals("Notes") && e.getSource() == gui.panel1.tfNotes) {
                gui.panel1.tfNotes.setText("");
            } else if(gui.panel2.tfName.getText().equals("Name") && e.getSource() == gui.panel2.tfName) {
                gui.panel2.tfName.setText("");
            } else if(gui.panel2.tfPhone.getText().equals("Phone") && e.getSource() == gui.panel2.tfPhone) {
                gui.panel2.tfPhone.setText("");
            } else if(gui.panel2.tfEmail.getText().equals("Email") && e.getSource() == gui.panel2.tfEmail) {
                gui.panel2.tfEmail.setText("");
            } else if(gui.panel3.tfName.getText().equals("Name") && e.getSource() == gui.panel3.tfName) {
                gui.panel3.tfName.setText("");
            } else if(gui.panel3.tfPhone.getText().equals("Phone") && e.getSource() == gui.panel3.tfPhone) {
                gui.panel3.tfPhone.setText("");
            } else if(gui.panel3.tfEmail.getText().equals("Email") && e.getSource() == gui.panel3.tfEmail) {
                gui.panel3.tfEmail.setText("");
            } else if(gui.panel3.tfAddress.getText().equals("Address") && e.getSource() == gui.panel3.tfAddress) {
                gui.panel3.tfAddress.setText("");
            } else if(gui.panel3.tfNotes.getText().equals("Notes") && e.getSource() == gui.panel3.tfNotes) {
                gui.panel3.tfNotes.setText("");
            }
        }

        public void focusLost(FocusEvent e) { // If the text field loses focus and is blank, set the default text back
            if(gui.panel1.tfType.getText().equals("") && e.getSource() == gui.panel1.tfType) {
                gui.panel1.tfType.setText("Type");
            } else if(gui.panel1.tfValue.getText().equals("") && e.getSource() == gui.panel1.tfValue) {
                gui.panel1.tfValue.setText("Value");
            } else if(gui.panel1.tfBeginDate.getText().equals("") && e.getSource() == gui.panel1.tfBeginDate) {
                gui.panel1.tfBeginDate.setText("Begin Date");
            } else if(gui.panel1.tfEndDate.getText().equals("") && e.getSource() == gui.panel1.tfEndDate) {
                gui.panel1.tfEndDate.setText("End Date");
            } else if(gui.panel1.tfOwner.getText().equals("") && e.getSource() == gui.panel1.tfOwner) {
                gui.panel1.tfOwner.setText("Owner");
            } else if(gui.panel1.tfSubject.getText().equals("") && e.getSource() == gui.panel1.tfSubject) {
                gui.panel1.tfSubject.setText("Subject");
            } else if(gui.panel1.tfDocument.getText().equals("") && e.getSource() == gui.panel1.tfDocument) {
                gui.panel1.tfDocument.setText("Document");
            } else if(gui.panel1.tfNotes.getText().equals("") && e.getSource() == gui.panel1.tfNotes) {
                gui.panel1.tfNotes.setText("Notes");
            } else if(gui.panel2.tfName.getText().equals("") && e.getSource() == gui.panel2.tfName) {
                gui.panel2.tfName.setText("Name");
            } else if(gui.panel2.tfPhone.getText().equals("") && e.getSource() == gui.panel2.tfPhone) {
                gui.panel2.tfPhone.setText("Phone");
            } else if(gui.panel2.tfEmail.getText().equals("") && e.getSource() == gui.panel2.tfEmail) {
                gui.panel2.tfEmail.setText("Email");
            } else if(gui.panel3.tfName.getText().equals("") && e.getSource() == gui.panel3.tfName) {
                gui.panel3.tfName.setText("Name");
            } else if(gui.panel3.tfPhone.getText().equals("") && e.getSource() == gui.panel3.tfPhone) {
                gui.panel3.tfPhone.setText("Phone");
            } else if(gui.panel3.tfEmail.getText().equals("") && e.getSource() == gui.panel3.tfEmail) {
                gui.panel3.tfEmail.setText("Email");
            } else if(gui.panel3.tfAddress.getText().equals("") && e.getSource() == gui.panel3.tfAddress) {
                gui.panel3.tfAddress.setText("Address");
            } else if(gui.panel3.tfNotes.getText().equals("") && e.getSource() == gui.panel3.tfNotes) {
                gui.panel3.tfNotes.setText("Notes");
            }
        }
    }

    private class ListenForClickPayment implements TableModelListener{
        public void tableChanged(TableModelEvent e) {
            if(e.getType()==TableModelEvent.UPDATE){
                gui.panel1.errorMessage.setText(
                        ClientMain.db.updatePayment(gui.panel1.table.getSelectedRow(),
                        gui.panel1.table.getValueAt(e.getLastRow(),e.getColumn()),
                        Database.defaultTableModelPayment.getColumnName(gui.panel1.table.getSelectedColumn())));
            }
        }
    }

    private class ListenForClickAgent implements TableModelListener{
        public void tableChanged(TableModelEvent e) {
            if(e.getType()==TableModelEvent.UPDATE){
                gui.panel2.errorMessage.setText(ClientMain.db.updateAgent(gui.panel2.table.getSelectedRow(),
                        gui.panel2.table.getValueAt(e.getLastRow(),e.getColumn()),
                        Database.defaultTableModelAgent.getColumnName(gui.panel2.table.getSelectedColumn())));
            }
        }
    }


    private class ListenForClickSubject implements TableModelListener{
        public void tableChanged(TableModelEvent e) {
            if(e.getType()==TableModelEvent.UPDATE){
                gui.panel3.errorMessage.setText(ClientMain.db.updateSubject(gui.panel3.table.getSelectedRow(),
                        gui.panel3.table.getValueAt(e.getLastRow(),e.getColumn()),
                        Database.defaultTableModelSubject.getColumnName(gui.panel3.table.getSelectedColumn())));
            }
        }
    }
}
