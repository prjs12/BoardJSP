package command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import service.LoginFailException;
import service.LoginService;
import service.User;
//Java Class로 Servlet 형태를 구현
public class LoginHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/loginForm.jsp";
	//문제가 있을시 loginForm으로 보내줄 변수를 지정
	private LoginService loginService = new LoginService();
	//Servlet 에서는 Service단으로 보내야 하기때문에 , Service 구현 및 객체 생성
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req,res);
		}else if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req,res);
		}else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		// TODO Auto-generated method stub
		return FORM_VIEW;
	}
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// TODO Auto-generated method stub
		String id = trim(req.getParameter("id")); // trim = 공백제거
		String password = trim(req.getParameter("password"));
		
		Map<String, Boolean> errors = new HashMap<String, Boolean>();
		req.setAttribute("errors", errors);
		
		if(id==null || id.isEmpty())
			errors.put("id", Boolean.TRUE);
		
		if (password==null || password.isEmpty())
			errors.put("password",Boolean.TRUE);
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		try {
			User user = loginService.login(id, password);
			req.getSession().setAttribute("authUser", user);
			res.sendRedirect(req.getContextPath()+"/index.jsp");
			return null;
		} catch(LoginFailException e) {
			errors.put("idOrPwNotMatch", Boolean.TRUE);
			return FORM_VIEW;
		}
	}

	private String trim(String str) {
		return str == null? null:str.trim();
	}
}
