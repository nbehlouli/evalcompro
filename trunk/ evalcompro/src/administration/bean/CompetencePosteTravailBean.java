package administration.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class CompetencePosteTravailBean {
	
	private HashMap <String,HashMap<String, HashMap<String, ArrayList<String>> >> listefamilles;

	private ArrayList <String >posteTravail;

	public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> getListefamilles() {
		return listefamilles;
	}

	public void setListefamilles(
			HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> listefamilles) {
		this.listefamilles = listefamilles;
	}

	public ArrayList<String> getPosteTravail() {
		return posteTravail;
	}

	public void setPosteTravail(ArrayList<String> posteTravail) {
		this.posteTravail = posteTravail;
	}
	

}
