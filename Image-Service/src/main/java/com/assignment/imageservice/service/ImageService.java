package com.assignment.imageservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * @author Merlin
 *
 */
public interface ImageService {
	
	InputStream processImage(String predefinedType, String dummyName, String reference) throws IOException, MalformedURLException;

}
