package action;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.lang.Strings;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
public class loginAction extends GenericForwardComposer{
	
	Button login;
	Window main;
	
	
	Session session;
	Textbox usertb;
	Textbox pwdtb;
	Label msg;
	Label userName;
	Div loginDiv;
	Div  userDiv;
	
	private Div div;

	public void onClick$login(MouseEvent event) throws Exception
	{	
		
		String user = usertb.getValue();
		String pwd = pwdtb.getValue();
		if(Strings.isBlank(user) || Strings.isEmpty(pwd))
		{
			msg.setValue("*Le nom d'utilisateur et le mot de passe sont obligatoires!");
			return;
		}
		if(!"1234".equals(pwd))
		{
			msg.setValue("*Mot de passe incorrect!");
			return;
		}
//		session.setAttribute("user",user);
//		loginDiv.setVisible(false);
//		userDiv.setVisible(true);
//		userName.setValue(user);
//		msg.setValue("");

		
		Map data = new HashMap();
		
		data.put("name", user);
		data.put("age", pwd);

		Executions.createComponents("../menu/borderlayout.zul", div, data);
		
		//permet de fermer la fenetre login
		
		main=(Window)this.self;
		main.detach();
	}
	
	public void doLogout()
	{
		session.removeAttribute("user");
		
		loginDiv.setVisible(true);
		userDiv.setVisible(false);
		userName.setValue("");
	}
	public void doOK()
	{
		if(loginDiv.isVisible()){
			//doLogin();
		}else{
			doLogout();
		}
	}

	//String user = (String)session.getAttribute("user");


}
