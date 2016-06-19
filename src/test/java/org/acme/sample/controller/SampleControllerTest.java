package org.acme.sample.controller;

import static org.junit.Assert.assertEquals;

import org.acme.sample.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest(randomPort = true)
public class SampleControllerTest {

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Value("${local.server.port}")
    private int port;
	
	@Test
	public void hello() {
		ResponseEntity<String> entity = this.restTemplate.getForEntity(
				getUrl("/samples/hello?name=world"), String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals("Hello world", entity.getBody());
	}

	@Test
	public void helloWithoutParameter() {
		ResponseEntity<String> entity = this.restTemplate
				.getForEntity(getUrl("/samples/hello"), String.class);
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}

	private String getUrl(String path) {
		return "http://localhost:" + port + path;  
	}
}