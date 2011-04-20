package common;

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

    
    // variable contenant la structure du menu principale
    private ArborescenceMenu arborescenceMenubean;
	public static ApplicationFacade getInstance() {
        if (instance == null) {
        	ApplicationFacadeSynchornizee();
        }
        return instance;
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

	
    
}
