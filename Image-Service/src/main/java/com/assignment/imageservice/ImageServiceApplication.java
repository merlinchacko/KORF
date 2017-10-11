package com.assignment.imageservice;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
public class ImageServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ImageServiceApplication.class, args);
	}

	public void run(String... arg0) throws Exception {

		RestTemplate restTemplate = new RestTemplate();
		FileOutputStream fileOuputStream=null;

		String outputPath = System.getProperty("user.home") + File.separator + UUID.randomUUID().toString() + ".png";
		String url = "http://localhost:8080/image/show/{predefinedType}/{dummyName}";

		// URI (URL) parameters
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("predefinedType", "original");
		uriParams.put("dummyName", "blazer");

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("reference", "google_logo_image_test");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<byte[]> response = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri() , HttpMethod.GET, entity, byte[].class);

		System.out.println("--------------response status-------------->"+response.getStatusCode());

		try { 
			fileOuputStream = new FileOutputStream(outputPath); 
			fileOuputStream.write(response.getBody());
		} finally {
			fileOuputStream.close();
		}
	}
}
