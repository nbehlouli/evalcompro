package compagne.bean;

public class SuiviCompagneBean   {
	
	private int pourcentage;
	private String evaluateur ;
	
	
	public SuiviCompagneBean() {
		
	}
	
	public SuiviCompagneBean(int pourcentage, String evaluateur) {
		super();
		this.pourcentage = pourcentage;
		this.evaluateur = evaluateur;
	}
	public String getEvaluateur() {
		return evaluateur;
	}
	public void setEvaluateur(String evaluateur) {
		this.evaluateur = evaluateur;
	}
	public int getPourcentage() {
		return pourcentage;
	}
	public void setPourcentage(int pourcentage) {
		this.pourcentage = pourcentage;
	}
	
	

}
