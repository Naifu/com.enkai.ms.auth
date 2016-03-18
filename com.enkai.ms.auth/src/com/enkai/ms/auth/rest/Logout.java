package com.enkai.ms.auth.rest;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("/logout")
public class Logout {

	  @GET
	  @Produces(MediaType.TEXT_PLAIN)
	  public Response logout(@CookieParam("name") Cookie cookie) {
	      if (cookie != null) {
	          NewCookie newCookie = new NewCookie(cookie, null, 0, false);
	          return Response.ok("OK").cookie(newCookie).build();
	      }
	      return Response.ok("OK - No session").build();
	  }
	
}
