package classes;

class Supplier
{
	public String name; 
	public String adress;
	private String owner;

	public void setOwner(String s) { owner=s; }
	public String getOwner() { return owner; }


	private static int count=0; //licznik instancji

	public void setName(String username){
		name = username;
	}
}
