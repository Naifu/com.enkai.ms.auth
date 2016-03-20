package com.enkai.ms.auth.rest;

import java.io.Serializable;

import javax.ejb.Local;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path( "enkai-resource-proxy" )
public interface RESTResourceProxy extends Serializable {

    @POST
    @Path( "login" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response login(
        @Context HttpHeaders httpHeaders,
        @FormParam( "username" ) String username,
        @FormParam( "password" ) String password );

    @POST
    @Path( "logout" )
    public Response logout(
        @Context HttpHeaders httpHeaders
    );
    
    @POST
    @Path( "registerClient" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response registerKey(); 
    
    @GET
    @Path( "test-get-method" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response testGetMethod(
    	@Context HttpHeaders httpHeaders
    );
    
    @GET
    @Path( "authCheck")
    public Response authCheck(
    	@Context HttpHeaders httpHeaders
    );
}