package org.acme.note.controller;

import org.acme.sample.Application;
import org.acme.sample.model.Note;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class NoteControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(NoteControllerTest.class);

	private RestTemplate restTemplate;

	@Value("${local.server.port}")
    private int port;

    @Value("${spring.jersey.application-path}")
    private String basePath;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    private DefaultOAuth2ClientContext clientContext;

    private ResourceOwnerPasswordResourceDetails resourceDetails;

    @Before
    public void setUp() {
        resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("chris");
        resourceDetails.setPassword("prueba");
        resourceDetails.setAccessTokenUri(format("http://localhost:%d/oauth/token", port));
        resourceDetails.setClientId(clientId);
        resourceDetails.setClientSecret(clientSecret);
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(asList("read", "write"));

        clientContext = new DefaultOAuth2ClientContext();
    }

	@Test
	public void create() {
        restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("text", "prueba");

        try {
            URI resultUri = this.restTemplate.postForLocation(
                    getUrl("/notes/create"), map);
            assertTrue(resultUri.getPath().matches("/notes/[0-9]+"));
        } catch(HttpStatusCodeException ex) {
            logger.error(ex.getResponseBodyAsString());
        }
	}

	private String getUrl(String path) {
		return "http://localhost:" + port + basePath + path;
	}
}