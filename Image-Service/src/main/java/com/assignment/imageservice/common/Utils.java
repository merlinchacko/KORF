package com.assignment.imageservice.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

/**
 * @author Merlin
 *
 */
@Component
public class Utils {

	/**
	 * @param inputImagePath
	 * @param outputImagePath
	 * @param percent
	 * @throws IOException
	 * Resize an image by changing its width and height with a value
	 */
	public static void resize(String inputImagePath, String outputImagePath, double percent) throws IOException {

		File inputFile = new File(inputImagePath);
		BufferedImage inputImage = ImageIO.read(inputFile);
		int scaledWidth = (int) (inputImage.getWidth() * percent);
		int scaledHeight = (int) (inputImage.getHeight() * percent); 

		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		// extracts extension of output file
		String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
		// writes to output file
		ImageIO.write(outputImage, formatName, new File(outputImagePath));
	}

}
