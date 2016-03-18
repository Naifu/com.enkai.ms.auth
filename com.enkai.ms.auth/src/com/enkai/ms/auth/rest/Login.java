package com.enkai.ms.auth.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.enkai.ms.auth.ldap.LDAPService;

@Path("/login")
public class Login {
	
	// This method is called if TEXT_PLAIN is request
	  @GET
	  @Produces(MediaType.TEXT_PLAIN)
	  public Response login(@QueryParam("username") String userName, @QueryParam("password") String passWord) {
		  
		  NewCookie cookie = null;
		  String response = "OK";
		  
		  try {

			  switch (new LDAPService().login(userName, passWord)) {
			  case 0:
				  response = "FAILED";
				  break;
			  case 1:
				  cookie = new NewCookie("name", "123", null, null, null, 60, false);
				  response = "OK";
				  break;
			  case 2:
				  cookie = new NewCookie("name", "123", null, null, null, 60, false);
				  response = "EXPIRESOON";
				  break;
			  case 3:
				  response = "EXPIRED";
				  break;
			  }
		  
		  } catch (Exception e) {
			  response = "ERROR";
		  }
		  
		  if (cookie == null)
			  return Response.ok(response).build();
		  else
			  return Response.ok(response).cookie(cookie).build();
	  }
	  
//	  // This method is called if XML is request
//	  @GET
//	  @Produces(MediaType.TEXT_XML)
//	  public String sayXMLHello() {
//	    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
//	  }
//
//	  // This method is called if HTML is request
//	  @GET
//	  @Produces(MediaType.TEXT_HTML)
//	  public String sayHtmlHello() {
//	    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
//	        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
//	  }

}
