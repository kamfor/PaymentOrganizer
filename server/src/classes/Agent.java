package classes;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by kamil on 27.11.16.
 * Data structure class
 */
public class Agent implements Serializable {
    public int id;
    public String name;
    public String phone;
    public String email;
    public Float commission;

    public Agent(int par_id, String par_name,String par_phone, String par_email, Float par_commission){
        id  = par_id;
        name = par_name;
        phone = par_phone;
        email= par_email;
        commission = par_commission;
    }

    /**
     * @return String vector
     */
    public Vector<String> toVector(){
        Vector<String> temp = new Vector<String>();
        temp.addElement(String.valueOf(id));
        temp.addElement(name);
        temp.addElement(phone);
        temp.addElement(email);
        temp.addElement(String.valueOf(commission));
        return temp;
    }

    /**
     * Prints data fields
     */
    public void printObject(){
        System.out.println("ID:"+id);
        System.out.println("Name:"+name);
        System.out.println("Phone:"+phone);
        System.out.println("Email:"+email);
        System.out.println("Commission:"+commission);
    }
}