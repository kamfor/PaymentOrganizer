package pl.foryszewski ;

class Supplier
{
	public String name; 
	public String adress;
	private string owner;

	public void setOwner(String s) { owner=s; }
	public String getOwner() { return owner; }


	private static int count=0; //licznik instancji
	
	Supplier() { reg = count++; } // konstruktor klasy 

	public void setName(string username){
		name = username;
	}
}
