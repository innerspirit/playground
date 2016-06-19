package org.acme.sample.config;

import org.acme.sample.controller.SampleController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(SampleController.class);
	}

}