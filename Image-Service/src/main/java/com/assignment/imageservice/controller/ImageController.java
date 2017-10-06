package com.assignment.imageservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.imageservice.common.Utils;


/**
 * @author Merlin
 *
 */
@RestController
public class ImageController {

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<String> download() {
		System.out.println("enterer");
		try(InputStream in = new URL("https://www.google.nl//images/branding/googlelogo/2x/googlelogo_color_272x92dp.png").openStream()){
			try {
				Files.copy(in, Paths.get("D:/aaa"));
				 String inputImagePath = "D:/aaa";
			        String outputImagePath1 = "D:/Puppy_Fixed.jpg";
			        String outputImagePath2 = "D:/Puppy_Smaller.jpg";
			        String outputImagePath3 = "D:/Puppy_Bigger.jpg";
			 
				 // resize to a fixed width (not proportional)
	            int scaledWidth = 1024;
	            int scaledHeight = 768;
	            Utils.resize(inputImagePath, outputImagePath1, scaledWidth, scaledHeight);
	 
	            // resize smaller by 50%
	            double percent = 0.5;
	            Utils.resize(inputImagePath, outputImagePath2, percent);
	 
	            // resize bigger by 50%
	            percent = 1.5;
	            Utils.resize(inputImagePath, outputImagePath3, percent);
	            
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Downloaded Successfully", HttpStatus.OK);
	}

}
