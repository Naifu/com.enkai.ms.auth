package com.enkai.ms.auth.rest;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import com.enkai.ms.auth.intf.HTTPHeaderNames;

/**
 * Filter für die REST Calls
 * 
 * @author	Dirk
 * @version	1.0
 */
@Provider
@PreMatching
public class RESTResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter( ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {

        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );    // Hier könnte auf bestimmte IP Nummern gefiltert werden, statt generell '*'
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" ); // Methoden die erlaubt werden (POST für Login notwendig)
        responseCtx.getHeaders().add( "Access-Control-Allow-Headers", HTTPHeaderNames.CLIENT_KEY + ", " + HTTPHeaderNames.AUTH_TOKEN ); // Client Key und Token werden als Header Werte erlaubt
    }
}