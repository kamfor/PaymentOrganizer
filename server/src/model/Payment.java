package model;

import java.io.Serializable;
import java.util.Vector;

public class Payment implements Serializable {
    public int id;
    public Boolean accepted;
    public String type;
    public Float value;
    public java.util.Date begin_date;
    public java.util.Date end_date;
    public int owner_id;
    public int subject_id;
    public String document_name;
    public String notes;
    public static final Object[] paymentColumns = new Object[]{"Zaakceptowano","Typ","Wartość","Data wystawienia","Termin płatności","Agent","Podmmiot","Nzawa dokumentu","Notatki"};

    public Payment(int par_id, Boolean par_accepted, String par_type, Float par_value,java.util.Date par_begin_date, java.util.Date par_end_date,
                   int par_owner_id, int par_subject_id, String par_document_name, String par_notes){
        id  = par_id;
        accepted  = par_accepted;
        type = par_type;
        value =par_value;
        begin_date = par_begin_date;
        end_date = par_end_date;
        owner_id = par_owner_id;
        subject_id = par_subject_id;
        document_name = par_document_name;
        notes = par_notes;
    }

    public Vector<Object> toVector(){
        Vector<Object> temp = new Vector<>();
        //temp.addElement(String.valueOf(id));
        temp.addElement(accepted);
        temp.addElement(type);
        temp.addElement(value.toString());
        temp.addElement(begin_date.toString());
        temp.addElement(end_date.toString());
        temp.addElement(String.valueOf(owner_id));
        temp.addElement(String.valueOf(subject_id));
        temp.addElement(document_name);
        temp.addElement(notes);
        return temp;
    }
}
