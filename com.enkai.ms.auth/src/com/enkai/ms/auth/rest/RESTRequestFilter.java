package com.enkai.ms.auth.rest;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.enkai.ms.auth.intf.HTTPHeaderNames;

/**
 * Filter für alle REST Anfragen
 * 
 * @author	Dirk
 * @version	1.0
 *
 */
@Provider
@PreMatching
public class RESTRequestFilter implements ContainerRequestFilter {

    /* (non-Javadoc)
     * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
     */
    @Override
    public void filter( ContainerRequestContext requestCtx ) throws IOException {

        String path = requestCtx.getUriInfo().getPath();

        // IMPORTANT!!! First, Acknowledge any pre-flight test from browsers for this case before validating the headers (CORS stuff)
        if ( requestCtx.getRequest().getMethod().equals( "OPTIONS" ) ) {
            requestCtx.abortWith( Response.status( Response.Status.OK ).build() );

            return;
        }

        // Prüfen ob der Client Key bekannt ist
        Authenticator restAuthenticator = Authenticator.getInstance();
        String clientKey = requestCtx.getHeaderString( HTTPHeaderNames.CLIENT_KEY );

//        if ( !restAuthenticator.isclientKeyValid( clientKey ) ) {
//            requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() ); // Client Key unbekannt => Verbindung ablehnen
//            return;
//        }

        // Für alle Anfragen ausser Loginversuche und Client Registrierung den Authorisierungs Token überprüfen
        if ( !path.startsWith( "enkai-resource/login/" ) && !path.startsWith( "enkai-resource/registerClient/" ) ) {
            String authToken = requestCtx.getHeaderString( HTTPHeaderNames.AUTH_TOKEN );

            if ( !restAuthenticator.isAuthTokenValid( clientKey, authToken ) ) {
                requestCtx.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build() ); // Token unbekannt => Verbindung ablehnen
            }
        }
    }
}