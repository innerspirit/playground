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
import org.springframework.http.*;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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
        final HashMap<String, String> map =  new HashMap<String, String>(1);
        map.put("text", "prueba");

        try {
            String result = this.restTemplate.postForObject(
                    getUrl("/notes"), map, String.class);
            assertTrue(result.startsWith("Note successfully created"));
        } catch(HttpStatusCodeException ex) {
            logger.error(ex.getResponseBodyAsString());
        }
	}

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:notes.sql")
    public void update() {
        restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>(1);
        map.add("text", "nuevo valor");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        this.restTemplate.put(getUrl("/notes/35"), request);

        ResponseEntity<String> newEntity = this.restTemplate.getForEntity(
                getUrl("/notes/find"), String.class, map);
        String newBody = newEntity.getBody();

        assertEquals(HttpStatus.OK, newEntity.getStatusCode());
        assertTrue(newBody.matches("The note id is: 35"));
    }

    @Test
    public void find() {
        restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        final HashMap<String, String> map1 = new HashMap<String, String>(1);
        map1.put("text", "prueba");

        try {
            this.restTemplate.postForObject(getUrl("/notes"), map1, String.class);

            ResponseEntity<String> newEntity = this.restTemplate.getForEntity(
                    getUrl("/notes/find"), String.class, map1);
            String newBody = newEntity.getBody();

            assertEquals(HttpStatus.OK, newEntity.getStatusCode());
            assertTrue(newBody.matches("The note id is: [0-9]+"));

        } catch(HttpStatusCodeException ex) {
            logger.error(ex.getResponseBodyAsString());
        }
    }

	private String getUrl(String path) {
		return "http://localhost:" + port + basePath + path;
	}
}