package administration.bean;

import java.io.Serializable;
import java.util.Date;

public class AdministrationLoginBean implements Serializable {
	
	private String nom;
	private String prenom;
	private String login;
	private String motdepasse;
	private String profile;
	private String basedonnee;
	private String date_deb_val;
	private String date_fin_val;
	private String datemodifpwd;
	
	 
	
	
	
	public AdministrationLoginBean(int employeid, String nom, String prenom,
			String login, String motdepasse, String profile, String basedonnee,
			String date_deb_val, String date_fin_val, String datemodifpwd) {
		
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.motdepasse = motdepasse;
		this.profile = profile;
		this.basedonnee = basedonnee;
		this.date_deb_val = date_deb_val;
		this.date_fin_val = date_fin_val;
		this.datemodifpwd = datemodifpwd;
	}
	
	

	public AdministrationLoginBean() {
		
		// TODO Auto-generated constructor stub
	}



	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMotdepasse() {
		return motdepasse;
	}
	public void setMotdepasse(String motdepasse) {
		this.motdepasse = motdepasse;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getBasedonnee() {
		return basedonnee;
	}
	public void setBasedonnee(String basedonnee) {
		this.basedonnee = basedonnee;
	}
	public String getDate_deb_val() {
		return date_deb_val;
	}
	public void setDate_deb_val(String date_deb_val) {
		this.date_deb_val = date_deb_val;
	}
	public String getDate_fin_val() {
			 
		return date_fin_val;
	}
	public void setDate_fin_val(String date_fin_val) {
		this.date_fin_val = date_fin_val;
	}
	public String getDatemodifpwd() {
		return datemodifpwd;
	}
	public void setDatemodifpwd(String datemodifpwd) {
		this.datemodifpwd = datemodifpwd;
	}
	
	
}
	
	
	
