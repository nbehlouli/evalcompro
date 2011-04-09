package administration.action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.Version;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.lang.Strings;
import org.zkoss.zul.Div; 
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;

import administration.model.LoginModel;


public class Login extends GenericForwardComposer{
	Button login;
	Window main;
	Div   loginDiv;
	Div   userDiv;
	Label msg;
	Label userName;
	Textbox usertb;
	Textbox pwdtb;
	LoginModel init =new LoginModel();
	Div div;
	
/*	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		main.setTitle("Hello ZK");
		main.setBorder("normal");
	}
	
	public void onClick$sayHelloBtn(){
		showMsg();
	}*/
	
	void showMsg(){
		String message = "You are running ZK Successfully, The ZK Version is "+Version.UID;
		alert(message);
	}
	
	public void onClick$login(MouseEvent event) throws Exception
	{
		String user = usertb.getValue();
		String pwd = pwdtb.getValue();
		
		
		Messagebox.show("Hello, " + event.getName());
		if(Strings.isBlank(user) || Strings.isEmpty(pwd)){
			msg.setValue("*Need user name and password!");
			return;
		}
		
		
		try 
		{
			
			boolean result=init.checkLoginPwd(user,pwd);
			if (result==true) 
			{
				msg.setValue("authentifié");
			} 
			else
			{ 
				msg.setValue("utilisateur out mot de passe erroné");
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*session.setAttribute("user",user);
		loginDiv.setVisible(false);
		userDiv.setVisible(true);
		userName.setValue(user);
		msg.setValue("");*/
//        Map data = new HashMap();
//     
//		data.put("name", "111");
//		data.put("age", "lll");
//		System.out.println("vant de rendre invisible");
//		
//		main.setVisible(false);
//		Executions.createComponents("/index.zul",null, data);

Map data = new HashMap();
		
		data.put("name", user);
		data.put("age", pwd);

		Executions.createComponents("../menu/borderlayout.zul", div, data);
		
		//permet de fermer la fenetre login
		
		main=(Window)this.self;
		main.detach();
	}
	public void doLogout(){
		session.removeAttribute("user");
		
		loginDiv.setVisible(true);
		userDiv.setVisible(false);
		userName.setValue("");
	}
	public void doOK(){
		if(loginDiv.isVisible()){
			//onClick$login();
		}else{
			doLogout();
		}
	}
	
	//String user = (String)session.getAttribute("user");

}
