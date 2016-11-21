package pl.foryszewski

public class Payment{
	int date; 
	int end_date; 
	float value; 
	string document_number; 
	string notes; 
	bool active
	int actual_payoff; 
	

	int returnDate() {
		return date;
	}
   
	void accept(int userdate, float uservalue){
	   	value = uservalue;
		date = userdate;
	}
}
