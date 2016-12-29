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
    public String adress;
    public String notes;

    public Subject(int par_id, String par_name,String par_phone, String par_email, String par_adress, String par_notes){
        id  = par_id;
        name = par_name;
        phone = par_phone;
        email= par_email;
        adress = par_adress;
        notes = par_notes;
    }

    public void toString(Vector<String> append){
        append.clear();
        append.addElement(String.valueOf(id));
        append.addElement(name);
        append.addElement(phone);
        append.addElement(email);
        append.addElement(adress);
        append.addElement(notes);
    }

    public Vector<String> toVector(){
        Vector<String> temp = new Vector<String>();
        temp.addElement(String.valueOf(id));
        temp.addElement(name);
        temp.addElement(phone);
        temp.addElement(email);
        temp.addElement(adress);
        temp.addElement(notes);
        return temp;
    }

    public void printObject(){
        System.out.println("ID:"+id);
        System.out.println("Name:"+name);
        System.out.println("Phone:"+phone);
        System.out.println("Email:"+email);
        System.out.println("Adress:"+adress);
        System.out.println("Notes:"+notes);
    }
}