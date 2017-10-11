package com.assignment.imageservice.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.assignment.imageservice.common.AWSOperations;
import com.assignment.imageservice.common.Utils;
import com.assignment.imageservice.service.ImageService;

/**
 * @author Merlin
 *
 */
@Service("imageService")
public class ImageServiceImpl implements ImageService {

	private String filepath =  System.getProperty("user.home") + File.separator;
	private InputStream inputStream = null;


	/* (non-Javadoc)
	 * @see com.assignment.imageservice.service.ImageService#processImage(java.lang.String, java.lang.String, java.lang.String)
	 * Will download an image from the URL, update the image to AWS S3 bucket
	 */
	@Override
	public InputStream processImage(String predefinedType, String dummyName, String reference) throws IOException, MalformedURLException {

		String fileName =  filepath + reference + ".png";

		//processing an image from a url, hardcoding the original image source for now, should be passed as parameter
		try(InputStream in = new URL("https://www.google.nl//images/branding/googlelogo/2x/googlelogo_color_272x92dp.png").openStream()){
			try {
				File file = new File(fileName);
				if(!file.exists()) {
					Files.copy(in, Paths.get(fileName));
				}

				fetchImageFromS3Bucket(predefinedType, dummyName, reference, fileName);
			} catch (IOException e) {
				
				e.printStackTrace();
				throw new IOException();
			}
		} catch (MalformedURLException e1) {
			
			e1.printStackTrace();
			throw new MalformedURLException();
		} catch (IOException e1) {
			
			e1.printStackTrace();
			throw new IOException();
		}
		return inputStream;
	}


	/**
	 * @param predefinedType
	 * @param dummyName
	 * @param reference
	 * @param fileName
	 * @throws IOException
	 * Fetch resized image from AWS S3 bucket.
	 * Checks the reference image exist in S3 bucket, 
	 * if yes check its resized version exists in S3 bucket if yes return the same from S3 bucket, if no upload the same and return it.
	 * if no upload original image and resized image to S3 bucket
	 */
	private void fetchImageFromS3Bucket(String predefinedType, String dummyName, String reference, String fileName) throws IOException {

		String originalImagePathInS3 = getKeyName(predefinedType, dummyName, reference);
		String resizedImagePathInS3  = getKeyName("thumbnail", dummyName, reference);

		if(AWSOperations.isObjectExist(originalImagePathInS3)) {
			
			if(AWSOperations.isObjectExist(resizedImagePathInS3)) {
				
				inputStream = AWSOperations.getObjectFromS3Bucket(resizedImagePathInS3);
			} else {
				
				uploadResizedImageToS3(fileName, resizedImagePathInS3);
			}
		} else {
			
			AWSOperations.uploadObjectToS3Bucket(originalImagePathInS3, fileName);
			uploadResizedImageToS3(fileName, resizedImagePathInS3);
		}
	}


	/**
	 * @param fileName
	 * @param resizedImagePathInS3
	 * @throws IOException
	 * Upload an image after resizing by changing its height and width to AWS S3 bucket and returns the same
	 */
	private void uploadResizedImageToS3(String fileName, String resizedImagePathInS3) throws IOException {
		
		String outputPath = filepath + UUID.randomUUID().toString()+ ".png";
		double percent = 0.5;

		Utils.resize(fileName, outputPath, percent);
		
		AWSOperations.uploadObjectToS3Bucket(resizedImagePathInS3, outputPath);
		
		inputStream = AWSOperations.getObjectFromS3Bucket(resizedImagePathInS3);
	}


	/**
	 * @param predefinedType
	 * @param dummyName
	 * @param reference
	 * @return path where in S3 bucket, object is stored
	 * path is calculated based on passed predefined type, dummy name and original reference for image 
	 */
	private String getKeyName(String predefinedType, String dummyName, String reference) {

		StringBuilder keyName = new StringBuilder();
		keyName.append(predefinedType + File.separator);
		
		if(reference.length() > 4 && reference.length() < 8){

			keyName.append(StringUtils.substring(reference, 0, 4));
		}else if(reference.length() > 8) {

			keyName.append(StringUtils.substring(reference, 0, 4) + File.separator + StringUtils.substring(reference, 4, 8));
		}
		keyName.append(File.separator + reference);

		return keyName.toString();
	}

}
