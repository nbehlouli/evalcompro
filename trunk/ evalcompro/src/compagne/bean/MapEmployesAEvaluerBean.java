package compagne.bean;

import java.util.HashMap;

public class MapEmployesAEvaluerBean {
	
	HashMap<String, EmployesAEvaluerBean> MapclesnomEmploye=new HashMap <String, EmployesAEvaluerBean>();
	HashMap<String, EmployesAEvaluerBean> Mapclesposte=new HashMap <String, EmployesAEvaluerBean>();
	public HashMap<String, EmployesAEvaluerBean> getMapclesnomEmploye() {
		return MapclesnomEmploye;
	}
	public void setMapclesnomEmploye(
			HashMap<String, EmployesAEvaluerBean> mapclesnomEmploye) {
		MapclesnomEmploye = mapclesnomEmploye;
	}
	public HashMap<String, EmployesAEvaluerBean> getMapclesposte() {
		return Mapclesposte;
	}
	public void setMapclesposte(HashMap<String, EmployesAEvaluerBean> mapclesposte) {
		Mapclesposte = mapclesposte;
	}
	
	
}
