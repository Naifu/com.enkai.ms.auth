package com.enkai.ms.auth.rest;

import java.security.GeneralSecurityException;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enkai.ms.auth.intf.HTTPHeaderNames;

/**
 * REST Call Handler
 *  
 * @author	Dirk
 * @version 1.0
 *
 */
@Stateless( name = "RESTResource", mappedName = "ejb/RESTResource" )
@Path( "enkai-resource" )
public class RESTResource implements RESTResourceProxy {

	private static final long serialVersionUID = 8428236722685616669L;

	/* (non-Javadoc)
	 * @see com.enkai.ms.auth.rest.RESTResourceProxy#login(javax.ws.rs.core.HttpHeaders, java.lang.String, java.lang.String)
	 */
	@POST
    @Path( "login" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response login(
        @Context HttpHeaders httpHeaders,
        @FormParam( "username" ) String username,
        @FormParam( "password" ) String password ) {

        Authenticator restAuthenticator = Authenticator.getInstance();
        String clientKey = httpHeaders.getHeaderString( HTTPHeaderNames.CLIENT_KEY );

        try {
            String authToken = restAuthenticator.login( clientKey, username, password );
            
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            
            jsonObjBuilder.add( "auth_token", authToken );
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
            
        } catch ( final LoginException ex ) {
        	
        	JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add( "message", ex.getMessage() );
            JsonObject jsonObj = jsonObjBuilder.build();

            return getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
    }

    /* (non-Javadoc)
     * @see com.enkai.ms.auth.rest.RESTResourceProxy#logout(javax.ws.rs.core.HttpHeaders)
     */
    @POST
    @Path( "logout" )
    public Response logout(
        @Context HttpHeaders httpHeaders ) {
        try {
            Authenticator authenticator = Authenticator.getInstance();
            String clientKey = httpHeaders.getHeaderString( HTTPHeaderNames.CLIENT_KEY );
            String authToken = httpHeaders.getHeaderString( HTTPHeaderNames.AUTH_TOKEN );

            authenticator.logout( clientKey, authToken );

            return getNoCacheResponseBuilder( Response.Status.NO_CONTENT ).build();
        } catch ( final GeneralSecurityException ex ) {
            return getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).build();
        }
    }

    /**
     * Deaktiviert Cache Control für den Response Header
     * 
     * @param	status			Status der Response
     * @return	ResponseBuilder	Reponse mit deaktiviertem Caching
     */
    private Response.ResponseBuilder getNoCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( true );
        cc.setMaxAge( -1 );
        cc.setMustRevalidate( true );

        return Response.status( status ).cacheControl( cc );
    }

	@GET
	@Path("test-get-method")
	public Response testGetMethod(
		@Context HttpHeaders httpHeaders) {
		
		JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add( "message", "okidoki läuft" );
        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
	}
}