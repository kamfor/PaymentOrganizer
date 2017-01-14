package model;

import java.io.Serializable;
import java.util.Vector;

public class Agent implements Serializable {
    public int id;
    public String name;
    public String phone;
    public String email;
    public Float commission;
    public static Object[] agentColumns = new Object[]{"ID","Nazwa","Telefon","Email","Należność"};

    public Agent(int par_id, String par_name,String par_phone, String par_email, Float par_commission){
        id  = par_id;
        name = par_name;
        phone = par_phone;
        email= par_email;
        commission = par_commission;
    }

    public Vector<String> toVector(){
        Vector<String> temp = new Vector<String>();
        temp.addElement(String.valueOf(id));
        temp.addElement(name);
        temp.addElement(phone);
        temp.addElement(email);
        temp.addElement(String.valueOf(commission));
        return temp;
    }
}