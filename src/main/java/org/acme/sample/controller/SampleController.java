package org.acme.sample.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Path("/samples")
public class SampleController {

	@GET
	@Path("/hello")
	public String hello() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails details = (UserDetails) authentication.getPrincipal();
            String username = details.getUsername();

            return "Hello " + username;
        } else {
            throw new AuthenticationCredentialsNotFoundException("Cannot retrieve user details.");
        }
	}

}
