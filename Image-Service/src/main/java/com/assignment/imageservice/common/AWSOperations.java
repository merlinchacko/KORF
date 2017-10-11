package com.assignment.imageservice.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.assignment.imageservice.ImageServiceApplication;

/**
 * @author Merlin
 *
 */
@Component
public class AWSOperations {

	private static String bucketName     = "image-resize-service/";
	private static AmazonS3 s3Client;

	private AWSOperations() {
		
		try {
			
			s3Client = new AmazonS3Client(new PropertiesCredentials(ImageServiceApplication.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * @param keyName
	 * @param uploadFileName
	 * @throws IOException
	 * upload an object to AWS S3 bucket
	 */
	public static void uploadObjectToS3Bucket(String keyName, String uploadFileName) throws IOException {

		try {
			
			System.out.println("Uploading a new object to S3 from a file\n");
			FileInputStream stream = new FileInputStream(uploadFileName);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, stream, objectMetadata);
			PutObjectResult result = s3Client.putObject(putObjectRequest);
			System.out.println("----------------------------------------------------------------------------Etag:" + result.getETag() + "-->" + result);

		} catch (AmazonServiceException ase) {
			
			System.out.println("Caught an AmazonServiceException, which " +
					"means your request made it " +
					"to Amazon S3, but was rejected with an error response" +
					" for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			
			System.out.println("Caught an AmazonClientException, which " +
					"means the client encountered " +
					"an internal error while trying to " +
					"communicate with S3, " +
					"such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	/**
	 * @param keyName
	 * @return
	 * @throws IOException
	 * fetch an object from AWS S3 bucket
	 */
	public static InputStream getObjectFromS3Bucket(String keyName) throws IOException {

		S3Object s3object = null;
		try {
			
			System.out.println("Downloading an object");
			s3object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));
			System.out.println("Content-Type: "  + s3object.getObjectContent());
		} catch (AmazonServiceException ase) {
			
			System.out.println("Caught an AmazonServiceException, which" +
					" means your request made it " +
					"to Amazon S3, but was rejected with an error response" +
					" for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			
			System.out.println("Caught an AmazonClientException, which means"+
					" the client encountered " +
					"an internal error while trying to " +
					"communicate with S3, " +
					"such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
		return s3object.getObjectContent();
	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 * check an object exists in AWS S3 bucket
	 */
	public static boolean isObjectExist(String object) throws IOException {

		return s3Client.doesObjectExist(bucketName, object);
	}
}
