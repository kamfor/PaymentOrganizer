package classes;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by kamil on 27.11.16.
 */
public class Payment implements Serializable {
    public int done;
    public int id;
    public String type;
    public String value;
    public java.util.Date begin_date;
    public java.util.Date end_date;
    public int owner_id;
    public int subject_id;
    public String document_name;
    public String notes;

    public Payment(int par_done, int par_id, String par_type, String par_value,java.util.Date par_begin_date, java.util.Date par_end_date,
                   int par_owner_id, int par_subject_id, String par_document_name, String par_notes){
        done = par_done;
        id  = par_id;
        type = par_type;
        value =par_value;
        begin_date = par_begin_date;
        end_date = par_end_date;
        owner_id = par_owner_id;
        subject_id = par_subject_id;
        document_name = par_document_name;
        notes = par_notes;
    }

    public void toString(Vector<String> append){
        append.clear();
        append.addElement(String.valueOf(done));
        append.addElement(String.valueOf(id));
        append.addElement(type);
        append.addElement(value);
        append.addElement(begin_date.toString());
        append.addElement(end_date.toString());
        append.addElement(String.valueOf(owner_id));
        append.addElement(String.valueOf(subject_id));
        append.addElement(document_name);
        append.addElement(notes);
    }

    public Vector<String> toVector(){
        Vector<String> temp = new Vector<String>();
        temp.addElement(String.valueOf(done));
        temp.addElement(String.valueOf(id));
        temp.addElement(type);
        temp.addElement(value);
        temp.addElement(begin_date.toString());
        temp.addElement(end_date.toString());
        temp.addElement(String.valueOf(owner_id));
        temp.addElement(String.valueOf(subject_id));
        temp.addElement(document_name);
        temp.addElement(notes);
        return temp;
    }

    public void printPaymentObject(){
        System.out.println("ID:"+id);
        System.out.println("Accepted"+done);
        System.out.println("Type:"+type);
        System.out.println("Value:"+value);
        System.out.println("Begin Date:"+begin_date);
        System.out.println("End Date:"+end_date);
        System.out.println("Owner:"+owner_id);
        System.out.println("Subject:"+subject_id);
        System.out.println("Document:"+document_name);
        System.out.println("Notes:"+notes);
    }
}
