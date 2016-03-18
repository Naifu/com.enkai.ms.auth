package com.enkai.ms.auth.ldap;

import java.util.Properties;

import com.enkai.ms.util.PropertyReader;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.PasswordExpiredControl;
import com.unboundid.ldap.sdk.controls.PasswordExpiringControl;

public class LDAPService {

	private Properties ldapprops;
	
	public int login (String userName, String passWord) {
				
		LDAPConnection connection = new LDAPConnection();
		BindResult bindResult;
		
		int retValue = 0;
		
//		String info = "";
		
		try {
			
			ldapprops = new PropertyReader().getProperties("ldap.properties"); 
		
			connection.connect(ldapprops.getProperty("serverName"), Integer.parseInt(ldapprops.getProperty("serverPort")));
			
			try {
				BindRequest bindRequest = new SimpleBindRequest("cn=" + userName + "," + ldapprops.getProperty("scope"), passWord);
				bindResult = connection.bind(bindRequest);
				
				retValue = 1;
				
				PasswordExpiringControl expiringControl = PasswordExpiringControl.get(bindResult);
			
				if (expiringControl != null)
					retValue = 2;
				    //info = "Ihr Password läuft bald ab!";
				 
//				SearchResult result = connection.search( ldapprops.getProperty("scope"), SearchScope.SUB, "(uid=" + userName + ")", "cn", "displayName", "jpegPhoto", "title", "mobile", "telephoneNumber", "mail", "l", "departmentNumber");
//				
//				if (result.getEntryCount() > 0) {
//					SearchResultEntry entry = result.getSearchEntries().get(0);
//					
//					// user found
//				}
				
			} catch (LDAPException le) {
				
				bindResult = new BindResult(le.toLDAPResult());
				//ResultCode resultCode = le.getResultCode();
//				String errorMessageFromServer = le.getDiagnosticMessage();
				
				PasswordExpiredControl expiredControl = PasswordExpiredControl.get(bindResult);
			
				 if (expiredControl != null) {
					//info = "Das Kennwort ist abgelaufen!";
					 retValue = 3;
				 } else {
					//info = errorMessageFromServer == null ? "Benutzername und/oder Passwort falsch!" : errorMessageFromServer;
					 retValue = 0;
				 }
			}
			
//			if (loggedIn) {
//				String message = "Willkommen " + commonName;
//				
//				if (!info.equals(""))
//					message += "\n" + info;
//				
//				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Willkommen", message);
//				SecurityTools.setUserName(username);
//			} else {
//				msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Fehler", info);  
//			}
			
		} catch (Exception e) {

			retValue = 0;
			
		} finally {
			if (null != connection)
				connection.close();
		}
		
		return retValue;
		
	}
	
}
