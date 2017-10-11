package com.assignment.imageservice.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.util.IOUtils;
import com.assignment.imageservice.service.ImageService;


/**
 * @author Merlin
 *
 */
@RestController
public class ImageController {
	
	@Autowired
	ImageService imageService;
	

	/**
	 * @param predefinedType
	 * @param dummyName
	 * @param reference
	 * @return processed image
	 * returns an image by resizing its size 
	 */
	@RequestMapping(value = "/image/show/{predefinedType}/{dummyName}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> procesImage(@PathVariable(value="predefinedType") String predefinedType, 
												@PathVariable(value="dummyName") String dummyName,
												@RequestParam String reference) {
		byte[] output = null;
		try {
			
			InputStream inputStream = imageService.processImage(predefinedType, dummyName, reference);
			
			output = IOUtils.toByteArray(inputStream);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(output , HttpStatus.OK);
	}
}
