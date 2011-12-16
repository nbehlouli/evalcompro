package administration.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import compagne.bean.GestionEmployesBean;
import compagne.model.GestionEmployesModel;

import administration.bean.EmployeCompteBean;
import administration.bean.FichePosteBean;
import administration.bean.StructureEntrepriseBean;
import administration.model.EmployeCompteModel;
import administration.model.FichePosteModel;
import administration.model.StructureEntrepriseModel;



public class ChargementMasseAction extends GenericForwardComposer {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AnnotateDataBinder binder;
	Window win;
	List<StructureEntrepriseBean>savemodel = new ArrayList<StructureEntrepriseBean>();
	List<StructureEntrepriseBean> model = new ArrayList<StructureEntrepriseBean>();
	List<StructureEntrepriseBean> Addedmodel = new ArrayList<StructureEntrepriseBean>();
	List<FichePosteBean> savemodel2 = new ArrayList<FichePosteBean>();
	List<FichePosteBean> model2 = new ArrayList<FichePosteBean>();
	List<FichePosteBean> Addedmodel2 = new ArrayList<FichePosteBean>();
	List<EmployeCompteBean> savemodel3 = new ArrayList<EmployeCompteBean>();
	List<EmployeCompteBean> model3 = new ArrayList<EmployeCompteBean>();
	List<EmployeCompteBean> Addedmodel3 = new ArrayList<EmployeCompteBean>();
	Button okAdd;
	Div divupdown;
	
	@SuppressWarnings("deprecation")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// cr�ation des donn�es 
		StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
		
		FichePosteModel init =new FichePosteModel();
		EmployeCompteModel init3= new EmployeCompteModel();
		model=structureEntrepriseModel.checkStructureEntreprise();

		model2=init.loadFichesPostes();
		model3=init3.loadListEmployes();
		
		HashMap<String,StructureEntrepriseBean> mapStructureEntreprise= init.getStructureEntreprise();
		

