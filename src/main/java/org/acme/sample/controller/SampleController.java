package org.acme.sample.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

@Component
@Path("/samples")
public class SampleController {

	@GET
	@Path("/hello")
	public String hello(@QueryParam("name") @NotNull String name) {
		return "Hello " + name;
	}

}
