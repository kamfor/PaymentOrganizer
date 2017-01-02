package classes;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by kamil on 27.11.16.
 */
public class Subject implements Serializable {
    public int id;
    public String name;
    public String phone;
    public String email;
    public String address;
    public String bill;
    public String notes;

    public Subject(int par_id, String par_name,String par_phone, String par_email, String par_address, String par_bill, String par_notes){
        id  = par_id;
        name = par_name;
        phone = par_phone;
        email= par_email;
        address = par_address;
        bill = par_bill;
        notes = par_notes;
    }

    public void toString(Vector<String> append){
        append.clear();
        append.addElement(String.valueOf(id));
        append.addElement(name);
        append.addElement(phone);
        append.addElement(email);
        append.addElement(address);
        append.addElement(bill);
        append.addElement(notes);
    }

    public Vector<String> toVector(){
        Vector<String> temp = new Vector<String>();
        temp.addElement(String.valueOf(id));
        temp.addElement(name);
        temp.addElement(phone);
        temp.addElement(email);
        temp.addElement(address);
        temp.addElement(bill);
        temp.addElement(notes);
        return temp;
    }

    public void printObject(){
        System.out.println("ID:"+id);
        System.out.println("Name:"+name);
        System.out.println("Phone:"+phone);
        System.out.println("Email:"+email);
        System.out.println("Address:"+address);
        System.out.println("Bill:"+bill);
        System.out.println("Notes:"+notes);
    }
}