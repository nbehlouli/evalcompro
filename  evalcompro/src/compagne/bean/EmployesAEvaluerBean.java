package compagne.bean;

import java.util.ArrayList;

public class EmployesAEvaluerBean {
	
	private int id_employe;
	private String poste_travail;
	private int code_poste;
	private String nom_employe;
	
	
	private ArrayList <String> famille=new ArrayList<String>();
	private ArrayList <Integer> code_famille=new ArrayList<Integer>();
	public int getId_employe() {
		return id_employe;
	}
	public void setId_employe(int id_employe) {
		this.id_employe = id_employe;
	}
	public String getPoste_travail() {
		return poste_travail;
	}
	public void setPoste_travail(String poste_travail) {
		this.poste_travail = poste_travail;
	}
	public int getCode_poste() {
		return code_poste;
	}
	public void setCode_poste(int code_poste) {
		this.code_poste = code_poste;
	}
	public String getNom_employe() {
		return nom_employe;
	}
	public void setNom_employe(String nom_employe) {
		this.nom_employe = nom_employe;
	}
	public ArrayList<String> getFamille() {
		return famille;
	}
	public void setFamille(ArrayList<String> famille) {
		this.famille = famille;
	}
	public ArrayList<Integer> getCode_famille() {
		return code_famille;
	}
	public void setCode_famille(ArrayList<Integer> code_famille) {
		this.code_famille = code_famille;
	}
	
	

}
