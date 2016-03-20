package com.enkai.ms.auth.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.security.GeneralSecurityException;
import javax.security.auth.login.LoginException;

import com.enkai.ms.auth.ldap.LDAPService;

/**
 * Objekt zum speichern und prüfen der Client Keys und Tokens 
 * 
 * @author	Dirk
 * @version	1.0
 */
public final class Authenticator {

    private static Authenticator authenticator = null;

    private final Map<String, String> clientKeysStorage = new HashMap<String, String>();
    private final Map<String, String> authorizationTokensStorage = new HashMap<String, String>();

    private Authenticator() {}

    /**
     * Singleton generieren
     * 
     * @author	Dirk
     * @Version	1.0
     * @return	Authenticator	Objekt
     */
    public static Authenticator getInstance() {
    	
        if ( authenticator == null ) {
            authenticator = new Authenticator();
        }

        return authenticator;
    }
    
    /**
     * Registriert einen client Key
     *
     * @param	clientKey	Der neue Client Key
     */
    public void registerClient (String clientKey) {
    	clientKeysStorage.put(clientKey, "");
    }

    /**
     * Hauptroutine für Logins - Nutzt LDAP und generiert bei erfolgreichem Login das User Token
     * 
     * @author	Dirk
     * @version	1.0
     * 
     * @param	clientKey	Der Key des Clients der bei einem Login Versuch als grundsätzliches Gate dient
     * @param	username	Der Benutzername im LDAP
     * @param	password	Das Kennwort im LDAP
     * @return	String		Das generierte Token oder null
     * @throws	LoginException
     */
    public String login( String clientKey, String username, String password ) throws LoginException {
    	
    	try {
    	
	    	if ( clientKeysStorage.containsKey( clientKey ) ) {
	    		new LDAPService().login(username, password);
	        	
	    		String authToken = UUID.randomUUID().toString();
	    		clientKeysStorage.put(clientKey, username);
	        	authorizationTokensStorage.put( authToken, username );
	        	return authToken;
	        } else {
	        	throw new LoginException ("Unknown client key");
	        }
	    	
    	} catch (LoginException lex) {
    		throw lex;
    	} catch (Exception ex) {
    		throw new LoginException ("Generic exception occured");
    	}
    	
    }

    /**
     * Prüft, ob ein Client Key gültig ist und ob der Authentifizierungs Token bereits bekannt ist
     * 
     * @author	Dirk
     * @version	1.0
     *
     * @param	clientKey	Der Client Key
     * @param	authToken	Das Authorisierungs Token das nach dem Login generiert wurde
     * @return	boolean		true = Client Key und Token erfolgreich validiert
     * 						false = Client Key oder Token nicht validiert
     */
    public boolean isAuthTokenValid( String clientKey, String authToken ) {

    	if ( isclientKeyValid( clientKey ) ) {
            if ( authorizationTokensStorage.containsKey( authToken ) ) {
            	if (clientKeysStorage.get(clientKey).equals(authorizationTokensStorage.get(authToken))) {
            		return true;
            	}
            }
        }

        return false;
    }

    /**
     * Prüft, ob ein Client Key bekannt ist
     * 
     * @author	Dirk
     * @version	1.0
     *
     * @param	clientKey	Der Client Key
     * @return	boolean		TRUE = Der Client Key ist bekannt
     * 						FALSE = Der Client Key ist unbekannt
     */
    public boolean isclientKeyValid( String clientKey ) {
        return clientKeysStorage.containsKey( clientKey );
    }

    /**
     * Führt einen Logout aus und löscht damit den Token aus dem Store
     * 
     * @param	clientKey	Der Client Key der Session
     * @param	authToken	Das Authorisierungs Token der Session
     * @throws GeneralSecurityException
     */
    public void logout( String clientKey, String authToken ) throws GeneralSecurityException {
        
    	if ( clientKeysStorage.containsKey( clientKey ) ) {
            if ( authorizationTokensStorage.containsKey( authToken ) ) {
            	authorizationTokensStorage.remove( authToken );
                return;
            }
        }

        throw new GeneralSecurityException( "Service Key and / or authorisation token do not match" );
    }
}