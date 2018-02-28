package com.main;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Properties;
import java.io.*;
import java.util.*;
import java.sql.*;

import com.UserLogin.UserLoginDBMethods;
import com.UserLogin.UserLoginDBObj;
import com.util.DatabaseUtiluty;
import com.util.CustomPropertyConfigurator;

public class hrms_user_login extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = Logger.getLogger(hrms_user_login.class); 
	
	String lDBUser  = "";
	String lDBPswd  = "";
	String lDBUrl   = "";
	private static final String DBUSER	= "username";
	private static final String DBPASSWORD	= "password";
	private static final String HOSTURL	= "host";

    /**Initialize global variables*/
	public void init(ServletConfig config) throws ServletException {
		LOGGER.info(hrms_user_login.class.getName()+":"+Level.INFO+":"+"Initializing started");
		ServletContext context = config.getServletContext();
		Properties props = (Properties) context.getAttribute("DbDetails");
		
		lDBUser = props.getProperty(DBUSER);
		lDBPswd = props.getProperty(DBPASSWORD);
		lDBUrl  = props.getProperty(HOSTURL);;
		LOGGER.info(hrms_user_login.class.getName()+":"+Level.INFO+":"+"Initializing completed");		
		super.init(config);
	}
	/**Process the HTTP Get request*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		doPost(request, response);
	}
	/**Process the HTTP Post request*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
	    response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		session.setAttribute("lErrorMsg",null);
		String target = "/jsp/hrms_user_login.jsp";
		String action = request.getParameter("action");
		String action_submit = request.getParameter("action_submit");
		String action_chngpswd = request.getParameter("action_chngpswd");
		System.out.println("action_submit=="+action_submit);
		if ( action_submit != null || action_chngpswd != null ){
			if ( request.getParameter("submit").equals("Submit") ){
				System.out.println("in the  Submit");
				if ( action_submit.equals("hrms_user_login_submit") ){
					System.out.println("in the hrms_user_login_submit ");
					action = "hrms_user_login_submit";
				}
				else
					if (action_submit.equals("login_pswd_change_submit")){
						action = "login_pswd_change_submit";
					}
			}
			else 
				if ( request.getParameter("submit").equals("Change Password") ){
					if ( action_chngpswd.equals("hrms_change_pswd_submit") )
					action = "hrms_change_pswd_submit";
				}  
		}
		if (action!=null) {
			System.out.println("in the  "+action);
			if (action.equals("hrms_user_login_submit")){
				String lUserId = "";
				String lUserName = "";
				String lUserPswd = "";
				lUserId = (String)request.getParameter("user_id");
				lUserName = (String)request.getParameter("user_name");
				lUserPswd = (String)request.getParameter("user_pswd");
				UserLoginDBObj userLoginDBObj = new UserLoginDBObj();
				UserLoginDBMethods userLoginDBMethods = new UserLoginDBMethods(lDBUser,lDBPswd,lDBUrl);
				userLoginDBObj = (UserLoginDBObj)userLoginDBMethods.getRecordByPrimaryKey(lUserId,lUserName,lUserPswd);
				if ( userLoginDBObj != null && ( userLoginDBObj.user_id != null && (userLoginDBObj.user_id).length() > 0) ){
					target = "/jsp/hrms_default.jsp";
				}
				else{
					String lErrorMsg = "User Does Not Exist!!"; 
					session.setAttribute("lErrorMsg",lErrorMsg);
					target = "/jsp/hrms_user_login.jsp";
				}	
			}
			else
				if (action.equals("hrms_change_pswd_submit")){
					target = "/jsp/hrms_user_login_pswd_change.jsp";
				}
				else
				if (action.equals("login_pswd_change_submit")){ 
					UserLoginDBObj popUserLoginDBObj = new UserLoginDBObj();
					UserLoginDBMethods userLoginDBMethods = new UserLoginDBMethods(lDBUser,lDBPswd,lDBUrl);
					String lUserId = "";
					String lUserName = "";
					String lCurPswd = "";
					String lNewPswd = "";
					String lRetypePswd = "";
					popUserLoginDBObj = (UserLoginDBObj)userLoginDBMethods.populateUserLoginDBObjFromReq(request);  
					lRetypePswd = (String)request.getParameter("retype_pswd");
					if ( (popUserLoginDBObj.new_pswd).equals(lRetypePswd) ){
						UserLoginDBObj userLoginDBObj = new UserLoginDBObj();
						userLoginDBObj = (UserLoginDBObj)userLoginDBMethods.getRecordByPrimaryKey(popUserLoginDBObj.user_id,popUserLoginDBObj.user_name,popUserLoginDBObj.old_pswd);
						if ( userLoginDBObj != null && ( userLoginDBObj.user_id != null && (userLoginDBObj.user_id).length() > 0) ){
							int rval = userLoginDBMethods.updateUserLoginByPrimaryKey(popUserLoginDBObj);
							if ( rval > 0 ){
								target = "/jsp/hrms_user_login.jsp";
							}
							else{
								target = "/jsp/hrms_user_login_pswd_change.jsp";
							}
						}
						else{
							String lErrorMsg = "User Does Not Exist!!"; 
							session.setAttribute("lErrorMsg",lErrorMsg);
							target = "/jsp/hrms_user_login_pswd_change.jsp";
						}
					} 
					else{
						String lErrorMsg = "Retype Correct Password!!"; 
						session.setAttribute("lErrorMsg",lErrorMsg);
						target = "/jsp/hrms_user_login_pswd_change.jsp";
					}
				}
			} // (action== null )
			/* forwarding the request/response to the targeted view */
			RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(target);
			requestDispatcher.forward(request, response);
		} // doPost closed
		/* write the methods that are used in class  */
}// class closed


