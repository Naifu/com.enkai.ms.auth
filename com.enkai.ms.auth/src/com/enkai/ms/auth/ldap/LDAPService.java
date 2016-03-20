package com.enkai.ms.auth.ldap;

import java.util.Properties;

import javax.security.auth.login.LoginException;

import com.enkai.ms.util.PropertyReader;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.PasswordExpiredControl;
import com.unboundid.ldap.sdk.controls.PasswordExpiringControl;

/**
 * Service Objekt für die Kommunikation mit dem LDAP
 * 
 * @author	Dirk
 * @version	1.0
 */

public class LDAPService {

	/**
	 * Prüft die Authentifizierung eines Benutzers gegenüber dem LDAP
	 * 
	 * @author	Dirk
	 * @version	1.0
	 * 
	 * @param	userName	Der Benutzername der zu prüfen ist
	 * @param	passWord	Das Kennwort das zu prüfen ist
	 * @throws	LoginException 
	 */
	public void login (String userName, String passWord) throws LoginException {
				
		LDAPConnection connection = new LDAPConnection();
		BindResult bindResult;
		
		try {
			
			Properties ldapprops = new PropertyReader().getProperties("ldap.properties"); 
			connection.connect(ldapprops.getProperty("serverName"), Integer.parseInt(ldapprops.getProperty("serverPort")));
			
			try {
				BindRequest bindRequest = new SimpleBindRequest("cn=" + userName + "," + ldapprops.getProperty("scope"), passWord);
				bindResult = connection.bind(bindRequest);
				
				PasswordExpiringControl expiringControl = PasswordExpiringControl.get(bindResult);
			
				if (expiringControl != null)  // password läuft bald ab
					throw new LoginException ("Password will expire soon");
				
			} catch (LDAPException le) {
				
				bindResult = new BindResult(le.toLDAPResult());
				PasswordExpiredControl expiredControl = PasswordExpiredControl.get(bindResult);
			
				 if (expiredControl != null) {
					 throw new LoginException ("Password expired");
				 } else {
					 throw new LoginException ("Login failed"); 
				 }
			}
			
		} catch (LoginException lex) {
			throw lex;
		} catch (Exception ex) {
			throw new LoginException ("Login failed");
		} finally {
			if (null != connection)
				connection.close();
		}
		
	}
	
}
