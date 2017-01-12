package model;

import java.io.Serializable;
import java.util.Vector;

public class Subject implements Serializable {
    public int id;
    public String name;
    public String phone;
    public String email;
    public String address;
    public Float bill;
    public String notes;
    public static Object[] subjectColumns = new Object[]{"ID","Name","Phone","Email","Address","Bill","Notes"};

    public Subject(int par_id, String par_name,String par_phone, String par_email, String par_address, Float par_bill, String par_notes){
        id  = par_id;
        name = par_name;
        phone = par_phone;
        email= par_email;
        address = par_address;
        bill = par_bill;
        notes = par_notes;
    }

    public Vector<String> toVector(){
        Vector<String> temp = new Vector<String>();
        temp.addElement(String.valueOf(id));
        temp.addElement(name);
        temp.addElement(phone);
        temp.addElement(email);
        temp.addElement(address);
        temp.addElement(String.valueOf(bill));
        temp.addElement(notes);
        return temp;
    }
}