		Iterator<FichePosteBean> iterator=model2.iterator();
		while(iterator.hasNext())
		{
			FichePosteBean ficheBean=iterator.next();
			String codeStructure=ficheBean.getCode_structure();
			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				ficheBean.setCodeStructLibelle(valeurCode_Structure);
			}
		}
		
		Iterator<EmployeCompteBean> iterator2=model3.iterator();
		while(iterator2.hasNext())
		{
			EmployeCompteBean employeCompteBean=iterator2.next();
			String codeStructure=employeCompteBean.getCode_structure();
			
			StructureEntrepriseBean structure=mapStructureEntreprise.get(codeStructure);
			if(structure!=null)
			{
				String libell_division=structure.getLibelleDivision();
				String libelleDirection=structure.getLibelleDirection();
				String libelleUnite=structure.getLibelleUnite();
				String libelleDepartement=structure.getLibelleDepartement();
				String libelleService=structure.getLibelleService();
				String libelleSection=structure.getLibelleSection();
				if(libell_division==null) libell_division="";
				if(libelleDirection==null)libelleDirection="";
				if(libelleUnite==null)libelleUnite="";
				if(libelleDepartement==null)libelleDepartement="";
				if(libelleService==null)libelleService="";
				if(libelleSection==null)libelleSection="";
				String valeurCode_Structure=codeStructure+","+libell_division+","+libelleDirection+","+libelleUnite+","+libelleDepartement+","+ libelleService+","+libelleSection;
				employeCompteBean.setCode_structure(valeurCode_Structure);
			}
		}
		comp.setVariable(comp.getId() + "Ctrl", this, true);

		binder = new AnnotateDataBinder(comp);
		
		binder.loadAll();
		
		
		
	}

	public List<StructureEntrepriseBean> getModel() {
		return model;
	}
	public List<FichePosteBean> getModel2() {
		return model2;
	}

	public List<EmployeCompteBean> getModel3() {
		return model3;
	}
	 public void onClick$download() {
			//chargement du contenu de la table Fiche_Poste et creation du fichier excel
			FichePosteModel fichePostemodel =new FichePosteModel();
			
			//creation du document xls
			HSSFWorkbook workBook = new HSSFWorkbook();
			
			
			//cr�ation de l'onglet structure entreprise et remplissage des donn�es
			
			StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
			
			structureEntrepriseModel.downloadStructureEntrepriseDataToXls(workBook);
			
			//creation de l'onglet listePostetravail et remplissage des donn�es
			fichePostemodel.downloadFichePosteDataToXls(workBook);
			
			//creation de l'onglet liste employ�s et remplissage des donn�es
			EmployeCompteModel employeCompte=new EmployeCompteModel();
			employeCompte.downloadEmployeCompteDataToXls(workBook,model3);
			
			//enregistrement des donn�es dans un fihcier xls
			FileOutputStream fOut;
			try 
			{
				fOut = new FileOutputStream("Donnees_Evalcom.xls");
				workBook.write(fOut);
				fOut.flush();
				fOut.close();
				
				File file = new File("Donnees_Evalcom.xls");
				Filedownload.save(file, "XLS");
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	 public void onClick$upload() 
	 {
		 	 
		 okAdd.setVisible(true);
		 Executions.getCurrent().getDesktop().setAttribute("org.zkoss.zul.Fileupload.target", divupdown);
			
		 try 
		 {
				
				Fileupload fichierupload=new Fileupload();
				
				//Media me=fichierupload.get("Merci de selectionner le fichier qui doit �tre charg�", "Chargement de fichier", true);
				Media me=fichierupload.get("Merci de selectionner le fichier qui doit �tre charg�", "Chargement de fichier", true);
				
				processMedia(me);
				

		 } 
		 catch (InterruptedException e) 
		 {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }		 
		 
	 }
	 public void onClick$okAdd()
	 {
		 String rejet="";
	 
		 
			try {
				if (Messagebox.show("Voulez vous charger les donnes dans la base ?", "Prompt", Messagebox.YES|Messagebox.NO,
						    Messagebox.QUESTION) == Messagebox.YES) 
				{

						//binder.loadAll();
					 //mise � jour  de la table structure_entreprise

						StructureEntrepriseModel structureEntrepriseModel=new StructureEntrepriseModel();
					
						 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(Addedmodel);
						 List<StructureEntrepriseBean> donneeRejetes =listeDonnees.get("supprimer");
						 Addedmodel=null;
						 Addedmodel=listeDonnees.get("inserer");;
						
						 model=savemodel;
						//raffrechissement de l'affichage
						Iterator<StructureEntrepriseBean> index=Addedmodel.iterator();
						while(index.hasNext())
						{
							StructureEntrepriseBean donnee=index.next();
							model.add(donnee);
							
						}
				
						binder.loadAll();
						if(donneeRejetes.size()!=0)
						{
							String listeRejet=new String("-->");
							//Afficharge de la liste des donn�es rejet�es
							Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
							while(index1.hasNext())
							{
								StructureEntrepriseBean donnee=index1.next();
								String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
								+";"+donnee.getLibelleDivision()
								 +";"+donnee.getCodeDirection()
								+";"+donnee.getLibelleDirection()
								+";"+donnee.getCodeUnite()
								+";"+donnee.getLibelleUnite()
								+";"+donnee.getCodeDepartement()
								+ ";"+donnee.getLibelleDepartement()
								+";"+donnee.getCodeService()
								+";"+donnee.getLibelleService()
								+ ";"+donnee.getCodesection()
								+ ";"+donnee.getLibelleSection();
								listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne
								
							}
							//AfficherFenetreRejet(listeRejet);
							rejet=rejet+listeRejet;

						}
					 //mise � jour de la table poste_Travail_description
						System.out.println("avant BDD poste");						
						FichePosteModel fichePosteModel=new FichePosteModel();
						System.out.println("apres BDD structure");						
						 HashMap <String,List<FichePosteBean>> listeDonneesPoste=fichePosteModel.ChargementDonneedansBdd(Addedmodel2);
						 List<FichePosteBean> donneeRejetesPoste =listeDonneesPoste.get("supprimer");
						 Addedmodel2=null;
						 Addedmodel2=listeDonneesPoste.get("inserer");;
						 System.out.println("1");				
						 model2=savemodel2;
						//raffrechissement de l'affichage
						Iterator<FichePosteBean> indexPoste=Addedmodel2.iterator();
						while(indexPoste.hasNext())
						{
							FichePosteBean donnee=indexPoste.next();
							model2.add(donnee);
							
						}
						 System.out.println("2");
						//binder.loadAll();
						if(donneeRejetes.size()!=0)
						{
							System.out.println("2.1");
							String listeRejet=new String("-->");
							//Afficharge de la liste des donn�es rejet�es
							Iterator<FichePosteBean> index1 =donneeRejetesPoste.iterator();
							while(index1.hasNext())
							{
								FichePosteBean donnee=index1.next();
								String donneeString=donnee.getCode_poste()+";"+donnee.getIs_cadre()
								+";"+donnee.getSommaire_poste()
								 +";"+donnee.getTache_responsabilite()
								+";"+donnee.getCode_formation()
								+";"+donnee.getFormation_professionnelle()
								+";"+donnee.getExperience()
								+";"+donnee.getProfile_poste()
								+ ";"+donnee.getHierarchie()
								+";"+donnee.getCode_structure()
								+";"+donnee.getCode_gsp();
								listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne
								
							}
							//AfficherFenetreRejet(listeRejet);
							rejet=rejet+listeRejet;
						}
							 System.out.println("3");
							//mise � jour de des tables Compte et Employe
							
							EmployeCompteModel employeCompteModel=new EmployeCompteModel();
							System.out.println("avant BDD employe");							
							 HashMap <String,List<EmployeCompteBean>> listeDonneesEmployeCompte=employeCompteModel.ChargementDonneedansBdd(Addedmodel3);
							 List<EmployeCompteBean> donneeRejetesEmplyeCompte =listeDonneesEmployeCompte.get("supprimer");
							 Addedmodel3=null;
							 Addedmodel3=listeDonneesEmployeCompte.get("inserer");;
							 System.out.println("aprest BDD structure");							
							 model3=savemodel3;
							//raffrechissement de l'affichage
							Iterator<EmployeCompteBean> indexEmployeCompte=Addedmodel3.iterator();
							while(indexEmployeCompte.hasNext())
							{
								EmployeCompteBean donnee=indexEmployeCompte.next();
								model3.add(donnee);
								
							}
					
							binder.loadAll();
							if(donneeRejetesEmplyeCompte.size()!=0)
							{
								String listeRejet1=new String("-->");
								//Afficharge de la liste des donn�es rejet�es
								Iterator<EmployeCompteBean> index2 =donneeRejetesEmplyeCompte.iterator();
								while(index2.hasNext())
								{
									EmployeCompteBean donnee=index2.next();
									String donneeString=donnee.getNom()+";"+donnee.getPrenom()
									+";"+donnee.getProfile()
									 +";"+donnee.getVal_date_deb()
									+";"+donnee.getVal_date_fin()
									+";"+donnee.getDate_naissance()
									+";"+donnee.getDate_recrutement()
									+";"+donnee.getCode_formation()
									+ ";"+donnee.getCode_poste()
									+";"+donnee.getEmail()
									+";"+donnee.getEst_evaluateur()
									+";"+donnee.getEst_responsable_rh()
									+";"+donnee.getCode_structure();
									listeRejet1=listeRejet1+System.getProperty("line.separator")+donneeString;//saut de ligne
									
								}
								//AfficherFenetreRejet(listeRejet);
								rejet=rejet+listeRejet1;

						}
						
					 //mise � jour des tables compte de la base common et employe
						
						binder.loadAll();
						AfficherFenetreRejet1(rejet);	
				
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		
		
	 }
	   /**
	     * cette m�thode permet d'afficher les donn�es rejet�es et qui n'ont pas �t� int�gres dans la table
	     */
	    public void AfficherFenetreRejet1(String listeRejet)
	    {
	    	System.out.println("affichage fenetre rejet");
	    	Map<String, String> listDonne=new HashMap <String, String>();
			listDonne.put("rejet", listeRejet);
			
			
//			Map<String, String > ll=new HashMap<String, String>();
//			String ss="rrrrrrr"+System.getProperty("line.separator")+"gggggggg"; 
//			ll.put("rejet", ss);
	    	final Window win2 = (Window) Executions.createComponents("../pages/REJDATA.zul", self, listDonne);
	        // We send a message to the Controller of the popup that it works in popup-mode.
	        win2.setAttribute("popup", true);
	        
	        //decoratePopup(win);
	        try 
	        {
	            win2.doModal();
	           
	        } 
	        catch (InterruptedException ex) 
	        {
	           ex.printStackTrace();
	        } 
	        catch (SuspendNotAllowedException ex) 
	        {
	            ex.printStackTrace();
	        }
	    }	 
	 /**
	  * cette m�thode permet de charger les donn�es qui se trouvent dans le fichier xls
	  * @param med
	  */
	 	public String processMedia(Media med)
		{
	 		List <FichePosteBean> listFichePoste=null;
			//Media med=event.getMedia();
	 		String rejet="";
			
			if ((med != null)&&(med.getName()!=null)) 
			{
				String filename = med.getName();
				
				if ( filename.indexOf(".xls") == -1 ) 
				{
				  alert(filename + " n'est pas un fichier excel");
				} 
				else 
				{
					
				  // process the file...
					StructureEntrepriseModel structureEntrepriseModel =new StructureEntrepriseModel();
					if ( filename.endsWith(".xls") ) 
					{
						//lecture et upload de fichiers OLE2 Office Documents 
						List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadStructureEntrepriseXLSFile(med.getStreamData());
						List<StructureEntrepriseBean> donneeRejetes;
						try 
						{
//							 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(liste);
//							 donneeRejetes =listeDonnees.get("supprimer");
//							 liste=null;
//							 liste=listeDonnees.get("inserer");;
							
							//sauvegarde du contenu du model
							savemodel=model;
							//raffrechissement de l'affichage
							Iterator<StructureEntrepriseBean> index=liste.iterator();
							while(index.hasNext())
							{
								StructureEntrepriseBean donnee=index.next();
								model.add(donnee);
								
							}
					
							binder.loadAll();
//							if(donneeRejetes.size()!=0)
//							{
//								String listeRejet=new String("-->");
//								//Afficharge de la liste des donn�es rejet�es
//								Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
//								while(index1.hasNext())
//								{
//									StructureEntrepriseBean donnee=index1.next();
//									String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
//									+";"+donnee.getLibelleDivision()
//									 +";"+donnee.getCodeDirection()
//									+";"+donnee.getLibelleDirection()
//									+";"+donnee.getCodeUnite()
//									+";"+donnee.getLibelleUnite()
//									+";"+donnee.getCodeDepartement()
//									+ ";"+donnee.getLibelleDepartement()
//									+";"+donnee.getCodeService()
//									+";"+donnee.getLibelleService()
//									+ ";"+donnee.getCodesection()
//									+ ";"+donnee.getLibelleSection();
//									listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne
//									
//								}
//								//AfficherFenetreRejet(listeRejet);
//								rejet=rejet+listeRejet;
//
//							}
						} 
						catch (Exception e) 
						{
								// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//lecture des donn�es se trouvant dans l'onglet liste poste
						FichePosteModel fichePosteModel=new FichePosteModel();
						listFichePoste=fichePosteModel.uploadXLSFile(med.getStreamData());
						//raffrechissement de l'affichage
						Iterator<FichePosteBean> index=listFichePoste.iterator();
						while(index.hasNext())
						{
							FichePosteBean donnee=index.next();
							model2.add(donnee);
							Addedmodel2.add(donnee);
							
						}
						//lecture des donn�es se trouvant dans l'onglet liste_employ�s
						EmployeCompteModel employeCompteModel=new EmployeCompteModel();
						List<EmployeCompteBean> listEmployeCompteBean= employeCompteModel.uploadXLSFile(med.getStreamData());
						
						//raffrechissement de l'affichage
						Iterator<EmployeCompteBean> index1=listEmployeCompteBean.iterator();
						while(index1.hasNext())
						{
							
							EmployeCompteBean donnee=index1.next();
							model3.add(donnee);
							Addedmodel3.add(donnee);
							
						}
						binder.loadAll();
					}
					else
						if(filename.endsWith(".xlsx"))
						{
							
							// lecture de fichiers Office 2007+ XML
							List<StructureEntrepriseBean> liste=structureEntrepriseModel.uploadStructureEntrepriseXLSXFile(med.getStreamData());
							List<StructureEntrepriseBean> donneeRejetes;
							try 
							{
//								 HashMap <String,List<StructureEntrepriseBean>> listeDonnees=structureEntrepriseModel.ChargementDonneedansBdd(liste);
//								 donneeRejetes =listeDonnees.get("supprimer");
//								 liste=null;
//								 liste=listeDonnees.get("inserer");;
								
							
								//raffrechissement de l'affichage
								Iterator<StructureEntrepriseBean> index=liste.iterator();
								while(index.hasNext())
								{
									StructureEntrepriseBean donnee=index.next();
									model.add(donnee);
									
								}
						
								binder.loadAll();
//								if(donneeRejetes.size()!=0)
//								{
//									String listeRejet=new String("-->");
//									//Afficharge de la liste des donn�es rejet�es
//									Iterator<StructureEntrepriseBean> index1 =donneeRejetes.iterator();
//									while(index1.hasNext())
//									{
//										StructureEntrepriseBean donnee=index1.next();
//										String donneeString=donnee.getCodestructure()+";"+donnee.getCodeDivision()
//										+";"+donnee.getLibelleDivision()
//										 +";"+donnee.getCodeDirection()
//										+";"+donnee.getLibelleDirection()
//										+";"+donnee.getCodeUnite()
//										+";"+donnee.getLibelleUnite()
//										+";"+donnee.getCodeDepartement()
//										+ ";"+donnee.getLibelleDepartement()
//										+";"+donnee.getCodeService()
//										+";"+donnee.getLibelleService()
//										+ ";"+donnee.getCodesection()
//										+ ";"+donnee.getLibelleSection();
//										listeRejet=listeRejet+System.getProperty("line.separator")+donneeString;//saut de ligne
//										
//									}
//									///AfficherFenetreRejet(listeRejet);
//									rejet=rejet+listeRejet;
//								}
							} 
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//lecture des donn�es se trouvant dans l'onglet liste poste
							FichePosteModel fichePosteModel=new FichePosteModel();
							listFichePoste=fichePosteModel.uploadXLSXFile(med.getStreamData());
							//lecture des donn�es se trouvant dans l'onglet liste_employ�s
							EmployeCompteModel employeCompteModel=new EmployeCompteModel();
							List<EmployeCompteBean> listEmployeCompteBean= employeCompteModel.uploadXLSXFile(med.getStreamData());
							
							//raffrechissement de l'affichage
							Iterator<EmployeCompteBean> index1=listEmployeCompteBean.iterator();
							while(index1.hasNext())
							{
								EmployeCompteBean donnee=index1.next();
								model3.add(donnee);
								Addedmodel3.add(donnee);
								
							}
							binder.loadAll();
						}
					
					} 				
				}
			return rejet;
			}	


}
