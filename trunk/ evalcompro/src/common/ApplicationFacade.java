package common;

import java.util.ArrayList;

import administration.bean.CompteEntrepriseDatabaseBean;
import administration.bean.StructureEntrepriseBean;
import common.bean.ArborescenceMenu;

public class ApplicationFacade {

	
	/**
     * Instance static sur la classe --> singleton
     */
    private static ApplicationFacade instance = null;
    
    /**
     * Constructeur prive --> singleton
     */
    private ApplicationFacade() {

    }
    
    

    //variable contenant les informations pour se connecter à la base evalcom associée à une entreprise spécifique
    
    /**
     * TODO enlever l'initialisation de la variable compteEntrepriseDatabasebean
     */
   private  CompteEntrepriseDatabaseBean compteEntrepriseDatabasebean =new CompteEntrepriseDatabaseBean("","","");
    // variable contenant la structure du menu principale
    private ArborescenceMenu arborescenceMenubean;
    //variable contenant la database id rattachée à l'utilisateur
    private int client_database_id;
    
	public int getClient_database_id() {
		return client_database_id;
	}

	public void setClient_database_id(int client_database_id) {
		this.client_database_id = client_database_id;
	}

	public static ApplicationFacade getInstance() {
        if (instance == null) {
        	ApplicationFacadeSynchornizee();
        }
        return instance;
    }

    public CompteEntrepriseDatabaseBean getCompteEntrepriseDatabasebean() {
		return compteEntrepriseDatabasebean;
	}

	public void setCompteEntrepriseDatabasebean(
			CompteEntrepriseDatabaseBean compteEntrepriseDatabasebean) {
		this.compteEntrepriseDatabasebean = compteEntrepriseDatabasebean;
	}

	public synchronized static void ApplicationFacadeSynchornizee() {
        if (instance == null) {
            instance = new ApplicationFacade();
        }
    }
    
    public ArborescenceMenu getArborescenceMenubean() {
		return arborescenceMenubean;
	}

	public void setArborescenceMenubean(ArborescenceMenu arborescenceMenubean) {
		this.arborescenceMenubean = arborescenceMenubean;
	}
	
	public void resetArguments(){
		
		this.compteEntrepriseDatabasebean=null;
		this.arborescenceMenubean=null;
		this.client_database_id=0;
	}

	
    
}
