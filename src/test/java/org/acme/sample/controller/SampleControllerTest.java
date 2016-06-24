package org.acme.sample.controller;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import org.acme.sample.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class SampleControllerTest {

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
	public void hello() {
        restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
		ResponseEntity<String> entity = this.restTemplate.getForEntity(
				getUrl("/samples/hello"), String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals("Hello chris", entity.getBody());
	}

	@Test(expected = HttpClientErrorException.class)
	public void helloWithoutUser() {
        restTemplate = new RestTemplate();
		ResponseEntity<String> entity = this.restTemplate
				.getForEntity(getUrl("/samples/hello"), String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, entity.getStatusCode());
	}

	private String getUrl(String path) {
		return "http://localhost:" + port + basePath + path;
	}
}